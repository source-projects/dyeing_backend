package com.main.glory.servicesImpl;

import com.main.glory.Dao.SupplierDao;
import com.main.glory.Dao.SupplierRateDao;
import com.main.glory.model.supplier.requestmodals.AddSupplierRateRequest;
import com.main.glory.model.supplier.Supplier;
import com.main.glory.model.supplier.requestmodals.UpdateSupplierRequest;
import com.main.glory.services.SupplierServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.Date;
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
            supplier.setCreated_date(new Date(System.currentTimeMillis()));
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
            Double disc = (supplier).get().getDiscount_percentage();
            Double gst = (supplier).get().getGst_percentage();
            addSupplierRateRequest.getSupplierRates().forEach(e -> {
                e.setSupplierId(addSupplierRateRequest.getId());
                e.setCreated_date(new Date(System.currentTimeMillis()));
                e.setDiscounted_rate(e.getRate() * (1 - disc/100));
                e.setGst_rate(e.getDiscounted_rate() * (1 + gst/100));
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
            supplier.setDiscount_percentage(updateSupplierRequest.getDiscount_percentage());
            supplier.setGst_percentage(updateSupplierRequest.getGst_percentage());
            supplier.setPayment_terms(updateSupplierRequest.getPayment_terms());
            supplier.setRemark(updateSupplierRequest.getRemark());
            supplier.setSupplier_name(updateSupplierRequest.getSupplier_name());
            supplier.setUpdated_date(new Date(System.currentTimeMillis()));
            supplier.setUpdated_by(updateSupplierRequest.getUpdated_by());
            supplierDao.save(supplier);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
