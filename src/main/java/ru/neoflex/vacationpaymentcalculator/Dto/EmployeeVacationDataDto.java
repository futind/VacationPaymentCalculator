package ru.neoflex.vacationpaymentcalculator.Dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class EmployeeVacationDataDto {
    
    @NotNull
    @Min(0)
    @Schema(description = "Средняя зарплата за 12 месяцев", example = "123456.33",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private BigDecimal yearlyPay;

    @NotNull
    @Schema(description = "Дата начала отпуска", example = "2025-04-04",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private LocalDate startOfVacation;

    @NotNull
    @Schema(description = "Дата последнего дня отпуска", example = "2025-04-18",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private LocalDate endOfVacation;

    @JsonIgnore
    @AssertTrue(message = "Start of vacation must be before the end of vacation")
    public boolean datesAreValid() {
        return startOfVacation.isBefore(endOfVacation);
    }
}
