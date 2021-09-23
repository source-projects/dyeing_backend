package com.main.glory.Dao;

import com.main.glory.model.party.Party;
import com.main.glory.model.supplier.Supplier;
import com.main.glory.model.supplier.responce.GetAllSupplierWithName;
import com.main.glory.model.supplier.responce.SupplierResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@EnableJpaRepositories
public interface SupplierDao extends JpaRepository<Supplier, Long> {

    @Query("select new com.main.glory.model.supplier.Supplier(s.id, s.supplierName, s.discountPercentage, s.gstPercentage, s.remark, s.createdBy, s.createdDate, s.updatedDate, s.paymentTerms, s.updatedBy, s.userHeadData) from Supplier s")
    List<Supplier> findAllWithoutRates();

    @Query("select new com.main.glory.model.supplier.Supplier(s.id, s.supplierName, s.discountPercentage, s.gstPercentage, s.remark, s.createdBy, s.createdDate, s.updatedDate, s.paymentTerms, s.updatedBy, s.userHeadData) from Supplier s where s.userHeadData.id = :userHeadId OR s.createdBy.id=:userHeadId")
    List<Supplier> findAllWithoutRatesByUserHeadId(Long userHeadId);

    @Query("select new com.main.glory.model.supplier.Supplier(s.id, s.supplierName, s.discountPercentage, s.gstPercentage, s.remark, s.createdBy, s.createdDate, s.updatedDate, s.paymentTerms, s.updatedBy, s.userHeadData) from Supplier s where s.createdBy.id = :createdBy")
    List<Supplier> findAllWithoutRatesByCreatedBy(Long createdBy);

    @Query("select s from Supplier s where s.id=:id")
    Optional<Supplier> findById(Long id);

    @Query("select new com.main.glory.model.supplier.responce.GetAllSupplierWithName(s.id, s.supplierName) from Supplier s")
    List<GetAllSupplierWithName> findAllName();

    @Query("select new com.main.glory.model.supplier.Supplier(s.id, s.supplierName, s.discountPercentage, s.gstPercentage, s.remark, s.createdBy, s.createdDate, s.updatedDate, s.paymentTerms, s.updatedBy, s.userHeadData) from Supplier s where s.userHeadData.id = :userHeadId OR s.createdBy.id=:id")
    List findAllWithoutRatesByUserHeadIdAndCreatedBy(Long id, Long userHeadId);


    @Query("select s from Supplier s where s.id=:id")
    Supplier findBySupplierId(Long id);

    @Query("select s from Supplier s")
    List<Supplier> getAllSupplierList();

    @Query("select s from Supplier s where s.supplierName=:name AND s.id!=:id")
    Optional<Supplier> isSupplierByName(String name, Long id);

    @Query("select s from Supplier s where s.id=:supplierId")
    Optional<Supplier> getSupplierById(Long supplierId);

    @Query("select s from Supplier s where s.createdBy.id=:id OR s.userHeadData.id=:id1")
    List<Supplier> getSupplierByCreatedAndUserHeadId(Long id, Long id1);

    @Modifying
    @Transactional
    @Query("update Supplier x set x.qualityName.id=:qualityNameId where x.id IN (:supplierIds)")
    void updateSupplierWithQualityNameId(List<Long> supplierIds, Long qualityNameId);

    @Query("select new com.main.glory.model.supplier.responce.SupplierResponse(x) from Supplier x where x.qualityName.id=:qualityNameId")
    List<SupplierResponse> getAllSupplierListByQualityNameId(Long qualityNameId);
}
