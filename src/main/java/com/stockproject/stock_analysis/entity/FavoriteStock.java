package com.stockproject.stock_analysis.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "favorite_stock")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String stockName;

    @Column(nullable = false, unique = true)
    private String stockCode;
}
