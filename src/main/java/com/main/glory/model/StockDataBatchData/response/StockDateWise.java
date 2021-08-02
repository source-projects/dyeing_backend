package com.main.glory.model.StockDataBatchData.response;
import com.main.glory.servicesImpl.StockBatchServiceImpl;

import java.util.Date;

public class StockDateWise {
    private String batchId;
    private Double totalMtr;
    private Double totalWT;
    private String qualityName;
    public String getBatchId() {
        return batchId;
    }

    public String getQualityName() {
        return qualityName;
    }
    public Date getReceiveDate() {
        return receiveDate;
    }

    public String getPchallanRef() {
        return pchallanRef;
    }


    private Date receiveDate;
    public Double getTotalMtr() {
        return StockBatchServiceImpl.changeInFormattedDecimal(totalMtr);
    }

    public void addToTotalMtr(Double mtr) {
        this.totalMtr +=StockBatchServiceImpl.changeInFormattedDecimal(mtr);
    }


    public Double getTotalWT() {
        return  StockBatchServiceImpl.changeInFormattedDecimal(totalWT);
    }

    public void addToTotalWT(Double wt) {
        this.totalWT +=StockBatchServiceImpl.changeInFormattedDecimal(wt);
    }

    private String pchallanRef;

    public StockDateWise(String batchId, Double totalMtr, Double totalWT, String qualityName, Date receiveDate, String pchallanRef) {
        this.batchId = batchId;
        this.totalMtr = StockBatchServiceImpl.changeInFormattedDecimal(totalMtr);
        this.totalWT =StockBatchServiceImpl.changeInFormattedDecimal(totalWT);
        this.qualityName = qualityName;
        this.receiveDate = receiveDate;
        this.pchallanRef = pchallanRef;
    }

}
