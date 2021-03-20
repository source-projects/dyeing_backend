package com.main.glory.servicesImpl;

import com.main.glory.Dao.StockAndBatch.BatchDao;
import com.main.glory.Dao.productionPlan.ProductionPlanDao;
import com.main.glory.Dao.user.UserDao;
import com.main.glory.model.StockDataBatchData.BatchData;
import com.main.glory.model.StockDataBatchData.StockMast;
import com.main.glory.model.StockDataBatchData.response.GetBatchDetailByProduction;
import com.main.glory.model.dyeingProcess.DyeingChemicalData;
import com.main.glory.model.dyeingProcess.DyeingProcessData;
import com.main.glory.model.dyeingSlip.DyeingSlipData;
import com.main.glory.model.dyeingSlip.DyeingSlipItemData;
import com.main.glory.model.dyeingSlip.DyeingSlipMast;
import com.main.glory.model.jet.JetData;
import com.main.glory.model.jet.JetMast;
import com.main.glory.model.jet.request.AddJetData;
import com.main.glory.model.jet.responce.GetJetData;
import com.main.glory.model.party.Party;
import com.main.glory.model.productionPlan.request.AddDirectBatchToJet;
import com.main.glory.model.productionPlan.request.AddProductionWithJet;
import com.main.glory.model.productionPlan.request.GetAllProduction;
import com.main.glory.model.productionPlan.request.GetAllProductionWithShadeData;
import com.main.glory.model.productionPlan.ProductionPlan;
import com.main.glory.model.quality.Quality;
import com.main.glory.model.quality.response.GetQualityResponse;
import com.main.glory.model.shade.ShadeData;
import com.main.glory.model.shade.ShadeMast;
import com.main.glory.model.supplier.Supplier;
import com.main.glory.model.supplier.SupplierRate;
import com.main.glory.model.user.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("productionPlanServiceImpl")
public class ProductionPlanImpl {

    @Autowired
    SupplierServiceImpl supplierService;

    @Autowired
    DyeingProcessServiceImpl dyeingProcessService;

    @Autowired
    DyeingSlipServiceImpl dyeingSlipService;
    @Autowired
    ProductionPlanDao productionPlanDao;
    @Autowired
    JetServiceImpl jetService;

    @Autowired
    UserDao userDao;
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

    public ProductionPlan getProductionData(Long id) throws Exception{
        ProductionPlan productionPlan = productionPlanDao.getByProductionId(id);

        if(productionPlan==null)
            throw new Exception("data not found for production:");

        return productionPlan;

    }

    public Boolean deleteById(Long id) throws Exception {
       ProductionPlan productionPlan = productionPlanDao.getByProductionId(id);
        if (productionPlan==null)
            return false;

        if(productionPlan.getStatus()==true)
        throw new Exception("production is already planned with Jet");

        //change the status of batch if all the condition are
        List<BatchData> batchDataList = batchDao.findByControlIdAndBatchId(productionPlan.getStockId(),productionPlan.getBatchId());
        for(BatchData batchData: batchDataList)
        {
            batchDao.updateProductionPlanned(batchData.getId(),false);
        }
        productionPlanDao.deleteProductionById(id);
        return true;

    }

    public List<GetAllProductionWithShadeData> getAllProductionData() throws Exception{

        List<GetAllProductionWithShadeData> record=new ArrayList<>();
        Optional<List<GetAllProductionWithShadeData>> list =productionPlanDao.getAllProductionWithColorTone();//new ArrayList<>();
        if(list.isEmpty())
              throw new Exception("no data found");


        return list.get();


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
        GetBatchDetailByProduction data;
        if(productionExist==null)
            throw new Exception("no data found");

        StockMast stockMast=stockBatchService.getStockByStockId(productionExist.getStockId());
        Double totalWt = batchDao.getAllBatchQtyByBatchIdAndStockId(productionExist.getBatchId(),productionExist.getStockId());
        Party party=partyServiceImp.getPartyDetailById(stockMast.getPartyId());
        if(productionExist.getShadeId()==null)
        {
            data=new GetBatchDetailByProduction(party,totalWt,stockMast,batchId);
        }
        else
        {
            Optional<ShadeMast> shadeMast = shadeService.getShadeMastById(productionExist.getShadeId());
            data=new GetBatchDetailByProduction(party,totalWt,shadeMast,stockMast,batchId);
        }


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

    public List<ProductionPlan> getProductionByQualityId(Long id) {
        List<ProductionPlan> list = productionPlanDao.getAllProductionByQualityId(id);
        return list;
    }

    public List<ProductionPlan>getAllProductinByPartyId(Long id) {
        return productionPlanDao.getAllProuctionByPartyId(id);
    }

    public List<ProductionPlan> getProductionByStockId(Long id) {
        List<ProductionPlan> productionPlans = productionPlanDao.getProductionByStockId(id);
        return productionPlans;
    }

    @Transactional
    public Long saveProductionPlanWithJet(AddProductionWithJet productionPlan) throws Exception {

        //if production id is 0 then create the record with shade or jet

        if(productionPlan.getProductionId()==null || productionPlan.getProductionId()==0)
        {
            //create the record


            // first of all we have to store the infor of production any how
            //then process for the jet if the jet id is not null
            ProductionPlan productionPlanExist = productionPlanDao.getProductionByBatchAndStockId(productionPlan.getBatchId(),productionPlan.getStockId());
            if(productionPlanExist!=null)
                throw new Exception("same production available with batch and stock");



            //update the status of  batches as well
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
            productionPlanExist = new ProductionPlan(productionPlan);
            ProductionPlan x = productionPlanDao.save(productionPlanExist);




            //now check that the jet id is null or not
            if(productionPlan.getJetId()==0 || productionPlan.getJetId()==null)
            {
                return x.getId();
            }
            else
            {
                //we have to add the same data with jet as well
                List<AddJetData> jetDataList = new ArrayList<>();
                AddJetData addJetData = new AddJetData();
                addJetData.setProductionId(x.getId());
                addJetData.setControlId(productionPlan.getJetId());
                addJetData.setSequence(12l);
                jetDataList.add(addJetData);

                jetService.saveJetData(jetDataList);
                return x.getId();
            }


        }
        else
        {
            //update the record of only shade , if jet is not assign and check the status as well

            //check the production is already added in jet or not
            ProductionPlan productionPlanExist = productionPlanDao.getByProductionId(productionPlan.getProductionId());
            if(productionPlanExist.getStatus()==true)
                throw new Exception("production is already in jet");


            //change the status of production
            Optional<ShadeMast> shadeMastExist = shadeService.getShadeMastById(productionPlan.getShadeId());
            if(shadeMastExist.isEmpty())
                throw new Exception("no shade found");

            productionPlanExist=new ProductionPlan(productionPlan);

            ProductionPlan x = productionPlanDao.save(productionPlanExist);
            //check now that the jet is coming or not

            //now check that the jet id is null or not
            if(productionPlan.getJetId()==0 || productionPlan.getJetId()==null)
            {
                return x.getId();
            }
            else
            {
                //we have to add the same data with jet as well
                List<AddJetData> jetDataList = new ArrayList<>();
                AddJetData addJetData = new AddJetData();
                addJetData.setProductionId(x.getId());
                addJetData.setControlId(productionPlan.getJetId());
                addJetData.setSequence(12l);
                jetDataList.add(addJetData);

                jetService.saveJetData(jetDataList);
                return x.getId();
            }


        }




    }

    @Transactional
    public void saveDirectDyeingSlip(AddDirectBatchToJet record) throws Exception {
        //store direct dyeing slip with jet and with shade or else shade colour

        /*

        1.check the record exist or not
        2.create the production
        3.add the record to the jet
        4.create the dyeing slip for the batch
        5.change the batch status

         */

        Double totalBatchCapacity = 0.0;

        Long jetSequence=0l;

        Double totalBatchWt = stockBatchService.getWtByControlAndBatchId(record.getStockId(),record.getBatchId());
        //process type ::directDyeing

        ProductionPlan productionPlan =new ProductionPlan(record);
        /*if(productionPlan.getShadeId()==null && productionPlan.getColorName().isEmpty())
            throw new Exception("please enter shade or color name");*/



        //check the production is exist with batch and stock or not

        ProductionPlan productionPlanExist = productionPlanDao.getProductionByBatchAndStockId(record.getBatchId(),record.getStockId());
        if(productionPlanExist!=null)
            throw new Exception("batch and stock is already exist");



        //jet capacity check and store the jet record
        JetMast jetMast = jetService.getJetMastById(record.getJetId());
        List<GetJetData> jetDataList = jetService.getJetRecordData(record.getJetId());


        for(GetJetData getJetData : jetDataList)
        {
           ProductionPlan jetWithProduction = productionPlanDao.getByProductionId(getJetData.getProductionId());
           totalBatchCapacity+=stockBatchService.getWtByControlAndBatchId(jetWithProduction.getStockId(),jetWithProduction.getBatchId());

           if(jetSequence < getJetData.getSequence())
           {
               jetSequence=getJetData.getSequence();
           }
        }

        if(totalBatchCapacity+totalBatchWt>jetMast.getCapacity())
           throw new Exception("Batch WT is greather than Jet capacity please reduce or remove the Batch");

        ProductionPlan x = productionPlanDao.save(productionPlan);

        JetData jetData = new JetData(x,jetSequence+1,jetMast);
        jetService.saveJetRecord(jetData);





        //create the dyeing slip for the given dyeing record

        DyeingSlipMast dyeingSlipMast = new DyeingSlipMast(record,x);
        List<DyeingSlipData> dyeingSlipDataList = new ArrayList<>();
        //dyeing slip record
        DyeingSlipData dyeingSlipData = new DyeingSlipData(record.getDyeingSlipData());
        List<DyeingSlipItemData> dyeingSlipItemDataList  =new ArrayList<>();

        //if shade id is not null then get the shade item as well
        if(record.getShadeId()!=null) {
            Optional<ShadeMast> shadeMast = shadeService.getShadeMastById(record.getShadeId());
            for (ShadeData shadeData : shadeMast.get().getShadeDataList()) {
                Optional<Supplier> supplier = supplierService.getSupplier(shadeData.getSupplierId());
                SupplierRate supplierRate = supplierService.getSupplierRateByItemId(shadeData.getSupplierItemId());
                dyeingSlipItemDataList.add(new DyeingSlipItemData(shadeData, supplierRate, supplier.get(), totalBatchWt));
            }
        }



        dyeingSlipItemDataList.addAll(dyeingSlipData.getDyeingSlipItemData());
        dyeingSlipData.setDyeingSlipItemData(dyeingSlipItemDataList);
        dyeingSlipDataList.add(dyeingSlipData);

        dyeingSlipMast.setDyeingSlipDataList(dyeingSlipDataList);
        dyeingSlipService.addDirectDyeingSlip(dyeingSlipMast);



        //change the batch status

        //update the status of  batches as well
        List<BatchData> batchDataList = batchService.getBatchById(x.getBatchId(),x.getStockId());

        //ProductionPlan shadeAndStockIsExist = productionPlan.findByStockIdAndShadeId(productionPlan.)

        for(BatchData batchData:batchDataList)
        {
            if(batchData.getIsProductionPlanned()==false)
                batchData.setIsProductionPlanned(true);
            batchDao.save(batchData);
        }





    }

/*
    public List<BatchData> getAllBatch(Long partyId, Long qualityEntryId, String batchId) {

    }*/
}
