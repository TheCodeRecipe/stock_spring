package com.stockproject.stock_analysis.service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.stockproject.stock_analysis.entity.KoreaStockAnalysis;
import com.stockproject.stock_analysis.entity.UpdateLog;
import com.stockproject.stock_analysis.repository.KoreaStockAnalysisRepository;
import com.stockproject.stock_analysis.repository.UpdateLogRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.List;

@Service
public class KoreaStockAnalysisService {

    private final KoreaStockAnalysisRepository repository;
    private final UpdateLogRepository updateLogRepository;

    @Value("${file.download.path}")
    private String fileDownloadPath;

    public KoreaStockAnalysisService(KoreaStockAnalysisRepository repository, UpdateLogRepository updateLogRepository) {
        this.repository = repository;
        this.updateLogRepository = updateLogRepository;
    }

    public String saveFromCsv() {
        try {
            // 테이블 데이터 삭제
            repository.truncateTable();
            System.out.println("기존 데이터를 모두 삭제했습니다.");
            
            // 파일 경로 생성
            String filePath = fileDownloadPath + "/korea_analysis_combined.csv";
            
            // CSV 파일 읽기
            List<KoreaStockAnalysis> stocks = new CsvToBeanBuilder<KoreaStockAnalysis>(new FileReader(filePath))
                    .withType(KoreaStockAnalysis.class)
                    .build()
                    .parse();

            // 디버깅용: 모든 데이터 출력
            System.out.println("CSV에서 읽은 데이터:");
            for (KoreaStockAnalysis stock : stocks) {
                System.out.println(stock);
            }

            // 현재 시간으로 업로드 일시 설정
            Timestamp now = new Timestamp(System.currentTimeMillis());
            stocks.forEach(stock -> stock.setUploadDate(now));
         
            // 데이터 저장
            repository.saveAll(stocks);

            // 로그 추가
            String marketType = "KR";
            String logDescription = String.format("%s 시장 데이터 %d건 업데이트 완료", marketType, stocks.size());

            UpdateLog updateLog = new UpdateLog();
            updateLog.setUpdateTime(now);
            updateLog.setMarketType(marketType);
            updateLog.setDescription(logDescription);

            updateLogRepository.save(updateLog);
            
            return "CSV 데이터가 성공적으로 저장되었습니다!";
        } catch (Exception e) {
            // 오류 발생 시 디버깅 정보 출력
            System.err.println("CSV 처리 중 오류 발생!");
            e.printStackTrace();
            return "CSV 저장 중 오류 발생: " + e.getMessage();
        }
    }
    
    public KoreaStockAnalysis getStockByCode(String stockcode) {
        return repository.findByStockcode(stockcode).orElse(null);
    }
}
