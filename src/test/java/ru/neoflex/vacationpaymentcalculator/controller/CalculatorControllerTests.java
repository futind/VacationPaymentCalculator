package ru.neoflex.vacationpaymentcalculator.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.neoflex.vacationpaymentcalculator.dto.EmployeeVacationDataDto;
import ru.neoflex.vacationpaymentcalculator.service.ICalculatorService;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CalculatorControllerTests {

    @Mock
    ICalculatorService calculatorService;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @BeforeEach
    void setUp() {
        ICalculatorController calculatorController = new CalculatorController(calculatorService);
        mockMvc = MockMvcBuilders.standaloneSetup(calculatorController).build();
    }

    @Test
    void validRequestReturnsCorrectResult() throws Exception {
        EmployeeVacationDataDto validData = EmployeeVacationDataDto.builder()
                .yearlyPay(BigDecimal.valueOf(600000))
                .startOfVacation(LocalDate.of(1970, 1, 1))
                .endOfVacation(LocalDate.of(1970, 3, 1))
                .build();

        BigDecimal someResult = new BigDecimal(12345);

        when(calculatorService.calculate(any(EmployeeVacationDataDto.class))).thenReturn(someResult);

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.get("/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validData))
        ).andExpect(
                status().isOk()
        ).andReturn();

        BigDecimal returnedResult = new BigDecimal(mvcResult.getResponse().getContentAsString());

        assertEquals(someResult, returnedResult);
    }

    @Test
    void NegativeYearlyPayIsBadRequest() throws Exception {
        EmployeeVacationDataDto invalidData = EmployeeVacationDataDto.builder()
                .yearlyPay(BigDecimal.valueOf(-1))
                .startOfVacation(LocalDate.of(1970, 1, 1))
                .endOfVacation(LocalDate.of(1970, 2, 1))
                .build();

        this.mockMvc.perform(MockMvcRequestBuilders.get("/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidData))
        ).andExpect(
                status().isBadRequest()
        );

        verify(calculatorService, never()).calculate(any());
    }

    @Test
    void wronglyOrderedDatesReturnBadRequest() throws Exception {
        EmployeeVacationDataDto invalidDataDto = EmployeeVacationDataDto.builder()
                .yearlyPay(BigDecimal.valueOf(600000))
                .startOfVacation(LocalDate.of(2000, 1, 1))
                .endOfVacation(LocalDate.of(1999, 1, 1))
                .build();

        this.mockMvc.perform(MockMvcRequestBuilders.get("/calculate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidDataDto))
        ).andExpect(
                status().isBadRequest()
        );

        verify(calculatorService, never()).calculate(any());
    }
}
