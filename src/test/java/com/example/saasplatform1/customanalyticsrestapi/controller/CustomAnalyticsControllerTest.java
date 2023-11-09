package com.example.saasplatform1.customanalyticsrestapi.controller;


import com.example.saasplatform1.customanalyticsrestapi.contract.CustomAnalyticsDataFilterRequest;
import com.example.saasplatform1.customanalyticsrestapi.contract.CustomAnalyticsDataResponse;
import com.example.saasplatform1.customanalyticsrestapi.contract.GetTotalProfitAndCountResponse;
import com.example.saasplatform1.customanalyticsrestapi.service.CustomAnalyticsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
public class CustomAnalyticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomAnalyticsService customAnalyticsService;

    @Test
    public void testUploadFile() throws Exception {

        //Given
        MockMultipartFile file = new MockMultipartFile("file", "test.csv", "text/plain", "file content".getBytes());

        //When
        when(customAnalyticsService.uploadDataFromCsv(any(MultipartFile.class))).thenReturn("File uploaded successfully");

        //Then
        mockMvc.perform(multipart("/custom-analytics/upload")
                        .file(file))
                .andExpect(status().isCreated())
                .andExpect(content().string("File uploaded successfully"));
    }

    @Test
    public void testListAllCustomAnalyticsData() throws Exception {
        //Given
        int pageNo = 0;
        int pageSize = 10;
        String path = "/custom-analytics/list-all";
        CustomAnalyticsDataResponse response = new CustomAnalyticsDataResponse();
        response.setId(1L);
        response.setGeographicLocation("Test");
        response.setTotalSales(1.0);

        //Then
        when(customAnalyticsService.listAllCustomAnalyticsData(pageNo, pageSize)).thenReturn(List.of(response));

        mockMvc.perform(get(path)
                        .param("pageNo", String.valueOf(pageNo))
                        .param("pageSize", String.valueOf(pageSize))
                        .contentType(MediaType.APPLICATION_JSON))

                //When
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].geographicLocation", is("Test")))
                .andExpect(jsonPath("$[0].totalSales", is(1.0)));
    }

    @Test
    public void testDownloadCsvTemplate() throws Exception {
        // Given
        String csvTemplate = "Header1,Header2,Value1,Value2";
        when(customAnalyticsService.generateCsvTemplate()).thenReturn(csvTemplate);

        mockMvc.perform(get("/custom-analytics/download-csv-template"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=csv-template.csv"))
                .andExpect(header().string("Content-Type", "text/csv"))
                .andReturn();
    }

    @Test
    public void testListSearchCustomAnalyticsData() throws Exception {
        //Given
        int pageNo = 0;
        int pageSize = 10;
        String sortBy = "date";
        String order ="asc";


        CustomAnalyticsDataFilterRequest filter = new CustomAnalyticsDataFilterRequest();
        ObjectMapper objectMapper = new ObjectMapper();
        String request = objectMapper.writeValueAsString(filter);
        filter.setProductCategory("Electronics");

        String path = "/custom-analytics/search";
        CustomAnalyticsDataResponse response = new CustomAnalyticsDataResponse();
        response.setId(1L);
        response.setGeographicLocation("Test");
        response.setTotalSales(1.0);

        //Then
        when(customAnalyticsService.searchBasedOnFilterAndSort(pageNo, pageSize,sortBy,order,filter)).thenReturn(List.of(response));

        mockMvc.perform(get(path)
                        .param("pageNo", String.valueOf(pageNo))
                        .param("pageSize", String.valueOf(pageSize))
                        .param("sortBy", sortBy)
                        .param("order", order)
                        .param("productCategory", filter.getProductCategory())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))

                //When
                .andExpect(status().isOk())
                /*.andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].geographicLocation", is("Test")))
                .andExpect(jsonPath("$[0].totalSales", is(1.0)))*/;
    }

    @Test
    public void tesGetTotalSalesAndCount() throws Exception {
        //Given
        LocalDate fromDate = LocalDate.parse("2023-01-01");;
        LocalDate toDate = LocalDate.parse("2023-01-02");
        String productCategory = "Electronics";



        String path = "/custom-analytics/product-category/total";
        GetTotalProfitAndCountResponse response = new GetTotalProfitAndCountResponse();
        response.setTotalProductCount(100.0);
        response.setTotalProductProfit(100000.0);


        //Then
        when(customAnalyticsService.getTotalSalesAndCount(fromDate,toDate,productCategory)).thenReturn(response);

        mockMvc.perform(get(path)
                        .param("fromDate", String.valueOf(fromDate))
                        .param("toDate", String.valueOf(toDate))

                        .param("productCategory", String.valueOf(productCategory))
                        .contentType(MediaType.APPLICATION_JSON))


                //When
                .andExpect(status().isOk())
                //.andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("totalProductCount", is(100.0)))
                .andExpect(jsonPath("totalProductProfit", is(100000.0)));
    }

    @Test
    public void tesGetLocationTotalSalesAndCount() throws Exception {
        //Given
        LocalDate fromDate = LocalDate.parse("2023-01-01");;
        LocalDate toDate = LocalDate.parse("2023-01-02");
        String geographicLocation = "Test";



        String path = "/custom-analytics/geographic-location/total";
        GetTotalProfitAndCountResponse response = new GetTotalProfitAndCountResponse();
        response.setTotalProductCount(100.0);
        response.setTotalProductProfit(100000.0);


        //Then
        when(customAnalyticsService.getLocationTotalSalesAndCount(fromDate,toDate,geographicLocation)).thenReturn(response);

        mockMvc.perform(get(path)
                        .param("fromDate", String.valueOf(fromDate))
                        .param("toDate", String.valueOf(toDate))

                        .param("geographicLocation", String.valueOf(geographicLocation))
                        .contentType(MediaType.APPLICATION_JSON))


                //When
                .andExpect(status().isOk())
                //.andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("totalProductCount", is(100.0)))
                .andExpect(jsonPath("totalProductProfit", is(100000.0)));
    }

}
