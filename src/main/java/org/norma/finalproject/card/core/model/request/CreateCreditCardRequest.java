package org.norma.finalproject.card.core.model.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
public class CreateCreditCardRequest {

    @NotNull
    @Pattern(regexp = "\\d{4}")
    private String password;

    @NotNull(message = "Date Cannot be null.")
    private Date cutOffDate;
}