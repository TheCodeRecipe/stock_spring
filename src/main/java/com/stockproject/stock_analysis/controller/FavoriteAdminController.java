package com.stockproject.stock_analysis.controller;

import com.stockproject.stock_analysis.entity.FavoriteStock;
import com.stockproject.stock_analysis.repository.FavoriteStockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteAdminController {

    @Autowired
    private FavoriteStockRepository favoriteStockRepository;

    public FavoriteAdminController(FavoriteStockRepository favoriteStockRepository) {
        this.favoriteStockRepository = favoriteStockRepository;
    }
    
    // 관심종목 추가
    @PostMapping
    public ResponseEntity<?> addFavoriteStock(@RequestBody FavoriteStock favoriteStock) {
        // 중복 체크
        boolean exists = favoriteStockRepository.existsByStockCodeOrStockName(
                favoriteStock.getStockCode(),
                favoriteStock.getStockName()
        );
        
        Map<String, Object> response = new HashMap<>();

        if (exists) {
        	response.put("success", false);
            response.put("message", "Stock already exists.");
            return ResponseEntity.ok(response); // HTTP 200 OK
        }

        // 중복이 없으면 저장
        FavoriteStock savedStock = favoriteStockRepository.save(favoriteStock);
        response.put("success", true);
        response.put("message", "관심종목이 성공적으로 추가되었습니다.");
        response.put("data", savedStock);
        return ResponseEntity.ok(response); // HTTP 200 OK
    }

    // 모든 관심종목 조회
    @GetMapping
    public List<FavoriteStock> getAllFavorites() {
        return favoriteStockRepository.findAll();
    }
    
    // 관심종목 수정
    @PutMapping("/{id}")
    public ResponseEntity<?> updateFavoriteStock(@PathVariable("id") Long id, @RequestBody FavoriteStock updatedStock) {
        Optional<FavoriteStock> stockOptional = favoriteStockRepository.findById(id);
        if (stockOptional.isPresent()) {
            FavoriteStock stock = stockOptional.get();

            stock.setStockName(updatedStock.getStockName());
            stock.setStockCode(updatedStock.getStockCode());
            favoriteStockRepository.save(stock);
            return ResponseEntity.ok().body(Map.of("success", true, "message", "관심종목이 성공적으로 업데이트되었습니다."));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("success", false, "message", "관심종목을 찾을 수 없습니다."));
        }
    }


    // 특정 관심종목 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFavoriteStock(@PathVariable("id") Long id) {
        if (favoriteStockRepository.existsById(id)) {
            favoriteStockRepository.deleteById(id);
            return ResponseEntity.ok().body(Map.of("success", true, "message", "관심종목이 성공적으로 삭제되었습니다."));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("success", false, "message", "삭제할 관심종목을 찾을 수 없습니다."));
        }
    }
}
