package com.example.saasplatform1.customanalyticsrestapi.service;


import com.example.saasplatform1.customanalyticsrestapi.contract.GetTotalProfitAndCountResponse;

import com.example.saasplatform1.customanalyticsrestapi.contract.CustomAnalyticsDataFilterRequest;

import com.example.saasplatform1.customanalyticsrestapi.contract.CustomAnalyticsDataResponse;
import com.example.saasplatform1.customanalyticsrestapi.exception.CustomDataUploadException;
import com.example.saasplatform1.customanalyticsrestapi.exception.FileNotPresentException;
import com.example.saasplatform1.customanalyticsrestapi.model.CustomAnalyticsData;
import com.example.saasplatform1.customanalyticsrestapi.repository.CustomAnalyticsRepository;
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
import jakarta.persistence.criteria.Predicate;

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
import java.util.stream.Collectors;

@Service
public class CustomAnalyticsService {
    public final CustomAnalyticsRepository customAnalyticsRepository;
    public final ModelMapper modelMapper;

    @Autowired
    public CustomAnalyticsService(CustomAnalyticsRepository customAnalyticsRepository, ModelMapper modelMapper) {
        this.customAnalyticsRepository = customAnalyticsRepository;
        this.modelMapper = modelMapper;
    }


    public String uploadDataFromCsv(MultipartFile file) {
        if (file == null || (!file.getOriginalFilename().endsWith(".csv"))) {
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
        } catch (IOException | ParseException ex) {
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
            (int pageNo, int pageSize, String sortBy, String order, CustomAnalyticsDataFilterRequest filter) {

        Sort sort = Sort.by(Sort.Direction.fromString(order), sortBy);
        Pageable pageable = PageRequest.of(pageNo, pageSize, sort);

        Page<CustomAnalyticsData> customAnalyticsData = customAnalyticsRepository.findAll(getSpecification(filter), pageable);

        return customAnalyticsData.getContent().stream().map(customAnalyticsData1 ->
                        modelMapper.map(customAnalyticsData1, CustomAnalyticsDataResponse.class))
                .collect(Collectors.toList());
    }
    private Specification<CustomAnalyticsData> getSpecification(CustomAnalyticsDataFilterRequest filter) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if(filter.getDate() != null){
                predicates.add(criteriaBuilder.equal(root.get("date"), filter.getDate()));
            }
            if (filter.getProductCategory() != null) {
                predicates.add(criteriaBuilder.equal(root.get("productCategory"), filter.getProductCategory()));
            }
            if (filter.getGeographicLocation() != null) {
                predicates.add(criteriaBuilder.equal(root.get("geographicLocation"), filter.getGeographicLocation()));
            }
            query.where(predicates.toArray(new Predicate[0]));

            return null;
        };
    }

    public String generateCsvTemplate() {
        StringBuilder csvTemplate = new StringBuilder();
        String[] headers = {
                "Date",
                "Product Category",
                "Geographic Location",
                "Total Sales",
                "Total Profit",
                "Average Session Time",
                "Unique Visitors"
        };
        // Append the headers
        for (String header : headers) {
            csvTemplate.append(header).append(","); // Use a tab as a delimiter
        }
        csvTemplate.setLength(csvTemplate.length() - 1);
        csvTemplate.append("\n");

        return csvTemplate.toString();
    }

    public GetTotalProfitAndCountResponse getTotalSalesAndCount(LocalDate fromDate, LocalDate toDate, String productCategory) {
        List<CustomAnalyticsData> dataList = new ArrayList<>();

        if (fromDate != null && toDate != null && productCategory != null) {
            dataList = customAnalyticsRepository.findByDateBetweenAndProductCategory(fromDate, toDate, productCategory);
        } else if (fromDate != null && toDate != null) {
            dataList = customAnalyticsRepository.findByDateBetween(fromDate, toDate);
        }

        double totalCount = dataList.stream().mapToDouble(C->C.getTotalSales()).sum();
        double totalProfit = dataList.stream().mapToDouble(C->C.getTotalProfit()).sum();
        return GetTotalProfitAndCountResponse.builder().totalProductCount(totalCount).totalProductProfit(totalProfit).build();
    }

    public GetTotalProfitAndCountResponse getLocationTotalSalesAndCount(LocalDate fromDate, LocalDate toDate, String geographicLocation) {
        List<CustomAnalyticsData> dataList = new ArrayList<>();

        if (fromDate != null && toDate != null && geographicLocation != null) {
            dataList = customAnalyticsRepository.findByDateBetweenAndGeographicLocation(fromDate, toDate, geographicLocation);
        } else if (fromDate != null && toDate != null) {
            dataList = customAnalyticsRepository.findByDateBetween(fromDate, toDate);
        }

        double totalCount = dataList.stream().mapToDouble(C->C.getTotalSales()).sum();
        double totalProfit = dataList.stream().mapToDouble(C->C.getTotalProfit()).sum();
        return GetTotalProfitAndCountResponse.builder().totalProductCount(totalCount).totalProductProfit(totalProfit).build();
    }

}
