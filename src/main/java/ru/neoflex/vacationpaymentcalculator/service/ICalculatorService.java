package ru.neoflex.vacationpaymentcalculator.service;

import org.springframework.stereotype.Service;
import ru.neoflex.vacationpaymentcalculator.dto.EmployeeVacationDataDto;

import java.math.BigDecimal;

@Service
public interface ICalculatorService {

    /**
     * Метод, который на основе предоставленных данных высчитывает сумму отпускных.
     * @param vacationDataDto DTO, содержащее данные о зарплате за последний год и дату выхода в отпуск и из него.
     * @return Объект BigDecimal, который содержит рассчитанную сумму отпускных.
     */
    BigDecimal calculate(EmployeeVacationDataDto vacationDataDto);
}
