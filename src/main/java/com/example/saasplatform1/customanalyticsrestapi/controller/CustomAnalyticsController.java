package com.example.saasplatform1.customanalyticsrestapi.controller;

import com.example.saasplatform1.customanalyticsrestapi.service.CustomAnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/uploads")
public class CustomAnalyticsController {
    public final CustomAnalyticsService customAnalyticsService;
    @Autowired
    public CustomAnalyticsController(CustomAnalyticsService customAnalyticsService){
        this.customAnalyticsService = customAnalyticsService;
    }

    @PostMapping()
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file){
        if (file != null && file.getOriginalFilename() != null && file.getOriginalFilename().endsWith(".csv")) {
                customAnalyticsService.uploadDataFromCsv(file);
                return ResponseEntity.ok("CSV file uploaded and data loaded successfully.");
        } else {
            return ResponseEntity.badRequest().body("Only CSV files are allowed.");
        }
    }
}
