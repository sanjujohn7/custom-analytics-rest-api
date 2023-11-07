package com.example.saasplatform1.customanalyticsrestapi.controller;

import com.example.saasplatform1.customanalyticsrestapi.contract.CustomAnalyticsDataResponse;
import com.example.saasplatform1.customanalyticsrestapi.model.CustomAnalyticsData;
import com.example.saasplatform1.customanalyticsrestapi.service.CustomAnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
            @RequestParam(value = "filterBy", required = false) String filterBy
    ){
        List<CustomAnalyticsDataResponse> response = customAnalyticsService.searchBasedOnFilterAndSort(
                pageNo, pageSize, sortBy, order, filterBy
        );
        return ResponseEntity.ok(response);
    }

}
