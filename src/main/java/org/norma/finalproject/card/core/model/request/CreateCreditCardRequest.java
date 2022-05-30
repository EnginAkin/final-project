package org.norma.finalproject.card.core.model.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
public class CreateCreditCardRequest {

    @NotNull
    @Pattern(regexp = "\\d{4}",message = "Password must be only 4 digit")
    private String password;

    @NotNull
    @Min(0)
    private BigDecimal creditCardLimit;
}
