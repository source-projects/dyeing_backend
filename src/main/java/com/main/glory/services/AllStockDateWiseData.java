package com.main.glory.services;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.main.glory.Dao.PartyDao;
import com.main.glory.Dao.StockAndBatch.StockMastDao;
import com.main.glory.Dao.quality.QualityDao;
import com.main.glory.model.StockDataBatchData.BatchData;
import com.main.glory.model.StockDataBatchData.StockMast;
import com.main.glory.model.StockDataBatchData.response.AllStockDateWiseDataUnderParty;
import com.main.glory.model.StockDataBatchData.response.StockDateWise;
import com.main.glory.model.party.Party;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("allStockDateWiseData")
public class AllStockDateWiseData {
    private List<AllStockDateWiseDataUnderParty> data=new ArrayList<AllStockDateWiseDataUnderParty>();
    private Date from;
    private Date to;
    @Autowired
    PartyDao partyDao;
    @Autowired
    QualityDao qualityDao;
    @Autowired
    StockMastDao stockMastDao;



    public List<AllStockDateWiseDataUnderParty> getAllStockDateWiseData(String from,String to) throws Exception{
        this.from = DataConversion.stringToDate(from);
        this.to = DataConversion.stringToDate(to);
        partyDao.count();
        qualityDao.count();
        HashMap<Long,HashMap> dataMap=new HashMap<Long,HashMap>();

        List<StockMast> stockMastList = stockMastDao.getAllStockByRecievedDate(this.from, this.to);
        System.out.println("stockMastList length -"+Integer.toString(stockMastList.size()));
        for(int i=0;i<stockMastList.size();i++){
            StockMast stockMast=stockMastList.get(i);
            Party party=partyDao.findByPartyId(stockMast.getPartyId());
            String partyName=party.getPartyName();
            String partyCode=party.getPartyCode();
            String qualityName=qualityDao.findById(stockMast.getQualityId()).get().getQualityName();
            Date receiveDate=stockMast.getReceiveDate();


            
            if(!dataMap.containsKey(stockMast.getPartyId()))
            {
                dataMap.put(stockMast.getPartyId(),new HashMap<>() {{
                put("partyName", partyName);
                put("partyCode", partyCode);
                }}
                );
            }
            


            
            List<BatchData> batchDataList=stockMast.getBatchData();
            for(int j=0;j<batchDataList.size();j++){
                BatchData batchData=batchDataList.get(j);
                String pchallanRef=batchData.getPchallanRef();
                String batchId=batchData.getBatchId();
                Double mtr=batchData.getMtr();
                Double wt=batchData.getWt();


                if(dataMap.get(stockMast.getPartyId()).containsKey(batchId)){
                    StockDateWise stockDateWise=(StockDateWise)dataMap.get(stockMast.getPartyId()).get(batchId);
                    stockDateWise.addToTotalMtr(mtr);
                    stockDateWise.addToTotalWT(wt);
                }
                else{
                    dataMap.get(stockMast.getPartyId()).put(batchId, new StockDateWise(batchId,mtr,wt,qualityName,receiveDate,pchallanRef));
                }
            
            }
                      

        
        }
        for (Map.Entry<Long,HashMap> entry : dataMap.entrySet()){
            String partyName=(String)entry.getValue().get("partyName");
            String partyCode=(String)entry.getValue().get("partyCode");
            entry.getValue().remove("partyName");
            entry.getValue().remove("partyCode");
            HashMap<String,StockDateWise> partyData=entry.getValue();
            if (partyData.values().size()==0)
            continue;

            AllStockDateWiseDataUnderParty allStockDateWiseDataUnderParty=new AllStockDateWiseDataUnderParty();
            allStockDateWiseDataUnderParty.setPartyCode(partyCode);
            allStockDateWiseDataUnderParty.setPartyName(partyName);
            
            for(Map.Entry<String,StockDateWise> entry2 :partyData.entrySet()){
                allStockDateWiseDataUnderParty.addStockDateWiseData(entry2.getValue());
            }
            data.add(allStockDateWiseDataUnderParty);
            
        }


        return data;

    }
    
    

    public List<AllStockDateWiseDataUnderParty> getData() {
        return data;
    }

    public void addData(AllStockDateWiseDataUnderParty data) {
        this.data.add(data);
    }



    
}
