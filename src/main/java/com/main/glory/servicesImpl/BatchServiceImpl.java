package com.main.glory.servicesImpl;

import com.main.glory.Dao.batch.BatchDataDao;
import com.main.glory.Dao.batch.BatchGrDetailDao;
import com.main.glory.Dao.batch.BatchMastDao;
import com.main.glory.Dao.QualityDao;
import com.main.glory.Dao.fabric.FabStockDataDao;
import com.main.glory.Dao.fabric.FabStockMastDao;
import com.main.glory.model.quality.Quality;
import com.main.glory.model.batch.BatchData;
import com.main.glory.model.batch.BatchMast;
import com.main.glory.model.fabric.FabStockData;
import com.main.glory.services.BatchServicesInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service("batchServiceImpl")
public class BatchServiceImpl implements BatchServicesInterface {

    @Autowired
    private BatchMastDao batchMastDao;

    @Autowired
    private BatchDataDao batchDataDao;

    @Autowired
    private BatchGrDetailDao batchGrDetailDao;

    @Autowired
    private QualityDao qualityDao;

    @Autowired
    private FabStockMastDao fabStockMastDao;

    @Autowired
    private FabStockDataDao fabStockDataDao;

    @Transactional
    public void createBatch(BatchMast batchMast) throws Exception{

        /*
        * Validations
        * phase 1: fab id should be present in the db.
        * phase 2: Batch must not be already created.
        * phase 3: Required amount of mtr and wt for batch creation is present in the stock.
        * phase 4: Mtr and Wt Validation based on Quality (wt per 100 mtr).
        */

        Optional<Quality> quality = qualityDao.findById(batchMast.getQualityId());

        if(quality.isEmpty()){
            throw new Exception("This quality does not exist");
        }

        Double ratio = quality.get().getWtPer100m();

        for (BatchData e : batchMast.getBatchData()) {
            Optional<FabStockData> fabStockData = fabStockDataDao.findById(e.getFabInId());
            if (fabStockData.isEmpty()) {
                throw new Exception("No fabric in of id: " + e.getFabInId());
            }

            if(fabStockData.get().getBatchCreated()){
                throw new Exception("Batch already created for fabricIn with id:" + e.getFabInId() + " and GR:" + e.getGr());
            }

            if(fabStockData.get().getMtr() < e.getMtr() || fabStockData.get().getWt() < e.getWt()){
                throw new Exception("Mtr and wt is more than in Stock for GR:"+e.getGr());
            }

            if(fabStockData.get().getWt() != fabStockData.get().getMtr() * ratio / 100.0){
                throw new Exception("Mtr and wt ratio is not as per quality for GR:"+e.getGr());
            }
        }

        /*
        * Batch Creation Process:
        * find the stock.
        *   if the stock amount equals required batch size
        *       create the batch and set batchCreated = true for that fab_stock
        *   else
        *       update the fab in to the amount used in the batch creation
        *           - then set isCut = true as that stock is sliced.
        *           - and set batchCreated = true
        *       create new fab stock with the remaining amount
        *       give old stock's GR as a refGR to this new stock
        *
        */
        for (BatchData e : batchMast.getBatchData()) {
            FabStockData fabStockData = fabStockDataDao.findById(e.getFabInId()).get();
            if(e.getMtr() == fabStockData.getMtr()){
                fabStockData.setIsCut(false);
                fabStockData.setBatchCreated(true);
            } else if(e.getMtr() < fabStockData.getMtr()) {
                fabStockData.setIsCut(true);
                fabStockData.setBatchCreated(true);
                FabStockData newData = new FabStockData(fabStockData);
                newData.setId(null);
                newData.setMtr(fabStockData.getMtr() - e.getMtr());
                newData.setWt(fabStockData.getWt() - e.getWt());
                newData.setRefGr(fabStockData.getGr());
                fabStockData.setMtr(e.getMtr());
                fabStockData.setWt(e.getWt());
                fabStockDataDao.save(fabStockData);
                fabStockDataDao.save(newData);
            }
        }
        BatchMast batchMast1 = batchMastDao.save(batchMast);

    }

    @Transactional
    public void updateBatch(BatchMast batchMast) throws Exception {
        Optional<BatchMast> original = batchMastDao.findById(batchMast.getId());

        if(original.isEmpty()){
            throw new Exception("No such batch present with id:"+batchMast.getId());
        }

        // Validate, if batch is not given to the production planning then throw the exception
        if(original.get().getIsProductionPlaned()){
            throw new Exception("Batch is already sent to production, for id:"+batchMast.getId());
        }

        batchMastDao.save(batchMast);
    }

    @Transactional
    public BatchMast getBatchMastById(Long id) {
        var data=batchMastDao.findById(id);
        if(data.isEmpty())
            return null;
        else
            return data.get();
    }

    @Transactional
    public void deleteBatch(Long id) throws Exception{
        Optional<BatchMast> batchMast = batchMastDao.findById(id);
        if(batchMast.isEmpty()){
            throw new Exception("No such batch present with id:"+id);
        }

        if(Objects.equals(batchMast.get().getIsProductionPlaned(),true)){
            throw new Exception("Can't delete the batch, already in production, for id:"+id);
        }

        for (BatchData batchDatum : batchMast.get().getBatchData()) {
            Long fabId = batchDatum.getFabInId();
            fabStockDataDao.findById(fabId).get().setBatchCreated(false);
        }

        batchMastDao.deleteById(id);
    }

    @Transactional
    public List<BatchMast> getBatchList(){
        return batchMastDao.findAllByIdWithoutData();
    }
}

