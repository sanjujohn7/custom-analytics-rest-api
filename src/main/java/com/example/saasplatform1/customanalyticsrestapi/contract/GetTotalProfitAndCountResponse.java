package com.example.saasplatform1.customanalyticsrestapi.contract;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetTotalProfitAndCountResponse {
    private double totalProductProfit;

    private double totalProductCount;

}
