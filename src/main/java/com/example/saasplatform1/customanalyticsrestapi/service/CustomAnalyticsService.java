package com.example.saasplatform1.customanalyticsrestapi.service;

import com.example.saasplatform1.customanalyticsrestapi.contract.CustomAnalyticsDataResponse;
import com.example.saasplatform1.customanalyticsrestapi.exception.CustomDataUploadException;
import com.example.saasplatform1.customanalyticsrestapi.exception.FileNotPresentException;
import com.example.saasplatform1.customanalyticsrestapi.model.CustomAnalyticsData;
import com.example.saasplatform1.customanalyticsrestapi.repository.CustomAnalyticsRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class CustomAnalyticsService {
    public final CustomAnalyticsRepository customAnalyticsRepository;
    public final ModelMapper modelMapper;
    @Autowired
    public CustomAnalyticsService(CustomAnalyticsRepository customAnalyticsRepository, ModelMapper modelMapper){
        this.customAnalyticsRepository = customAnalyticsRepository;
        this.modelMapper = modelMapper;
    }


    public String uploadDataFromCsv(MultipartFile file) {
        if (file == null || (!file.getOriginalFilename().endsWith(".csv"))){
        throw new FileNotPresentException("Invalid file. Please provide a CSV file.");
        }
        try (Reader reader = new InputStreamReader(file.getInputStream())) {
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            for (CSVRecord csvRecord : csvParser) {
                CustomAnalyticsData customAnalyticsData = CustomAnalyticsData.builder()
                        .date(parseDate(dateFormat, csvRecord.get("Date")))
                        .productCategory(csvRecord.get("Product Category"))
                        .geographicLocation(csvRecord.get("Geographic Location"))
                        .totalSales(Double.parseDouble(csvRecord.get("Total Sales")))
                        .totalProfit(Double.parseDouble(csvRecord.get("Total Profit")))
                        .averageSessionTime(Double.parseDouble(csvRecord.get("Average Session Time")))
                        .uniqueVisitors(Integer.parseInt(csvRecord.get("Unique Visitors")))
                        .build();

                customAnalyticsRepository.save(customAnalyticsData);
            }
            return "CSV file uploaded and data loaded successfully.";
        }catch (IOException | ParseException ex){
            throw new CustomDataUploadException("Failed to upload and process CSV file");
        }
    }
    private LocalDate parseDate(SimpleDateFormat dateFormat, String dateString) throws ParseException {
        Date parsedDate = dateFormat.parse(dateString);
        return parsedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }


    public List<CustomAnalyticsDataResponse> listAllCustomAnalyticsData(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<CustomAnalyticsData> customAnalyticsData =
                customAnalyticsRepository.findAll(pageable);
        return customAnalyticsData.getContent().stream().map(customAnalyticsData1 ->
                modelMapper.map(customAnalyticsData1, CustomAnalyticsDataResponse.class))
                .collect(Collectors.toList());
    }

    public List<CustomAnalyticsDataResponse> searchBasedOnFilterAndSort
            (int pageNo, int pageSize, String sortBy, String order, String filterBy) {

        Sort sort = Sort.by(Sort.Direction.fromString(order), sortBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<CustomAnalyticsData> customAnalyticsData = customAnalyticsRepository.findAll(pageable);

        return customAnalyticsData.getContent().stream().map(customAnalyticsData1 ->
                        modelMapper.map(customAnalyticsData1, CustomAnalyticsDataResponse.class))
                .collect(Collectors.toList());
    }

}
