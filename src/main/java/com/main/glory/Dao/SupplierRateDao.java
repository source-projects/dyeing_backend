package com.main.glory.Dao;

import com.main.glory.model.supplier.GetAllSupplierRate;
import com.main.glory.model.supplier.SupplierRate;
import com.main.glory.model.supplier.responce.GetItemWithSupplier;
import com.main.glory.model.supplier.responce.ItemWithSupplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;


public interface SupplierRateDao extends JpaRepository<SupplierRate, Long> {
    List<SupplierRate> findBySupplierId(Long aLong);
    @Query("Select new com.main.glory.model.supplier.GetAllSupplierRate(q, (Select p.supplierName from Supplier p where p.id = q.supplierId)) from SupplierRate q where supplierId IS NOT NULL")
    List<GetAllSupplierRate> findWithSupplierName();


    @Query("Select new com.main.glory.model.supplier.responce.ItemWithSupplier(q.id as id,q.itemName as itemName,q.supplierId as supplierId,SUM(q.rate) as RATE) from SupplierRate q where supplierId IS NOT NULL GROUP BY  id,supplierId")
    List<ItemWithSupplier> findAllSupplierItem();
}
