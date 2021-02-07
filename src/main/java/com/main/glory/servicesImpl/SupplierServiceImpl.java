package com.main.glory.servicesImpl;

import com.main.glory.Dao.SupplierDao;
import com.main.glory.Dao.SupplierRateDao;
import com.main.glory.Dao.user.UserDao;
import com.main.glory.model.supplier.GetAllSupplierRate;
import com.main.glory.model.supplier.SupplierRate;
import com.main.glory.model.supplier.requestmodals.AddSupplierRateRequest;
import com.main.glory.model.supplier.Supplier;
import com.main.glory.model.supplier.requestmodals.UpdateSupplierRatesRequest;
import com.main.glory.model.supplier.requestmodals.UpdateSupplierRequest;
import com.main.glory.model.supplier.responce.*;
import com.main.glory.model.user.UserData;
import com.main.glory.services.SupplierServiceInterface;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service("SupplierServiceImpl")
public class SupplierServiceImpl implements SupplierServiceInterface {
    @Autowired
    SupplierDao supplierDao;

    @Autowired
    SupplierRateDao supplierRateDao;

    @Autowired
    EntityManager entityManager;
    @Autowired
    ModelMapper modelMapper;

    @Autowired
    UserDao userDao;


    @Override
    @Transactional
    public Boolean addSupplier(Supplier supplier) {
        try {
            supplierDao.save(supplier);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
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

    @Override
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

    @Override
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
            supplierDao.save(supplier);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean updateSupplierRates(UpdateSupplierRatesRequest updateSupplierRatesRequest) {
        try {
            Long sid = updateSupplierRatesRequest.getSupplierId();
            //System.out.println("_______________"+sid);
//            supplierRateDao.setInactive(sid);
            Optional<Supplier> temp = supplierDao.findById(sid);
            if (temp.isEmpty()) {
                return false;
            }
            Supplier s = temp.get();
            s.setSupplierRates(updateSupplierRatesRequest.getSupplierRates());
            supplierDao.saveAndFlush(s);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List getAllSupplier(String getBy, Long id) throws Exception {
        List s = null;
        if (id == null) {
            s = supplierDao.findAllWithoutRates();
        } else if (getBy.equals("own")) {
            s = supplierDao.findAllWithoutRatesByCreatedBy(id);
        } else if (getBy.equals("group")) {
            UserData userData = userDao.findUserById(id);

            if(userData.getUserHeadId()==0) {
                //master user
                s = supplierDao.findAllWithoutRatesByUserHeadIdAndCreatedBy(id,id);
            }
            else {
                s = supplierDao.findAllWithoutRatesByUserHeadIdAndCreatedBy(id,userData.getUserHeadId());
            }

        }

        if (s.isEmpty())
            throw new Exception("data not added yet");
        return s;
    }

    public Object getAllRates() {
        try {
            List<GetAllSupplierRate> getAllSupplierRateList = null;
            List<GetAllSupplierRatesResponse> supplierRatesResponses = null;
            getAllSupplierRateList = supplierRateDao.findWithSupplierName();
            modelMapper.getConfiguration().setAmbiguityIgnored(true);
            supplierRatesResponses = modelMapper.map(getAllSupplierRateList, List.class);
            if (supplierRatesResponses.isEmpty())
                throw new Exception("no data found");

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
            if (rateAndItemList.isEmpty())
                throw new Exception("no data found");

            return rateAndItemList;
        }
        return null;

    }

    public List<GetAllSupplierWithName> getAllSupplierName() throws Exception {
        List<GetAllSupplierWithName> s = supplierDao.findAllName();

        if (s.isEmpty())
            throw new Exception("no data found");
        return s;
    }

    public List<GetItemWithSupplier> getAllItemWithSupplierName() throws Exception {
        List<GetItemWithSupplier> getItemWithSupplierList = new ArrayList<>();

        List<ItemWithSupplier> itemWithSupplier = supplierRateDao.findAllSupplierItem();

        for (ItemWithSupplier item : itemWithSupplier) {

            if (item.getSupplierId() != null) {
                Optional<Supplier> supplier = supplierDao.findById(item.getSupplierId());
                if (supplier.isEmpty())
                    continue;

                GetItemWithSupplier getItemWithSupplier = new GetItemWithSupplier(supplier.get(), item);
                getItemWithSupplierList.add(getItemWithSupplier);
            }

        }

        if (getItemWithSupplierList.isEmpty())
            throw new Exception("no data found");

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
}
