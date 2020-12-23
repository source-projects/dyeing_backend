package com.main.glory.servicesImpl;

import com.main.glory.Dao.PartyDao;
import com.main.glory.Dao.QualityDao;
import com.main.glory.Dao.StockAndBatch.BatchDao;
import com.main.glory.Dao.StockAndBatch.StockMastDao;
import com.main.glory.model.StockDataBatchData.BatchData;
import com.main.glory.model.StockDataBatchData.StockMast;
import com.main.glory.model.StockDataBatchData.request.MergeSplitBatch;
import com.main.glory.model.StockDataBatchData.response.*;
import com.main.glory.model.party.Party;
import com.main.glory.model.quality.Quality;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.support.NullValue;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service("stockBatchServiceImpl")
public class StockBatchServiceImpl {

    @Autowired
    StockMastDao stockMastDao;

    @Autowired
    private QualityDao qualityDao;

    @Autowired
    BatchDao batchDao;


    @Autowired
    PartyDao partyDao;


    public List<StockMast> getAllStockBatch(Long qualityId) {

        List<StockMast> stock = stockMastDao.findByQualityId(qualityId);
        System.out.println(stock);
        return stock;

    }


    @Transactional
    public Boolean saveStockBatch(StockMast stockMast) throws Exception {
       Date dt = new Date(System.currentTimeMillis());
        stockMast.setCreatedDate(dt);
        stockMast.setIsProductionPlanned(false);
        Optional<Quality> quality=qualityDao.findById(stockMast.getQualityId());
        try {
            if (!quality.isPresent()) {
                throw new Exception("Insert Quality first");
            }
            else
            {
                StockMast x = stockMastDao.save(stockMast);

                return true;

            }
        }
        catch(Exception e)
        {
            return false;
        }
    }



    @Transactional
    public List<GetAllStockWithPartyNameResponse> getAllStockBatch(String getBy, Long id) throws Exception {
        Optional<List<GetAllStockWithPartyNameResponse>> data = null;
        Boolean flag = false;
        if(id ==  null){
            data = stockMastDao.getAllStockWithPartyName();

        }
        else if(getBy.equals("own")){
            data = stockMastDao.getAllStockWithPartyNameByCreatedBy(id);

        }

        else if(getBy.equals("group")){
            data = stockMastDao.getAllStockWithPartyNameByUserHeadId(id);
        }
        if(data.isEmpty()) throw new Exception("no data found");
        else return data.get();
    }

    @Transactional
    public Optional<StockMast> getStockBatchById(Long id) throws Exception{
            Optional<StockMast> data = stockMastDao.findById(id);
            if(data.isPresent()){
                return data;
            }
            else
                throw new Exception ("no data found for StockId: "+id);
    }

    @Transactional
    public void updateBatch(StockMast stockMast) throws Exception {
        Optional<StockMast> original = stockMastDao.findById(stockMast.getId());
        if(original.isEmpty()){
            throw new Exception("No such batch present with id:"+stockMast.getId());
        }

        // Validate, if batch is not given to the production planning then throw the exception
        if(original.get().getIsProductionPlanned()){
            throw new Exception("BatchData is already sent to production, for id:"+stockMast.getId());
        }



        //for delete the batch gr if not coming from FE

        //##Get the data first from the list
        Map<Long,Boolean> batchGr=new HashMap<>();
        List<BatchData> batchData = batchDao.findByControlId(stockMast.getId());
        for(BatchData batch: batchData)
        {
            batchGr.put(batch.getId(),false);
            //System.out.println(batch.getId());
        }

        //change the as per the data is coming from FE
        for(BatchData batch:stockMast.getBatchData())
        {
            //System.out.println("coming:"+batch.getId());
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

        //update record
        stockMastDao.save(stockMast);


    }

    @Transactional
    public void deleteStockBatch(Long id) throws Exception{
        Optional<StockMast> stockMast = stockMastDao.findById(id);
        if(stockMast.isEmpty()){
            throw new Exception("No such stock batch present with id:"+id);
        }

        if(Objects.equals(stockMast.get().getIsProductionPlanned(),true)){
            throw new Exception("Can't delete the batch, already in production, for id:"+id);
        }

        stockMastDao.deleteById(id);
    }


    public List<StockMast> findByQualityId(Long id) {
        return stockMastDao.findByQualityId(id);
    }

    public List<BatchData> getBatchById(String batchId,Long controlId) throws Exception{

        List<BatchData> batchData = batchDao.findByControlIdAndBatchIdAndIsExtra(controlId,batchId,false);


        if(batchData.isEmpty())
            throw new Exception("Batch is not available for batchId:"+batchId);


        return  batchData;

    }

    public List<GetAllBatch> getBatchByPartyAndQuality(Long qualityId, Long partyId) throws Exception{

        List<StockMast> stockMast = stockMastDao.findByQualityIdAndPartyId(qualityId,partyId);
        if(stockMast.isEmpty())
        {
            throw new Exception("No batch found");
        }


        List<GetAllBatch> getAllBatchList =new ArrayList<>();
        List<String> batchName =new ArrayList<>();
        List<Boolean> productionPlanned =new ArrayList<>();
        List<Long> controlId =new ArrayList<>();

        GetAllBatch getAllBatch;

        for(StockMast stockMast1:stockMast)
        {

            List<BatchData> batch = batchDao.findByControlId(stockMast1.getId());

            for(BatchData batchData : batch)
            {
                if(batchData.getIsBillGenrated()==true)
                    continue;

                //Take another arraylist because it is not working with Object arrayList
                if(!batchName.contains(batchData.getBatchId()))
                {
                    batchName.add(batchData.getBatchId());
                    controlId.add(batchData.getControlId());
                    productionPlanned.add(batchData.getIsProductionPlanned());
                }
                else if(!controlId.contains(batchData.getControlId()))
                {
                    batchName.add(batchData.getBatchId());
                    controlId.add(batchData.getControlId());
                    productionPlanned.add(batchData.getIsProductionPlanned());
                }

            }



        }
        Optional<Party> party=partyDao.findById(partyId);
        Optional<Quality> quality =qualityDao.findById(qualityId);

        //storing all the data of batchName to object
        for(int x=0;x<controlId.size();x++)
        {
            if(quality.get()!=null&&party.get()!=null)
            {
                if(!quality.isPresent() && !party.isPresent())
                    continue;

                getAllBatch=new GetAllBatch(party.get(),quality.get());
                getAllBatch.setBatchId(batchName.get(x));
                getAllBatch.setControlId(controlId.get(x));
                getAllBatch.setProdctionPlanned(productionPlanned.get(x));
                getAllBatchList.add(getAllBatch);

            }

        }


        if(getAllBatchList.isEmpty())
            throw new Exception("May all batches planned or not available ");
        return getAllBatchList;


    }

    public void updateBatchForMerge(List<MergeSplitBatch> batchData1) throws Exception{
        int k=0,i=0,n=batchData1.size();

        while(i<n) {
            k=0;
            for (BatchData batchData : batchData1.get(i).getBatchDataList()) {
                if(batchData.getIsProductionPlanned()==true)
                    throw new Exception("Production is planned already so Batch can't be updated for id:"+batchData.getBatchId());

                batchData.setBatchId(batchData1.get(i).getBatchId());
                batchData.setControlId(batchData1.get(i).getControlId());
                batchDao.save(batchData);
                k++;
            }
            i++;
        }

    }


    public List<GetAllBatch> getBatchWithoutProductionPlanByPartyAndQuality(Long qualityId, Long partyId) throws Exception{

        List<StockMast> stockMast = stockMastDao.findByQualityIdAndPartyId(qualityId,partyId);
        if(stockMast==null)
        {
            throw new Exception("No such batch is available for partyId:"+partyId+" and QualityId:"+qualityId);
        }

        List<GetAllBatch> getAllBatchList =new ArrayList<>();
        List<String> batchName =new ArrayList<>();
        List<Boolean> productionPlanned =new ArrayList<>();
        List<Long> controlId =new ArrayList<>();

        GetAllBatch getAllBatch;

        for(StockMast stockMast1:stockMast)
        {

            List<BatchData> batch = batchDao.findByControlId(stockMast1.getId());

            for(BatchData batchData : batch)
            {
                if(batchData.getIsBillGenrated()==true)
                    continue;

                //Take another arraylist because it is not working with Object arrayList
                if(!batchName.contains(batchData.getBatchId()))
                {
                    batchName.add(batchData.getBatchId());
                    controlId.add(batchData.getControlId());
                    productionPlanned.add(batchData.getIsProductionPlanned());
                }
                else if(!controlId.contains(batchData.getControlId()))
                {
                    batchName.add(batchData.getBatchId());
                    controlId.add(batchData.getControlId());
                    productionPlanned.add(batchData.getIsProductionPlanned());
                }

            }



        }

        //storing all the data of batchName to object
        for(int x=0;x<controlId.size();x++)
        {
            getAllBatch=new GetAllBatch();
            getAllBatch.setBatchId(batchName.get(x));
            getAllBatch.setControlId(controlId.get(x));
            getAllBatch.setProdctionPlanned(productionPlanned.get(x));
            getAllBatchList.add(getAllBatch);
        }


        if(getAllBatchList.isEmpty())
            throw new Exception("May all batches planned or not available ");
        return getAllBatchList;




    }

    public List<GetAllBatch> getAllBatchByMaster(Long userHeadId) throws Exception{


        List<StockMast> stockMast = stockMastDao.findByUserHeadId(userHeadId);
        if(stockMast==null)
        {
            throw new Exception("No such batch is available for master:"+userHeadId);
        }

        List<GetAllBatch> getAllBatchList =new ArrayList<>();
        List<String> batchName =new ArrayList<>();
        List<Boolean> productionPlanned =new ArrayList<>();
        List<Long> controlId =new ArrayList<>();

        GetAllBatch getAllBatch;

        for(StockMast stockMast1:stockMast)
        {

            List<BatchData> batch = batchDao.findByControlId(stockMast1.getId());

            for(BatchData batchData : batch)
            {
                if(batchData.getIsBillGenrated()==true)
                    continue;

                //Take another arraylist because it is not working with Object arrayList
                if(!batchName.contains(batchData.getBatchId()))
                {
                    batchName.add(batchData.getBatchId());
                    controlId.add(batchData.getControlId());
                    productionPlanned.add(batchData.getIsProductionPlanned());
                }
                else if(!controlId.contains(batchData.getControlId()))
                {
                    batchName.add(batchData.getBatchId());
                    controlId.add(batchData.getControlId());
                    productionPlanned.add(batchData.getIsProductionPlanned());
                }

            }



        }

        //storing all the data of batchName to object
        for(int x=0;x<batchName.size();x++)
        {
            getAllBatch=new GetAllBatch();
               getAllBatch.setBatchId(batchName.get(x));
            getAllBatch.setControlId(controlId.get(x));
            getAllBatch.setProdctionPlanned(productionPlanned.get(x));

            Optional<StockMast> stockMast1 = stockMastDao.findById(controlId.get(x));
            Optional<Party> party = partyDao.findById(stockMast1.get().getPartyId());
            Optional<Quality> quality = qualityDao.findById(stockMast1.get().getQualityId());

            getAllBatch.setPartyId(party.get().getId());
            getAllBatch.setPartyName(party.get().getPartyName());
            getAllBatch.setQualityEntryId(quality.get().getId());
            getAllBatch.setQualityId(quality.get().getQualityId());
            getAllBatch.setQualityName(quality.get().getQualityName());
            getAllBatch.setQualityType(quality.get().getQualityType());

            getAllBatchList.add(getAllBatch);
        }


        if(getAllBatchList.isEmpty())
            throw new Exception("May all batches planned or not available ");
        return getAllBatchList;


    }

    public List<BatchToPartyAndQuality> getAllBatchDetail()  throws Exception{
        List<BatchToPartyAndQuality> getAllBatchWithPartyAndQualities=new ArrayList<>();
        List<GetBatchWithControlId> batchData = batchDao.findAllBasedOnControlIdAndBatchId();

        for(GetBatchWithControlId batch : batchData)
        {


            Optional<StockMast> stockMast=stockMastDao.findById(batch.getControlId());

            if(stockMast.get().getQualityId()!=null && stockMast.get().getPartyId()!=null)
            {
                Optional<Quality> quality=qualityDao.findById(stockMast.get().getQualityId());

                Optional<Party> party=partyDao.findById(stockMast.get().getPartyId());

                BatchToPartyAndQuality batchToPartyAndQuality=new BatchToPartyAndQuality(quality.get(),party.get(),batch);

                getAllBatchWithPartyAndQualities.add(batchToPartyAndQuality);



            }





        }
        if(getAllBatchWithPartyAndQualities.isEmpty())
            throw new Exception("No data found");

        return  getAllBatchWithPartyAndQualities;
    }

    public BatchToPartyAndQuality getPartyAndQualityByBatch(Long controlId, String batchId) throws Exception{
        Optional<StockMast> stockMast = stockMastDao.findById(controlId);

        if(!stockMast.isPresent())
            throw new Exception("Stock is not found for batchId:"+batchId);

        Optional<Party> party=partyDao.findById(stockMast.get().getPartyId());
        if(!party.isPresent())
            throw new Exception("Party not found for batchId:"+batchId);

        Optional<Quality> quality = qualityDao.findById(stockMast.get().getQualityId());

        if(!quality.isPresent())
            throw new Exception("Quality not found for batchId:"+batchId);


        BatchToPartyAndQuality batchToPartyAndQuality=new BatchToPartyAndQuality();
        batchToPartyAndQuality.setPartyId(party.get().getId());
        batchToPartyAndQuality.setPartyName(party.get().getPartyName());
        batchToPartyAndQuality.setQualityEntryId(quality.get().getId());
        batchToPartyAndQuality.setQualityId(quality.get().getQualityId());
        batchToPartyAndQuality.setQualityName(quality.get().getQualityName());
        batchToPartyAndQuality.setBatchId(batchId);
        batchToPartyAndQuality.setControlId(controlId);
        return batchToPartyAndQuality;
    }

    public Boolean deleteStockBatchWithControlAndBatchID(Long controlId, String batchId) throws Exception {

        List<BatchData> batchData = batchDao.findByControlIdAndBatchId(controlId,batchId);
        if(batchData.isEmpty())
            throw new Exception("Batch not found for id:"+batchId);
        for(BatchData batch:batchData)
        {
            batchDao.deleteById(batch.getId());
        }
        return true;

    }


    public void deleteBatchGr(Long id) throws Exception{
        Optional<BatchData> batchData = batchDao.findById(id);
        if(!batchData.isPresent())
            throw new Exception("batch gr not found");

        batchDao.deleteById(batchData.get().getId());

    }

    public void updateBatchSplit(List<MergeSplitBatch> batchData1)throws Exception {

        int k=0,i=0,n=batchData1.size();

        while(i<n) {
            k=0;
            if(batchData1.get(i).getIsSplit()==true)
            {
                List<BatchData> checkBatchIsAvailable = batchDao.findByControlIdAndBatchId(batchData1.get(i).getControlId(),batchData1.get(i).getBatchId());
                if(!checkBatchIsAvailable.isEmpty())
                {
                    throw new Exception("Batch id is already exist");
                }
            }
            for (BatchData batchData : batchData1.get(i).getBatchDataList()) {

                batchData.setBatchId(batchData1.get(i).getBatchId());
                batchData.setControlId(batchData1.get(i).getControlId());
                batchDao.save(batchData);
                k++;
            }
            i++;
        }


    }

    public Boolean IsBatchAvailable(Long controlId, String batchId) {
        List<BatchData> checkBatchIsAvaialble =  batchDao.findByControlIdAndBatchId(controlId,batchId);
        if(checkBatchIsAvaialble.isEmpty())
        {
            return true;
        }

        return false;

    }

    public List<StockMast> getBatchByPartyId(Long partyId) {

        Optional<List<StockMast>> stockMasts = stockMastDao.findByPartyId(partyId);


        return stockMasts.get();
    }
}
