package com.main.glory.servicesImpl;

import com.main.glory.Dao.QualityDao;
import com.main.glory.Dao.StockAndBatch.BatchDao;
import com.main.glory.Dao.StockAndBatch.StockMastDao;
import com.main.glory.model.StockDataBatchData.BatchData;
import com.main.glory.model.StockDataBatchData.StockMast;
import com.main.glory.model.quality.Quality;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service("stockBatchServiceImpl")
public class StockBatchServiceImpl {

    @Autowired
    StockMastDao stockMastDao;

    @Autowired
    private QualityDao qualityDao;

    @Autowired
    BatchDao batchDao;

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
    public List<StockMast> getAllStockBatch(){
        List<StockMast> data = stockMastDao.findAll();
        if(data.isEmpty())
            return null;
        else
            return data;
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

    public List<BatchData> getAllBatchByQuality(Long qualityId) {
        Optional<Quality> quality = qualityDao.findById(qualityId);
       try {
           if (quality.isPresent())
           {
               StockMast stock= stockMastDao.findByQualityId(quality.get().getId());
               List<BatchData> dataList = batchDao.findByControlId(stock.getId());
               System.out.print(stock);
               System.out.print(dataList);
               return dataList;

           } else
               throw new Exception("Quality not found");
       }
       catch (Exception e)
       {
           System.out.print(e.getMessage());
       }
    return null;
    }
}
