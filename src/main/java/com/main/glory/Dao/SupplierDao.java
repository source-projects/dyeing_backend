package com.main.glory.Dao;

import com.main.glory.model.supplier.Supplier;
import com.main.glory.model.supplier.SupplierRate;
import org.hibernate.annotations.Where;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

@EnableJpaRepositories
public interface SupplierDao extends JpaRepository<Supplier, Long> {
//    @Where(clause = "is_active = 1")
//    @Query(value = "SELECT s FROM supplier as s inner join supplier_rate as sr on s.id = sr.supplier_id WHERE (sr.is_active = :active and s.id = :entry_id)", nativeQuery = true)
////    @Query("")
//    Supplier findActiveById(@Param("entry_id") Long aLong, @Param("active") Boolean active);
}
