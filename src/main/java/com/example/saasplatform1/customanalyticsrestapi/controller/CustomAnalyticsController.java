package com.example.saasplatform1.customanalyticsrestapi.controller;


import com.example.saasplatform1.customanalyticsrestapi.contract.GetTotalProfitAndCountResponse;

import com.example.saasplatform1.customanalyticsrestapi.contract.CustomAnalyticsDataFilterRequest;

import com.example.saasplatform1.customanalyticsrestapi.contract.CustomAnalyticsDataResponse;
import com.example.saasplatform1.customanalyticsrestapi.service.CustomAnalyticsService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/custom-analytics")
public class CustomAnalyticsController {
    public final CustomAnalyticsService customAnalyticsService;
    @Autowired
    public CustomAnalyticsController(CustomAnalyticsService customAnalyticsService){
        this.customAnalyticsService = customAnalyticsService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file){
        String response = customAnalyticsService.uploadDataFromCsv(file);
      return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/list-all")
    public ResponseEntity<List<CustomAnalyticsDataResponse>> listAllCustomAnalyticsData(
            @RequestParam(value = "pageNo", defaultValue = "0") int pageNo, @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        List<CustomAnalyticsDataResponse> responseList = customAnalyticsService.listAllCustomAnalyticsData(pageNo, pageSize);
        return ResponseEntity.ok(responseList);
    }

    @GetMapping("/search")
    public ResponseEntity<List<CustomAnalyticsDataResponse>> searchBasedOnFilterAndSort(
            @RequestParam(value = "pageNo", defaultValue = "0") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "sortBy", defaultValue = "date") String sortBy,
            @RequestParam(value = "order", defaultValue = "asc") String order,
            @ModelAttribute CustomAnalyticsDataFilterRequest filter
    ){
        List<CustomAnalyticsDataResponse> response = customAnalyticsService.searchBasedOnFilterAndSort(
                pageNo, pageSize, sortBy, order, filter
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/download-csv-template")
    public void downloadCsvTemplate(HttpServletResponse response) throws IOException {
        // Set the response headers for a CSV file download
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=csv-template.csv");

        String csvTemplate = customAnalyticsService.generateCsvTemplate();

        // Write the template to the response
        response.getWriter().write(csvTemplate);
    }

    @GetMapping("/product-category/total")
    public ResponseEntity<GetTotalProfitAndCountResponse> AggregateProductCategoryCustomAnalyticsData(
            @RequestParam(value = "fromDate", required = true) LocalDate fromDate,
            @RequestParam(value = "toDate", required = true) LocalDate toDate ,
            @RequestParam(value = "productCategory", required = false) String productCategory){
        GetTotalProfitAndCountResponse response = customAnalyticsService.getTotalSalesAndCount(fromDate,toDate,productCategory);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/geographic-location/total")
    public ResponseEntity<GetTotalProfitAndCountResponse> AggregateLocationCustomAnalyticsData(
            @RequestParam(value = "fromDate", required = true) LocalDate fromDate,
            @RequestParam(value = "toDate", required = true) LocalDate toDate ,
            @RequestParam(value = "geographicLocation", required = false) String geographicLocation){
        GetTotalProfitAndCountResponse response = customAnalyticsService.getLocationTotalSalesAndCount(fromDate,toDate,geographicLocation);
        return ResponseEntity.ok(response);
    }

}
