package ru.neoflex.vacationpaymentcalculator.Service;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import ru.neoflex.vacationpaymentcalculator.Dto.EmployeeVacationDataDto;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.stream.IntStream;

@Service
@Primary
@Log4j2
public class CalculatorService implements ICalculatorService {

    /**
     * Среднее количество календарных дней в месяце
     */
    private static final BigDecimal AVERAGE_CALENDAR_DAYS_PER_MONTH = BigDecimal.valueOf(29.3);

    private static final Integer MONTHS_IN_YEAR = 12;
    private static final BigDecimal AVERAGE_CALENDAR_DAYS_PER_YEAR = AVERAGE_CALENDAR_DAYS_PER_MONTH
                                                             .multiply(BigDecimal.valueOf(MONTHS_IN_YEAR));

    /**
     * Граница, по которой высчитывается ставка налогообложения
     */
    private static final BigDecimal PROGRESSIVE_TAXING_THRESHOLD = BigDecimal.valueOf(5e+6);
    private static final BigDecimal PROGRESSIVE_TAX_RATE = BigDecimal.valueOf(0.15);
    private static final BigDecimal DEFAULT_TAX_RATE = BigDecimal.valueOf(0.13);

    // Используется в качестве плейсхолдера, значение переменной не имеет важности при расчётах
    private static final Integer CURRENT_YEAR = LocalDate.now().getYear();

    /**
     * Множество государственных праздников
     */
    private static final Set<LocalDate> PUBLIC_HOLIDAYS = Set.of(LocalDate.of(CURRENT_YEAR, 1, 1),
                                                                 LocalDate.of(CURRENT_YEAR, 1, 2),
                                                                 LocalDate.of(CURRENT_YEAR, 1, 3),
                                                                 LocalDate.of(CURRENT_YEAR, 1, 4),
                                                                 LocalDate.of(CURRENT_YEAR, 1, 5),
                                                                 LocalDate.of(CURRENT_YEAR, 1, 6),
                                                                 LocalDate.of(CURRENT_YEAR, 1, 7),
                                                                 LocalDate.of(CURRENT_YEAR, 1, 8),
                                                                 LocalDate.of(CURRENT_YEAR, 2, 23),
                                                                 LocalDate.of(CURRENT_YEAR, 3, 8),
                                                                 LocalDate.of(CURRENT_YEAR, 5, 1),
                                                                 LocalDate.of(CURRENT_YEAR, 5, 9),
                                                                 LocalDate.of(CURRENT_YEAR, 6, 12),
                                                                 LocalDate.of(CURRENT_YEAR, 11, 4)
                                                                );

    @Override
    @GetMapping("/calculate")
    public BigDecimal calculate(EmployeeVacationDataDto vacationDataDto) {
        BigDecimal taxRate = vacationDataDto.getYearlyPay()
                         .compareTo(PROGRESSIVE_TAXING_THRESHOLD) >= 0 ? PROGRESSIVE_TAX_RATE : DEFAULT_TAX_RATE;

        BigDecimal averageDailyPay = vacationDataDto.getYearlyPay().divide(AVERAGE_CALENDAR_DAYS_PER_YEAR,
                                                                                2, RoundingMode.HALF_UP);

        long vacationLength = ChronoUnit.DAYS.between(vacationDataDto.getStartOfVacation(),
                                                      vacationDataDto.getEndOfVacation()) + 1 -
                              countUnpaidDays(vacationDataDto.getStartOfVacation(),
                                                vacationDataDto.getEndOfVacation());


        BigDecimal vacationPayGross = averageDailyPay.multiply(BigDecimal.valueOf(vacationLength));
        BigDecimal vacationPayAfterTax = vacationPayGross.multiply(BigDecimal.ONE.subtract(taxRate));

        return vacationPayAfterTax.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Метод, который считает количество выходных/праздничных дней в периоде [startDate, endDate].
     * @param startDate - дата выхода в отпуск
     * @param endDate - дата последнего дня отпуска
     * @return количество выходных/праздничных дней во время отпуска
     */
    private long countUnpaidDays(LocalDate startDate, LocalDate endDate) {
        return IntStream.iterate(0, i -> i + 1)
                .limit(ChronoUnit.DAYS.between(startDate, endDate) + 1)
                .mapToObj(startDate::plusDays)
                .filter(x -> isWeekend(x) || isHoliday(x))
                .count();
    }

    /**
     * Метод, который определяет, является ли дата date выходным днём.
     * @param date - определённая дата в формате LocalDate
     * @return true - если date - суббота или воскресенье, иначе false
     */
    private boolean isWeekend(LocalDate date) {
        return date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY;
    }

    /**
     * Метод, который определяет, является ли дата date государственным праздником.
     * @param date - определённая дата в формате LocalDate
     * @return true - если date - государственный праздник, иначе false
     */
    private boolean isHoliday(LocalDate date) {
        return PUBLIC_HOLIDAYS.stream()
                .anyMatch(x -> x.getDayOfMonth() == date.getDayOfMonth() && x.getMonth() == date.getMonth());
    }
}
