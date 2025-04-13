package ru.neoflex.vacationpaymentcalculator.Controller;

import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.neoflex.vacationpaymentcalculator.Dto.EmployeeVacationDataDto;
import ru.neoflex.vacationpaymentcalculator.Service.ICalculatorService;

import java.math.BigDecimal;

@RestController
@Log4j2
@Primary
public class CalculatorController implements ICalculatorController {

    private final ICalculatorService calculatorService;

    public CalculatorController(ICalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    @Override
    @GetMapping("/calculate")
    public ResponseEntity<BigDecimal> calculate(@RequestBody @Valid EmployeeVacationDataDto vacationDataDto) {
        return new ResponseEntity<>(calculatorService.calculate(vacationDataDto), HttpStatus.OK);
    }
}
