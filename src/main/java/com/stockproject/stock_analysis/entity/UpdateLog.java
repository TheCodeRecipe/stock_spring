package com.stockproject.stock_analysis.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "update_logs")
@Getter
@Setter
@NoArgsConstructor
public class UpdateLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "update_time", nullable = false)
    private Timestamp updateTime;

    @Column(name = "market_type", nullable = false)
    private String marketType;

    @Column(name = "description", nullable = false)
    private String description;
}
