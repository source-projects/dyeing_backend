package com.main.glory.servicesImpl;

import com.main.glory.Dao.StockAndBatch.BatchDao;
import com.main.glory.Dao.StockAndBatch.StockMastDao;
import com.main.glory.model.ConstantFile;
import com.main.glory.model.StockDataBatchData.BatchData;
import com.main.glory.model.StockDataBatchData.StockMast;
import com.main.glory.model.StockDataBatchData.request.GetCompleteFinishMtrDetail;
import com.main.glory.model.jet.JetData;
import com.main.glory.model.jet.JetStatus;
import com.main.glory.model.productionPlan.ProductionPlan;
import com.main.glory.model.quality.response.GetQualityResponse;
import com.main.glory.model.user.UserData;
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


    @Autowired
    ProductionPlanImpl productionPlanService;
    @Autowired
    JetServiceImpl jetService;

    ConstantFile constantFile;

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
        String mergeBatchId = batchDataList.get(0).getMergeBatchId()==null?null:batchDataList.get(0).getMergeBatchId();

        if(mergeBatchId==null) {

            //getting the extra batch who's invoice is not created
            List<BatchData> extraBatchList = batchDao.findByBatchIdWithoutBillGenerated( batchId);

            for (BatchData batchData : extraBatchList) {
                extraBatch.put(batchData.getId(), true);
            }

            for (BatchData batchData : batchDataList) {

                //if it extra batch then create it
                if (batchData.getId() == 0) {
                    batchData.setIsExtra(true);
                    batchData.setMtr(0.0);
                    //get the pchallan n by sequence id whic is coming from FE
                    BatchData batchDataForPChallanRef = batchDao.getBatchDataById(batchData.getSequenceId());
                    batchData.setPchallanRef(batchDataForPChallanRef.getPchallanRef());

                } else {
                    //if it is already available then replace the flag from the hash map

                        extraBatch.replace(batchData.getId(), false);

                    //save the extra batch
                }
                batchData.setIsFinishMtrSave(true);
                batchData.setIsProductionPlanned(true);

                batchDao.save(batchData);

            }

            //##Iterate the loop and delete the record who flag is true in extra batch
            for (Map.Entry<Long, Boolean> entry : extraBatch.entrySet()) {
                System.out.println(entry.getKey() + ":" + entry.getValue());
                if (entry.getValue() == true) {
                    //delete the extra batch gr who's the invoice is not created
                    batchDao.deleteByIdWithExtraFlag(entry.getKey(),true);
                }
            }

            //update the flag

        }
        else {

            //getting the extra batch who's invoice is not created
            List<BatchData> extraBatchList = batchDao.findByMergeBatchIdWithoutBillGenerated(mergeBatchId,batchId);

            for (BatchData batchData : extraBatchList) {
                extraBatch.put(batchData.getId(), true);
            }

            for (BatchData batchData : batchDataList) {

                //if it extra batch then create it
                if (batchData.getId() == 0) {
                    batchData.setIsExtra(true);
                    batchData.setMtr(0.0);

                    //get the pchallan n by sequence id whic is coming from FE
                    BatchData batchDataForPChallanRef = batchDao.getBatchDataById(batchData.getSequenceId());
                    batchData.setPchallanRef(batchDataForPChallanRef.getPchallanRef());

                } else {
                    //if it is already available then replace the flag from the hash map

                        extraBatch.replace(batchData.getId(), false);
                    //save the extra batch
                }
                batchData.setIsFinishMtrSave(true);
                batchData.setIsProductionPlanned(true);
                batchData.setMergeBatchId(mergeBatchId);
                batchDao.save(batchData);

            }

            //##Iterate the loop and delete the record who flag is true in extra batch
            for (Map.Entry<Long, Boolean> entry : extraBatch.entrySet()) {
                System.out.println(entry.getKey() + ":" + entry.getValue());
                if (entry.getValue() == true) {
                    //delete the extra batch gr who's the invoice is not created
                    batchDao.deleteByIdWithExtraFlag(entry.getKey(),true);
                }
            }

            //update the flag
        }


    }

    public List<BatchData> getBatchById(String batchId, String controlId) throws Exception{
        try {
            List<BatchData> batchData=null;
            if(batchId.contains("-"))
            {
                batchData  = batchDao.getBatchByMergeBatchIdAndBatchIdForFinishMtrSave((batchId.split("-")[1]),batchId.split("-")[0]);
                /*for(BatchData batch:batchDataList)
                {
                    batch.setBatchId(batch.getMergeBatchId()+"-s"+batch.getBatchId());
                    batchData.add(batch);
                }*/
            }
            else
            {
                batchData = batchDao.getBatchForFinishMtrByBatchId(batchId);
            }

            if (batchData.isEmpty())
                throw new Exception(constantFile.StockBatch_Not_Found + batchId);

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
            throw new Exception(constantFile.Batch_Data_Not_Found);

        batchDao.deleteById(id);
    }

    public List<BatchData> getBatchByBatchIdId(String batchId) {
        return batchDao.getBatchByBatchId(batchId);
    }

    public List<BatchData> getBatchByMergeBatchId(String batchId) {
        return batchDao.getBatchByMergeBatchId(batchId);
    }


    public void removeBatchFromFinishMtrByProductionId(Long productionId) throws Exception {
        ProductionPlan productionPlanExist = productionPlanService.getProductionDataById(productionId);

        if(productionPlanExist==null)
            throw new Exception(ConstantFile.Production_Not_Found);

        JetData jetData = jetService.getJetDataByProductionId(productionId);
        if(jetData==null)
            throw new Exception(ConstantFile.Production_Record_Not_Exist_With_Jet);


        jetService.updateJetDataStatus(jetData.getId(), JetStatus.inQueue);

        //update batch data also
        if (productionPlanExist.getIsMergeBatchId())
        {
            batchDao.updateFinishMtrSaveAndFinishMtrByMergeBatchId(productionPlanExist.getBatchId(),0.0,false);
        }
        else
        {
            batchDao.updateFinishMtrSaveAndFinishMtrByBatchId(productionPlanExist.getBatchId(),0.0,false);
        }





    }
}
