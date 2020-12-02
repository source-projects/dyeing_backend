package com.main.glory.servicesImpl;

import com.main.glory.Dao.SupplierDao;
import com.main.glory.Dao.SupplierRateDao;
import com.main.glory.model.supplier.GetAllSupplierRate;
import com.main.glory.model.supplier.SupplierRate;
import com.main.glory.model.supplier.requestmodals.AddSupplierRateRequest;
import com.main.glory.model.supplier.Supplier;
import com.main.glory.model.supplier.requestmodals.UpdateSupplierRatesRequest;
import com.main.glory.model.supplier.requestmodals.UpdateSupplierRequest;
import com.main.glory.model.supplier.responce.GetAllSupplierRatesResponse;
import com.main.glory.model.supplier.responce.GetSupplierWithRateAndItem;
import com.main.glory.model.supplier.responce.RateAndItem;
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


    @Override
    @Transactional
    public Boolean addSupplier(Supplier supplier){
        try{
            supplierDao.save(supplier);
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Boolean addSupplierRates(AddSupplierRateRequest addSupplierRateRequest) {
        try{
            Optional<Supplier> supplierOptional = (Optional<Supplier>) supplierDao.findById(addSupplierRateRequest.getId());
            if(!supplierOptional.isPresent()){
                return false;
            }
            Supplier supplier = supplierOptional.get();
            Double disc = (supplier).getDiscountPercentage();
            Double gst = (supplier).getGstPercentage();
            addSupplierRateRequest.getSupplierRates().forEach(e -> {
                e.setDiscountedRate(e.getRate() * (1 - disc/100));
                e.setGstRate(e.getDiscountedRate() * (1 + gst/100));
                supplier.getSupplierRates().add(e);
            });
            supplierDao.saveAndFlush(supplier);
//            supplierRateDao.saveAll(addSupplierRateRequest.getSupplierRates());
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
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    @Override
    @Transactional
    public Boolean updateSupplier(UpdateSupplierRequest updateSupplierRequest) {
        try{
            //System.out.println(updateSupplierRequest.getId());
            Supplier supplier = ((Optional<Supplier>) supplierDao.findById(updateSupplierRequest.getId())).get();
            if(supplier == null){
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
        try{
            Long sid = updateSupplierRatesRequest.getSupplierId();
            //System.out.println("_______________"+sid);
//            supplierRateDao.setInactive(sid);
            Optional<Supplier> temp = supplierDao.findById(sid);
            if(temp.isEmpty()){
                return false;
            }
            Supplier s = temp.get();
            Double disc = (s).getDiscountPercentage();
            Double gst = (s).getGstPercentage();
            updateSupplierRatesRequest.getSupplierRates().forEach(e -> {
                e.setDiscountedRate(e.getRate() * (1 - disc/100));
                e.setGstRate(e.getDiscountedRate() * (1 + gst/100));
            });
            s.setSupplierRates(updateSupplierRatesRequest.getSupplierRates());
            supplierDao.saveAndFlush(s);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List getAllSupplier(String getBy, Long id) {
        List s = null;
        if(id == null){
            s = supplierDao.findAllWithoutRates();
        }
        else if(getBy.equals("own")){
            s = supplierDao.findAllWithoutRatesByCreatedBy(id);
        }
        else if(getBy.equals("group")){
            s = supplierDao.findAllWithoutRatesByUserHeadId(id);
        }
        return s;
    }

	public Object getAllRates() {
	    try{
	        List<GetAllSupplierRate> getAllSupplierRateList=null;
	        List<GetAllSupplierRatesResponse> supplierRatesResponses=null;
	        getAllSupplierRateList = supplierRateDao.findWithSupplierName();
            modelMapper.getConfiguration().setAmbiguityIgnored(true);
            supplierRatesResponses = modelMapper.map(getAllSupplierRateList, List.class);
	        return supplierRatesResponses;
        } catch (Exception e){
	        e.printStackTrace();
	        return false;
        }
    }

    public String getSupplierName(Long id) {
        Optional<Supplier> data = supplierDao.findById(id);
        return data.get().getSupplierName();
    }

    public List<RateAndItem> getSupplierItemAndRate(Long id) {
        Optional<Supplier> supplier = supplierDao.findById(id);
        if(supplier.isPresent())
        {
            List<RateAndItem> rateAndItemList =new ArrayList<>();

            List<SupplierRate> supplierRateList = supplierRateDao.findBySupplierId(id);
            for(SupplierRate supplierRate : supplierRateList)
            {
                RateAndItem rateAndItem = new RateAndItem(supplierRate);
                rateAndItemList.add(rateAndItem);
            }

            return rateAndItemList;
        }
        return null;

    }
}
