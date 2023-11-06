package com.example.saasplatform1.customanalyticsrestapi.service;

import com.example.saasplatform1.customanalyticsrestapi.model.CustomAnalyticsAndDimension;
import com.example.saasplatform1.customanalyticsrestapi.repository.CustomAnalyticsRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Service
public class CustomAnalyticsService {
    public final CustomAnalyticsRepository customAnalyticsRepository;
    @Autowired
    public CustomAnalyticsService(CustomAnalyticsRepository customAnalyticsRepository){
        this.customAnalyticsRepository = customAnalyticsRepository;
    }


    public void uploadDataFromCsv(MultipartFile file) {
        try (Reader reader = new InputStreamReader(file.getInputStream())) {
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader());
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            for (CSVRecord csvRecord : csvParser) {
                CustomAnalyticsAndDimension customAnalyticsAndDimension = CustomAnalyticsAndDimension.builder()
                        .date(parseDate(dateFormat, csvRecord.get("Date")))
                        .productCategory(csvRecord.get("Product Category"))
                        .geographicLocation(csvRecord.get("Geographic Location"))
                        .totalSales(Double.parseDouble(csvRecord.get("Total Sales")))
                        .totalProfit(Double.parseDouble(csvRecord.get("Total Profit")))
                        .averageSessionTime(Double.parseDouble(csvRecord.get("Average Session Time")))
                        .uniqueVisitors(Integer.parseInt(csvRecord.get("Unique Visitors")))
                        .build();

                customAnalyticsRepository.save(customAnalyticsAndDimension);
            }
        }catch (Exception ex){

        }
    }

    private LocalDate parseDate(SimpleDateFormat dateFormat, String dateString) throws ParseException {
        Date parsedDate = dateFormat.parse(dateString);
        return parsedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
