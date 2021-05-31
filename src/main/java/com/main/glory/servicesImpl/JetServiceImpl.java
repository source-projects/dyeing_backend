package com.main.glory.servicesImpl;

import com.main.glory.Dao.Jet.JetDataDao;
import com.main.glory.Dao.Jet.JetMastDao;
import com.main.glory.Dao.QuantityRangeDao;
import com.main.glory.Dao.StockAndBatch.BatchDao;
import com.main.glory.Dao.color.ColorDataDao;
import com.main.glory.Dao.hmi.HMIMastDao;
import com.main.glory.Dao.productionPlan.ProductionPlanDao;
import com.main.glory.Dao.quality.QualityNameDao;
import com.main.glory.Dao.qualityProcess.ChemicalDao;
import com.main.glory.model.ConstantFile;
import com.main.glory.model.StockDataBatchData.BatchData;
import com.main.glory.model.StockDataBatchData.StockMast;
import com.main.glory.model.color.ColorBox;
import com.main.glory.model.dyeingProcess.DyeingChemicalData;
import com.main.glory.model.dyeingProcess.DyeingProcessData;
import com.main.glory.model.dyeingProcess.DyeingProcessMast;
import com.main.glory.model.dyeingSlip.DyeingSlipData;
import com.main.glory.model.dyeingSlip.DyeingSlipItemData;
import com.main.glory.model.dyeingSlip.DyeingSlipMast;
import com.main.glory.model.hmi.HMIMast;
import com.main.glory.model.jet.JetStatus;
import com.main.glory.model.jet.request.*;
import com.main.glory.model.jet.JetData;
import com.main.glory.model.jet.JetMast;
import com.main.glory.model.jet.responce.*;
import com.main.glory.model.productionPlan.ProductionPlan;
import com.main.glory.model.productionPlan.request.GetAllProductionWithShadeData;
import com.main.glory.model.qty.QuantityRange;
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
    HMIMastDao hmiMastDao;

    @Autowired
    PartyServiceImp partyServiceImp;
    @Autowired
    QuantityRangeDao quantityRangeDao;
    @Autowired
    ProductionPlanDao productionPlanDao;
    @Autowired
    DyeingSlipServiceImpl dyeingSlipService;
    @Autowired
    QualityServiceImp qualityServiceImp;

    @Autowired
    QualityNameDao qualityNameDao;

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

    ConstantFile constantFile;

    public void saveJet(AddJet jetMast) throws Exception {


        Optional<JetMast> jetExist = jetMastDao.findByName(jetMast.getName());
        if (jetExist.isPresent()) {
            throw new Exception(constantFile.Jet_Exist_With_Name);
        }
        JetMast newJet = new JetMast(jetMast);
        jetMastDao.save(newJet);

    }


    public void saveJetData(List<AddJetData> jetDataList) throws Exception {

        //get the dyeing process color box list
        Long productionId = jetDataList.get(0).getProductionId();

        //check the production is available or not


        ProductionPlan productionPlanExits = productionPlanService.getProductionData(productionId);
        if (productionPlanExits.getStatus())
            throw new Exception(ConstantFile.Production_With_Jet);

        Optional<ShadeMast> shadeMast = shadeService.getShadeMastById(productionPlanExits.getShadeId());


        if (shadeMast.isEmpty()) {
            throw new Exception("no shade found with id:" + productionPlanExits.getShadeId());
        }


        //dyeing process
        DyeingProcessMast dyeingProcessMast = dyeingProcessService.getDyeingProcessById(shadeMast.get().getProcessId());


        //count the color/chemical amt to deduct
        Double colorAmtToDeduct = 0.0;
        //get the batch is merge batch id or batch id merge batcg id
        BatchData isMergeBatch = batchDao.getIsMergeBatchId(productionPlanExits.getBatchId());
        BatchData isBatchId = batchDao.getIsBatchId(productionPlanExits.getBatchId());
        Double totalBatchWt;
        if (productionPlanExits.getIsMergeBatchId() == false) {
            //get total wt of batch id
            totalBatchWt = batchDao.getTotalWtByBatchId(productionPlanExits.getBatchId());
        } else {
            //get total wt of merge batch uid
            totalBatchWt = batchDao.getTotalWtByMergeBatchId(productionPlanExits.getBatchId());
        }

/*
        //check the capacity first for the color box issue had that much capacity to fill the batch or not
        for(ShadeData shadeData:shadeMast.get().getShadeDataList())
        {
            String suppplierName = supplierService.getSupplierNameByItemId(shadeData.getSupplierItemId());
            SupplierRate supplierRate=supplierService.getSupplierRateByItemId(shadeData.getSupplierItemId());
            Double data=0.0;
            if(supplierRate.getItemType().equals("Color"))
            colorAmtToDeduct = (shadeData.getConcentration()*totalBatchWt)/100;

            List<ColorBox> colorBoxList = colorService.getColorBoxListByItemId(shadeData.getSupplierItemId());

            if(colorBoxList.isEmpty())
                throw new Exception("no color box is available for batch, supplier:"+suppplierName+", item:"+supplierRate.getItemName());

            for(ColorBox c:colorBoxList)
            {
                data += c.getQuantityLeft();

            }
            if(colorAmtToDeduct > data)
                throw new Exception("issue the box first because required color amt:"+colorAmtToDeduct+" and available is:"+data+" for batch, item:"+shadeData.getItemName());


        }*/



/*
        //List<DyeingProcessData> dyeingProcessDataList = dyeingProcessService.dyeingProcessDataWithShadeTypeAndCategory(dyeingProcessMast.getId(),shadeMast.get().getCategory());
        //check the capacity first for the chemical box issue had that much capacity to fill the batch or not
        for(DyeingProcessData dyeingProcessData:dyeingProcessMast.getDyeingProcessData())
        {
            for(DyeingChemicalData dyeingChemicalData:dyeingProcessData.getDyeingChemicalData())
            {
                //get the Only dyeing chemical item based on shade type
                    if (dyeingChemicalData.getShadeType().equals(shadeMast.get().getCategory()) || dyeingChemicalData.getShadeType().equals("DEFAULT")) {
                    String suppplierName = supplierService.getSupplierNameByItemId(dyeingChemicalData.getItemId());
                    SupplierRate supplierRate = supplierService.getSupplierRateByItemId(dyeingChemicalData.getItemId());
                    Double data = 0.0;
                    if (supplierRate.getItemType().equals("Chemical"))
                        colorAmtToDeduct = (dyeingChemicalData.getConcentration() * totalBatchWt * dyeingProcessData.getLiquerRation()) / 1000;

                    List<ColorBox> colorBoxList = colorService.getColorBoxListByItemId(dyeingChemicalData.getItemId());

                    if (colorBoxList.isEmpty())
                        throw new Exception("no chemical box is available for batch, supplier:" + suppplierName + ", item:" + supplierRate.getItemName());

                    for (ColorBox c : colorBoxList) {
                        data += c.getQuantityLeft();

                    }
                    if (colorAmtToDeduct > data)
                        throw new Exception("issue the box first because required chemical amt:" + colorAmtToDeduct + " and available is:" + data + " for batch, item:" + dyeingChemicalData.getItemId());

                }
            }


        }


 */

        //*********  Color item checks end


        //save to jet data first check the capacity
        Double availableJetCapacity = 0.0;

        Double availableBatchInJetCapacity = 0.0;
        Double newBatchCapacity = 0.0;

        //first check the capacity and production detail is available or not
        for (AddJetData addJetData : jetDataList) {
            if (addJetData.getControlId() == null)
                throw new Exception(ConstantFile.Null_Record_Passed);

            if (addJetData.getProductionId() == null)
                throw new Exception(ConstantFile.Null_Record_Passed);

            Optional<JetMast> jetMastExist = jetMastDao.findById(addJetData.getControlId());

            ProductionPlan productionPlanExist = productionPlanService.getProductionData(addJetData.getProductionId());

            Optional<JetData> jetDataExistWithProducton = jetDataDao.findByControlIdAndProductionId(addJetData.getControlId(), addJetData.getProductionId());

            if (jetMastExist.isEmpty())
                throw new Exception("Jet Mast is not found");

            if (productionPlanExist == null)
                throw new Exception("production data not found");

            if (jetDataExistWithProducton.isPresent())
                throw new Exception(ConstantFile.Production_With_Jet);


            //jetCapacity
            availableJetCapacity = jetMastExist.get().getCapacity();


            //existing jet capacity with batch
            List<JetData> exstingJetData = jetDataDao.findByControlId(jetMastExist.get().getId());
            for (JetData jetData : exstingJetData) {
                ProductionPlan productionPlan = productionPlanService.getProductionDataById(jetData.getProductionId());
                if (productionPlanExits != null) {

                    Double batchDataQTY = 0.0;
                    if (productionPlan.getIsMergeBatchId() == false)
                        batchDataQTY = batchDao.getBatchWithoutFinishMtrQTYById(productionPlan.getBatchId());
                    else
                        batchDataQTY = batchDao.getBatchWithoutFinishMtrQTYByMergeId(productionPlan.getBatchId());
                    //    System.out.println(batchDataQTY.getWT());
                    if (batchDataQTY != 0.0)
                        availableBatchInJetCapacity += batchDataQTY;

                }
            }


            //new batch capacity
            ProductionPlan productionPlan = productionPlanService.getProductionData(addJetData.getProductionId());
            if (productionPlan != null) {
                if (productionPlan.getIsMergeBatchId() == false) {
                    Double batchDataQTY = batchDao.getBatchWithoutFinishMtrQTYById(productionPlan.getBatchId());
                    //       System.out.println(batchDataQTY.getWT());
                    if (batchDataQTY != null)
                        newBatchCapacity += batchDataQTY;
                } else {
                    Double batchDataQTY = batchDao.getBatchWithoutFinishMtrQTYByMergeId(productionPlan.getBatchId());
                    //       System.out.println(batchDataQTY.getWT());
                    if (batchDataQTY != null)
                        newBatchCapacity += batchDataQTY;
                }

            }


            //add the capacity with new batch capacity
            newBatchCapacity += availableBatchInJetCapacity;

            //check the capacity is fullfill the requirement
            if (newBatchCapacity > availableJetCapacity)
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
           /* for(ShadeData shadeData:shadeMast.get().getShadeDataList())
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


            }*/


           /*
            //deduct the chemical amt from the box as per the dyeing process
            for(DyeingProcessData dyeingProcessData:dyeingProcessMast.getDyeingProcessData())
            {
                for(DyeingChemicalData dyeingChemicalData:dyeingProcessData.getDyeingChemicalData()) {
                    //get the dyeing process item based on shade type

                    if (dyeingChemicalData.getShadeType().equals(shadeMast.get().getCategory()) || dyeingChemicalData.getShadeType().equals("DEFAULT")) {
                        colorAmtToDeduct = (dyeingProcessData.getLiquerRation() * totalBatchWt * dyeingChemicalData.getConcentration()) / 1000;
                        System.out.println(dyeingChemicalData.getItemId() + ":chemical amt deduct:" + colorAmtToDeduct);
                        List<ColorBox> colorBoxList = colorService.getColorBoxListByItemId(dyeingChemicalData.getItemId());

                        for (ColorBox colorBox : colorBoxList) {
                            if ((colorAmtToDeduct - colorBox.getQuantityLeft()) > 0) {
                                System.out.println("use chemical:" + colorBox.getBoxNo());
                                colorBox.setFinished(true);
                                colorAmtToDeduct = colorAmtToDeduct - colorBox.getQuantityLeft();
                                colorBox.setQuantityLeft(0.0);
                            } else {
                                System.out.println("\"use chemical:\"" + colorBox.getBoxNo());
                                colorBox.setQuantityLeft(colorBox.getQuantityLeft() - colorAmtToDeduct);
                                break;
                            }

                        }
                    }
                }



            }

            */

            // 2. now also enter the entire data of process into the slip table if the above condition is fulfilled as per the requirement

            DyeingSlipMast dyeingSlipMast = new DyeingSlipMast();
            List<DyeingSlipData> dyeingSlipDataList = new ArrayList<>();


//            //check the slip is already exist or not
//            DyeingSlipMast dyeingSlipMastExist = dyeingSlipService.getDyeingSlipByBatchStockId(productionPlanExist.getBatchId(),productionPlanExits.getStockId());
//            if(dyeingSlipMastExist!=null)
//                throw new Exception("slip already is exist for batch id:"+productionPlanExits.getBatchId());


            //set the dyeing slip master table first
            dyeingSlipMast.setJetId(jetMastExist.get().getId());
            dyeingSlipMast.setProductionId(productionPlanExist.getId());
            dyeingSlipMast.setBatchId(productionPlanExits.getBatchId());
            //dyeingSlipMast.setStockId(productionPlanExist.getStockId());

            for (DyeingProcessData dyeingProcessData : dyeingProcessMast.getDyeingProcessData()) {
                DyeingSlipData dyeingSlipData = new DyeingSlipData(dyeingProcessData);
                List<DyeingSlipItemData> slipItemLists = new ArrayList<>();
                for (DyeingChemicalData dyeingChemicalData : dyeingProcessData.getDyeingChemicalData()) {

                    if (dyeingChemicalData.getShadeType().equals(shadeMast.get().getCategory()) || dyeingChemicalData.getShadeType().equals("DEFAULT")) {
                        DyeingSlipItemData slipItemList = new DyeingSlipItemData();
                        slipItemList.setItemId(dyeingChemicalData.getItemId());
                        SupplierRate supplierRateItem = supplierService.getSupplierRateByItemId(dyeingChemicalData.getItemId());
                        slipItemList.setItemName(supplierRateItem.getItemName());

                        String name = supplierService.getSupplierNameByItemId(dyeingChemicalData.getItemId());
                        slipItemList.setSupplierName(name);

                        Supplier supplier = supplierService.getSupplierByItemId(dyeingChemicalData.getItemId());
                        slipItemList.setSupplierId(supplier.getId());

                        Optional<SupplierRate> supplierRate = supplierService.getItemById(dyeingChemicalData.getItemId());

                        //for item type is color or not
                        if (supplierRate.get().getItemType().equals("Color"))
                            slipItemList.setIsColor(true);
                        else
                            slipItemList.setIsColor(false);


                        Double amtQty = 0.0;
                        if (supplierRate.isPresent()) {
                            if (supplierRate.get().getItemType().equals("Color"))
                                amtQty = (dyeingChemicalData.getConcentration() * totalBatchWt) / 100;
                            else {

                                if (dyeingChemicalData.getByChemical().equals("L")) {
                                    amtQty = (dyeingChemicalData.getConcentration() * totalBatchWt * dyeingProcessData.getLiquerRation()) / 1000;
                                    //function call to check in the range
                                    amtQty = getAmountInRange(amtQty);
                                } else if (dyeingChemicalData.getByChemical().equals("W")) {
                                    amtQty = (dyeingChemicalData.getConcentration() * totalBatchWt) / 100;
                                    //function call to check in the range
                                    amtQty = getAmountInRange(amtQty);
                                } else if (dyeingChemicalData.getByChemical().equals("F")) {
                                    amtQty = dyeingChemicalData.getConcentration();
                                    //function call to check in the range
                                    amtQty = getAmountInRange(amtQty);
                                }
                            }
                        }


                        slipItemList.setQty(amtQty);
                        slipItemLists.add(slipItemList);
                    }
                }
                dyeingSlipData.setDyeingSlipItemData(slipItemLists);
                dyeingSlipDataList.add(dyeingSlipData);


            }
            dyeingSlipMast.setDyeingSlipDataList(dyeingSlipDataList);

            DyeingSlipMast x = dyeingSlipService.saveDyeingSlipMastFromProcess(dyeingSlipMast);

            //****** also add the shade data with the slip of dyeing process of dyeing function

            //get the dyeing function from the dyeing process of batch
            DyeingSlipData getDyeingProcess = dyeingSlipService.getDyeingProcessDataOnlyBySlipMast(x.getId());

            //if it is exisiting then add shade data with that dyeing process
            List<DyeingSlipItemData> dyeingSlipItemDataList = new ArrayList<>();
            if (getDyeingProcess != null) {
                //add the privious one item in dyeing list

                for (DyeingSlipItemData dyeingSlipItemData : getDyeingProcess.getDyeingSlipItemData()) {
                    dyeingSlipItemDataList.add(dyeingSlipItemData);
                }

                for (ShadeData shadeData : shadeMast.get().getShadeDataList()) {
                    Supplier supplier = supplierService.getSupplierByItemId(shadeData.getSupplierItemId());
                    SupplierRate supplierRate = supplierService.getSupplierRateByItemId(shadeData.getSupplierItemId());

                    DyeingSlipItemData dyeingSlipItemData = new DyeingSlipItemData(shadeData, supplierRate, supplier, totalBatchWt);

                    dyeingSlipItemDataList.add(dyeingSlipItemData);
                }
            } else {
                //if not exist then create slip for dyeing with shade data only
                getDyeingProcess = new DyeingSlipData();
                getDyeingProcess.setControlId(x.getId());
                getDyeingProcess.setProcessType("Dyeing");
                for (ShadeData shadeData : shadeMast.get().getShadeDataList()) {
                    Supplier supplier = supplierService.getSupplierByItemId(shadeData.getSupplierItemId());
                    SupplierRate supplierRate = supplierService.getSupplierRateByItemId(shadeData.getSupplierItemId());

                    DyeingSlipItemData dyeingSlipItemData = new DyeingSlipItemData(shadeData, supplierRate, supplier, totalBatchWt);

                    dyeingSlipItemDataList.add(dyeingSlipItemData);
                }

            }
            getDyeingProcess.setDyeingSlipItemData(dyeingSlipItemDataList);
            dyeingSlipService.saveDyeingSlipDataOnly(getDyeingProcess);

            //if not then create on function and add the shade data with function by the given formula


            // 3. change the status of production
            productionPlanExist.setStatus(true);
            productionPlanDao.save(productionPlanExist);


            long count = 0;


            //get the count of production in existing jet
            List<JetData> existingJetData = jetDataDao.findByControlId(jetDataList.get(0).getControlId());
            if (existingJetData.isEmpty()) {
                //save the jet data
                JetData saveJetData = new JetData(addJetData, productionPlanExist);
                saveJetData.setSequence(1l);
                jetDataDao.save(saveJetData);
            } else {
                for (JetData jetData : existingJetData) {
                    count++;
                }
                //save jet with production
                count = count + 1;
                JetData saveJetData = new JetData(addJetData, productionPlanExist);

                saveJetData.setSequence(count);
                jetDataDao.save(saveJetData);


            }


        }


    }

    Double getAmountInRange(Double amtQty) {

        List<QuantityRange> quantityRangeList = quantityRangeDao.getAllRange();
        Double valueToReturn = 0.0;

        int i = 0;
        for (QuantityRange q : quantityRangeList) {
            if (q.getValue() > amtQty) {

                if (i > 0 && i < quantityRangeList.size()) {

                    valueToReturn = (q.getValue() + quantityRangeList.get(i - 1).getValue()) / 2;// i'th contain last index
                    if (amtQty < valueToReturn) {
                        valueToReturn = quantityRangeList.get(i - 1).getValue();
                    } else {
                        valueToReturn = q.getValue();
                    }
                } else {
                    if (i == 0)
                        valueToReturn = quantityRangeList.get(0).getValue();
                    else
                        valueToReturn = quantityRangeList.get(quantityRangeList.size() - 1).getValue();
                }

                break;
            }
            i++;

        }
        return valueToReturn;

    }

    public List<GetJetData> getJetData(Long id) throws Exception {

        List<GetJetData> getJetDataList = new ArrayList<>();
        List<JetData> jetDataList = jetDataDao.findByControlIdWithExistingProduction(id);
        if (jetDataList.isEmpty())
            throw new Exception(ConstantFile.Jet_Not_Found);

        for (JetData jetData : jetDataList) {
            getJetDataList.add(new GetJetData(jetData));
        }
        return getJetDataList;
    }

    public List<GetJetData> getJetRecordData(Long id) throws Exception {

        List<GetJetData> getJetDataList = new ArrayList<>();
        List<JetData> jetDataList = jetDataDao.findByControlIdWithExistingProduction(id);

        for (JetData jetData : jetDataList) {
            getJetDataList.add(new GetJetData(jetData));
        }
        return getJetDataList;
    }

    public List<GetStatus> getJetStatusList() throws Exception {

        List<GetStatus> getStatusList = new ArrayList<>();

        for (JetStatus jetStatus : JetStatus.values()) {
            getStatusList.add(new GetStatus(jetStatus));
        }

        if (getStatusList.isEmpty())
            throw new Exception("No status found");

        return getStatusList;


    }

    public void updateJetData(UpdateJetData jetDataToUpdate) throws Exception {

        //change the production sequnce in same jet

        if (jetDataToUpdate.getTo().getJetId().equals(jetDataToUpdate.getFrom().getJetId())) {
            //check the production is avaialbe or not
            ProductionPlan productionExist = productionPlanService.getProductionData(jetDataToUpdate.getFrom().getProductionId());

            if (productionExist == null)
                throw new Exception(ConstantFile.Production_Not_Found);
            productionExist = productionPlanService.getProductionData(jetDataToUpdate.getTo().getProductionId());

            if (productionExist == null)
                throw new Exception(ConstantFile.Production_Not_Found);

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


        } else {
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
                    List<BatchData> batchDataList = batchDao.getBatchByBatchId(productionPlan.getBatchId());

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
            List<BatchData> batchDataList = batchDao.getBatchByBatchId(productionPlan.getBatchId());
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


    public List<GetJetData> getJetDataWithInQueueProdution(Long id) throws Exception {

        List<GetJetData> getJetDataList = new ArrayList<>();
        List<JetData> jetDataList = jetDataDao.findByControlId(id);
        if (jetDataList.isEmpty())
            throw new Exception(constantFile.Jet_Not_Found);

        //fetch the data which are in Queue
        for (JetData jetData : jetDataList) {
            if (jetData.getStatus() == JetStatus.inQueue) {
                getJetDataList.add(new GetJetData(jetData));
            }
        }

        /*if(getJetDataList.isEmpty())
            throw new Exception(commonMessage.Jet_Not_Found);*/
        return getJetDataList;
    }

    public List<GetAllJetMast> getAllJetData() throws Exception {
        List<JetMast> jetMastList = jetMastDao.getAll();

        List<GetAllJetMast> getAllJetMast = new ArrayList<>();

        /*if(jetMastList.isEmpty())
            return  jetMastList;*/

        int i = 0;
        for (JetMast jetMast : jetMastList) {
            List<GetJetData> jetDataList = new ArrayList<>();
            List<JetData> existJetDataList = jetDataDao.findByControlId(jetMast.getId());

            for (JetData jetData : existJetDataList) {
                if (jetData.getStatus() == JetStatus.inQueue) {
                    ProductionPlan productionPlan = productionPlanService.getProductionDataById(jetData.getProductionId());
                    if (productionPlan == null)
                        continue;
                    GetAllProductionWithShadeData data = productionPlanService.getProductionWithColorToneByBatchId(productionPlan.getBatchId());
                    GetJetData getJetData = null;
                    if (productionPlan.getIsDirect() == false) {
                        ShadeMast colorTone = shadeService.getColorToneByProductionId(jetData.getProductionId());
                        if (colorTone != null) {
                            DyeingProcessMast dyeingProcessMast = dyeingProcessService.getDyeingProcessById(colorTone.getProcessId());
                            getJetData = new GetJetData(data, jetData, colorTone, dyeingProcessMast);
                            getJetData.setBatchId(productionPlan.getBatchId());
                            jetDataList.add(getJetData);
                        }
                    } else {
                        getJetData = new GetJetData(jetData, data);
                        getJetData.setBatchId(productionPlan.getBatchId());
                        getJetData.setProcessName("directDyeing");
                        jetDataList.add(getJetData);
                    }



                    /*//getJetData.setBatchId(productionPlan.getBatchId());
                    if(getJetData.getPartyId()!=null)*/

                }

            }

            GetAllJetMast allJetMast = new GetAllJetMast();
            allJetMast.setId(jetMast.getId());
            allJetMast.setCapacity(jetMast.getCapacity());
            allJetMast.setName(jetMast.getName());

            if (!jetDataList.isEmpty())
                allJetMast.setJetDataList(jetDataList);

            getAllJetMast.add(allJetMast);
        }

        return getAllJetMast;

    }

    public void updateProductionStatus(ChangeStatus jetDataToUpdate, String id) throws Exception {

        /*
         * before change the status of batch to complete from the jet
         * make sure the user had permission and belong to the batch
         * then batch can be updated
         *
         ****** optional****
         */

        Optional<JetData> jetDataExist = jetDataDao.findByControlIdAndProductionId(jetDataToUpdate.getControlId(), jetDataToUpdate.getProdcutionId());

        if (jetDataExist.isEmpty())
            throw new Exception("No data found");

        if (jetDataExist.get().getStatus() == JetStatus.success)
            throw new Exception("data can't update because production is already completed");

        List<String> getStatusList = new ArrayList<>();

        for (JetStatus jetStatus : JetStatus.values()) {
            getStatusList.add(jetStatus.toString());
        }

        if (!getStatusList.contains(jetDataToUpdate.getStatus()))
            throw new Exception("no status found");

        //if the status is success then update the sequence of respected jet
        if (jetDataToUpdate.getStatus().equals("success")) {
            //check the sequence of production is first or not


          /*  if(jetDataExist.get().getSequence()!=1)
                throw new Exception("unable to update the production");*/

            //System.out.println(jetDataExist.get().getStatus());
            List<JetData> jetDataList = jetDataDao.findByControlId(jetDataToUpdate.getControlId());
            for (JetData jetData : jetDataList) {
                if (jetData.getProductionId() == jetDataExist.get().getProductionId()) {
                    //update the status of production
                    jetDataExist.get().setStatus(JetStatus.success);
                    jetDataDao.save(jetDataExist.get());

                } else {
                    //update the sequence of production by +1 from the jet
                    long number = jetData.getSequence();
                    number--;
                    jetData.setSequence(number);
                    jetDataDao.save(jetData);
                }
            }

        } else {
            //update the any other status except success update that on
            if (!getStatusList.contains(jetDataToUpdate.getStatus()))
                throw new Exception("Wrong status found");

            jetDataExist.get().setStatus(JetStatus.valueOf(jetDataToUpdate.getStatus()));
            //System.out.println(jetDataExist.get().getStatus());
            jetDataDao.save(jetDataExist.get());
        }


    }

    public Boolean deleteJetDataByProductionId(Long id) throws Exception {
        JetData jetData = jetDataDao.findByProductionId(id);
        if (jetData == null) {
            throw new Exception("production data not found");
        }

        //update the existing production from jet because the production is going to be deleted
        List<JetData> jetDataList = jetDataDao.findByControlIdWithExistingProduction(jetData.getControlId());
        for (JetData jetData1 : jetDataList) {
            if (jetData1.getSequence() > jetData.getSequence()) {
                long number = jetData1.getSequence();
                number--;
                jetData1.setSequence(number);
                jetDataDao.save(jetData1);
            }
        }

        //update the status from production table
        ProductionPlan productionPlan = productionPlanService.getProductionData(id);
        productionPlan.setStatus(false);
        productionPlanService.saveProductionPlan(productionPlan);


        jetDataDao.deleteByProductionId(id);
        return true;


    }

    public GetJetSlip getJetSlipData(Long jetId, Long productionId) throws Exception {
        GetJetSlip data = new GetJetSlip();

        ProductionPlan productionPlanExist = productionPlanService.getProductionData(productionId);
        Optional<JetMast> jetMastExist = jetMastDao.getJetById(jetId);

        if (jetMastExist.isEmpty())
            throw new Exception(ConstantFile.Jet_Not_Found);

        Optional<JetData> jetExistWithProduction = jetDataDao.findByControlIdAndProductionId(jetId, productionId);

        if (jetExistWithProduction.isEmpty())
            throw new Exception(ConstantFile.Jet_Exist_Without_Production);

        Double wt = batchDao.getTotalWtByBatchId(productionPlanExist.getBatchId());

        //get the party and quality and shade data
        StockMast stockMast = null;//stockBatchService.getStockById(productionPlanExist.getStockId());
        GetQualityResponse quality = null;//qualityServiceImp.getQualityByID(stockMast.getQualityId());
        Optional<ShadeMast> shadeMast = shadeService.getShadeMastById(productionPlanExist.getShadeId());
        if (shadeMast.isEmpty())
            throw new Exception(constantFile.Shade_Not_Found);

        //basic data
        data.setColorTone(shadeMast.get().getColorTone());
        data.setPartyShadeNo(shadeMast.get().getPartyShadeNo());
        data.setQualityId(quality.getQualityId());
        data.setQualityEntryId(stockMast.getQualityId());
        data.setBatchWt(wt);
        //data.setStockId(productionPlanExist.getStockId());
        data.setBatchId(productionPlanExist.getBatchId());
        data.setJetId(jetId);
        data.setProductionId(productionId);

        //process with item data
        List<GetJetSlipData> getJetSlipData = new ArrayList<>();


        DyeingProcessMast dyeingProcessMast = dyeingProcessService.getDyeingProcessById(shadeMast.get().getProcessId());

        for (DyeingProcessData dyeingProcessData : dyeingProcessMast.getDyeingProcessData()) {
            List<SlipItemList> itemLists = new ArrayList<>();
            for (DyeingChemicalData dyeingChemicalData : dyeingProcessData.getDyeingChemicalData()) {
                SlipItemList item = new SlipItemList();
                SupplierRate supplierRateItem = supplierService.getSupplierRateByItemId(dyeingChemicalData.getItemId());
                item.setItemName(supplierRateItem.getItemName());
                String supplier = supplierService.getSupplierNameByItemId(dyeingChemicalData.getItemId());
                item.setSupplierName(supplier);
                //count the qty by liquerration * processConcentration * batchWt

                //get the jet mast data

//                if(jetMastExist.get().getLiquorRatio()==null || jetMastExist.get().getLiquorRatio().equals(""))
//                    throw new Exception("jet liquer ration can't be null");

                Double qty = (dyeingProcessData.getLiquerRation() * dyeingChemicalData.getConcentration() * wt) / 1000;
                item.setQty(qty);
                itemLists.add(item);

            }

            //add to the list
            getJetSlipData.add(new GetJetSlipData(dyeingProcessData, itemLists));

        }
        if (getJetSlipData.isEmpty())
            throw new Exception("no process data found ");

        data.setSlipDataList(getJetSlipData);


        return data;

    }


    public Boolean deleteJetMastByJetId(Long id) throws Exception {

        Optional<JetMast> jetMast = jetMastDao.getJetById(id);
        if (jetMast.isEmpty())
            throw new Exception(constantFile.Jet_Not_Found);

        //check the child record available for the jet or not
        if (!jetMast.get().getJetDataList().isEmpty())
            throw new Exception(constantFile.Jet_Record_Exist);

        jetMastDao.deleteByJetId(id);
        return true;


    }

    public JetData getJetDataByProductionId(Long productionId) {

        JetData jetData = jetDataDao.findByProductionId(productionId);
        return jetData;

    }

    public List<AddJet> getAllJet() throws Exception {

        List<AddJet> list = new ArrayList<>();
        List<JetMast> jetMastList = jetMastDao.getAll();

        if (jetMastList.isEmpty())
            return list;

        for (JetMast jetMast : jetMastList) {
            list.add(new AddJet(jetMast));
        }
        return list;
    }

    public JetData getJetDataByProductionIdWithoutFilter(Long id) {

        JetData jetData = jetDataDao.getJetDataByProductionId(id);
        return jetData;
    }

    public void removeProductionFromJet(Long jetId, Long productionId) throws Exception {

        //check first the production is already in jet or not
        JetData jetDataExist = jetDataDao.jetDataExistWithJetIdAndProductionId(jetId, productionId);
        if (jetDataExist == null)
            throw new Exception(ConstantFile.Jet_Exist_Without_Production);

        ProductionPlan productionPlan = productionPlanService.getProductionData(productionId);

            /*

            *********************** Task to perform while removing the production from jet *************

             1.take back the amount of color and chemical which is used for dyeing batch
             2.remove the data of dyeing slip as well
             3.change the status of production
             4.change the production sequence of jet data & remove the entry from jet data table

             */


        // 1. take back the amount of color which is issue for the batch and change the status of that boxes

       /* Double totalBatchWt = stockBatchService.getWtByControlAndBatchId(productionPlan.getStockId(),productionPlan.getBatchId());
        Double colorAmtUsed=0.0;//color and chemical used
        Optional<ShadeMast> shadeMast = shadeService.getShadeMastById(productionPlan.getShadeId());

        for(ShadeData shadeData:shadeMast.get().getShadeDataList())
        {
            colorAmtUsed = (shadeData.getConcentration()*totalBatchWt)/100;//amt of color for the item
            System.out.println("used:"+colorAmtUsed);

            Long boxId = colorDataDao.getLatestIssuedColorBoxByColorDataId(shadeData.getSupplierItemId());
            ColorBox colorBox=colorService.getColorBoxById(boxId);
            colorBox.setFinished(false);
            colorBox.setQuantityLeft(colorBox.getQuantityLeft()+colorAmtUsed);

        }*/

        //take back the chemical color boxes that are being used
/*
        DyeingProcessMast dyeingProcessMast = dyeingProcessService.getDyeingProcessById(shadeMast.get().getProcessId());
        for(DyeingProcessData dyeingProcessData:dyeingProcessMast.getDyeingProcessData())
        {
            for(DyeingChemicalData dyeingChemicalData:dyeingProcessData.getDyeingChemicalData())
            {
                colorAmtUsed = (dyeingChemicalData.getConcentration()*totalBatchWt*dyeingProcessData.getLiquerRation())/1000;//amt of color for the item
                System.out.println("used:"+colorAmtUsed);

                Long boxId = colorDataDao.getLatestIssuedColorBoxByColorDataId(dyeingChemicalData.getItemId());
                ColorBox colorBox=colorService.getColorBoxById(boxId);
                colorBox.setFinished(false);
                colorBox.setQuantityLeft(colorBox.getQuantityLeft()+colorAmtUsed);

            }

        }
*/


        // 2.remove the dyeing slip from the dyeign slip based on jet and produciton id
        DyeingSlipMast dyeingSlipMast = dyeingSlipService.getDyeingSlipByProductionId(productionPlan.getId());

        dyeingSlipService.deleteDyeingSlipDataByMastId(dyeingSlipMast.getId());

        //3.change the status of production
        productionPlan.setStatus(false);

        //4. remove the production from jet data table and change the sequesce of jet data

        //change the remain production sequence
        List<JetData> jetDataList = jetDataDao.findByControlId(jetId);
        for (JetData jetData : jetDataList) {
            if (jetData.getSequence() > jetDataExist.getSequence())
                jetData.setSequence(jetData.getSequence() - 1);

        }
        //remover the jet record
        jetDataDao.deleteJetDataById(jetDataExist.getId());


        ///remove the production record if the production record added by direct
        if (productionPlan.getIsDirect() == true) {
            //change the status of batches as well

            List<BatchData> batchDataList = batchDao.getBatchByBatchId(productionPlan.getBatchId());
            batchDataList.forEach(e -> {
                e.setIsProductionPlanned(false);
                batchDao.save(e);
            });

            productionPlanDao.deleteProductionById(productionPlan.getId());

        }

    }

    public void updateJet(AddJet jetMast) throws Exception {
        Optional<JetMast> jetMastExist = jetMastDao.getJetById(jetMast.getId());
        if (jetMastExist.isEmpty())
            throw new Exception(constantFile.Jet_Not_Found);

        JetMast jetToUpdate = new JetMast(jetMast);
        jetToUpdate.setId(jetMast.getId());
        List<JetData> jetDataList = jetDataDao.findByControlId(jetMast.getId());

        if (jetMast.getCapacity() < jetMastExist.get().getCapacity())
            throw new Exception("jet capacity can't be update because already batch available with that capacity");

        jetMastDao.save(jetToUpdate);
        for (JetData jetData : jetDataList) {
            //jetData.setControlId(jetToUpdate.getId());
            jetDataDao.updateJetWithId(jetData.getId(), jetToUpdate.getId());
        }

    }

    public JetMast getJetMastById(Long id) throws Exception {
        Optional<JetMast> jetMast = jetMastDao.getJetById(id);
        if (jetMast.isEmpty())
            throw new Exception(constantFile.Jet_Not_Found);
        return jetMast.get();
    }

    public Boolean getJetIsDeletable(Long id) throws Exception {
        Optional<JetMast> jetMastExist = jetMastDao.getJetById(id);
        if (jetMastExist.isEmpty())
            throw new Exception(constantFile.Jet_Not_Found);

        List<JetData> jetDataList = jetDataDao.findByControlId(id);
        if (jetDataList.isEmpty())
            return true;
        else
            return false;

    }

    public void saveJetRecord(JetData jetData) {
        jetDataDao.save(jetData);
    }

    public List<JetData> getAllProductionInTheQueue() {
        return jetDataDao.getAllProductionInTheQueue();
    }

    public List<JetData> getAllProductionSuccessFromJet() {
        return jetDataDao.getAllProductionSuccessFromJet();
    }

    public void hmiSaveJetdata(JetStart record) throws Exception {
        //check that the production is completed or not

        JetData jetDataExist = jetDataDao.getJetDataByProductionId(record.getProductionId());

        if(jetDataExist==null)
            throw new Exception(ConstantFile.Jet_Not_Found);

        if(jetDataExist.getStatus().equals(JetStatus.success))
            throw new Exception(ConstantFile.Jet_Record_Completed);


        if(!jetDataExist.getControlId().equals(record.getJetNo()))
            throw new Exception(ConstantFile.Production_Record_Not_Exist_With_Jet);

        ProductionPlan productionPlan = productionPlanService.getProductionDataById(jetDataExist.getProductionId());

        if(productionPlan.getStatus()==false)
            throw new Exception(ConstantFile.Production_Record_Not_Planned);

        //check that batch id is merge batch id or not
        BatchData isMergeBatch = batchDao.getIsMergeBatchId(productionPlan.getBatchId());
        Double totalWt = 0.0;
        if(isMergeBatch==null)
        {
            totalWt = batchDao.getTotalWtByBatchId(productionPlan.getBatchId());
        }
        else
        {
            totalWt = batchDao.getTotalWtByMergeBatchId(productionPlan.getBatchId());
        }

        HMIMast hmiMast = new HMIMast(productionPlan,jetDataExist,totalWt,record);

        hmiMastDao.save(hmiMast);
    }
}
