package com.main.glory.servicesImpl;

import com.main.glory.Dao.StockAndBatch.BatchDao;
import com.main.glory.Dao.productionPlan.ProductionPlanDao;
import com.main.glory.model.StockDataBatchData.BatchData;
import com.main.glory.model.StockDataBatchData.StockMast;
import com.main.glory.model.StockDataBatchData.response.GetBatchDetailByProduction;
import com.main.glory.model.party.Party;
import com.main.glory.model.productionPlan.request.GetAllProduction;
import com.main.glory.model.productionPlan.request.GetAllProductionWithShadeData;
import com.main.glory.model.productionPlan.ProductionPlan;
import com.main.glory.model.quality.Quality;
import com.main.glory.model.quality.response.GetQualityResponse;
import com.main.glory.model.shade.ShadeMast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("productionPlanServiceImpl")
public class ProductionPlanImpl {

    @Autowired
    ProductionPlanDao productionPlanDao;

    @Autowired
    QualityServiceImp qualityServiceImp;

    @Autowired
    PartyServiceImp partyServiceImp;

    @Autowired
    ShadeServiceImpl shadeService;

    @Autowired
    StockBatchServiceImpl stockBatchService;

    @Autowired
    BatchImpl batchService;

    @Autowired
    BatchDao batchDao;

    public Long saveProductionPlan(ProductionPlan productionPlan) throws Exception {

        Optional<Quality> qualityIsExist= qualityServiceImp.getQualityByIDAndPartyId(productionPlan.getQualityEntryId(),productionPlan.getPartyId());

        Optional<ShadeMast> shadeMastExist=shadeService.getShadeMastById(productionPlan.getShadeId());

        if(qualityIsExist.isPresent() && shadeMastExist.isPresent())
        {
            //check already batch and stock is exist or not
            ProductionPlan productionPlanExist = productionPlanDao.getProductionByBatchAndStockId(productionPlan.getBatchId(),productionPlan.getStockId());
            if(productionPlanExist!=null)
                throw new Exception("already stock and batch is exist");

            List<BatchData> batchDataList = batchService.getBatchById(productionPlan.getBatchId(),productionPlan.getStockId());
            if(batchDataList.isEmpty())
                throw new Exception("No batch data found");

            //ProductionPlan shadeAndStockIsExist = productionPlan.findByStockIdAndShadeId(productionPlan.)

            for(BatchData batchData:batchDataList)
            {
                if(batchData.getIsProductionPlanned()==false)
                    batchData.setIsProductionPlanned(true);
                batchDao.save(batchData);
            }
            productionPlanDao.save(productionPlan);
            return productionPlan.getId();

        }

        else
            throw new Exception("unable to insert the record");

    }
    @Transactional
    public ProductionPlan getProductionData(Long id) throws Exception{
        Optional<ProductionPlan> productionPlan = productionPlanDao.getByProductionId(id);

        if(productionPlan.isEmpty())
            throw new Exception("data not found for production:");

        return productionPlan.get();

    }

    public Boolean deleteById(Long id) throws Exception {
        Optional<ProductionPlan> productionPlan = productionPlanDao.findById(id);
        if (productionPlan.isEmpty())
            return false;

        if(productionPlan.get().getStatus()==true)
        throw new Exception("production is already planned with Jet");
        productionPlanDao.deleteById(id);
        return true;

    }

    public List<GetAllProductionWithShadeData> getAllProductionData() throws Exception{

        Optional<List<GetAllProductionWithShadeData>> productionWithShadeData = productionPlanDao.getAllProductionWithColorTone();
        if(productionWithShadeData.isEmpty())
            throw new Exception("no data found");

        return productionWithShadeData.get();


    }

    public void updateProductionPlan(ProductionPlan productionPlan) throws Exception{

        Optional<ProductionPlan> productionPlanExist = productionPlanDao.findById(productionPlan.getId());
        if(productionPlanExist.isEmpty())
            throw new Exception("no data found");

        productionPlanDao.save(productionPlan);

    }

    public List<GetAllProductionWithShadeData> getAllProductionDataWithAndWithoutPlan() throws Exception {
        Optional<List<GetAllProductionWithShadeData>> productionWithShadeData = productionPlanDao.getAllProduction();
        if(productionWithShadeData.isEmpty())
            throw new Exception("no data found");


        return productionWithShadeData.get();


    }

    //list of production who are not added yet into the jet
    public List<ProductionPlan> getAllProductionListByPartyAndQuality(Long partyId, Long qualityEntryId) {
        List<ProductionPlan> list = productionPlanDao.getProductionByPartyAndQuality(partyId,qualityEntryId);
        return list;

    }

    public ProductionPlan getProductionDataByBatchAndStock(String batchId, Long controlId) {
        ProductionPlan productionPlan=productionPlanDao.getProductionByBatchAndStockId(batchId,controlId);
        return productionPlan;
    }

    public GetBatchDetailByProduction getBatchDetailByProductionAndBatchId(Long productionId, String batchId) throws Exception {

        ProductionPlan productionExist = productionPlanDao.getProductionByBatchAndProduction(batchId,productionId);

        if(productionExist==null)
            throw new Exception("no data found");

        StockMast stockMast=stockBatchService.getStockByStockId(productionExist.getStockId());
        Optional<ShadeMast> shadeMast = shadeService.getShadeMastById(productionExist.getShadeId());
        Double totalWt = batchDao.getAllBatchQtyByBatchIdAndStockId(productionExist.getBatchId(),productionExist.getStockId());
        Party party=partyServiceImp.getPartyDetailById(stockMast.getPartyId());

        GetBatchDetailByProduction data =new GetBatchDetailByProduction(party,totalWt,shadeMast.get(),stockMast,batchId);

        return data;

    }

    public List<GetAllProduction> getAllProductionWithoutFilter() throws Exception {
        List<GetAllProduction> dataList = new ArrayList<>();
        Optional<List<GetAllProductionWithShadeData>> list = productionPlanDao.getAllProduction();

        if(list.isEmpty())
            throw new Exception("no data found");

        for(GetAllProductionWithShadeData g:list.get())
        {
            Party party = partyServiceImp.getPartyDetailById(g.getPartyId());
            GetQualityResponse quality=qualityServiceImp.getQualityByID(g.getQualityEntryId());

            if(party==null)
                continue;
            if (quality==null)
                continue;

            dataList.add(new GetAllProduction(g,party,quality));
        }

        return dataList;
    }

    public List<ProductionPlan> getProductionByShadeId(Long shadeId) {
        return productionPlanDao.getAllProductionByShadeId(shadeId);
    }

/*
    public List<BatchData> getAllBatch(Long partyId, Long qualityEntryId, String batchId) {

    }*/
}
