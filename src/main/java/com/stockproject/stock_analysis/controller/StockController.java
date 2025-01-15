package com.stockproject.stock_analysis.controller;

import com.stockproject.stock_analysis.entity.Stock;
import com.stockproject.stock_analysis.entity.UpdateLog;
import com.stockproject.stock_analysis.entity.KoreaStockAnalysis;
import com.stockproject.stock_analysis.service.KoreaStockAnalysisService;
import com.stockproject.stock_analysis.service.StockService;
import com.stockproject.stock_analysis.service.UpdateLogService;
import com.stockproject.stock_analysis.service.UserDataLoadService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/stocks")
public class StockController {
    private final StockService stockService;
    private final UserDataLoadService userDataLoadService;
    private final UpdateLogService updateLogService;
    private final KoreaStockAnalysisService koreaStockAnalysisService;

    @Autowired
    public StockController(StockService stockService, UserDataLoadService userDataLoadService,UpdateLogService updateLogService,KoreaStockAnalysisService koreaStockAnalysisService) {
        this.stockService = stockService;
        this.userDataLoadService = userDataLoadService;
        this.updateLogService = updateLogService;
        this.koreaStockAnalysisService = koreaStockAnalysisService;
    }

    @Value("${file.download.path}") // 저장할 디렉토리
    private String downloadPath;


    
    // 최신 시간
    @GetMapping("/last-update")
    public ResponseEntity<?> getLastUpdate(@RequestParam(name = "market_type") String marketType) {
        try {
            Optional<UpdateLog> lastUpdate = updateLogService.getLastUpdate(marketType);

            if (lastUpdate.isPresent()) {
                UpdateLog updateLog = lastUpdate.get();
                return ResponseEntity.ok().body(Map.of(
                    "last_update", updateLog.getUpdateTime(),
                    "market_type", updateLog.getMarketType()
                ));
            } else {
                return ResponseEntity.status(404).body(Map.of(
                    "message", String.format("No updates yet for market type '%s'.", marketType)
                ));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of(
                "error", "Failed to fetch last update: " + e.getMessage()
            ));
        }
    }
    
    
    @PostMapping("/upload")
    public ResponseEntity<String> uploadCsv() {
        String message = koreaStockAnalysisService.saveFromCsv();
        return ResponseEntity.ok(message);
    }
    
    @GetMapping("/{stockcode}")
    public ResponseEntity<?> getStockByCode(@PathVariable("stockcode") String stockcode) {
        try {
            KoreaStockAnalysis stock = koreaStockAnalysisService.getStockByCode(stockcode);
            if (stock != null) {
                return ResponseEntity.ok().body(stock);
            } else {
                return ResponseEntity.status(404).body("No stock found with code: " + stockcode);
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to fetch stock: " + e.getMessage());
        }
    }   
    
    
    // 유저별 Stock 리스트 조회
    @GetMapping
    public ResponseEntity<Page<Stock>> getStocks(
        @RequestParam("username") String username,
        Pageable pageable
    ) {
        Page<Stock> stocks = stockService.getAvailableStocks(username, pageable);
        return ResponseEntity.ok(stocks);
    }

    // 유저별 사용자 분석 데이터 조회
    @GetMapping("/analysis")
    public ResponseEntity<List<KoreaStockAnalysis>> getUserAnalysisData(
        @RequestParam("username") String username
    ) {
        List<KoreaStockAnalysis> analysisData = userDataLoadService.getUserAnalysisData(username);
        return ResponseEntity.ok(analysisData);
    }
    
    // 차트용 데이터
    @GetMapping("/chart/{stockcode}")
    public ResponseEntity<?> getStockData(@PathVariable("stockcode") String stockcode) {
        try {
            String normalizedStockCode = stockcode.toLowerCase(); // 소문자로 변환
            String folderPath = downloadPath + "/korea_stocks_data_parts";
            File folder = new File(folderPath);

            // 폴더가 존재하지 않으면 오류 반환
            if (!folder.exists() || !folder.isDirectory()) {
                return ResponseEntity.status(404).body("폴더가 존재하지 않습니다: " + folderPath);
            }

            // 폴더 내 파일 검색
            File[] files = folder.listFiles((dir, name) -> name.contains("_" + String.format("%06d", Integer.parseInt(normalizedStockCode)) + "_"));
            
            if (files == null || files.length == 0) {
                return ResponseEntity.status(404).body("해당 StockCode에 해당하는 파일을 찾을 수 없습니다: " + normalizedStockCode);
            }

            // 첫 번째 파일 읽기
            File targetFile = files[0];
            List<Map<String, String>> result = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(targetFile))) {
                String line;
                String[] headers = null;

                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",");
                    if (headers == null) {
                        headers = Arrays.stream(values)
                                        .map(header -> header.replace("\uFEFF", "").trim()) // BOM 및 공백 제거
                                        .toArray(String[]::new);
                    } else {
                        Map<String, String> row = new HashMap<>();
                        for (int i = 0; i < headers.length; i++) {
                            row.put(headers[i], values[i].trim());
                        }
                        result.add(row);
                    }
                }
            }

            List<Map<String, String>> filteredResult = new ArrayList<>();
            for (Map<String, String> row : result) {
                Map<String, String> filteredRow = new HashMap<>();
                filteredRow.put("Date", row.get("Date"));
                filteredRow.put("StockName", row.get("StockName"));
                filteredRow.put("Close", row.get("Close"));
                filteredResult.add(filteredRow);
            }

            return ResponseEntity.ok(filteredResult);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("오류 발생: " + e.getMessage());
        }
    }
}
