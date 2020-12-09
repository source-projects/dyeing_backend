package com.main.glory.servicesImpl;

import com.main.glory.Dao.PartyDao;
import com.main.glory.Dao.QualityDao;
import com.main.glory.Dao.StockAndBatch.BatchDao;
import com.main.glory.Dao.StockAndBatch.StockMastDao;
import com.main.glory.model.StockDataBatchData.BatchData;
import com.main.glory.model.StockDataBatchData.StockMast;
import com.main.glory.model.StockDataBatchData.request.MergeBatch;
import com.main.glory.model.StockDataBatchData.response.*;
import com.main.glory.model.party.Party;
import com.main.glory.model.quality.Quality;
import org.hibernate.engine.jdbc.batch.spi.Batch;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<GetAllStockWithPartyNameResponse> getAllStockBatch(String getBy, Long id){
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
        if(data.isEmpty()) return null;
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

    public void updateBatchForMerge(List<MergeBatch> batchData1) throws Exception{
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
        for(int x=0;x<controlId.size();x++)
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

    public List<GetAllBatchWithPartyAndQuality> getAllBatchDetail() {
        List<GetAllBatchWithPartyAndQuality> getAllBatchWithPartyAndQualities=new ArrayList<>();
        List<BatchData> batchData = batchDao.getAllBatchWithControlId();
        for(BatchData batch:batchData)
        {
            GetAllBatchWithPartyAndQuality getAllBatchWithPartyAndQuality=new GetAllBatchWithPartyAndQuality();
            getAllBatchWithPartyAndQuality.setBatchId(batch.getBatchId());
            getAllBatchWithPartyAndQuality.setControlId(batch.getControlId());

            Optional<StockMast> stockMast = stockMastDao.findById(batch.getControlId());
            Optional<Party> party = partyDao.findById(stockMast.get().getPartyId());
            Optional<Quality> quality=qualityDao.findById(stockMast.get().getQualityId());

            getAllBatchWithPartyAndQuality.setQualityEntryId(quality.get().getId());
            getAllBatchWithPartyAndQuality.setQualityId(quality.get().getQualityId());
            getAllBatchWithPartyAndQuality.setQualityName(quality.get().getQualityName());
            getAllBatchWithPartyAndQuality.setQualityType(quality.get().getQualityType());
            getAllBatchWithPartyAndQuality.setPartyId(party.get().getId());
            getAllBatchWithPartyAndQuality.setPartyName(party.get().getPartyName());
            getAllBatchWithPartyAndQualities.add(getAllBatchWithPartyAndQuality);
        }


        return getAllBatchWithPartyAndQualities;
    }
}
