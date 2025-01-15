package com.stockproject.stock_analysis.repository;

import com.stockproject.stock_analysis.entity.FavoriteStock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteStockRepository extends JpaRepository<FavoriteStock, Long> {
	boolean existsByStockCodeOrStockName(String stockCode, String stockName);
}
