package com.main.glory.model.StockDataBatchData.response;

import java.util.ArrayList;
import java.util.List;

public class AllStockDateWiseDataUnderParty {
    private String partyName;
    private String partyCode;
    private List<StockDateWise> allStockDateWiseData=new ArrayList<StockDateWise>();
    public String getPartyName() {
        return partyName;
    }
    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }
    public String getPartyCode() {
        return partyCode;
    }
    public void setPartyCode(String partyCode) {
        this.partyCode = partyCode;
    }
    public List<StockDateWise> getAllStockDateWiseData() {
        return allStockDateWiseData;
    }
    public void setAllStockDateWiseData(List<StockDateWise> allStockDateWiseData) {
        this.allStockDateWiseData = allStockDateWiseData;
    }    

    public void addStockDateWiseData(StockDateWise stockDateWise) {
        this.allStockDateWiseData.add( stockDateWise);
    }
    
}
