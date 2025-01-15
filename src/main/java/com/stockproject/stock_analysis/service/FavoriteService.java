package com.stockproject.stock_analysis.service;

import com.stockproject.stock_analysis.entity.Stock;
import com.stockproject.stock_analysis.entity.User;
import com.stockproject.stock_analysis.entity.UserFavorite;
import com.stockproject.stock_analysis.repository.StockRepository;
import com.stockproject.stock_analysis.repository.UserFavoriteRepository;
import com.stockproject.stock_analysis.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class FavoriteService {
    private final UserFavoriteRepository userFavoriteRepository;
    private final StockRepository stockRepository;
    private final UserRepository userRepository;

    @Autowired
    public FavoriteService(UserFavoriteRepository userFavoriteRepository, 
                           StockRepository stockRepository, 
                           UserRepository userRepository) {
        this.userFavoriteRepository = userFavoriteRepository;
        this.stockRepository = stockRepository;
        this.userRepository = userRepository;
    }

    public String addFavorite(String username, String stockCode) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Stock stock = stockRepository.findByStockCode(stockCode)
                .orElseThrow(() -> new IllegalArgumentException("주식을 찾을 수 없습니다."));

        UserFavorite favorite = new UserFavorite();
        favorite.setUser(user);
        favorite.setStock(stock);

        userFavoriteRepository.save(favorite);
        return "관심종목에 추가되었습니다.";
    }
    
    
    public String addFavoriteById(String username, Long stockId) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            return "존재하지 않는 사용자입니다.";
        }

        Optional<Stock> stockOptional = stockRepository.findById(stockId);
        if (stockOptional.isEmpty()) {
            return "존재하지 않는 종목입니다.";
        }

        User user = userOptional.get();
        Stock stock = stockOptional.get();

        if (userFavoriteRepository.existsByUserAndStock(user, stock)) {
            return "이미 추가된 관심 종목입니다.";
        }

        UserFavorite userFavorite = new UserFavorite();
        userFavorite.setUser(user);
        userFavorite.setStock(stock);
        userFavoriteRepository.save(userFavorite);

        return "관심 종목이 성공적으로 추가되었습니다.";
    }
    

    public List<Map<String, Object>> getFavorites(String username) {
        return userFavoriteRepository.findFavoritesWithStockDetails(username);
    }
    
    public void deleteFavoriteByUsernameAndStockCode(String username, String stockCode) {
        UserFavorite userFavorite = userFavoriteRepository.findByUserUsernameAndStockStockCode(username, stockCode)
            .orElseThrow(() -> new IllegalArgumentException("해당 관심 종목을 찾을 수 없습니다."));

        userFavoriteRepository.delete(userFavorite);
    }
}
