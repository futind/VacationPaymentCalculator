package ru.neoflex.vacationpaymentcalculator.Service;

import org.springframework.stereotype.Service;
import ru.neoflex.vacationpaymentcalculator.Dto.EmployeeVacationDataDto;

import java.math.BigDecimal;

@Service
public interface ICalculatorService {

    /**
     * Метод, который на основе предоставленных данных высчитывает сумму отпускных.
     * @param vacationDataDto - DTO, содержащее данные о зарплате за последний год и дату выхода в отпуск и из него.
     * @return рассчитанная сумма отпускных.
     */
    BigDecimal calculate(EmployeeVacationDataDto vacationDataDto);
}
