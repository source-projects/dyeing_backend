package com.main.glory.model.dispatch;

import com.main.glory.model.StockDataBatchData.BatchData;
import com.main.glory.model.dispatch.request.CreateDispatch;
import lombok.*;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Entity
public class DispatchMast {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    Date createdDate;
    Date updatedDate;
    Long createdBy;
    String prefix;
    Long postfix;
    Long paymentBunchId;//payment mast id
    Long userHeadId;
    Long updatedBy;
    Long partyId;
    Double discount;
    Double cgst;
    Double sgst;
    Double taxAmt;
    Double netAmt;

    public DispatchMast(CreateDispatch dispatchList) {
        //for storing the tax amoutt
        this.discount= dispatchList.getDiscount();
        this.cgst = dispatchList.getCgst();
        this.sgst =dispatchList.getSgst();
        this.taxAmt = dispatchList.getTaxAmt();
        this.netAmt = dispatchList.getNetAmt();
    }


    @PrePersist
    protected void onCreate() throws ParseException {
        this.createdDate = new Date(System.currentTimeMillis());
    }

    @PreUpdate
    protected void onUpdate()
    {
        this.updatedDate=new Date(System.currentTimeMillis());
    }

}
