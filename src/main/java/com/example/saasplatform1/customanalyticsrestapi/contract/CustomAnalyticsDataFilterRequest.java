package com.example.saasplatform1.customanalyticsrestapi.contract;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomAnalyticsDataFilterRequest {
    private LocalDate date;

    private String productCategory;

    private String geographicLocation;
}
