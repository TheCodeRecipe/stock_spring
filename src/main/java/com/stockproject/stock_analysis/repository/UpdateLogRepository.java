package com.stockproject.stock_analysis.repository;

import com.stockproject.stock_analysis.entity.UpdateLog;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;

@Repository
public interface UpdateLogRepository extends JpaRepository<UpdateLog, Long> {
	
	Optional<UpdateLog> findTopByMarketTypeOrderByUpdateTimeDesc(String marketType);
}
