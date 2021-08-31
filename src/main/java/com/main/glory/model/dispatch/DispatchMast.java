package com.main.glory.model.dispatch;


import com.main.glory.model.StockDataBatchData.BatchData;
import com.main.glory.model.dispatch.request.CreateDispatch;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

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

@Entity
public class DispatchMast{
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
    Double percentageDiscount;
    Double cgst;
    Double sgst;
    Double igst;
    Double taxAmt;
    Double netAmt;
    String remark;
    Date signUpdatedDate;
    @ColumnDefault("false")
    Boolean signByParty;
    String deliveryMode;

    public DispatchMast(CreateDispatch dispatchList) {
        //for storing the tax amount
        this.deliveryMode =dispatchList.getDeliveryMode()==null?null:dispatchList.getDeliveryMode();
        this.percentageDiscount = dispatchList.getPercentageDiscount();
        this.discount= dispatchList.getDiscount();
        this.cgst = dispatchList.getCgst();
        this.sgst =dispatchList.getSgst();
        this.igst = dispatchList.getIgst();
        this.taxAmt = dispatchList.getTaxAmt();
        this.netAmt = Math.floor(dispatchList.getNetAmt());
        this.remark = dispatchList.getRemark()==null?null:dispatchList.getRemark();
    }

    public DispatchMast(CreateDispatch createDispatch, DispatchMast dispatchMast) {
        this.id=dispatchMast.getId();
        this.createdDate = dispatchMast.getCreatedDate();
        this.createdBy = dispatchMast.getCreatedBy();
        this.updatedBy = dispatchMast.getUpdatedBy();
        this.userHeadId = dispatchMast.getUserHeadId();
        this.partyId = dispatchMast.getPartyId();
        this.percentageDiscount = createDispatch.getPercentageDiscount();
        this.discount = createDispatch.getDiscount();
        this.cgst = createDispatch.getCgst();
        this.sgst = createDispatch.getSgst();
        this.netAmt = createDispatch.getNetAmt();
        this.taxAmt = createDispatch.getTaxAmt();
        this.postfix =createDispatch.getInvoiceNo();
        this.remark = createDispatch.getRemark();
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
