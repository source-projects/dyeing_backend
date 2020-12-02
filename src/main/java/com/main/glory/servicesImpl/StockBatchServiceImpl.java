package com.main.glory.servicesImpl;

import com.main.glory.Dao.QualityDao;
import com.main.glory.Dao.StockAndBatch.BatchDao;
import com.main.glory.Dao.StockAndBatch.StockMastDao;
import com.main.glory.model.StockDataBatchData.BatchData;
import com.main.glory.model.StockDataBatchData.StockMast;
import com.main.glory.model.StockDataBatchData.request.MergeBatch;
import com.main.glory.model.StockDataBatchData.response.GetAllBatchWithId;
import com.main.glory.model.StockDataBatchData.response.GetAllStockWithPartyNameResponse;
import com.main.glory.model.party.Party;
import com.main.glory.model.quality.Quality;
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


    public List<StockMast> getAllStockBatch(Long qualityId) {

        List<StockMast> stock=stockMastDao.findByQualityId(qualityId);
        System.out.println(stock);
        return stock;

    }


    @Transactional
    public Boolean saveStockBatch(StockMast stockMast) throws Exception{
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
                Optional<List<BatchData>> batchData = batchDao.findByBatchId(stockMast.getBatchData().get(0).getBatchId());

                StockMast x = stockMastDao.save(stockMast);
                x.getBatchData().forEach(e -> {
                    e.setControlId(x.getId());
                });
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
                throw new Exception ("no data found for id: "+id);
    }

    @Transactional
    public void updateBatch(StockMast stockMast) throws Exception {
        Optional<StockMast> original = stockMastDao.findById(stockMast.getId());
        if(original.isEmpty()){
            throw new Exception("No such batch present with id:"+stockMast.getId());
        }
        // Validate, if batch is not given to the production planning then throw the exception
        System.out.println(original.get());
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

    public List<BatchData> getBatchById(String batchId) throws Exception{
        Optional<List<BatchData>> batchData = batchDao.findByBatchId(batchId);
        if(!batchData.isPresent())
            throw new Exception("Batch is not available for id:"+batchId);


        return  batchData.get();

    }

    public List<GetAllBatchWithId> getBatchByPartyAndQuality(Long qualityId, Long partyId) throws Exception{

        Optional<List<StockMast>> stockMast = stockMastDao.findByQualityIdAndPartyId(qualityId,partyId);
        if(!stockMast.isPresent())
        {
            throw new Exception("No such batch is available for partyId:"+partyId+" and QualityId:"+qualityId);
        }

        List<GetAllBatchWithId> getAllBatchWithIdList=new ArrayList<>();

        for(StockMast stock:stockMast.get())
        {
            if(stock == null )
                continue;

            GetAllBatchWithId getAllBatchWithId = new GetAllBatchWithId();

            Optional<List<BatchData>> batch = batchDao.findByControlId(stock.getId());
            for(BatchData batchData : batch.get())
            {
                if(batchData ==null)
                    continue;

                getAllBatchWithId.setId(batchData.getId());
                getAllBatchWithId.setBatchId(batchData.getBatchId());

                if(!getAllBatchWithIdList.contains(getAllBatchWithId)) {
                    getAllBatchWithIdList.add(getAllBatchWithId);
                }


            }

        }
        return getAllBatchWithIdList;
    }

    public void updateBatchForMerge(List<MergeBatch> batchData1) throws Exception{
        int k=0,i=0,n=batchData1.size();

        while(i<n) {
            k=0;
            for (BatchData batchData : batchData1.get(i).getBatchDataList()) {
                if(batchData.getIsProductionPlanned()==true)
                    throw new Exception("Production is planned already so Batch can't be updated for id:"+batchData.getId());

                batchData.setBatchId(batchData1.get(i).getBatchId());
                batchData.setControlId(batchData1.get(i).getControlId());
                batchDao.save(batchData);
                k++;
            }
            i++;
        }

    }
}
