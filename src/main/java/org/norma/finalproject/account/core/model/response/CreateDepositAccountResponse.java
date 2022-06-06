package org.norma.finalproject.account.core.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class CreateDepositAccountResponse {

    private String accountName;
    private String accountNo;
    private String ibanNo;


}
