package com.stockproject.stock_analysis.repository;

import com.stockproject.stock_analysis.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {
    Optional<Stock> findByStockCode(String stockCode);
    Optional<Stock> findByStockName(String stockName);
   
    @Query("SELECT s FROM Stock s WHERE s.id NOT IN :ids")
    Page<Stock> findAllByIdNotIn(@Param("ids") List<Long> ids, Pageable pageable);
}
