package com.main.glory.servicesImpl;

import com.main.glory.Dao.admin.ApproveByDao;
import com.main.glory.Dao.admin.CompanyDao;
import com.main.glory.model.admin.ApprovedBy;
import com.main.glory.model.admin.Company;
import com.main.glory.model.jet.request.AddJet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("adminServiceImpl")
public class AdminServciceImpl {



    @Autowired
    CompanyDao companyDao;

    @Autowired
    ApproveByDao approveByDao;


    public void saveCompanyName(Company company) throws Exception {

        Company companyExist = companyDao.findByCompanyName(company.getName());
        System.out.println(company.getId());
        if(companyExist!=null)
            throw new Exception("company name is already exist");

        Company companyX=new Company(company.getName());

        companyDao.save(companyX);


    }

    public void saveApprovedBy(ApprovedBy data) throws Exception {

        ApprovedBy approvedBy = approveByDao.findByApprovedByName(data.getName());
        if(approvedBy!=null)
            throw new Exception("already data exist");
        approveByDao.save(data);
    }

    public List<ApprovedBy> getApprovedByList() {
        return approveByDao.getAll();
    }

    public List<Company> getAllCompany() {
        return companyDao.getAllCompany();
    }


    public boolean deleteCompanyById(Long id) {
        try {
            Company companyExist = companyDao.getCompanyById(id);
            if (companyExist == null)
                throw new Exception("no data found");

            companyDao.deleteByCompanyId(id);
            return true;
        }catch (Exception e)
        {
            return false;
        }
    }

    public Boolean deleteApprovedById(Long id) {
        try {
            ApprovedBy approvedByExist = approveByDao.getApprovedById(id);
            if (approvedByExist == null)
                throw new Exception("no data found");

            approveByDao.deleteApprovedById(id);
            return true;


        }
        catch (Exception e)
        {
            return false;
        }
    }
}
