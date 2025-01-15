package com.stockproject.stock_analysis.service;

import com.stockproject.stock_analysis.entity.Stock;
import com.stockproject.stock_analysis.entity.User;
import com.stockproject.stock_analysis.repository.StockRepository;
import com.stockproject.stock_analysis.repository.UserFavoriteRepository;
import com.stockproject.stock_analysis.repository.UserRepository;
import com.stockproject.stock_analysis.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class StockService {
    private final StockRepository stockRepository;
    private final UserFavoriteRepository userFavoriteRepository;

    @Autowired
    public StockService(StockRepository stockRepository, UserFavoriteRepository userFavoriteRepository) {
        this.stockRepository = stockRepository;
        this.userFavoriteRepository = userFavoriteRepository;
    }
   
    public Page<Stock> getAvailableStocks(String username, Pageable pageable) {
        List<Long> favoriteStockIds = userFavoriteRepository.findByUserUsername(username)
            .stream()
            .map(userFavorite -> userFavorite.getStock().getId())
            .toList();
        return stockRepository.findAllByIdNotIn(favoriteStockIds, pageable);
    }
}
