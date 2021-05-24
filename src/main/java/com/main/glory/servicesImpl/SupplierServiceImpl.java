package com.main.glory.servicesImpl;

import com.main.glory.Dao.SupplierDao;
import com.main.glory.Dao.SupplierRateDao;
import com.main.glory.Dao.quality.QualityNameDao;
import com.main.glory.Dao.user.UserDao;
import com.main.glory.model.ConstantFile;
import com.main.glory.model.color.ColorData;
import com.main.glory.model.dyeingProcess.DyeingChemicalData;
import com.main.glory.model.dyeingSlip.DyeingSlipItemData;
import com.main.glory.model.dyeingSlip.responce.ItemListForDirectDyeing;
import com.main.glory.model.quality.QualityName;
import com.main.glory.model.shade.ShadeData;
import com.main.glory.model.supplier.GetAllSupplierRate;
import com.main.glory.model.supplier.SupplierRate;
import com.main.glory.model.supplier.requestmodals.AddSupplierRateRequest;
import com.main.glory.model.supplier.Supplier;
import com.main.glory.model.supplier.requestmodals.UpdateSupplierRatesRequest;
import com.main.glory.model.supplier.requestmodals.UpdateSupplierRequest;
import com.main.glory.model.supplier.responce.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.*;

@Service("SupplierServiceImpl")
public class SupplierServiceImpl {


    @Autowired
    QualityNameDao qualityNameDao;

    @Autowired
    SupplierDao supplierDao;

    @Autowired
    ColorServiceImpl colorService;

    @Autowired
    DyeingSlipServiceImpl dyeingSlipService;

    @Autowired
    ShadeServiceImpl shadeService;

    @Autowired
    DyeingProcessServiceImpl dyeingProcessService;

    @Autowired
    SupplierRateDao supplierRateDao;

    @Autowired
    EntityManager entityManager;
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    UserDao userDao;


    
    @Transactional
    public Boolean addSupplier(Supplier supplier,String id) {
        try {
            supplierDao.save(supplier);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    
    public Boolean isUniqueName(Long id, String name) {
        Optional<Supplier> s = supplierDao.isSupplierByName(name, id);
        if(s.isPresent())
            return true;
        else
            return false;
    }


    
    public Boolean addSupplierRates(AddSupplierRateRequest addSupplierRateRequest) {
        try {
            Supplier supplierExist = supplierDao.findBySupplierId(addSupplierRateRequest.getId());
            if(supplierExist==null)
                throw new Exception("no supplier found");

            List<SupplierRate> list=new ArrayList<>();
            for(SupplierRate s: addSupplierRateRequest.getSupplierRates())
            {
                SupplierRate supplierRate=new SupplierRate(s);
                list.add(supplierRate);

            }
            supplierRateDao.saveAll(list);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    
    public Optional<Supplier> getSupplier(Long id) {
        try {
//            Supplier s = supplierDao.findById(id).get();
            //           return s;
            return supplierDao.findById(id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    
    @Transactional
    public Boolean updateSupplier(UpdateSupplierRequest updateSupplierRequest) {
        try {
            //System.out.println(updateSupplierRequest.getId());
            Supplier supplier = ((Optional<Supplier>) supplierDao.findById(updateSupplierRequest.getId())).get();
            if (supplier == null) {
                return false;
            }

            supplier.setDiscountPercentage(updateSupplierRequest.getDiscountPercentage());
            supplier.setGstPercentage(updateSupplierRequest.getGstPercentage());
            supplier.setPaymentTerms(updateSupplierRequest.getPaymentTerms());
            supplier.setRemark(updateSupplierRequest.getRemark());
            supplier.setSupplierName(updateSupplierRequest.getSupplierName());
            supplier.setUpdatedDate(new Date(System.currentTimeMillis()));
            supplier.setUpdatedBy(updateSupplierRequest.getUpdatedBy());

            List<SupplierRate> list=supplierRateDao.getAllSupplierRateBySupplierId(updateSupplierRequest.getId());
            supplierDao.save(supplier);
            for(SupplierRate s:list)
            {
                supplierRateDao.updateItemBySupplierIdAndItemId(supplier.getId(),s.getId());
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    
    public void updateSupplierRates(UpdateSupplierRatesRequest updateSupplierRatesRequest) throws Exception {

    }


    public void updateSupplierRatesWithValidation(UpdateSupplierRatesRequest updateSupplierRatesRequest) throws Exception{

            Long sid = updateSupplierRatesRequest.getSupplierId();
            //System.out.println("_______________"+sid);

            List<SupplierRate> existingSupplierRateList = supplierRateDao.getAllSupplierRateBySupplierId(updateSupplierRatesRequest.getSupplierId());
            List<Long> existingItemId=new ArrayList<>();
            List<Long> comingItemList=new ArrayList<>();
            Map<Long,Boolean> toDeleteItem = new HashMap<>();

            for(SupplierRate supplierRate:updateSupplierRatesRequest.getSupplierRates())
            {
                comingItemList.add(supplierRate.getId());
            }

            for(SupplierRate supplierRate:existingSupplierRateList)
            {
                existingItemId.add(supplierRate.getId());
                toDeleteItem.put(supplierRate.getId(),false);
            }


            //now check the coming list is contain the record that are already availble in dyeing slip,process or shade or color data or not

            for(Long e :existingItemId)
            {
                //check in shade that the item is available in coming item list or not

                if(!comingItemList.contains(e))
                {
                    //if coming list not containe the record of existing data then check in
                    List<DyeingChemicalData> chemicalDataList = dyeingProcessService.getDyeingProcessChemicalDataByItemId(e);
                    if(!chemicalDataList.isEmpty())
                        throw new Exception(ConstantFile.DyeingProcess_Data_Exist);

                    List<ShadeData> shadeData = shadeService.getShadDataByItemId(e);
                    if(!shadeData.isEmpty())
                        throw new Exception(ConstantFile.Shade_Exist);

                    List<DyeingSlipItemData> dyeingSlipDataList = dyeingSlipService.getDyeingItemDataByItemId(e);
                    if(!dyeingSlipDataList.isEmpty())
                        throw new Exception(ConstantFile.Dyeing_Slip_Data_Exist);

                    List<ColorData> colorData =colorService.colorDataDao.getAllColorDataByItemId(e);
                    if(!colorData.isEmpty())
                        throw new Exception(ConstantFile.Color_Data_Exist);

                    //change the hash map values for delete the item later because the item is not coming from
                    toDeleteItem.replace(e,true);

                }



            }

        // using for-each loop for iteration over Map.entrySet()
        for (Map.Entry<Long,Boolean> entry : toDeleteItem.entrySet())
        {
            if(entry.getValue())
            {
                //delete if the flag is true
                supplierRateDao.deleteItemById(entry.getKey());
            }
        }


        Supplier temp = supplierDao.findBySupplierId(sid);

            if(temp!=null) {
                Supplier s = temp;
                s.setSupplierRates(updateSupplierRatesRequest.getSupplierRates());
                supplierDao.saveAndFlush(s);
            }


    }

    
    public List getAllSupplier(String getBy, Long id) throws Exception {
        List s = null;
        /*if (id == null) {
            s = supplierDao.findAllWithoutRates();
        } else if (getBy.equals("own")) {
            s = supplierDao.findAllWithoutRatesByCreatedBy(id);
        } else if (getBy.equals("group")) {
            UserData userData = userDao.findUserById(id);

            if(userData.getUserHeadId().equals(userData.getId())) {
                //master user
                s = supplierDao.findAllWithoutRatesByUserHeadIdAndCreatedBy(id,id);
            }
            else {
                UserData userOperator = userDao.getUserById(id);
                s = supplierDao.findAllWithoutRatesByUserHeadIdAndCreatedBy(userOperator.getUserHeadId(),userOperator.getUserHeadId());
            }

        }*/
/*
        if (s.isEmpty())
            throw new Exception("data not added yet");*/
        s = supplierDao.findAllWithoutRates();
        return s;
    }

    public Object getAllRates() {
        try {
            List<GetAllSupplierRate> getAllSupplierRateList = null;
            List<GetAllSupplierRatesResponse> supplierRatesResponses = null;
            getAllSupplierRateList = supplierRateDao.findWithSupplierName();
            modelMapper.getConfiguration().setAmbiguityIgnored(true);
            supplierRatesResponses = modelMapper.map(getAllSupplierRateList, List.class);
            /*if (supplierRatesResponses.isEmpty())
                throw new Exception("no data found");*/

            return supplierRatesResponses;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getSupplierName(Long id) {
        Optional<Supplier> data = supplierDao.findById(id);
        return data.get().getSupplierName();
    }

    public List<RateAndItem> getSupplierItemAndRate(Long id) throws Exception {
        Optional<Supplier> supplier = supplierDao.findById(id);
        if (supplier.isPresent()) {
            List<RateAndItem> rateAndItemList = new ArrayList<>();

            List<SupplierRate> supplierRateList = supplierRateDao.findBySupplierId(id);
            for (SupplierRate supplierRate : supplierRateList) {
                RateAndItem rateAndItem = new RateAndItem(supplierRate);
                rateAndItemList.add(rateAndItem);
            }
            /*if (rateAndItemList.isEmpty())
                throw new Exception("no data found");*/

            return rateAndItemList;
        }
        return null;

    }

    public List<GetAllSupplierWithName> getAllSupplierName() throws Exception {
        List<GetAllSupplierWithName> s = supplierDao.findAllName();

       /* if (s.isEmpty())
            throw new Exception(CommonMessage.Supplier_Not_Found);*/
        return s;
    }

    public List<GetItemWithSupplier> getAllItemWithSupplierName() throws Exception {
        List<GetItemWithSupplier> getItemWithSupplierList = new ArrayList<>();

        List<ItemWithSupplier> itemWithSupplier = supplierRateDao.findAllSupplierItem();

        for (ItemWithSupplier item : itemWithSupplier) {

            if (item.getSupplierId() != null) {
                Supplier supplier = supplierDao.findBySupplierId(item.getSupplierId());
                if (supplier==null)
                    continue;

                SupplierRate supplierRate = supplierRateDao.getSupplierRateByItemId(item.getId());

                GetItemWithSupplier getItemWithSupplier = new GetItemWithSupplier(supplier, item,supplierRate);
                getItemWithSupplierList.add(getItemWithSupplier);
            }

        }

        /*if (getItemWithSupplierList.isEmpty())
            throw new Exception("no data found");*/

        return getItemWithSupplierList;


    }

    public Optional<SupplierRate> getItemById(Long itemId) {
        return supplierRateDao.findById(itemId);
    }

    public String getSupplierNameByItemId(Long itemId) throws Exception {
        String name;
        name = supplierRateDao.getSupplierNameByItemId(itemId);
        if (name.isEmpty())
            throw new Exception("no supplier name found for given item");
        return name;
    }

    public Supplier getSupplierByItemId(Long itemId) {
        Supplier supplier = supplierRateDao.getSupplierByItemId(itemId);
        return supplier;
    }

    //get all color item
    public List<SupplierRate> getAllColorItemBySupplierId(Long supplierId) throws Exception {

        Supplier supplierExist = supplierDao.findBySupplierId(supplierId);

        if(supplierExist==null)
            throw new Exception("no supplier found");

        List<SupplierRate> colorItemList = supplierRateDao.findItemBySupplierId(supplierId,"Color");
        if(colorItemList.isEmpty())
            throw new Exception("no color item found");

       return colorItemList;
    }

    //getAllChemical List
    public List<SupplierRate> getAllChemicalItemBySupplierId(Long supplierId) throws Exception {

        Supplier supplierExist = supplierDao.findBySupplierId(supplierId);

        if(supplierExist==null)
            throw new Exception("no supplier found");

        List<SupplierRate> chemicalItemList = supplierRateDao.findItemBySupplierId(supplierId,"Chemical");
        if(chemicalItemList.isEmpty())
            throw new Exception("no color item found");

        return chemicalItemList;
    }

    public SupplierRate getSupplierRateByItemId(Long supplierItemId) {
        SupplierRate supplierRate = supplierRateDao.getSupplierRateByItemId(supplierItemId);
        return supplierRate;
    }

    public ItemListForDirectDyeing getSupplierWithItemByItemId(Long supplierItemId) {
        return supplierRateDao.getSupplierWithItemByItemId(supplierItemId) ;
    }

    public List<Supplier> getSupplierByCreatedAndUserHeadId(Long id, Long id1) {
        return supplierDao.getSupplierByCreatedAndUserHeadId(id,id1);
    }

    public Boolean supplierRateExist(String name, Long id) throws Exception {
        if(name==null || id==null)
            throw new Exception(ConstantFile.Null_Record_Passed);

        SupplierRate rate =  supplierRateDao.getSupplierRateByNameAndExceptId(name,id);
        if(rate!=null)
            return true;
        else
            return false;

    }

    public List<SupplierRate> getAllRatesByQualityNameId(Long qualityNameId) throws Exception {
        //check that the quality name exist or not

        Optional<QualityName> qualityNameExist = qualityNameDao.getQualityNameDetailById(qualityNameId);
        if(qualityNameExist.isEmpty())
            throw new Exception(ConstantFile.Quality_Name_Not_Exist);

        return supplierRateDao.getAllItemByQualityNameId(qualityNameId);

    }
}
