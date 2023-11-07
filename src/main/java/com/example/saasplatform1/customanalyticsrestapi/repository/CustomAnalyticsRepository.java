package com.example.saasplatform1.customanalyticsrestapi.repository;

import com.example.saasplatform1.customanalyticsrestapi.model.CustomAnalyticsData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CustomAnalyticsRepository extends JpaRepository<CustomAnalyticsData, Long> {
}
