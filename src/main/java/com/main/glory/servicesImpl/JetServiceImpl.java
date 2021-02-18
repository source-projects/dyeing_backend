package com.main.glory.servicesImpl;

import com.main.glory.Dao.Jet.JetDataDao;
import com.main.glory.Dao.Jet.JetMastDao;
import com.main.glory.Dao.StockAndBatch.BatchDao;
import com.main.glory.Dao.color.ColorDataDao;
import com.main.glory.Dao.productionPlan.ProductionPlanDao;
import com.main.glory.Dao.qualityProcess.ChemicalDao;
import com.main.glory.model.StockDataBatchData.BatchData;
import com.main.glory.model.StockDataBatchData.StockMast;
import com.main.glory.model.StockDataBatchData.response.GetBatchWithControlId;
import com.main.glory.model.color.ColorBox;
import com.main.glory.model.dyeingProcess.DyeingChemicalData;
import com.main.glory.model.dyeingProcess.DyeingProcessData;
import com.main.glory.model.dyeingProcess.DyeingProcessMast;
import com.main.glory.model.dyeingSlip.DyeingSlipData;
import com.main.glory.model.dyeingSlip.DyeingSlipItemData;
import com.main.glory.model.dyeingSlip.DyeingSlipMast;
import com.main.glory.model.jet.JetStatus;
import com.main.glory.model.jet.request.*;
import com.main.glory.model.jet.JetData;
import com.main.glory.model.jet.JetMast;
import com.main.glory.model.jet.responce.*;
import com.main.glory.model.productionPlan.ProductionPlan;
import com.main.glory.model.quality.response.GetQualityResponse;
import com.main.glory.model.shade.ShadeData;
import com.main.glory.model.shade.ShadeMast;
import com.main.glory.model.supplier.Supplier;
import com.main.glory.model.supplier.SupplierRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service("jetServiceImpl")
@Transactional
public class JetServiceImpl {

    @Autowired
    ProductionPlanDao productionPlanDao;
    @Autowired
    DyeingSlipServiceImpl dyeingSlipService;
    @Autowired
    QualityServiceImp qualityServiceImp;

    @Autowired
    QualityProcessImpl qualityProcessServiceImp;

    @Autowired
    SupplierServiceImpl supplierService;

    @Autowired
    JetMastDao jetMastDao;

    @Autowired
    ColorServiceImpl colorService;

    @Autowired
    ColorDataDao colorDataDao;

    @Autowired
    ChemicalDao chemicalDao;

    @Autowired
    DyeingProcessServiceImpl dyeingProcessService;

    @Autowired
    JetDataDao jetDataDao;

    @Autowired
    StockBatchServiceImpl stockBatchService;

    @Autowired
    ProductionPlanImpl productionPlanService;

    @Autowired
    ShadeServiceImpl shadeService;

    @Autowired
    BatchDao batchDao;

    public void saveJet(AddJet jetMast) throws Exception {


        Optional<JetMast> jetExist = jetMastDao.findByName(jetMast.getName());
        if(jetExist.isPresent()) {
            throw new Exception("jet is avaialble with name");
        }
        JetMast newJet=new JetMast(jetMast);
        jetMastDao.save(newJet);

    }



    public void saveJetData( List<AddJetData> jetDataList) throws Exception{

        //get the dyeing process color box list
        Long productionId = jetDataList.get(0).getProductionId();

        //check the production is available or not

        ProductionPlan productionPlanExits=productionPlanService.getProductionData(productionId);
        if(productionPlanExits.getStatus())
            throw new Exception("production is already added into jet");

        Optional<ShadeMast> shadeMast = shadeService.getShadeMastById(productionPlanExits.getShadeId());

        if(shadeMast.isEmpty())
        {
            throw new Exception("no shade found with id:"+productionPlanExits.getShadeId());
        }

        //count the color amt to deduct
        Double colorAmtToDeduct=0.0;
        Double totalBatchWt=stockBatchService.getWtByControlAndBatchId(productionPlanExits.getStockId(),productionPlanExits.getBatchId());

        //check the capacity first for the color box issue had that much capacity to fill the batch or not
        for(ShadeData shadeData:shadeMast.get().getShadeDataList())
        {
            String suppplierName = supplierService.getSupplierNameByItemId(shadeData.getSupplierItemId());
            SupplierRate supplierRate=supplierService.getSupplierRateByItemId(shadeData.getSupplierItemId());
            Double data=0.0;
            colorAmtToDeduct = (shadeData.getConcentration()*totalBatchWt)/100;
            List<ColorBox> colorBoxList = colorService.getColorBoxListByItemId(shadeData.getSupplierItemId());

            if(colorBoxList.isEmpty())
                throw new Exception("no box is available for batch, supplier:"+suppplierName+", item:"+supplierRate.getItemName());

            for(ColorBox c:colorBoxList)
            {
                data += c.getQuantityLeft();

            }
            if(colorAmtToDeduct > data)
                throw new Exception("issue the box first because required color amt:"+colorAmtToDeduct+" and available is:"+data+" for batch, item:"+shadeData.getId());


        }


        //*********  Color item checks end


        //save to jet data first check the capacity
        Double availableJetCapacity=0.0;

        Double availableBatchInJetCapacity=0.0;
        Double newBatchCapacity = 0.0;

        //first check the capacity and production detail is available or not
        for(AddJetData addJetData:jetDataList)
        {
            if(addJetData.getControlId()==null)
                throw new Exception("control id can't be null ");

            if(addJetData.getProductionId()==null)
                throw new Exception("pruduction id can't be null ");

            Optional<JetMast> jetMastExist = jetMastDao.findById(addJetData.getControlId());

            ProductionPlan productionPlanExist=productionPlanService.getProductionData(addJetData.getProductionId());

            Optional<JetData> jetDataExistWithProducton=jetDataDao.findByControlIdAndProductionId(addJetData.getControlId(),addJetData.getProductionId());

            if(jetMastExist.isEmpty())
                throw new Exception("Jet Mast is not found");

            if(productionPlanExist==null)
                throw new Exception("production data not found");

            if(jetDataExistWithProducton.isPresent())
                throw new Exception("Production is already exist with jet");


            //jetCapacity
            availableJetCapacity = jetMastExist.get().getCapacity();


            //existing jet capacity with batch
            List<JetData> exstingJetData = jetDataDao.findByControlId(jetMastExist.get().getId());
            for(JetData jetData:exstingJetData)
            {
                ProductionPlan productionPlan = productionPlanService.getProductionData(jetData.getProductionId());
                if (productionPlanExits!=null)
                {
                    GetBatchWithControlId batchDataQTY = stockBatchService.getBatchWithoutFinishMtrQTYById(productionPlan.getBatchId(),productionPlan.getStockId());
                //    System.out.println(batchDataQTY.getWT());
                    if(batchDataQTY.getWT()!=null)
                    availableBatchInJetCapacity+=batchDataQTY.getWT();

                }
            }



            //new batch capacity
            ProductionPlan productionPlan = productionPlanService.getProductionData(addJetData.getProductionId());
            if (productionPlan!=null)
            {
                GetBatchWithControlId batchDataQTY = stockBatchService.getBatchWithoutFinishMtrQTYById(productionPlan.getBatchId(),productionPlan.getStockId());
         //       System.out.println(batchDataQTY.getWT());
                if(batchDataQTY.getWT()!=null)
                newBatchCapacity+=batchDataQTY.getWT();

            }


            //add the capacity with new batch capacity
            newBatchCapacity+=availableBatchInJetCapacity;

            //check the capacity is fullfill the requirement
            if(newBatchCapacity > availableJetCapacity)
                throw new Exception("Batch WT is greather than Jet capacity please reduce or remove the Batch");

            //********** jet capcity check end



            Optional<JetMast> jetMastExistJetMast = jetMastDao.findById(addJetData.getControlId());


            /*

            *********************** If all the condition is return true then perform 3 task *************

             1.Shade color box deduct
             2.insert the data into the slip as per the process
             3.store the jet with production


             */

            // 1. deduct the color amt as per the shade concentration
            for(ShadeData shadeData:shadeMast.get().getShadeDataList())
            {
                colorAmtToDeduct = shadeData.getConcentration()*totalBatchWt;
                System.out.println(shadeData.getSupplierItemId()+":color amt deduct:"+colorAmtToDeduct);
                List<ColorBox> colorBoxList = colorService.getColorBoxListByItemId(shadeData.getSupplierItemId());

                for(ColorBox colorBox:colorBoxList)
                {
                    if((colorAmtToDeduct - colorBox.getQuantityLeft()) > 0 )
                    {
                        System.out.println("use Color:"+colorBox.getBoxNo());
                        colorBox.setFinished(true);
                        colorAmtToDeduct=colorAmtToDeduct-colorBox.getQuantityLeft();
                        colorBox.setQuantityLeft(0.0);
                    }
                    else
                    {
                        System.out.println("\"use Color:\""+colorBox.getBoxNo());
                        colorBox.setQuantityLeft(colorBox.getQuantityLeft() - colorAmtToDeduct);
                        break;
                    }

                }


            }


            // 2. now also enter the entire data of process into the slip table if the above condition is fulfilled as per the requirement
            DyeingProcessMast dyeingProcessMast = dyeingProcessService.getDyeingProcessById(shadeMast.get().getProcessId());
            DyeingSlipMast dyeingSlipMast = new DyeingSlipMast();
            List<DyeingSlipData> dyeingSlipDataList =new ArrayList<>();



//            //check the slip is already exist or not
//            DyeingSlipMast dyeingSlipMastExist = dyeingSlipService.getDyeingSlipByBatchStockId(productionPlanExist.getBatchId(),productionPlanExits.getStockId());
//            if(dyeingSlipMastExist!=null)
//                throw new Exception("slip already is exist for batch id:"+productionPlanExits.getBatchId());


            //set the dyeing slip master table first
            dyeingSlipMast.setJetId(jetMastExist.get().getId());
            dyeingSlipMast.setProductionId(productionPlanExist.getId());
            dyeingSlipMast.setBatchId(productionPlanExits.getBatchId());
            dyeingSlipMast.setStockId(productionPlanExist.getStockId());

            for(DyeingProcessData dyeingProcessData:dyeingProcessMast.getDyeingProcessData())
            {
                DyeingSlipData dyeingSlipData = new DyeingSlipData(dyeingProcessData);
                List<DyeingSlipItemData> slipItemLists = new ArrayList<>();
                for(DyeingChemicalData dyeingChemicalData:dyeingProcessData.getDyeingChemicalData())
                {
                    DyeingSlipItemData slipItemList =new DyeingSlipItemData();
                    slipItemList.setItemId(dyeingChemicalData.getItemId());
                    slipItemList.setItemName(dyeingChemicalData.getItemName());

                    String name = supplierService.getSupplierNameByItemId(dyeingChemicalData.getItemId());
                    slipItemList.setSupplierName(name);

                    Supplier supplier = supplierService.getSupplierByItemId(dyeingChemicalData.getItemId());
                    slipItemList.setSupplierId(supplier.getId());

                    Optional<SupplierRate> supplierRate = supplierService.getItemById(dyeingChemicalData.getItemId());
                    if(supplierRate.isPresent()) {
                        if (supplierRate.get().getItemType().equals("Color"))
                            slipItemList.setQty((dyeingChemicalData.getConcentration() * totalBatchWt) / 100);
                        else
                            slipItemList.setQty((dyeingChemicalData.getConcentration() * totalBatchWt *dyeingProcessData.getLiquerRation()) / 1000);
                    }

                    slipItemLists.add(slipItemList);
                }
                dyeingSlipData.setDyeingSlipItemData(slipItemLists);
                dyeingSlipDataList.add(dyeingSlipData);


            }
            dyeingSlipMast.setDyeingSlipDataList(dyeingSlipDataList);
            dyeingSlipService.saveDyeingSlipMast(dyeingSlipMast);


            // 3. change the status of production
            productionPlanExist.setStatus(true);
            productionPlanDao.save(productionPlanExist);


            long count=0;


            //get the count of production in existing jet
            List<JetData> existingJetData=jetDataDao.findByControlId(jetDataList.get(0).getControlId());
            if(existingJetData.isEmpty())
            {
                //save the jet data
                JetData saveJetData=new JetData(addJetData,productionPlanExist);
                saveJetData.setSequence(1l);
                jetDataDao.save(saveJetData);
            }
            else
            {
                for(JetData jetData:existingJetData)
                {
                    count++;
                }
                //save jet with production
                count=count+1;
                JetData saveJetData=new JetData(addJetData,productionPlanExist);

                saveJetData.setSequence(count);
                jetDataDao.save(saveJetData);


            }


        }




    }

    public List<GetJetData> getJetData(Long id) throws Exception{

        List<GetJetData> getJetDataList=new ArrayList<>();
        List<JetData> jetDataList = jetDataDao.findByControlIdWithExistingProduction(id);
        if(jetDataList.isEmpty())
            throw new Exception("No data found");

        for(JetData jetData:jetDataList)
        {
            getJetDataList.add(new GetJetData(jetData));
        }
        return getJetDataList;
    }

    public List<GetStatus> getJetStatusList() throws Exception{

        List<GetStatus> getStatusList=new ArrayList<>();

        for(JetStatus jetStatus:JetStatus.values())
        {
            getStatusList.add(new GetStatus(jetStatus));
        }

        if(getStatusList.isEmpty())
            throw new Exception("No status found");

        return getStatusList;


    }

    public void updateJetData(UpdateJetData jetDataToUpdate) throws Exception {

        //change the production sequnce in same jet

        if(jetDataToUpdate.getTo().getJetId().equals(jetDataToUpdate.getFrom().getJetId()))
        {
            //check the production is avaialbe or not
            ProductionPlan productionExist=productionPlanService.getProductionData(jetDataToUpdate.getFrom().getProductionId());

            if(productionExist==null)
                throw new Exception("no production data found");
            productionExist=productionPlanService.getProductionData(jetDataToUpdate.getTo().getProductionId());

            if(productionExist==null)
                throw new Exception("no production data found");

            //update the sequence of production as per the requirement
            //if the batch is able to fit into the new jet then update the data from the jet
            List<JetData> fromJetDataList = jetDataDao.findByControlIdWithExistingProduction(jetDataToUpdate.getFrom().getJetId());
            for (JetData jetData : fromJetDataList) {
                if (jetData.getSequence() > jetDataToUpdate.getFrom().getSequence()) {
                    long number = jetData.getSequence();
                    number--;
                    jetData.setSequence(number);
                    jetDataDao.save(jetData);

                }
            }

            jetDataDao.deleteByProductionId(jetDataToUpdate.getFrom().getProductionId());

            //now update the jet data from to jet list
            List<JetData> toJetDataList = jetDataDao.findByControlIdWithExistingProduction(jetDataToUpdate.getTo().getJetId());
            for (JetData jetData : toJetDataList) {
                if (jetData.getSequence() >= jetDataToUpdate.getTo().getSequence()) {
                    long number = jetData.getSequence();
                    number++;
                    jetData.setSequence(number);
                    jetDataDao.save(jetData);
                }
            }
            JetData newJetData = new JetData(jetDataToUpdate.getTo());
            jetDataDao.save(newJetData);



        }
        else {
            //change the production sequnce in between two jet


            Double newBatchCapacity = 0.0;
            Double jetExistingCapacity = 0.0;
            Double jetCapacity = 0.0;
            Optional<JetMast> jetMast = jetMastDao.findById(jetDataToUpdate.getTo().getJetId());
            if (jetMast.isEmpty())
                throw new Exception("no jet data found");

            jetCapacity = jetMast.get().getCapacity();

            List<JetData> jetDataList = jetDataDao.findByControlIdWithExistingProduction(jetMast.get().getId());

            //get the existing jet capacity
            if (!jetDataList.isEmpty()) {
                for (JetData jetData : jetDataList) {
                    ProductionPlan productionPlan = productionPlanService.getProductionData(jetData.getProductionId());
                    List<BatchData> batchDataList = batchDao.findByControlIdAndBatchId(productionPlan.getStockId(), productionPlan.getBatchId());

                    if (batchDataList.isEmpty())
                        throw new Exception("no batch or production data found");

                    for (BatchData batchData : batchDataList) {
                        if (batchData.getWt() != null)
                            jetExistingCapacity += batchData.getWt();
                    }

                }
            }

            //get the new batch capacity
            ProductionPlan productionPlan = productionPlanService.getProductionData(jetDataToUpdate.getFrom().getProductionId());
            List<BatchData> batchDataList = batchDao.findByControlIdAndBatchId(productionPlan.getStockId(), productionPlan.getBatchId());
            for (BatchData batchData : batchDataList) {
                newBatchCapacity += batchData.getWt();
            }


            //check existing batch capacity in jet with new batch capacity
            if ((newBatchCapacity + jetExistingCapacity) >= jetCapacity)
                throw new Exception("Batch WT is greather than Jet capacity please reduce or remove the Batch");


            //find production is with jet or not
            Optional<JetData> jetExistWithProduction = jetDataDao.findByControlIdAndProductionId(jetDataToUpdate.getFrom().getJetId(), jetDataToUpdate.getFrom().getProductionId());
            if (jetExistWithProduction.isEmpty())
                throw new Exception("no production data found with given jet");


            //if the batch is able to fit into the new jet then update the data from the jet
            List<JetData> fromJetDataList = jetDataDao.findByControlIdWithExistingProduction(jetDataToUpdate.getFrom().getJetId());
            for (JetData jetData : fromJetDataList) {
                if (jetData.getSequence() > jetDataToUpdate.getFrom().getSequence()) {
                    long number = jetData.getSequence();
                    number--;
                    jetData.setSequence(number);
                    jetDataDao.save(jetData);

                }
            }

            jetDataDao.deleteByProductionId(jetDataToUpdate.getFrom().getProductionId());

            //now update the jet data from to jet list
            List<JetData> toJetDataList = jetDataDao.findByControlIdWithExistingProduction(jetDataToUpdate.getTo().getJetId());
            for (JetData jetData : toJetDataList) {
                if (jetData.getSequence() >= jetDataToUpdate.getTo().getSequence()) {
                    long number = jetData.getSequence();
                    number++;
                    jetData.setSequence(number);
                    jetDataDao.save(jetData);
                }
            }
            JetData newJetData = new JetData(jetDataToUpdate.getTo());
            jetDataDao.save(newJetData);
        }

    }






    public List<GetJetData> getJetDataWithInQueueProdution(Long id) throws Exception{

        List<GetJetData> getJetDataList=new ArrayList<>();
        List<JetData> jetDataList = jetDataDao.findByControlId(id);
        if(jetDataList.isEmpty())
            throw new Exception("no data found");

        //fetch the data which are in Queue
        for(JetData jetData:jetDataList)
        {
            if(jetData.getStatus()==JetStatus.inQueue) {
                getJetDataList.add(new GetJetData(jetData));
            }
        }

        if(getJetDataList.isEmpty())
            throw new Exception("no data found");
        return getJetDataList;
    }

    public List<GetAllJetMast> getAllJetData() throws Exception{
        List<JetMast> jetMastList = jetMastDao.getAll();

        List<GetAllJetMast> getAllJetMast=new ArrayList<>();

        if(jetMastList.isEmpty())
            throw new Exception("not data found");

        int i=0;
        for(JetMast jetMast:jetMastList)
        {
            List<GetJetData> jetDataList = new ArrayList<>();
            List<JetData> existJetDataList =  jetDataDao.findByControlId(jetMast.getId());

            for(JetData jetData:existJetDataList)
            {
                if(jetData.getStatus()==JetStatus.inQueue)
                {
                    GetJetData getJetData=new GetJetData(jetData);
                    ProductionPlan productionPlan = productionPlanService.getProductionData(jetData.getProductionId());
                    getJetData.setBatchId(productionPlan.getBatchId());
                    jetDataList.add(getJetData);
                }

            }

            GetAllJetMast allJetMast = new GetAllJetMast();
            allJetMast.setId(jetMast.getId());
            allJetMast.setCapacity(jetMast.getCapacity());
            allJetMast.setName(jetMast.getName());

            if(!jetDataList.isEmpty())
                allJetMast.setJetDataList(jetDataList);

            getAllJetMast.add(allJetMast);
        }

        return getAllJetMast;

    }

    public void updateProductionStatus(ChangeStatus jetDataToUpdate) throws Exception{
        Optional<JetData> jetDataExist= jetDataDao.findByControlIdAndProductionId(jetDataToUpdate.getControlId(),jetDataToUpdate.getProdcutionId());

        if(jetDataExist.isEmpty())
            throw new Exception("No data found");

        if(jetDataExist.get().getStatus() == JetStatus.success)
            throw new Exception("data can't update because production is already completed");

        List<String> getStatusList=new ArrayList<>();

        for(JetStatus jetStatus:JetStatus.values())
        {
            getStatusList.add(jetStatus.toString());
        }

        if(!getStatusList.contains(jetDataToUpdate.getStatus()))
            throw new Exception("no status found");

        //if the status is success then update the sequence of respected jet
        if(jetDataToUpdate.getStatus().equals("success"))
        {
            //check the sequence of production is first or not


            if(jetDataExist.get().getSequence()!=1)
                throw new Exception("unable to update the production");

            //System.out.println(jetDataExist.get().getStatus());
            List<JetData> jetDataList = jetDataDao.findByControlId(jetDataToUpdate.getControlId());
            for(JetData jetData:jetDataList)
            {
                if(jetData.getProductionId()==jetDataExist.get().getProductionId())
                {
                    //update the status of production
                    jetDataExist.get().setStatus(JetStatus.success);
                    jetDataDao.save(jetDataExist.get());

                }
                else
                {
                    //update the sequence of production by +1 from the jet
                    long number=jetData.getSequence();
                    number--;
                    jetData.setSequence(number);
                    jetDataDao.save(jetData);
                }
            }

        }
        else
        {
            //update the any other status except success update that on
            if(!getStatusList.contains(jetDataToUpdate.getStatus()))
                throw new Exception("Wrong status found");

            jetDataExist.get().setStatus(JetStatus.valueOf(jetDataToUpdate.getStatus()));
            //System.out.println(jetDataExist.get().getStatus());
            jetDataDao.save(jetDataExist.get());
        }


    }

    public Boolean deleteJetDataByProductionId(Long id) throws Exception {
        JetData jetData=jetDataDao.findByProductionId(id);
        if(jetData==null) {
            throw new Exception("production data not found");
        }

        //update the existing production from jet because the production is going to be deleted
        List<JetData> jetDataList=jetDataDao.findByControlIdWithExistingProduction(jetData.getControlId());
        for(JetData jetData1:jetDataList)
        {
            if(jetData1.getSequence()>jetData.getSequence())
            {
                long number=jetData1.getSequence();
                number--;
                jetData1.setSequence(number);
                jetDataDao.save(jetData1);
            }
        }

        //update the status from production table
        ProductionPlan productionPlan=productionPlanService.getProductionData(id);
        productionPlan.setStatus(false);
        productionPlanService.saveProductionPlan(productionPlan);


        jetDataDao.deleteByProductionId(id);
        return true;




    }

    public GetJetSlip getJetSlipData(Long jetId, Long productionId) throws Exception {
        GetJetSlip data = new GetJetSlip();

        ProductionPlan productionPlanExist = productionPlanService.getProductionData(productionId);
        Optional<JetMast> jetMastExist = jetMastDao.getJetById(jetId);

        if(jetMastExist.isEmpty())
            throw new Exception("no jet found");

        Optional<JetData> jetExistWithProduction = jetDataDao.findByControlIdAndProductionId(jetId,productionId);

        if(jetExistWithProduction.isEmpty())
            throw new Exception("no production found with jet");

        Double wt = batchDao.getTotalWtByControlIdAndBatchId(productionPlanExist.getStockId(),productionPlanExist.getBatchId());

        //get the party and quality and shade data
        StockMast stockMast = stockBatchService.getStockById(productionPlanExist.getStockId());
        GetQualityResponse quality =qualityServiceImp.getQualityByID(stockMast.getQualityId());
        Optional<ShadeMast> shadeMast = shadeService.getShadeMastById(productionPlanExist.getShadeId());
        if (shadeMast.isEmpty())
            throw new Exception("no shade data found ");

        //basic data
        data.setColorTone(shadeMast.get().getColorTone());
        data.setPartyShadeNo(shadeMast.get().getPartyShadeNo());
        data.setQualityId(quality.getQualityId());
        data.setQualityEntryId(stockMast.getQualityId());
        data.setBatchWt(wt);
        data.setStockId(productionPlanExist.getStockId());
        data.setBatchId(productionPlanExist.getBatchId());
        data.setJetId(jetId);
        data.setProductionId(productionId);

        //process with item data
        List<GetJetSlipData> getJetSlipData =new ArrayList<>();


        DyeingProcessMast dyeingProcessMast = dyeingProcessService.getDyeingProcessById(shadeMast.get().getProcessId());

        for(DyeingProcessData dyeingProcessData : dyeingProcessMast.getDyeingProcessData())
        {
            List<SlipItemList> itemLists = new ArrayList<>();
            for(DyeingChemicalData dyeingChemicalData:dyeingProcessData.getDyeingChemicalData())
            {
                SlipItemList item =new SlipItemList();
                item.setItemName(dyeingChemicalData.getItemName());
                String supplier = supplierService.getSupplierNameByItemId(dyeingChemicalData.getItemId());
                item.setSupplierName(supplier);
                //count the qty by liquerration * processConcentration * batchWt

                //get the jet mast data

                if(jetMastExist.get().getLiquorRatio()==null || jetMastExist.get().getLiquorRatio().equals(""))
                    throw new Exception("jet liquer ration can't be null");

                Double qty = (jetMastExist.get().getLiquorRatio() * dyeingChemicalData.getConcentration() * wt) /1000;
                item.setQty(qty);
                itemLists.add(item);

            }

            //add to the list
            getJetSlipData.add(new GetJetSlipData(dyeingProcessData,itemLists));

        }
        if(getJetSlipData.isEmpty())
            throw new Exception("no process data found ");

        data.setSlipDataList(getJetSlipData);


        return data;

    }


    public Boolean deleteJetMastByJetId(Long id) {
        try {
            Optional<JetMast> jetMast  =jetMastDao.getJetById(id);
            if(jetMast.isEmpty())
                throw new Exception("no data found");
            jetMastDao.deleteByJetId(id);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public JetData getJetDataByProductionId(Long productionId) {

        JetData jetData = jetDataDao.findByProductionId(productionId);
        return jetData;

    }

    public List<AddJet> getAllJet() throws Exception {

        List<AddJet> list = new ArrayList<>();
        List<JetMast> jetMastList=jetMastDao.getAll();

        if(jetMastList.isEmpty())
            throw new Exception("no data found");

        for(JetMast jetMast:jetMastList)
        {
            list.add(new AddJet(jetMast));
        }
        return list;
    }

    public JetData getJetDataByProductionIdWithoutFilter(Long id) {

        JetData jetData = jetDataDao.getJetDataByProductionId(id);
        return jetData;
    }

    public void removeProductionFromJet(Long jetId, Long productionId) throws Exception {

        //check first the prodution is already in jet or not
        JetData jetDataExist = jetDataDao.jetDataExistWithJetIdAndProductionId(jetId,productionId);
        if(jetDataExist==null)
            throw new Exception("jet data with production not found");

        ProductionPlan productionPlan = productionPlanService.getProductionData(productionId);

            /*

            *********************** Task to perform while removing the production from jet *************

             1.take back the amount of color which is used for dyeing batch
             2.remove the data of dyeing slip as well
             3.change the status of production
             4.change the production sequence of jet data & remove the entry from jet data table

             */


        // 1. take back the amount of color which is issue for the batch and change the status of that boxes

        Double totalBatchWt = stockBatchService.getWtByControlAndBatchId(productionPlan.getStockId(),productionPlan.getBatchId());
        Double colorAmtUsed=0.0;
        Optional<ShadeMast> shadeMast = shadeService.getShadeMastById(productionPlan.getShadeId());

        for(ShadeData shadeData:shadeMast.get().getShadeDataList())
        {
            colorAmtUsed = shadeData.getConcentration()*totalBatchWt;//amt of color for the item
            System.out.println("used:"+colorAmtUsed);

            Long boxId = colorDataDao.getLatestIssuedColorBoxByColorDataId(shadeData.getSupplierItemId());
            ColorBox colorBox=colorService.getColorBoxById(boxId);
            colorBox.setFinished(false);
            colorBox.setQuantityLeft(colorBox.getQuantityLeft()+colorAmtUsed);

        }


        // 2.remove the dyeing slip from the dyeign slip based on jet and produciton id
        DyeingSlipMast dyeingSlipMast = dyeingSlipService.getDyeingSlipByProductionId(productionPlan.getId());

        dyeingSlipService.deleteDyeingSlipDataByMastId(dyeingSlipMast.getId());

        //3.change the status of production
        productionPlan.setStatus(false);

        //4. remove the production from jet data table and change the sequesce of jet data

        //change the remain production sequence
        List<JetData> jetDataList = jetDataDao.findByControlId(jetId);
        for(JetData jetData : jetDataList)
        {
            if(jetData.getSequence() > jetDataExist.getSequence())
                jetData.setSequence(jetData.getSequence()-1);

        }
        //remover the jet record
        jetDataDao.deleteJetDataById(jetDataExist.getId());

    }
}
