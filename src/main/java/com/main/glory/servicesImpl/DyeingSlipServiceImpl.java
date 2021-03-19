package com.main.glory.servicesImpl;

import com.main.glory.Dao.dyeingSlip.AdditionDyeingProcessSlipDao;
import com.main.glory.Dao.dyeingSlip.DyeingSlipDataDao;
import com.main.glory.Dao.dyeingSlip.DyeingSlipItemDataDao;
import com.main.glory.Dao.dyeingSlip.DyeingSlipMastDao;
import com.main.glory.model.dyeingSlip.DyeingSlipData;
import com.main.glory.model.dyeingSlip.DyeingSlipItemData;
import com.main.glory.model.dyeingSlip.DyeingSlipMast;
import com.main.glory.model.dyeingSlip.request.AddAddtionalSlip;
import com.main.glory.model.dyeingSlip.request.SlipFormatData;
import com.main.glory.model.dyeingSlip.responce.GetAllAdditionalDyeingSlip;
import com.main.glory.model.productionPlan.ProductionPlan;
import com.main.glory.model.quality.response.GetQualityResponse;
import com.main.glory.model.shade.ShadeMast;
import com.main.glory.model.supplier.SupplierRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("dyeingSlipServiceImpl")
public class DyeingSlipServiceImpl {

    @Autowired
    SupplierServiceImpl supplierService;

    @Autowired
    DyeingSlipDataDao dyeingSlipDataDao;

    @Autowired
    DyeingSlipItemDataDao dyeingSlipItemDataDao;

    @Autowired
    AdditionDyeingProcessSlipDao additionDyeingProcessSlipDao;

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

        DyeingSlipMast dyeingSlipMastExist = dyeingSlipMastDao.findByBatchIdAndProductionId(batchId, productionId);
        if(dyeingSlipMastExist==null)
            throw new Exception("no dyeing slip found");

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
        GetQualityResponse quality=qualityServiceImp.getQualityByID(productionPlan.getQualityEntryId());
        Double wt = stockBatchService.getWtByControlAndBatchId(dyeingSlipMastExist.getStockId(), dyeingSlipMastExist.getBatchId());
        Optional<ShadeMast> shadeMast = shadeService.getShadeMastById(productionPlan.getShadeId());
        if(shadeMast.isEmpty())
            throw new Exception("no shade data found");


        slipFormatData.setColorTone(shadeMast.get().getColorTone());
        slipFormatData.setColorName(shadeMast.get().getColorName());
        slipFormatData.setPartyShadeNo(shadeMast.get().getPartyShadeNo());
        slipFormatData.setTotalWt(wt);
        slipFormatData.setQualityId(quality.getQualityId());
        slipFormatData.setQualityEntryId(quality.getId());

        slipFormatData.setDyeingSlipDataList(dyeingSlipMastExist.getDyeingSlipDataList());


        return  slipFormatData;
    }

    public List<DyeingSlipMast> getAllDyeingSlip() throws Exception {

        List<DyeingSlipMast> dyeingSlipMast = dyeingSlipMastDao.getAllDyeingSlip();
        if(dyeingSlipMast.isEmpty())
            throw new Exception("no data found");
        return dyeingSlipMast;
    }

    public void addAddtionalSlipData(AddAddtionalSlip addAdditionDyeingSlipModel) throws Exception {

        //AdditionDyeingProcessSlip toStoreSlip =new AdditionDyeingProcessSlip();

        //check the begore the master dyeing slip is exit or not
            DyeingSlipMast dyeingSlipMast = dyeingSlipMastDao.getDyeingSlipByProductionId(addAdditionDyeingSlipModel.getProductionId());
            if(dyeingSlipMast==null) {
                throw new Exception("no dyeing slip found for given batch or production");
            }


            //check the dyeing slip already exist with batch or not

            DyeingSlipData dyeingSlipDataExistWithAdditional = dyeingSlipDataDao.getOnlyAdditionalSlipMastById(dyeingSlipMast.getId());
            if(dyeingSlipDataExistWithAdditional!=null)
                throw new Exception("additional process already exist with batch:"+addAdditionDyeingSlipModel.getBatchId());

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


    }

    public List<GetAllAdditionalDyeingSlip> getAllAddtionalDyeignSlip() throws Exception {
        List<GetAllAdditionalDyeingSlip> resultList =new ArrayList<>();
        List<GetAllAdditionalDyeingSlip> list =dyeingSlipMastDao.getAllAddtionalDyeingProcess();

        for(GetAllAdditionalDyeingSlip ad:list)
        {
            DyeingSlipData additionalDyeingSlip = dyeingSlipDataDao.getOnlyAdditionalSlipMastById(ad.getId());
            resultList.add(new GetAllAdditionalDyeingSlip(ad,additionalDyeingSlip));
        }
        if(resultList.isEmpty())
            throw new Exception("no data found");
        return resultList;
    }

    public GetAllAdditionalDyeingSlip getAdditionalDyeingSlipById(Long id) throws Exception {

        DyeingSlipData data = dyeingSlipDataDao.getOnlyAdditionalSlipMastById(id);
        if(data==null)
            throw new Exception("no additional process found");

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
                throw new Exception("no dyeing slip found for given batch or production");
            }
            if(!addAdditionDyeingSlipModel.getDyeingSlipData().getProcessType().equals("addition"))
                throw new Exception("process type is not found");

            addAdditionDyeingSlipModel.getDyeingSlipData().setControlId(dyeingSlipMast.getId());
            dyeingSlipDataDao.save(addAdditionDyeingSlipModel.getDyeingSlipData());
            return;


    }

    public Boolean deleteAdditionalDyeingSlipById(Long id) throws Exception {

        DyeingSlipMast existingDyeingSlip = dyeingSlipMastDao.getAdditionalDyeingSlipById(id);
        if(existingDyeingSlip==null)
            throw new Exception("no additonal slip found");

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
            throw new Exception("already dyeing slip exist for the production");
        dyeingSlipMastDao.save(dyeingSlipMast);

    }
}
