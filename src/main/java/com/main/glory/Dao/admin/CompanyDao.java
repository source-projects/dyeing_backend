package com.main.glory.Dao.admin;

import com.main.glory.model.admin.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CompanyDao extends JpaRepository<Company,Long> {
    @Query("select s from Company s where LOWER(s.name)=LOWER(:name)")
    Company findByCompanyName(String name);

    @Query("select s from Company s")
    List<Company> getAllCompany();

    @Query("select c from Company c where c.id=:id")
    Company getCompanyById(Long id);

    @Transactional
    @Modifying
    @Query("delete from Company c where c.id=:id")
    void deleteByCompanyId(Long id);
}
