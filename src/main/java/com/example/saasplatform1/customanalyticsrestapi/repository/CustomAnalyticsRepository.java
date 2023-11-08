package com.example.saasplatform1.customanalyticsrestapi.repository;

import com.example.saasplatform1.customanalyticsrestapi.model.CustomAnalyticsData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.time.LocalDate;
import java.util.List;

public interface CustomAnalyticsRepository extends JpaRepository<CustomAnalyticsData, Long>, JpaSpecificationExecutor<CustomAnalyticsData> {
    List<CustomAnalyticsData> findByDateBetweenAndProductCategory(LocalDate fromDate, LocalDate toDate, String productCategory);

    List<CustomAnalyticsData> findByDateBetweenAndGeographicLocation(LocalDate fromDate, LocalDate toDate, String geographicLocation);

    List<CustomAnalyticsData> findByDateBetween(LocalDate fromDate, LocalDate toDate);

}

