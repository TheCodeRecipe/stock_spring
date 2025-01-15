package com.stockproject.stock_analysis.controller;

import com.stockproject.stock_analysis.entity.UserFavorite;
import com.stockproject.stock_analysis.service.FavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/userfavorites")
public class FavoriteController {
    private final FavoriteService favoriteService;

    @Autowired
    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public ResponseEntity<?> addFavorite(@RequestBody Map<String, Object> request) {
        String userName = (String) request.get("userName");
        Long stockId = Long.valueOf(String.valueOf(request.get("stockId")));

        String message = favoriteService.addFavoriteById(userName, stockId);
        return ResponseEntity.ok(Map.of("success", true, "message", message));
    }


    @GetMapping
    public ResponseEntity<?> getFavorites(@RequestParam("username") String username) {
        List<Map<String, Object>> favorites = favoriteService.getFavorites(username);
        return ResponseEntity.ok(favorites);
    }
    
    
    @PostMapping("/delete")
    public ResponseEntity<?> deleteFavorite(@RequestBody Map<String, Object> request) {
        String username = (String) request.get("userName");
        String stockCode = (String) request.get("stockCode");

        favoriteService.deleteFavoriteByUsernameAndStockCode(username, stockCode);

        return ResponseEntity.ok(Map.of("success", true, "message", "관심 종목이 삭제되었습니다."));
    }


}
