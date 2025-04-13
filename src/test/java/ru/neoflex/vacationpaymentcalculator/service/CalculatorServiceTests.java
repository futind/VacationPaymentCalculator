package ru.neoflex.vacationpaymentcalculator.service;

import jdk.jfr.Description;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.neoflex.vacationpaymentcalculator.dto.EmployeeVacationDataDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class CalculatorServiceTests {

    private static final BigDecimal AVERAGE_CALENDAR_DAYS_PER_MONTH = BigDecimal.valueOf(29.3);

    private static final Integer MONTHS_IN_YEAR = 12;
    private static final BigDecimal AVERAGE_CALENDAR_DAYS_PER_YEAR = AVERAGE_CALENDAR_DAYS_PER_MONTH
                                                                     .multiply(BigDecimal.valueOf(MONTHS_IN_YEAR));

    private static final BigDecimal PROGRESSIVE_TAXING_THRESHOLD = BigDecimal.valueOf(5e+6);
    private static final BigDecimal PROGRESSIVE_TAX_RATE = BigDecimal.valueOf(0.15);
    private static final BigDecimal DEFAULT_TAX_RATE = BigDecimal.valueOf(0.13);

    private ICalculatorService calculatorService;

    @BeforeEach
    void setUp() {
        calculatorService = new CalculatorService();
    }

    @Test
    @Description("Simple test without holidays, with 4 weekends")
    void canCalculateWithoutHolidays() {

        // monthly salary = 50.000, yearlyPay = 600.000
        BigDecimal yearlyPay = new BigDecimal("600000");
        // 14 days, 4 weekends, 0 holidays => 10 working days
        LocalDate startDate = LocalDate.of(2025, 4, 1);
        LocalDate endDate = LocalDate.of(2025, 4, 15);

        // dailyPay = yearlyPay / AVERAGE_CALENDAR_DAYS_PER_YEAR
        BigDecimal dailyPay = yearlyPay.divide(AVERAGE_CALENDAR_DAYS_PER_YEAR, 2, RoundingMode.HALF_UP);
        // vacationPay = dailyPay * (10)
        BigDecimal vacationPay = dailyPay.multiply(BigDecimal.valueOf(10));
        BigDecimal taxedVacationPay = vacationPay.multiply(BigDecimal.ONE.subtract(DEFAULT_TAX_RATE));


        EmployeeVacationDataDto vacationDataDto = EmployeeVacationDataDto.builder()
                .yearlyPay(yearlyPay)
                .startOfVacation(startDate)
                .endOfVacation(endDate)
                .build();
        BigDecimal result = calculatorService.calculate(vacationDataDto);

        assertTrue((taxedVacationPay.subtract(result)).abs().compareTo(BigDecimal.valueOf(0.01)) < 0);
    }

    @Test
    @Description("Tricky test with more holidays that paid days")
    void canCalculateHolidaysCorrectly() {
        // monthly salary = 50.000, yearlyPay = 600.000
        BigDecimal yearlyPay = new BigDecimal("600000");
        // 14 days, 2 weekends, 8 holidays => 4 working days
        LocalDate startDate = LocalDate.of(2024, 12, 31);
        LocalDate endDate = LocalDate.of(2025, 1, 14);

        // dailyPay = yearlyPay / AVERAGE_CALENDAR_DAYS_PER_YEAR
        BigDecimal dailyPay = yearlyPay.divide(AVERAGE_CALENDAR_DAYS_PER_YEAR, 2, RoundingMode.HALF_UP);
        // vacationPay = dailyPay * (4)
        BigDecimal vacationPay = dailyPay.multiply(BigDecimal.valueOf(4));
        BigDecimal taxedVacationPay = vacationPay.multiply(BigDecimal.ONE.subtract(DEFAULT_TAX_RATE));


        EmployeeVacationDataDto vacationDataDto = EmployeeVacationDataDto.builder()
                .yearlyPay(yearlyPay)
                .startOfVacation(startDate)
                .endOfVacation(endDate)
                .build();
        BigDecimal result = calculatorService.calculate(vacationDataDto);

        assertTrue((taxedVacationPay.subtract(result)).abs().compareTo(BigDecimal.valueOf(0.01)) < 0);
    }

    @Test
    @Description("Test which indicates that progressive tax rate is being used when needed")
    void appliesProgressiveTaxRate() {
        BigDecimal yearlyPay = PROGRESSIVE_TAXING_THRESHOLD.add(BigDecimal.ONE);

        LocalDate startDate = LocalDate.of(2025, 4, 1);
        LocalDate endDate = LocalDate.of(2025, 4, 15);

        // dailyPay = yearlyPay / AVERAGE_CALENDAR_DAYS_PER_YEAR
        BigDecimal dailyPay = yearlyPay.divide(AVERAGE_CALENDAR_DAYS_PER_YEAR, 2, RoundingMode.HALF_UP);

        // vacationPay = dailyPay * (10)
        BigDecimal vacationPay = dailyPay.multiply(BigDecimal.valueOf(10));
        // apply progressive tax rate
        BigDecimal taxedVacationPay = vacationPay.multiply(BigDecimal.ONE.subtract(PROGRESSIVE_TAX_RATE));

        EmployeeVacationDataDto vacationDataDto = EmployeeVacationDataDto.builder()
                .yearlyPay(yearlyPay)
                .startOfVacation(startDate)
                .endOfVacation(endDate)
                .build();
        BigDecimal result = calculatorService.calculate(vacationDataDto);

        assertTrue((taxedVacationPay.subtract(result)).abs().compareTo(BigDecimal.valueOf(0.01)) < 0);
    }
}
