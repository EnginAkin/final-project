package org.norma.finalproject.common.response;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class GeneralDataResponse<T>  {
    private final T data;
    private  GeneralResponse generalResponse;
    public GeneralDataResponse(T data){
        this.data = data;
        this.generalResponse=new GeneralResponse("Successfull",true);
    }
}
