package com.main.glory.servicesImpl;

import com.main.glory.Dao.StockAndBatch.BatchDao;
import com.main.glory.Dao.StockAndBatch.StockMastDao;
import com.main.glory.model.StockDataBatchData.BatchData;
import com.main.glory.model.StockDataBatchData.StockMast;
import com.main.glory.model.StockDataBatchData.request.GetCompleteFinishMtrDetail;
import com.main.glory.model.party.Party;
import com.main.glory.model.quality.Quality;
import com.main.glory.model.quality.response.GetQualityResponse;
import com.main.glory.model.user.UserData;
import org.hibernate.engine.jdbc.batch.spi.Batch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("batchImpl")
public class BatchImpl {

    @Autowired
    private BatchDao batchDao;

    @Autowired
    StockMastDao stockMastDao;
    @Autowired
    UserServiceImpl userService;
    @Autowired
    PartyServiceImp partyServiceImp;
    @Autowired
    QualityServiceImp qualityServiceImp;

    public boolean isBatchIdExists(String name, Long id){

        Optional<BatchData> b = batchDao.isBatchUnique(name, id);
        if(b.isPresent()){
            return true;
        }else{
            return false;
        }
    }


    public void saveBatch(BatchData batchData) {

        batchDao.saveAndFlush(batchData);
    }


    public void updateFinishMtrBatch(List<BatchData> batchDataList) throws Exception{


        Map<Long,Boolean> extraBatch = new HashMap<>();

        Long controlId = batchDataList.get(0).getControlId();
        String batchId = batchDataList.get(0).getBatchId();


        //getting the extra batch who's invoice is not created
        List<BatchData> extraBatchList = batchDao.findByControlIdAndBatchIdWithExtraBatch(controlId,batchId);

        for(BatchData batchData: extraBatchList)
        {
            extraBatch.put(batchData.getId(),false);
        }

        for(BatchData batchData:batchDataList)
        {

            //if it extra batch then create it
            if(batchData.getId()==0) {
                batchData.setIsExtra(true);
                batchData.setMtr(0.0);
            }
            else
            {
                //if it is already available then replace the flag from the hash map
                if(batchData.getIsExtra()==true)
                    extraBatch.replace(batchData.getId(),true);
                //save the extra batch
            }
            batchData.setIsFinishMtrSave(true);
            batchData.setIsProductionPlanned(true);
            batchDao.save(batchData);
        }

        //##Iterate the loop and delete the record who flag is false in extra batch
        for(Map.Entry<Long,Boolean> entry:extraBatch.entrySet())
        {
            System.out.println(entry.getKey()+":"+entry.getValue());
            if(entry.getValue()==false)
            {
                //delete the extra batch gr who's the invoice is not created
                batchDao.deleteById(entry.getKey());
            }
        }

        //update the flag



    }

    public List<BatchData> getBatchById(String batchId, String controlId) throws Exception{
        try {
            List<BatchData> batchData=null;
            if(batchId.contains("-"))
            {
                batchData  = batchDao.getBatchByBatchIdWithMergeBatchId(batchId.split("-")[1],batchId.split("-")[0]);
                /*for(BatchData batch:batchDataList)
                {
                    batch.setBatchId(batch.getMergeBatchId()+"-s"+batch.getBatchId());
                    batchData.add(batch);
                }*/
            }
            else
            {
                batchData = batchDao.getBatchByBatchId(batchId);
            }

            if (batchData.isEmpty())
                throw new Exception("Batch is not available for batchId:" + batchId);

            return batchData;
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public List<BatchData> getBatchByDocId(String batchId, Long controlId) {
        return null;
    }

    public GetCompleteFinishMtrDetail getAllDetailBy(String batchId, Long controlId) throws Exception{
        GetCompleteFinishMtrDetail data=new GetCompleteFinishMtrDetail();

        Optional<StockMast> stockMast  = stockMastDao.findById(controlId);
        if(!stockMast.isPresent())
            throw new Exception("No batch found");

        List<BatchData> batchDataList = batchDao.findByControlIdAndBatchId(controlId,batchId);
        if(batchDataList.isEmpty())
            throw new Exception("No batch found");

        UserData userData = userService.getUserById(stockMast.get().getUserHeadId());
        String partyName=partyServiceImp.getPartyNameByPartyId(stockMast.get().getPartyId());
        GetQualityResponse quality = qualityServiceImp.getQualityByID(stockMast.get().getQualityId());
        Double mtr=0.0;
        Double wt=0.0;
        for(BatchData batchData:batchDataList)
        {
            mtr+=batchData.getMtr();
            wt+=batchData.getWt();
        }


        //set the data
        data.setUserHeadId(userData.getId());
        data.setMasterName(userData.getFirstName());
        data.setBatchData(batchDataList);
        data.setPcs(batchDataList.size());
        data.setBatchId(batchId);
        data.setControlId(controlId);
        data.setPartyId(stockMast.get().getPartyId());
        data.setPartyName(partyName);
        data.setQualityEntryId(quality.getId());
        data.setQualityName(quality.getQualityName());
        data.setQualityId(quality.getQualityId());
        data.setDate(stockMast.get().getCreatedDate());
        data.setTotalWt(wt);
        data.setTotalMtr(mtr);

        if(data.getPartyId()==null && data.getQualityEntryId()==null && data.getUserHeadId()==null)
            throw new Exception("no data found");

        return data;
    }

    public Optional<BatchData> getBatchByEntryId(Long batchEntryId) throws Exception{
        Optional<BatchData> batchData = batchDao.findById(batchEntryId);
        if(!batchData.isPresent())
            throw new Exception("batch gr is not found");

        return batchData;
    }

    public void deleteBatch(Long id) throws Exception {
        Optional<BatchData> batchData = batchDao.findById(id);
        if(!batchData.isPresent())
            throw new Exception("Batch not found");

        batchDao.deleteById(id);
    }

    public List<BatchData> getBatchByBatchIdId(String batchId) {
        return batchDao.getBatchByBatchId(batchId);
    }

    public List<BatchData> getBatchByMergeBatchId(String batchId) {
        return batchDao.getBatchByMergeBatchId(batchId);
    }
}
