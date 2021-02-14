package com.main.glory.servicesImpl;

import com.main.glory.Dao.dyeingSlip.AdditionDyeingProcessSlipDao;
import com.main.glory.Dao.dyeingSlip.DyeingSlipDataDao;
import com.main.glory.Dao.dyeingSlip.DyeingSlipItemDataDao;
import com.main.glory.Dao.dyeingSlip.DyeingSlipMastDao;
import com.main.glory.model.dyeingSlip.AdditionDyeingProcessSlip;
import com.main.glory.model.dyeingSlip.DyeingSlipData;
import com.main.glory.model.dyeingSlip.DyeingSlipItemData;
import com.main.glory.model.dyeingSlip.DyeingSlipMast;
import com.main.glory.model.dyeingSlip.request.AddAdditionDyeingSlipModel;
import com.main.glory.model.dyeingSlip.request.SlipFormatData;
import com.main.glory.model.dyeingSlip.responce.GetAllAdditionalSlip;
import com.main.glory.model.productionPlan.ProductionPlan;
import com.main.glory.model.quality.response.GetQualityResponse;
import com.main.glory.model.shade.ShadeMast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service("dyeingSlipServiceImpl")
public class DyeingSlipServiceImpl {

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
        SlipFormatData slipFormatData = new SlipFormatData(dyeingSlipMastExist);

        ProductionPlan productionPlan = productionPlanService.getProductionData(dyeingSlipMastExist.getProductionId());
        GetQualityResponse quality=qualityServiceImp.getQualityByID(productionPlan.getQualityEntryId());
        Double wt = stockBatchService.getWtByControlAndBatchId(dyeingSlipMastExist.getStockId(), dyeingSlipMastExist.getBatchId());
        Optional<ShadeMast> shadeMast = shadeService.getShadeMastById(productionPlan.getShadeId());
        if(shadeMast.isEmpty())
            throw new Exception("no shade data found");


        slipFormatData.setColorTone(shadeMast.get().getColorTone());
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

    public Boolean addAddtionalSlipData(AddAdditionDyeingSlipModel addAdditionDyeingSlipModel) throws Exception {

        AdditionDyeingProcessSlip toStoreSlip =new AdditionDyeingProcessSlip();
        try {
            DyeingSlipMast dyeingSlipMast = new DyeingSlipMast(addAdditionDyeingSlipModel);
            ProductionPlan productionPlan = productionPlanService.getProductionData(addAdditionDyeingSlipModel.getProductionId());

            //DyeingSlipMast existingDyeingSlip = dyeingSlipMastDao.findByBatchIdAndProductionId(addAdditionDyeingSlipModel.getBatchId(), addAdditionDyeingSlipModel.getProductionId());


            dyeingSlipMast.setStockId(productionPlan.getStockId());
            if(addAdditionDyeingSlipModel.getDyeingSlipMast().getDyeingSlipDataList()==null)
                throw new Exception("slip item can't be null");

            DyeingSlipMast x = dyeingSlipMastDao.save(dyeingSlipMast);


            //to store the addtional slip data
            toStoreSlip.setStockId(productionPlan.getStockId());
            toStoreSlip.setName(addAdditionDyeingSlipModel.getName());
            toStoreSlip.setBatchId(productionPlan.getBatchId());
            toStoreSlip.setCreatedBy(addAdditionDyeingSlipModel.getCreatedBy());
            toStoreSlip.setUserHeadId(addAdditionDyeingSlipModel.getUserHeadId());
            toStoreSlip.setProductionId(addAdditionDyeingSlipModel.getProductionId());
            toStoreSlip.setUpdatedBy(addAdditionDyeingSlipModel.getUpdatedBy());
            toStoreSlip.setCreatedDate(new Date(System.currentTimeMillis()));
            toStoreSlip.setProcessId(x.getId());

            additionDyeingProcessSlipDao.save(toStoreSlip);
            return true;
        }catch (Exception e)
        {
            return false;
        }

    }

    public List<GetAllAdditionalSlip> getAllAddtionalDyeignSlip() throws Exception {
        List<GetAllAdditionalSlip> list =new ArrayList<>();

        List<AdditionDyeingProcessSlip> additionSlipList = additionDyeingProcessSlipDao.getAllAdditional();
        for(AdditionDyeingProcessSlip slip:additionSlipList)
        {
            GetAllAdditionalSlip getAllAdditionalSlip = new GetAllAdditionalSlip(slip);
            list.add(getAllAdditionalSlip);
        }

        if (list.isEmpty())
            throw new Exception("no data found");

        return list;
    }

    public DyeingSlipMast getAdditionalDyeingSlipById(Long id) throws Exception {

        //get the process from addtional table
        AdditionDyeingProcessSlip additionDyeingProcessSlipExist = additionDyeingProcessSlipDao.getAdditionalDyeingSlipById(id);
        if(additionDyeingProcessSlipExist==null)
            throw new Exception("no data found");

        DyeingSlipMast data = dyeingSlipMastDao.getDyeingSlipById(additionDyeingProcessSlipExist.getProcessId());

        return data;

    }
}
