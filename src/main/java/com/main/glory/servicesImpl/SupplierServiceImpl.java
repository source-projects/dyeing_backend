package com.main.glory.servicesImpl;

import com.main.glory.Dao.SupplierDao;
import com.main.glory.Dao.SupplierRateDao;
import com.main.glory.model.supplier.requestmodals.AddSupplierRateRequest;
import com.main.glory.model.supplier.Supplier;
import com.main.glory.model.supplier.requestmodals.UpdateSupplierRatesRequest;
import com.main.glory.model.supplier.requestmodals.UpdateSupplierRequest;
import com.main.glory.services.SupplierServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
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


    @Override
    @Transactional
    public Boolean addSupplier(Supplier supplier){
        try{
            supplier.setCreatedDate(new Date(System.currentTimeMillis()));
            supplierDao.save(supplier);
            return true;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @Transactional
    public Boolean addSupplierRates(AddSupplierRateRequest addSupplierRateRequest) {
        try{
            Optional<Supplier> supplier = (Optional<Supplier>) supplierDao.findById(addSupplierRateRequest.getId());
            Double disc = (supplier).get().getDiscountPercentage();
            Double gst = (supplier).get().getGstPercentage();
            addSupplierRateRequest.getSupplierRates().forEach(e -> {
                e.setSupplierId(addSupplierRateRequest.getId());
                e.setCreatedDate(new Date(System.currentTimeMillis()));
                e.setDiscountedRate(e.getRate() * (1 - disc/100));
                e.setGstRate(e.getDiscountedRate() * (1 + gst/100));
            });
            supplierRateDao.saveAll(addSupplierRateRequest.getSupplierRates());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Object getSupplier(Long id) {

        try {
            Supplier s = supplierDao.findById(id).get();
            s.setSupplierRates(supplierRateDao.findBySupplierIdAndIsActive(s.getId(),true));
            return s;
        } catch (Exception e){
            return null;
        }

    }

    @Override
    @Transactional
    public Boolean updateSupplier(UpdateSupplierRequest updateSupplierRequest) {
        try{
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
    @Transactional
    public Boolean updateSupplierRates(UpdateSupplierRatesRequest updateSupplierRatesRequest) {
        try{
            Long sid = updateSupplierRatesRequest.getSupplierId();
            supplierRateDao.setInactive(sid);
            updateSupplierRatesRequest.getSupplierRates().forEach(e -> {
                e.setSupplierId(updateSupplierRatesRequest.getSupplierId());
            });
            supplierRateDao.saveAll(updateSupplierRatesRequest.getSupplierRates());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Object getAllSupplier() {
        try{
            return supplierDao.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
