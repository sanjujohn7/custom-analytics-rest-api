package com.example.saasplatform1.customanalyticsrestapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomAnalyticsData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    private String productCategory;

    private String geographicLocation;

    private double totalSales;

    private double totalProfit;

    private double averageSessionTime;

    private int uniqueVisitors;

}
