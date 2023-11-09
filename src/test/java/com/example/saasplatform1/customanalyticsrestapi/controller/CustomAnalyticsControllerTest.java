package com.example.saasplatform1.customanalyticsrestapi.controller;


import com.example.saasplatform1.customanalyticsrestapi.contract.CustomAnalyticsDataResponse;
import com.example.saasplatform1.customanalyticsrestapi.service.CustomAnalyticsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
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
}
