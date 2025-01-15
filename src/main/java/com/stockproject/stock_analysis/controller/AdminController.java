package com.stockproject.stock_analysis.controller;

import com.stockproject.stock_analysis.entity.KoreaStockAnalysis;
import com.stockproject.stock_analysis.service.FileDownloadService;
import com.stockproject.stock_analysis.service.KoreaStockAnalysisService;
import com.stockproject.stock_analysis.service.UserDataLoadService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserDataLoadService userDataLoadService;
    private final FileDownloadService fileDownloadService;
    private final KoreaStockAnalysisService koreaStockAnalysisService;

    @Value("${file.download.path}")
    private String downloadPath;
    
    @Value("${python.server.url}")
    private String pythonServerUrl;

    @Autowired
    public AdminController(UserDataLoadService userDataLoadService, 
                           FileDownloadService fileDownloadService, 
                           KoreaStockAnalysisService koreaStockAnalysisService) {
        this.userDataLoadService = userDataLoadService;
        this.fileDownloadService = fileDownloadService;
        this.koreaStockAnalysisService = koreaStockAnalysisService;
    }

    // 1. 전체 데이터 가져오기
    @GetMapping("/analysis/all")
    public ResponseEntity<?> getAllAnalysisData() {
        try {
            List<KoreaStockAnalysis> analysisData = userDataLoadService.getAllAnalysisData();
            return ResponseEntity.ok(analysisData);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("전체 데이터를 가져오는 중 오류가 발생했습니다.");
        }
    }

    // 2. 주식 업데이트 작업
    @PostMapping("/update-stocks")
    public ResponseEntity<?> updateStocks() {
        try {
            // 1단계: Python 서버 호출
        	String pythonUrl = pythonServerUrl + "/update-stocks";
            int pythonResponseCode = fileDownloadService.callPythonServer(pythonUrl);
            if (pythonResponseCode != 200) {
                return ResponseEntity.status(500).body("Python 서버 호출 실패: 응답 코드 " + pythonResponseCode);
            }

            System.out.println("Python 서버에서 CSV 생성 및 분석 완료.");

            // 2단계: 파일 다운로드 및 처리
            fileDownloadService.downloadAndProcessFiles();
            System.out.println("Python 서버에서 분석 CSV 다운로드 및 처리 완료.");

            // 3단계: DB에 업로드
            koreaStockAnalysisService.saveFromCsv();
            System.out.println("DB에 CSV 데이터 업로드 완료.");

            return ResponseEntity.ok("주식 업데이트 작업 완료!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("업데이트 중 오류 발생: " + e.getMessage());
        }
    }
}
