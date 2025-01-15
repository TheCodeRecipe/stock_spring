package com.stockproject.stock_analysis.entity;

import com.opencsv.bean.CsvBindByName;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Table(name = "korea_stock_analysis")
@Getter
@Setter
@NoArgsConstructor
public class KoreaStockAnalysis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CsvBindByName(column = "stockname")
    @Column(name = "stockname", nullable = false)
    private String stockname;

    @CsvBindByName(column = "stockcode")
    @Column(name = "stockcode", nullable = false, unique = true)
    private String stockcode;

    @CsvBindByName(column = "currentprice")
    @Column(name = "currentprice")
    private Float currentprice;

    @CsvBindByName(column = "price_change_value")
    @Column(name = "price_change_value")
    private Float priceChangeValue;

    @CsvBindByName(column = "price_change_status")
    @Column(name = "price_change_status")
    private String priceChangeStatus;

    @CsvBindByName(column = "volume")
    @Column(name = "volume")
    private Long volume;

    @CsvBindByName(column = "volumechangerate")
    @Column(name = "volumechangerate")
    private Float volumechangerate;

    @CsvBindByName(column = "action")
    @Column(name = "action")
    private String action;

    @CsvBindByName(column = "candle_pattern")
    @Column(name = "candle_pattern")
    private String candlePattern;

    @CsvBindByName(column = "macd_trend")
    @Column(name = "macd_trend")
    private String macdTrend;

    @CsvBindByName(column = "rsi_status")
    @Column(name = "rsi_status")
    private String rsiStatus;

    @CsvBindByName(column = "volume_trend")
    @Column(name = "volume_trend")
    private String volumeTrend;

    @CsvBindByName(column = "price_vs_bollinger")
    @Column(name = "price_vs_bollinger")
    private String priceVsBollinger;

    @CsvBindByName(column = "slope_5")
    @Column(name = "slope_5")
    private String slope5;

    @CsvBindByName(column = "slope_20")
    @Column(name = "slope_20")
    private String slope20;

    @CsvBindByName(column = "slope_60")
    @Column(name = "slope_60")
    private String slope60;

    @CsvBindByName(column = "slope_120")
    @Column(name = "slope_120")
    private String slope120;

    @CsvBindByName(column = "recent_max_volume_date")
    @Column(name = "recent_max_volume_date")
    private String recentMaxVolumeDate;

    @CsvBindByName(column = "recent_max_volume_change")
    @Column(name = "recent_max_volume_change")
    private Float recentMaxVolumeChange;

    @CsvBindByName(column = "recent_max_volume_trend")
    @Column(name = "recent_max_volume_trend")
    private String recentMaxVolumeTrend;

    @CsvBindByName(column = "recent_max_volume_value")
    @Column(name = "recent_max_volume_value")
    private Long recentMaxVolumeValue;

    @CsvBindByName(column = "support_1")
    @Column(name = "support_1")
    private String support1;

    @CsvBindByName(column = "support_2")
    @Column(name = "support_2")
    private String support2;

    @CsvBindByName(column = "support_3")
    @Column(name = "support_3")
    private String support3;

    @CsvBindByName(column = "resistance_1")
    @Column(name = "resistance_1")
    private String resistance1;

    @CsvBindByName(column = "resistance_2")
    @Column(name = "resistance_2")
    private String resistance2;

    @CsvBindByName(column = "resistance_3")
    @Column(name = "resistance_3")
    private String resistance3;

    @Column(name = "upload_date", nullable = false)
    private Timestamp uploadDate;

    @Override
    public String toString() {
        return "KoreaStockAnalysis{" +
                "id=" + id +
                ", stockcode='" + stockcode + '\'' +
                ", stockname='" + stockname + '\'' +
                ", currentprice=" + currentprice +
                ", priceChangeValue=" + priceChangeValue +
                ", priceChangeStatus='" + priceChangeStatus + '\'' +
                ", volume=" + volume +
                ", volumechangerate=" + volumechangerate +
                ", action='" + action + '\'' +
                ", candlePattern='" + candlePattern + '\'' +
                ", macdTrend='" + macdTrend + '\'' +
                ", rsiStatus='" + rsiStatus + '\'' +
                ", volumeTrend='" + volumeTrend + '\'' +
                ", priceVsBollinger='" + priceVsBollinger + '\'' +
                ", slope5='" + slope5 + '\'' +
                ", slope20='" + slope20 + '\'' +
                ", slope60='" + slope60 + '\'' +
                ", slope120='" + slope120 + '\'' +
                ", recentMaxVolumeDate='" + recentMaxVolumeDate + '\'' +
                ", recentMaxVolumeChange=" + recentMaxVolumeChange +
                ", recentMaxVolumeTrend='" + recentMaxVolumeTrend + '\'' +
                ", recentMaxVolumeValue=" + recentMaxVolumeValue +
                ", support1='" + support1 + '\'' +
                ", support2='" + support2 + '\'' +
                ", support3='" + support3 + '\'' +
                ", resistance1='" + resistance1 + '\'' +
                ", resistance2='" + resistance2 + '\'' +
                ", resistance3='" + resistance3 + '\'' +
                ", uploadDate=" + uploadDate +
                '}';
    }
}
