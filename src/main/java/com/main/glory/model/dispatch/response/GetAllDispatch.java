package com.main.glory.model.dispatch.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.main.glory.model.StockDataBatchData.response.BatchWithTotalMTRandFinishMTR;
import com.main.glory.model.dispatch.DispatchData;
import com.main.glory.model.dispatch.DispatchMast;
import com.main.glory.model.dispatch.request.BatchAndStockId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetAllDispatch {
    
    String invoiceNo;
    Boolean isSendToParty;
    Date date;
    String partyName;
    Long partyId;
    Double netAmt;
    Double totalMtr;
    Double finishMtr;
    Boolean signByParty;
    Boolean isRfInvoice;
    List<BatchWithTotalMTRandFinishMTR> batchList;

    public GetAllDispatch(DispatchData dispatchData) {
        this.invoiceNo=dispatchData.getInvoiceNo();
        this.isSendToParty=dispatchData.getIsSendToParty();
        this.date=dispatchData.getCreatedDate();
    }

    public GetAllDispatch(DispatchMast dispatchMast) {
        this.invoiceNo=dispatchMast.getDispatchDataList()==null?"null":dispatchMast.getPostfix();
        //this.isSendToParty=dispatchData.getIsSendToParty();
        this.date=dispatchMast.getCreatedDate();
        this.isRfInvoice =dispatchMast.getIsRfInvoice();
    }
}
