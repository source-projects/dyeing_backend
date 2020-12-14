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
import springfox.documentation.schema.Entry;

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


    public void saveBatch(BatchData batchData) {

        batchDao.saveAndFlush(batchData);
    }


    public void updateFinishMtrBatch(List<BatchData> batchDataList) throws Exception{

        //##Get the data first from the list
        Map<Long,Boolean> batchGr=new HashMap<>();
        List<BatchData> batchData = batchDao.findByControlId(batchDataList.get(0).getControlId());
        for(BatchData batch: batchData)
        {
            batchGr.put(batch.getId(),false);
            //System.out.println(batch.getId());
        }

        for(BatchData batch: batchDataList)
        {
            if(batchGr.containsKey(batch.getId()))
            {
                batchGr.replace(batch.getId(),true);
            }

        }

        //##Iterate the loop and delete the record who flag is false
        for(Map.Entry<Long,Boolean> entry:batchGr.entrySet())
        {
            System.out.println(entry.getKey()+":"+entry.getValue());
            if(entry.getValue()==false)
            {
                batchDao.deleteById(entry.getKey());
            }
        }

        for(BatchData batch:batchDataList)
        {
            if(batch.getId()!=batch.getSequenceId())
                batch.setIsExtra(true);

            batch.setIsFinishMtrSave(true);
            batchDao.save(batch);
        }

    }

    public List<BatchData> getBatchById(String batchId, Long controlId) throws Exception{
        List<BatchData> batchData = batchDao.findByControlIdAndBatchId(controlId,batchId);

        if(batchData.isEmpty())
            throw new Exception("Batch is not available for batchId:"+batchId);

        return  batchData;
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


        //set value to the data
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
}

