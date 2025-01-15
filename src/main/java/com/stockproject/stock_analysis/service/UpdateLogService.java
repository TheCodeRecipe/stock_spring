package com.stockproject.stock_analysis.service;

import com.stockproject.stock_analysis.entity.UpdateLog;
import com.stockproject.stock_analysis.repository.UpdateLogRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UpdateLogService {

    private final UpdateLogRepository updateLogRepository;

    public UpdateLogService(UpdateLogRepository updateLogRepository) {
        this.updateLogRepository = updateLogRepository;
    }

    public Optional<UpdateLog> getLastUpdate(String marketType) {
        return updateLogRepository.findTopByMarketTypeOrderByUpdateTimeDesc(marketType);
    }
}
