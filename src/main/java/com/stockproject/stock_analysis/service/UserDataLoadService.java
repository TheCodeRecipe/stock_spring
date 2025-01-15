package com.stockproject.stock_analysis.service;

import com.stockproject.stock_analysis.entity.KoreaStockAnalysis;
import com.stockproject.stock_analysis.repository.KoreaStockAnalysisRepository;
import com.stockproject.stock_analysis.repository.UserFavoriteRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserDataLoadService {

    private final UserFavoriteRepository userFavoriteRepository;
    private final KoreaStockAnalysisRepository koreaStockAnalysisRepository;

    @Autowired
    public UserDataLoadService(UserFavoriteRepository userFavoriteRepository, KoreaStockAnalysisRepository koreaStockAnalysisRepository) {
        this.userFavoriteRepository = userFavoriteRepository;
        this.koreaStockAnalysisRepository = koreaStockAnalysisRepository;
    }

    public List<KoreaStockAnalysis> getUserAnalysisData(String username) {
        // 관심 종목 가져오기
        List<String> favoriteStockCodes = userFavoriteRepository.findByUserUsername(username)
            .stream()
            .map(userFavorite -> userFavorite.getStock().getStockCode())
            .map(stockCode -> {
                // 숫자만 추출 후 앞의 0 제거
                String numericCode = stockCode.replaceAll("\\D", "");
                return String.valueOf(Integer.parseInt(numericCode)); // 앞의 0 제거
            })
            .toList();

        // 분석 데이터 가져오기
        return koreaStockAnalysisRepository.findByStockcodeIn(favoriteStockCodes);
    }
    
    // 전체 데이터를 가져오기
    public List<KoreaStockAnalysis> getAllAnalysisData() {
        return koreaStockAnalysisRepository.findAll();
    }
}
