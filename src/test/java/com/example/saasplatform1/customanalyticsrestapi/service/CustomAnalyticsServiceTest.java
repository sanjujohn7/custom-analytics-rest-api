package com.example.saasplatform1.customanalyticsrestapi.service;


import com.example.saasplatform1.customanalyticsrestapi.contract.CustomAnalyticsDataResponse;
import com.example.saasplatform1.customanalyticsrestapi.model.CustomAnalyticsData;
import com.example.saasplatform1.customanalyticsrestapi.repository.CustomAnalyticsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@AutoConfigureMockMvc
public class CustomAnalyticsServiceTest {

    private CustomAnalyticsRepository customAnalyticsRepository;
    private ModelMapper modelMapper;
    private CustomAnalyticsService customAnalyticsService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        customAnalyticsRepository = Mockito.mock(CustomAnalyticsRepository.class);
        modelMapper = mock(ModelMapper.class);
        customAnalyticsService = new CustomAnalyticsService(customAnalyticsRepository,modelMapper);
    }

    @Test
    public void testUploadDataFromCsv_Success() throws IOException {
        //Given
        String csvContent = "Date,Product Category,Geographic Location,Total Sales,Total Profit,Average Session Time,Unique Visitors\n" +
                "2023-01-01,Category1,Location1,100.0,50.0,30.5,500\n" +
                "2023-01-02,Category2,Location2,200.0,75.0,25.0,600";

        MultipartFile mockFile = mock(MultipartFile.class);

        //When
        when(mockFile.getOriginalFilename()).thenReturn("sample.csv");
        when(mockFile.getInputStream()).thenReturn(new ByteArrayInputStream(csvContent.getBytes()));

        String result = customAnalyticsService.uploadDataFromCsv(mockFile);

        verify(customAnalyticsRepository, times(2)).save(any());

        //Then
        assertEquals("CSV file uploaded and data loaded successfully.", result);
    }


    @Test
    public void testListAllCustomAnalyticsData() {

        //Given
        int pageNo = 1;
        int pageSize = 10;

        List<CustomAnalyticsData> customAnalyticsDataList = new ArrayList<>();
        customAnalyticsDataList.add(new CustomAnalyticsData(1L, LocalDate.parse("2023-10-30"),"Test","Test",1.0,1.0,1.0,1));

        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<CustomAnalyticsData> page = new PageImpl<>(customAnalyticsDataList);


        CustomAnalyticsDataResponse response = new CustomAnalyticsDataResponse();
        response.setId(1L);
        response.setDate(LocalDate.parse("2023-10-30"));
        response.setProductCategory("Test");

        //When
        when(customAnalyticsRepository.findAll(pageable)).thenReturn(page);
        when(modelMapper.map(customAnalyticsDataList.get(0), CustomAnalyticsDataResponse.class)).thenReturn(response);

        List<CustomAnalyticsDataResponse> result = customAnalyticsService.listAllCustomAnalyticsData(pageNo, pageSize);

        //Then
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(LocalDate.parse("2023-10-30"), result.get(0).getDate());
        assertEquals("Test", result.get(0).getProductCategory());
    }
}
