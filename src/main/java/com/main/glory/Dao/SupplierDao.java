package com.main.glory.Dao;

import com.main.glory.model.supplier.Supplier;
import com.main.glory.model.supplier.SupplierRate;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@EnableJpaRepositories
public interface SupplierDao extends JpaRepository<Supplier, Long> {
//    @Where(clause = "is_active = 1")
//    @Query(value = "SELECT s FROM supplier as s inner join supplier_rate as sr on s.id = sr.supplier_id WHERE (sr.is_active = :active and s.id = :entry_id)", nativeQuery = true)

//    @Query(value = "Select s.*, sr.* from supplier as s inner join supplier_rate as sr on s.id = sr.supplier_id WHERE sr.is_active = 1 and s.id = :entry_id", nativeQuery = true)
    @Query(value = "SELECT `id`, `created_date`, `discount_percentage`, `gst_percentage`, `payment_terms`, `remark`, `supplier_name`, `updated_by`, `updated_date`, `user_id` from supplier where id = :entry_id", nativeQuery = true)
    Supplier findByIdWithoutList(@Param("entry_id") Long aLong);

    Supplier findByIdAndIsActive(Long aLong, Boolean aBoolean);

    List<Supplier> findByIsActive(Boolean aBoolean);

    @Transactional
    @Modifying
    @Query("update Supplier s set s.isActive = 0 where s.id = :cid")
    public void setInactive(@Param("cid") Long id);
}
