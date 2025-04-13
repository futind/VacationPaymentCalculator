package ru.neoflex.vacationpaymentcalculator.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.vacationpaymentcalculator.Dto.EmployeeVacationDataDto;

import java.math.BigDecimal;

@RestController
public interface ICalculatorController {

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK", content = {
                    @Content(mediaType = "application/json")
            }),
            @ApiResponse(responseCode = "400", description = "Validation failed", content = {
                    @Content(mediaType = "application/json")
            })
    })
    @Operation(
            summary = "Расчёт суммы отпускных",
            description = """
                    На вход получаем EmployeeVacationDataDto, содержащий суммарные выплаты за расчётный период \
                    в 12 месяцев, дата выхода в отпуск и дата последнего дня отпуска. Данные проверяются на \
                    соответствие условиям (дата выхода в отпуск ранее даты последнего дня, \
                    сумма выплат не меньше 0, ...). В том случае, если проверки не будут пройдены в ответ на \
                    запрос вернётся код 400.\r\n
                    Сначала рассчитывается количество календарных дней за которые осуществляются выплаты (выходные и \
                    государственные праздники не считаются) и это количество дней умножается на вычесленную среднюю \
                    зарплату за день. \r\n
                    С кодом 200 возвращается сумма отпускных.
                    """
    )
    @GetMapping("/calculate")
    ResponseEntity<BigDecimal> calculate(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Данные о выплатах за расчётный период и даты отпуска сотрудника",
            required = true
    )
                                         @RequestBody @Valid EmployeeVacationDataDto vacationDataDto);
}
