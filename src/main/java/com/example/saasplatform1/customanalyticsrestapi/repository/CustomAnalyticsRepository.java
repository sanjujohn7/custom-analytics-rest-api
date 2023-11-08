package com.example.saasplatform1.customanalyticsrestapi.repository;

import com.example.saasplatform1.customanalyticsrestapi.model.CustomAnalyticsData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CustomAnalyticsRepository extends JpaRepository<CustomAnalyticsData, Long>, JpaSpecificationExecutor<CustomAnalyticsData> {
}
