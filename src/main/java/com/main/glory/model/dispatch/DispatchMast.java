package com.main.glory.model.dispatch;


import com.main.glory.model.StockDataBatchData.BatchData;
import com.main.glory.model.dispatch.request.CreateDispatch;
import com.main.glory.model.party.Party;
import com.main.glory.model.user.UserData;

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
    @ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="createdBy", referencedColumnName = "id", insertable = true, updatable = true)    
    private UserData createdBy;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="userHeadId", referencedColumnName = "id", insertable = true, updatable = true)
    private UserData userHeadData;

    Date updatedDate;
    String prefix;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="postfix", referencedColumnName = "id", insertable = true, updatable = true)
    DispatchData dispatchData;
    

    Long paymentBunchId;//payment mast id
    @ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="updatedBy", referencedColumnName = "id", insertable = true, updatable = true)    
    private UserData updatedBy;
    Double discount;
    Double percentageDiscount;
    Double cgst;
    Double sgst;
    Double igst;
    Double taxAmt;
    Double netAmt;
    String remark;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="partyId", referencedColumnName = "id", insertable = true, updatable = true)
    private Party party;

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
        this.userHeadData = dispatchMast.getUserHeadData();
        this.party = dispatchMast.getParty();
        this.percentageDiscount = createDispatch.getPercentageDiscount();
        this.discount = createDispatch.getDiscount();
        this.cgst = createDispatch.getCgst();
        this.sgst = createDispatch.getSgst();
        this.netAmt = createDispatch.getNetAmt();
        this.taxAmt = createDispatch.getTaxAmt();
        this.dispatchData =createDispatch.getDispatchData();
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
