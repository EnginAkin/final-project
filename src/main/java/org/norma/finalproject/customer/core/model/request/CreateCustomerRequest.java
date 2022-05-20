package org.norma.finalproject.customer.core.model.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.norma.finalproject.customer.core.model.AddressDto;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class CreateCustomerRequest {

    @NotNull(message = "Identity number cannot be null.")
    @Size(min = 11, max = 11,message = "Identity size must be 11 digit.")
    private String identityNumber;

    @NotNull(message = "name cannot be null.")
    private String name;

    @NotNull(message = "Surname cannot be null.")
    private String surname;

    @NotNull(message = "Password cannot be null")
    @Size(min = 6, max = 6,message = "password size must be 6 digit.")
    private String password;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @NotNull(message = "Date Cannot be null.")
    private Date birthDay; // 1999-03-03

    @NotNull(message = "Telephone Cannot be null.")
    @Pattern(regexp = "^[0-9]{10}$",message = "Only number and 11 digit accept for telephone number")
    private String telephone;

    @NotNull(message = "Income Cannot be null.")
    @Min(0)
    private BigDecimal income;

    //private AddressDto address;


}
