package com.main.glory.Dao.admin;

import com.main.glory.model.admin.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CompanyDao extends JpaRepository<Company,Long> {
    @Query("select s from Company s where LOWER(s.name)=LOWER(:name)")
    Company findByCompanyName(String name);

    @Query("select s from Company s")
    List<Company> getAllCompany();
}
