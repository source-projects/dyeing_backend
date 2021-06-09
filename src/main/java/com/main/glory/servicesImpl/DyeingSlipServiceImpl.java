package com.main.glory.servicesImpl;

import com.main.glory.Dao.StockAndBatch.BatchDao;

import com.main.glory.Dao.dyeingSlip.DyeingSlipDataDao;
import com.main.glory.Dao.dyeingSlip.DyeingSlipItemDataDao;
import com.main.glory.Dao.dyeingSlip.DyeingSlipMastDao;
import com.main.glory.model.ConstantFile;
import com.main.glory.model.StockDataBatchData.BatchData;
import com.main.glory.model.StockDataBatchData.response.GetBatchWithControlId;
import com.main.glory.model.dyeingSlip.DyeingSlipData;
import com.main.glory.model.dyeingSlip.DyeingSlipItemData;
import com.main.glory.model.dyeingSlip.DyeingSlipMast;
import com.main.glory.model.dyeingSlip.request.AddAddtionalSlip;
import com.main.glory.model.dyeingSlip.request.GetItemByShadeAndBatch;
import com.main.glory.model.dyeingSlip.request.SlipFormatData;
import com.main.glory.model.dyeingSlip.responce.BatchResponseWithSlip;
import com.main.glory.model.dyeingSlip.responce.GetAllAdditionalDyeingSlip;
import com.main.glory.model.dyeingSlip.responce.ItemListForDirectDyeing;
import com.main.glory.model.jet.JetMast;
import com.main.glory.model.productionPlan.ProductionPlan;
import com.main.glory.model.productionPlan.request.GetAllProductionWithShadeData;
import com.main.glory.model.quality.Quality;
import com.main.glory.model.quality.QualityName;
import com.main.glory.model.quality.response.GetQualityResponse;
import com.main.glory.model.shade.ShadeData;
import com.main.glory.model.shade.ShadeMast;
import com.main.glory.model.supplier.SupplierRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("dyeingSlipServiceImpl")
public class DyeingSlipServiceImpl {

    @Autowired
    AdminServciceImpl adminServcice;

    ConstantFile constantFile;

    @Autowired
    JetServiceImpl jetService;

    @Autowired
    BatchDao batchDao;

    @Autowired
    SupplierServiceImpl supplierService;

    @Autowired
    DyeingSlipDataDao dyeingSlipDataDao;

    @Autowired
    DyeingSlipItemDataDao dyeingSlipItemDataDao;

  /*  @Autowired
    AdditionDyeingProcessSlipDao additionDyeingProcessSlipDao;*/

    @Autowired
    ProductionPlanImpl productionPlanService;

    @Autowired
    DyeingSlipMastDao dyeingSlipMastDao;

    @Autowired
    StockBatchServiceImpl stockBatchService;

    @Autowired
    PartyServiceImp partyServiceImp;

    @Autowired
    QualityServiceImp qualityServiceImp;

    @Autowired
    ShadeServiceImpl shadeService;

    public void saveDyeingSlipMast(DyeingSlipMast dyeingSlipMast)
    {
        dyeingSlipMastDao.save(dyeingSlipMast);
    }

    public void updateDyeingSlip(DyeingSlipMast data) throws Exception {
        DyeingSlipMast dyeingSlipMast = dyeingSlipMastDao.getDyeingSlipById(data.getId());
        if(dyeingSlipMast==null)
            throw new Exception("no slip data found");

        dyeingSlipMastDao.saveAndFlush(data);

    }

    public SlipFormatData getDyeingSlipByBatchStockId(String batchId, Long productionId) throws Exception {

        Long totalPcs;
        DyeingSlipMast dyeingSlipMastExist = dyeingSlipMastDao.findByBatchIdAndProductionId(batchId, productionId);
        if(dyeingSlipMastExist==null)
            throw new Exception(ConstantFile.DyeingSlip_Not_Found);

        //set the color flag
        int i=0;
        for(DyeingSlipData dyeingSlipData:dyeingSlipMastExist.getDyeingSlipDataList())
        {
            for(DyeingSlipItemData dyeingSlipItemData:dyeingSlipData.getDyeingSlipItemData()) {
                SupplierRate supplierRate = supplierService.getSupplierRateByItemId(dyeingSlipItemData.getItemId());
                if (supplierRate.getItemType().equals("Color")) {
                    dyeingSlipItemDataDao.updateIsColorByItemId(supplierRate.getId(), true);
                } else {
                    dyeingSlipItemDataDao.updateIsColorByItemId(supplierRate.getId(), false);
                }
                i++;
            }
        }
        SlipFormatData slipFormatData = new SlipFormatData(dyeingSlipMastExist);

        ProductionPlan productionPlan = productionPlanService.getProductionData(dyeingSlipMastExist.getProductionId());
        GetQualityResponse quality=null;//qualityServiceImp.getQualityByID(productionPlan.getQualityEntryId());
        Double wt = 0.0;//stockBatchService.getWtByControlAndBatchId(dyeingSlipMastExist.getStockId(), dyeingSlipMastExist.getBatchId());
        if(productionPlan.getIsMergeBatchId()==true)
        {
            totalPcs = batchDao.getTotalPcsByMergeBatchId(productionPlan.getBatchId());
            wt = stockBatchService.getWtByMergeBatchId(productionPlan.getBatchId());
        }
        else {
            wt = stockBatchService.getWtByBatchId(productionPlan.getBatchId());
            totalPcs = batchDao.getTotalPcsByBatchId(productionPlan.getBatchId());

        }
        if(productionPlan.getShadeId()!=null)
        {
            Optional<ShadeMast> shadeMast = shadeService.getShadeMastById(productionPlan.getShadeId());
            slipFormatData.setColorTone(shadeMast.get().getColorTone());
            slipFormatData.setColorName(shadeMast.get().getColorName());
            slipFormatData.setPartyShadeNo(shadeMast.get().getPartyShadeNo());
        }
        else
        {
            slipFormatData.setColorTone("#fff");
            slipFormatData.setColorName("No Color");
            slipFormatData.setPartyShadeNo("-");
        }



        GetAllProductionWithShadeData record = productionPlanService.getProductionWithColorToneByBatchId(productionPlan.getBatchId());

        slipFormatData.setTotalWt(wt);
        slipFormatData.setBatchCount(totalPcs);
        slipFormatData.setQualityId(record.getQualityId());
        //slipFormatData.setQualityEntryId(quality.getId());
        JetMast jetMast =jetService.getJetMastById(slipFormatData.getJetId());
        if(jetMast==null)
            throw new Exception(ConstantFile.Jet_Not_Exist_With_Name);
        slipFormatData.setJetName(jetMast.getName());
        slipFormatData.setDyeingSlipDataList(dyeingSlipMastExist.getDyeingSlipDataList());


        return  slipFormatData;
    }

    public List<SlipFormatData> getAllDyeingSlip() throws Exception {

        List<SlipFormatData> list =new ArrayList<>();
        List<DyeingSlipMast> dyeingSlipMastList = dyeingSlipMastDao.getAllDyeingSlip();
        if(dyeingSlipMastList.isEmpty())
            throw new Exception(constantFile.DyeingSlip_Not_Found );

        for(DyeingSlipMast dyeingSlipMastExist:dyeingSlipMastList)
        {
            Long totalPcs;
            SlipFormatData slipFormatData = new SlipFormatData(dyeingSlipMastExist);
            ProductionPlan productionPlan = productionPlanService.getProductionData(dyeingSlipMastExist.getProductionId());
            GetQualityResponse quality=null;//qualityServiceImp.getQualityByID(productionPlan.getQualityEntryId());
            Double wt = 0.0;//stockBatchService.getWtByControlAndBatchId(dyeingSlipMastExist.getStockId(), dyeingSlipMastExist.getBatchId());
            if(productionPlan.getIsMergeBatchId()==true)
            {
                totalPcs = batchDao.getTotalPcsByMergeBatchId(productionPlan.getBatchId());
                wt = stockBatchService.getWtByMergeBatchId(productionPlan.getBatchId());
            }
            else {
                wt = stockBatchService.getWtByBatchId(productionPlan.getBatchId());
                totalPcs = batchDao.getTotalPcsByBatchId(productionPlan.getBatchId());

            }
            if(productionPlan.getShadeId()!=null)
            {
                Optional<ShadeMast> shadeMast = shadeService.getShadeMastById(productionPlan.getShadeId());
                slipFormatData.setColorTone(shadeMast.get().getColorTone());
                slipFormatData.setColorName(shadeMast.get().getColorName());
                slipFormatData.setPartyShadeNo(shadeMast.get().getPartyShadeNo());
            }
            else
            {
                slipFormatData.setColorTone("#fff");
                slipFormatData.setColorName("No Color");
                slipFormatData.setPartyShadeNo("-");
            }


            System.out.println("batch:"+productionPlan.getBatchId());

            GetAllProductionWithShadeData record = productionPlanService.getProductionWithColorToneByBatchId(productionPlan.getBatchId());

            slipFormatData.setTotalWt(stockBatchService.changeInFormattedDecimal(wt));
            slipFormatData.setBatchCount(totalPcs);
            slipFormatData.setQualityId(record.getQualityId());
            //slipFormatData.setQualityEntryId(quality.getId());
            JetMast jetMast =jetService.getJetMastById(slipFormatData.getJetId());
           /* if(jetMast==null)
                throw new Exception(ConstantFile.Jet_Not_Exist_With_Name);*/
            slipFormatData.setJetName(jetMast.getName());
            list.add(slipFormatData);
        }

        return list;
    }

    public Long addAdditionalSlipData(AddAddtionalSlip addAdditionDyeingSlipModel) throws Exception {

        //AdditionDyeingProcessSlip toStoreSlip =new AdditionDyeingProcessSlip();

        //check the begore the master dyeing slip is exit or not
            DyeingSlipMast dyeingSlipMast = dyeingSlipMastDao.getDyeingSlipByBatchId(addAdditionDyeingSlipModel.getBatchId());
            if(dyeingSlipMast==null) {
                throw new Exception(constantFile.DyeingSlip_Not_Found);
            }


            //check the additonal dyeing slip already exist with batch or not
            DyeingSlipData dyeingSlipDataExist = dyeingSlipDataDao.getOnlyAdditionalSlipMastById(dyeingSlipMast.getId());
            if(dyeingSlipDataExist!=null)
                throw new Exception(constantFile.Additional_DyeingSlip_Exist);

           /* DyeingSlipData dyeingSlipDataExistWithAdditional = dyeingSlipDataDao.getOnlyAdditionalSlipMastById(dyeingSlipMast.getId());
            if(dyeingSlipDataExistWithAdditional!=null)
                throw new Exception("additional process already exist with batch:"+addAdditionDyeingSlipModel.getBatchId());*/

            DyeingSlipData dyeingSlipData = new DyeingSlipData(addAdditionDyeingSlipModel.getDyeingSlipData());
            dyeingSlipData.setControlId(dyeingSlipMast.getId());
            DyeingSlipData x = dyeingSlipDataDao.save(dyeingSlipData);

            List<DyeingSlipItemData> list = new ArrayList<>();
            for(DyeingSlipItemData d:addAdditionDyeingSlipModel.getDyeingSlipData().getDyeingSlipItemData())
            {
                d.setControlId(x.getId());
                list.add(d);

            }

            dyeingSlipItemDataDao.saveAll(list);

            return dyeingSlipMast.getId();


    }

    public List<GetAllAdditionalDyeingSlip> getAllAdditionalDyeingSlip() throws Exception {
        List<GetAllAdditionalDyeingSlip> resultList =new ArrayList<>();
        List<GetAllAdditionalDyeingSlip> list =dyeingSlipMastDao.getAllAddtionalDyeingProcess();

        for(GetAllAdditionalDyeingSlip ad:list)
        {
            DyeingSlipData additionalDyeingSlip = dyeingSlipDataDao.getOnlyAdditionalSlipMastById(ad.getId());
            resultList.add(new GetAllAdditionalDyeingSlip(ad,additionalDyeingSlip));
        }
        if(resultList.isEmpty())
            throw new Exception(constantFile.DyeingSlip_Not_Found);
        return resultList;
    }

    public GetAllAdditionalDyeingSlip getAdditionalDyeingSlipById(Long id) throws Exception {

        DyeingSlipData data = dyeingSlipDataDao.getOnlyAdditionalSlipMastById(id);
        if(data==null)
            throw new Exception(constantFile.DyeingSlip_Not_Found);

        DyeingSlipMast dyeingSlipMastExist = dyeingSlipMastDao.getDyeingSlipById(id);

        GetAllAdditionalDyeingSlip dyeingSlipMast =new GetAllAdditionalDyeingSlip(dyeingSlipMastExist);
        int i=0;
        for(DyeingSlipItemData dyeingSlipItemData:data.getDyeingSlipItemData())
        {
            SupplierRate supplierRate = supplierService.getSupplierRateByItemId(dyeingSlipItemData.getItemId());
            if(supplierRate.getItemType().equals("Color"))
            {
                dyeingSlipItemDataDao.updateIsColorByItemId(supplierRate.getId(),true);
            }
            else {
                dyeingSlipItemDataDao.updateIsColorByItemId(supplierRate.getId(),true);
            }

            i++;
        }
        dyeingSlipMast.setDyeingSlipData(data);

        return dyeingSlipMast;

    }

    public void updateAddtionalDyeingSlip(AddAddtionalSlip addAdditionDyeingSlipModel) throws Exception {

           DyeingSlipMast dyeingSlipMast = dyeingSlipMastDao.getDyeingSlipByProductionId(addAdditionDyeingSlipModel.getProductionId());
            if(dyeingSlipMast==null) {
                throw new Exception(constantFile.DyeingSlip_Exist_Without_Production);
            }
            if(!addAdditionDyeingSlipModel.getDyeingSlipData().getProcessType().equals("addition"))
                throw new Exception(constantFile.DyeingSlip_Not_Found);

            addAdditionDyeingSlipModel.getDyeingSlipData().setControlId(dyeingSlipMast.getId());
            dyeingSlipDataDao.save(addAdditionDyeingSlipModel.getDyeingSlipData());
            return;


    }

    public Boolean deleteAdditionalDyeingSlipById(Long id) throws Exception {

        DyeingSlipMast existingDyeingSlip = dyeingSlipMastDao.getAdditionalDyeingSlipById(id);
        if(existingDyeingSlip==null)
            throw new Exception(constantFile.Additional_DyeingSlip_Not_Found);

        dyeingSlipItemDataDao.deleteByDyeingSlipId(id);
        dyeingSlipDataDao.deleteAdditionalSlipDataByDyeingSlipId(id);
        return true;

    }

    public DyeingSlipMast getDyeingSlipByProductionId(Long id) {
        DyeingSlipMast dyeingSlipMast = dyeingSlipMastDao.getDyeingSlipByProductionId(id);
        return dyeingSlipMast;
    }

    public void deleteDyeingSlipDataByMastId(Long id) {

        DyeingSlipMast dyeingSlipMast = dyeingSlipMastDao.getDyeingSlipById(id);
        for(DyeingSlipData dyeingSlipData:dyeingSlipMast.getDyeingSlipDataList())
        {
            //remove all the item and data from the dyeing slip
            dyeingSlipItemDataDao.deleteDyeingSlipItemByDyeingSlipDataId(dyeingSlipData.getId());
            dyeingSlipDataDao.deleteDyeingSlipDataById(dyeingSlipData.getId());

        }

        //remove the  dyeing mast process by id
        dyeingSlipMastDao.deleteDyeingSlipById(id);
    }

    public DyeingSlipMast saveDyeingSlipMastFromProcess(DyeingSlipMast dyeingSlipMast) {

        DyeingSlipMast x =dyeingSlipMastDao.save(dyeingSlipMast);
        return x;
    }

    public DyeingSlipData getDyeingProcessDataOnlyBySlipMast(Long id) {
        DyeingSlipData dyeingSlipData = dyeingSlipDataDao.getOnlyDyeingProcessByMastId(id);
        return dyeingSlipData;
    }

    public void saveDyeingSlipDataOnly(DyeingSlipData getDyeingProcess) {
        dyeingSlipDataDao.save(getDyeingProcess);
    }

    public List<DyeingSlipMast> getDyeingSlipByApprovedId(Long id) {
        List<DyeingSlipMast> dyeingSlipMasts = dyeingSlipMastDao.getDyeingSlipByApprovedById(id);
        return dyeingSlipMasts;
    }

    public void updateDyeingSlipWithApproveById(Long approved, Long dyeingId) {

        dyeingSlipMastDao.updateDyeingWithApprovedId(approved,dyeingId);
    }

    public List<DyeingSlipItemData> getDyeingItemDataByItemId(Long e) {
        return dyeingSlipItemDataDao.getAllItemByItemId(e);

    }

    public void addDirectDyeingSlip(DyeingSlipMast dyeingSlipMast) throws Exception {
        DyeingSlipMast dyeingSlipMastExist = dyeingSlipMastDao.getDyeingSlipByProductionId(dyeingSlipMast.getProductionId());
        if(dyeingSlipMastExist!=null)
            throw new Exception(constantFile.Additional_DyeingSlip_Exist);
        dyeingSlipMastDao.save(dyeingSlipMast);

    }

    public List<GetAllAdditionalDyeingSlip> getAllDirectDyeignSlip() throws Exception {
        List<GetAllAdditionalDyeingSlip> resultList =new ArrayList<>();
        //List<GetAllAdditionalDyeingSlip> list =dyeingSlipMastDao.getAllAddtionalDyeingProcess();
        List<GetAllAdditionalDyeingSlip> list =dyeingSlipMastDao.getAllDirectDyeingProcess();

        for(GetAllAdditionalDyeingSlip ad:list)
        {
            DyeingSlipData directDyeingSlip = dyeingSlipDataDao.getOnlyDirectSlipMastById(ad.getId());
            resultList.add(new GetAllAdditionalDyeingSlip(ad,directDyeingSlip));
        }
        if(resultList.isEmpty())
            throw new Exception(constantFile.DyeingSlip_Not_Found);
        return resultList;
    }

    public GetAllAdditionalDyeingSlip getDirectDyeingSlipById(Long id) throws Exception {
        DyeingSlipData data = dyeingSlipDataDao.getOnlyDirectSlipMastById(id);
        if(data==null)
            throw new Exception(constantFile.Direct_DyeingSlip_Not_Found);

        DyeingSlipMast dyeingSlipMastExist = dyeingSlipMastDao.getDyeingSlipById(id);

        GetAllAdditionalDyeingSlip dyeingSlipMast =new GetAllAdditionalDyeingSlip(dyeingSlipMastExist);
        int i=0;
        for(DyeingSlipItemData dyeingSlipItemData:data.getDyeingSlipItemData())
        {
            SupplierRate supplierRate = supplierService.getSupplierRateByItemId(dyeingSlipItemData.getItemId());
            if(supplierRate.getItemType().equals("Color"))
            {
                dyeingSlipItemDataDao.updateIsColorByItemId(supplierRate.getId(),true);
            }
            else {
                dyeingSlipItemDataDao.updateIsColorByItemId(supplierRate.getId(),true);
            }

            i++;
        }
        dyeingSlipMast.setDyeingSlipData(data);

        return dyeingSlipMast;
    }

    public void updateDirectDyeingSlip(AddAddtionalSlip addAdditionDyeingSlipModel) throws Exception {
        DyeingSlipMast dyeingSlipMast = dyeingSlipMastDao.getDyeingSlipByProductionId(addAdditionDyeingSlipModel.getProductionId());
        if(dyeingSlipMast==null) {
            throw new Exception(constantFile.Direct_DyeingSlip_Not_Found);
        }
        if(!addAdditionDyeingSlipModel.getDyeingSlipData().getProcessType().equals("directDyeing"))
            throw new Exception(constantFile.DyeingSlip_Not_Found);

        addAdditionDyeingSlipModel.getDyeingSlipData().setControlId(dyeingSlipMast.getId());
        dyeingSlipDataDao.save(addAdditionDyeingSlipModel.getDyeingSlipData());
        return;
    }

    public List<GetAllAdditionalDyeingSlip> getAllReDyeignSlip() throws Exception {
        List<GetAllAdditionalDyeingSlip> resultList =new ArrayList<>();
        //List<GetAllAdditionalDyeingSlip> list =dyeingSlipMastDao.getAllAddtionalDyeingProcess();
        List<GetAllAdditionalDyeingSlip> list =dyeingSlipMastDao.getAllReDyeingProcess();

        for(GetAllAdditionalDyeingSlip ad:list)
        {
            DyeingSlipData directDyeingSlip = dyeingSlipDataDao.getOnlyReDyeingSlipMastById(ad.getId());
            resultList.add(new GetAllAdditionalDyeingSlip(ad,directDyeingSlip));
        }
        if(resultList.isEmpty())
            throw new Exception(constantFile.DyeingSlip_Not_Found);
        return resultList;
    }

    public GetAllAdditionalDyeingSlip getReDyeingSlipById(Long id) throws Exception {
        DyeingSlipData data = dyeingSlipDataDao.getOnlyReDyeingSlipMastById(id);
        if(data==null)
            throw new Exception(constantFile.Re_DyeingSlip_Not_Found);

        DyeingSlipMast dyeingSlipMastExist = dyeingSlipMastDao.getDyeingSlipById(id);

        GetAllAdditionalDyeingSlip dyeingSlipMast =new GetAllAdditionalDyeingSlip(dyeingSlipMastExist);
        int i=0;
        for(DyeingSlipItemData dyeingSlipItemData:data.getDyeingSlipItemData())
        {
            SupplierRate supplierRate = supplierService.getSupplierRateByItemId(dyeingSlipItemData.getItemId());
            if(supplierRate.getItemType().equals("Color"))
            {
                dyeingSlipItemDataDao.updateIsColorByItemId(supplierRate.getId(),true);
            }
            else {
                dyeingSlipItemDataDao.updateIsColorByItemId(supplierRate.getId(),true);
            }

            i++;
        }
        dyeingSlipMast.setDyeingSlipData(data);

        return dyeingSlipMast;
    }

    public void updateReDyeingSlip(AddAddtionalSlip addAdditionDyeingSlipModel) throws Exception {
        DyeingSlipMast dyeingSlipMast = dyeingSlipMastDao.getDyeingSlipByProductionId(addAdditionDyeingSlipModel.getProductionId());
        if(dyeingSlipMast==null) {
            throw new Exception(constantFile.DyeingSlip_Not_Found);
        }
        if(!addAdditionDyeingSlipModel.getDyeingSlipData().getProcessType().equals("Re-Dyeing"))
            throw new Exception(constantFile.Re_DyeingSlip_Not_Found);

        addAdditionDyeingSlipModel.getDyeingSlipData().setControlId(dyeingSlipMast.getId());
        dyeingSlipDataDao.save(addAdditionDyeingSlipModel.getDyeingSlipData());
        return;
    }

    public List<ItemListForDirectDyeing> getItemListByShadeAndBatch(GetItemByShadeAndBatch record) throws Exception {
        List<ItemListForDirectDyeing> list =new ArrayList<>();

        BatchData isBatchId = batchDao.getIsBatchId(record.getBatchId());
        if(isBatchId!=null) {
            Double totalBatchWt = stockBatchService.getWtByBatchId(record.getBatchId());
            Optional<ShadeMast> shadeMastExist = shadeService.getShadeMastById(record.getShadeId());
            if (shadeMastExist.isEmpty())
                throw new Exception(constantFile.Shade_Not_Found);

            for (ShadeData shadeData : shadeMastExist.get().getShadeDataList()) {
                ItemListForDirectDyeing item = supplierService.getSupplierWithItemByItemId(shadeData.getSupplierItemId());
                if (item == null)
                    continue;

                list.add(new ItemListForDirectDyeing(item, shadeData, totalBatchWt));

            }

        }
        else
        {
            //it is merge batch id
            Double totalBatchWt = stockBatchService.getWtByMergeBatchId(record.getBatchId());
            Optional<ShadeMast> shadeMastExist = shadeService.getShadeMastById(record.getShadeId());
            if (shadeMastExist.isEmpty())
                throw new Exception(constantFile.Shade_Not_Found);

            for (ShadeData shadeData : shadeMastExist.get().getShadeDataList()) {
                ItemListForDirectDyeing item = supplierService.getSupplierWithItemByItemId(shadeData.getSupplierItemId());
                if (item == null)
                    continue;

                list.add(new ItemListForDirectDyeing(item, shadeData, totalBatchWt));

            }
        }
        return list;
    }

    public BatchResponseWithSlip getAdditionalDyeingSlipForPrintById(Long id) throws Exception {
        DyeingSlipData data = dyeingSlipDataDao.getOnlyAdditionalSlipMastById(id);
        if(data==null)
            throw new Exception(constantFile.Additional_DyeingSlip_Not_Found);

        DyeingSlipMast dyeingSlipMastExist = dyeingSlipMastDao.getDyeingSlipById(id);

        BatchResponseWithSlip dyeingSlipMast =null;
        ProductionPlan productionPlan = productionPlanService.getById(dyeingSlipMastExist.getProductionId());
        String qualityId=null;
        Double totalWt;
        ShadeMast shadeMast=null;
        Long batchCount=0l;
        if(productionPlan.getIsMergeBatchId()==true)
        {
            totalWt = batchDao.getTotalWtByMergeBatchIdWithProduction(productionPlan.getBatchId());
            if(productionPlan.getIsDirect()==false)
            {
                shadeMast = shadeService.getShadeById(productionPlan.getShadeId());
                batchCount++;
            }

            //multi quality
            List<GetBatchWithControlId> batchWithControlIdList = batchDao.getBatchesByMergeBatchId(productionPlan.getBatchId());
            for(GetBatchWithControlId batch:batchWithControlIdList)
            {
               Quality quality = qualityServiceImp.getQualityByStockId(batch.getControlId());
               qualityId=(qualityId==null?quality.getQualityId():qualityId+","+quality.getQualityId());
            }
        }
        else
        {
            totalWt = batchDao.getTotalWtByBatchIdWithProduction(productionPlan.getBatchId());
            if(productionPlan.getIsDirect()==false)
            {
                shadeMast = shadeService.getShadeById(productionPlan.getShadeId());
            }
            Long stockId = batchDao.getControlIdByBatchId(productionPlan.getBatchId());
            Quality quality = qualityServiceImp.getQualityByStockId(stockId);
            qualityId =quality.getQualityId();
            batchCount=1l;
        }


        dyeingSlipMast=new BatchResponseWithSlip(data,shadeMast,totalWt,qualityId,batchCount);


        return dyeingSlipMast;


    }
}
