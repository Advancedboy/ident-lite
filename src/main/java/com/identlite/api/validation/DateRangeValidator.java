package com.identlite.api.validation;

import com.identlite.api.dto.UpdateBookingDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, UpdateBookingDto> {

    @Override
    public boolean isValid(UpdateBookingDto dto, ConstraintValidatorContext context) {
        LocalDate start = dto.getStartDate();
        LocalDate end = dto.getEndDate();

        // Если одно из полей null — пропускаем проверку
        if (start == null || end == null) {
            return true;
        }

        // Проверяем, что end > start
        return end.isAfter(start);
    }
}
