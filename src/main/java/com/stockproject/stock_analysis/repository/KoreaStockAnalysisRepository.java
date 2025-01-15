package com.stockproject.stock_analysis.repository;

import com.stockproject.stock_analysis.entity.KoreaStockAnalysis;
import com.stockproject.stock_analysis.entity.Stock;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface KoreaStockAnalysisRepository extends JpaRepository<KoreaStockAnalysis, Long> {
	List<KoreaStockAnalysis> findByStockcodeIn(List<String> stockcodes);
	
	@Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE korea_stock_analysis", nativeQuery = true)
    void truncateTable();
	
	Optional<KoreaStockAnalysis> findByStockcode(String stockcode);
}
