package com.main.glory.model.dispatch.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.main.glory.model.dispatch.DispatchMast;
import com.main.glory.servicesImpl.StockBatchServiceImpl;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
//@AllArgsConstructor
public class MonthlyDispatchReport {

    //private int month;
    private String month;
    private Double finishMtr;
    private Double discount;
    private Double taxAmt;
    @JsonIgnore
    private int year;
    private Double cgst;
    private Double sgst;
    private Double igst;
    private Double netAmt;
    @JsonIgnore
    private String unit;
    @JsonIgnore
    private DispatchMast dm;

    //@Query("select new com.main.glory.model.dispatch.response.MonthlyDispatchReport(SUM(dd.batchData.finishMtr),concat(Month(dm.createdDate),', ',Year(dm.createdDate)),dm) from DispatchData dd INNER JOIN DispatchMast dm ON dd.controlId = dm.id where (Date(dm.createdDate)>=Date(:from) OR :from IS NULL) AND (Date(dm.createdDate)<=Date(:to) OR :to IS NULL) AND (:userHeadId IS NULL OR dm.userHeadData.id=:userHeadId) GROUP by dm.id")
    public MonthlyDispatchReport(Double finishMtr,String month,  DispatchMast dm) {
        this.month = month;
        this.finishMtr = finishMtr;
        this.dm = dm;
        this.discount= dm.getDiscount();
        this.taxAmt = dm.getTaxAmt();
        this.cgst = dm.getParty().getState().equalsIgnoreCase("Gujarat")?dm.getCgst():0.0;
        this.sgst = dm.getParty().getState().equalsIgnoreCase("Gujarat")?dm.getSgst():0.0;
        this.igst = dm.getParty().getState().equalsIgnoreCase("Gujarat")?0.0:dm.getIgst()==null?0.0:dm.getIgst();
        this.netAmt = dm.getNetAmt();

    }




    //@Query("select new com.main.glory.model.dispatch.response.MonthlyDispatchReport(SUM(dd.batchData.finishMtr),SUM(dm.taxAmt),SUM(dm.discount),function('date_format', dm.createdDate, '%Y, %m') as DateMonth,SUM(dm.netAmt),dd.billingUnit) from DispatchData dd INNER JOIN DispatchMast dm ON dd.controlId = dm.id where (Date(dm.createdDate)>=Date(:from) OR :from IS NULL) AND (Date(dm.createdDate)<=Date(:to) OR :to IS NULL) AND (:userHeadId IS NULL OR dm.userHeadData.id=:userHeadId) GROUP BY function('date_format', dd.createdDate, '%Y, %m'),dd.billingUnit")
//    public MonthlyDispatchReport(Double finishMtr, Double taxAmt, Double discount, String month, Double netAmt, String unit) {
//        this.month = month;
//        this.finishMtr = finishMtr;
//        this.taxAmt = taxAmt;
//        this.discount = discount;
//        this.year = year;
//        this.netAmt = netAmt;
//        this.unit = unit;
//    }



    public MonthlyDispatchReport(Double finishMtr, Double taxAmt, Double discount, Double netAmt, String dateFormat) {
        //this.month = month;
        this.finishMtr = StockBatchServiceImpl.changeInFormattedDecimal(finishMtr);
        this.taxAmt = StockBatchServiceImpl.changeInFormattedDecimal(taxAmt);
        this.discount = StockBatchServiceImpl.changeInFormattedDecimal(discount);
        //this.year = year;
        this.netAmt = StockBatchServiceImpl.changeInFormattedDecimal(netAmt);
        this.month = dateFormat;
    }

    public MonthlyDispatchReport(String key) {
        this.month = key;
    }

    public MonthlyDispatchReport(String month, Double totalNetAmt, Double totalTaxAmt, Double totalFinishMtr, Double discount,Double cgst,Double sgst,Double igst) {
        this.month = month;
        this.finishMtr = StockBatchServiceImpl.changeInFormattedDecimal(totalFinishMtr);
        this.discount = StockBatchServiceImpl.changeInFormattedDecimal(discount);
        this.taxAmt = StockBatchServiceImpl.changeInFormattedDecimal(totalTaxAmt);
        this.netAmt = StockBatchServiceImpl.changeInFormattedDecimal(totalNetAmt);
        this.igst = StockBatchServiceImpl.changeInFormattedDecimal(igst);
        this.cgst = StockBatchServiceImpl.changeInFormattedDecimal(cgst);
        this.sgst = StockBatchServiceImpl.changeInFormattedDecimal(sgst);
    }




    public void addFinishMtr(Double finishMtr){
        this.finishMtr+=finishMtr;
    }

    public void addTaxAmt(Double taxAmt){
        this.taxAmt= this.taxAmt + taxAmt;
    }

    public void addDiscount(Double discount){
        this.discount+=discount;
    }

    public void addNetAmt(Double netAmt){
        this.netAmt+=netAmt;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MonthlyDispatchReport that = (MonthlyDispatchReport) o;
        return month.equals(that.month);
    }

    @Override
    public int hashCode() {
        return Objects.hash(month);
    }
}
