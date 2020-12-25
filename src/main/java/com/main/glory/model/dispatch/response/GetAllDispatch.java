package com.main.glory.model.dispatch.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.main.glory.model.dispatch.DispatchData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetAllDispatch {


    String invoiceNo;
    Boolean isSendToParty;
    Date date;

    public GetAllDispatch(DispatchData dispatchData) {
        this.invoiceNo=dispatchData.getInvoiceNo();
        this.isSendToParty=dispatchData.getIsSendToParty();
        this.date=dispatchData.getCreatedDate();
    }
}
