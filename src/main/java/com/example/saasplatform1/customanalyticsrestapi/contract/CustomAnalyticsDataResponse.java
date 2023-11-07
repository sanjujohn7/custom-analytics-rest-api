package com.example.saasplatform1.customanalyticsrestapi.contract;

import lombok.*;

import java.time.LocalDate;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomAnalyticsDataResponse {
    private Long id;

    private LocalDate date;

    private String productCategory;

    private String geographicLocation;

    private double totalSales;

    private double totalProfit;

    private double averageSessionTime;

    private int uniqueVisitors;
}
