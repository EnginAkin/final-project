package org.norma.finalproject.account.core.model.response;

import lombok.*;

import javax.persistence.Entity;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CreateDepositAccountResponse {


    private String accountName;
    private String accountNo;
    private String ibanNo;


}
