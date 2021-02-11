package com.main.glory.servicesImpl;

import com.main.glory.Dao.admin.ApproveByDao;
import com.main.glory.Dao.admin.CompanyDao;
import com.main.glory.model.admin.ApprovedBy;
import com.main.glory.model.admin.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("adminServiceImpl")
public class AdminServciceImpl {


    @Autowired
    CompanyDao companyDao;

    @Autowired
    ApproveByDao approveByDao;


    public void saveCompanyName(String name) throws Exception {

        Company companyExist = companyDao.findByCompanyName(name);
        if(companyExist!=null)
            throw new Exception("company name is already exist");

        Company company=new Company(name);

        companyDao.save(company);


    }

    public void saveApprovedBy(String name) throws Exception {

        ApprovedBy approvedBy = approveByDao.findByApprovedByName(name);
        if(approvedBy!=null)
            throw new Exception("already data exist");

        ApprovedBy approvedBy1 =new ApprovedBy(name);
        approveByDao.save(approvedBy1);
    }

    public List<ApprovedBy> getApprovedByList() {
        return approveByDao.getAll();
    }

    public List<Company> getAllCompany() {
        return companyDao.getAllCompany();
    }
}
