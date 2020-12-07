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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

            for (BatchData batch : batchDataList) {
                Optional<BatchData> batchData = batchDao.findById(batch.getId());

                if (batchData.isPresent()) {
                    batchData.get().setId(batch.getId());
                    batchData.get().setIsExtra(batch.getIsExtra());
                    batchData.get().setSequenceId(batch.getId());
                    batchData.get().setFinishMtr(batch.getFinishMtr());
                    batchData.get().setIsBillGenrated(batch.getIsBillGenrated());
                    batchData.get().setIsProductionPlanned(batch.getIsProductionPlanned());
                    batchData.get().setBatchId(batch.getBatchId());
                    batchData.get().setControlId(batch.getControlId());
                    batchDao.save(batchData.get());
                } else if(batch.getId()==0){
                    batchDao.save(batch);
                }
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

    public GetCompleteFinishMtrDetail getAllDetailBy(String batchId, Long controlId) {
        GetCompleteFinishMtrDetail data=new GetCompleteFinishMtrDetail();

        Optional<StockMast> stockMast  = stockMastDao.findById(controlId);
        List<BatchData> batchDataList = batchDao.findByControlIdAndBatchId(controlId,batchId);
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
        return data;
    }
}
