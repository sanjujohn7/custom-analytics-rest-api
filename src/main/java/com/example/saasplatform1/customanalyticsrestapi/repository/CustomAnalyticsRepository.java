package com.example.saasplatform1.customanalyticsrestapi.repository;

import com.example.saasplatform1.customanalyticsrestapi.model.CustomAnalyticsAndDimension;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomAnalyticsRepository extends JpaRepository<CustomAnalyticsAndDimension, Long> {
}
