package com.main.glory.services;

import com.main.glory.model.supplier.requestmodals.AddSupplierRateRequest;
import com.main.glory.model.supplier.Supplier;
import com.main.glory.model.supplier.requestmodals.UpdateSupplierRequest;

public interface SupplierServiceInterface {
    public Boolean addSupplier(Supplier supplier);
    public Boolean addSupplierRates(AddSupplierRateRequest addSupplierRateRequest);
    public Object getSupplier(Long id);
    public Boolean updateSupplier(UpdateSupplierRequest updateSupplierRequest);
}
