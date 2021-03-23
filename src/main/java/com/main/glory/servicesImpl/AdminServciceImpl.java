package com.main.glory.servicesImpl;

import com.main.glory.Dao.admin.*;
import com.main.glory.Dao.quality.QualityNameDao;
import com.main.glory.model.admin.*;
import com.main.glory.model.dyeingSlip.DyeingSlipMast;
import com.main.glory.model.purchase.Purchase;
import com.main.glory.model.purchase.response.PurchaseResponse;
import com.main.glory.model.quality.Quality;
import com.main.glory.model.quality.QualityName;
import com.main.glory.model.user.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("adminServiceImpl")
public class AdminServciceImpl {

    @Autowired
    PurchaseImpl purchaseService;

    @Autowired
    ReceiverByDao receiverByDao;
    @Autowired
    BatchSequneceDao batchSequneceDao;

    @Autowired
    InvoiceSequenceDao invoiceSequenceDao;

    @Autowired
    QualityServiceImp qualityServiceImp;

    @Autowired
    QualityNameDao qualityNameDao;

    @Autowired
    DyeingSlipServiceImpl dyeingSlipService;

    @Autowired
    DepartmentDao departmentDao;

    @Autowired
    CompanyDao companyDao;

    @Autowired
    ApproveByDao approveByDao;

    @Autowired
    UserServiceImpl userService;


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


    public boolean deleteCompanyById(Long id) throws Exception {

            Company companyExist = companyDao.getCompanyById(id);
            if (companyExist == null)
                throw new Exception("no data found");

            //check the company is assign to user or not

            List<UserData> userData = userService.getUserByCompanyId(companyExist.getId());
            if(!userData.isEmpty())
                throw new Exception("remove the user first");

            companyDao.deleteByCompanyId(id);
            return true;

    }

    public Boolean deleteApprovedById(Long id) throws Exception {

            ApprovedBy approvedByExist = approveByDao.getApprovedById(id);
            if (approvedByExist == null)
                throw new Exception("no data found");

            List<DyeingSlipMast> dyeingSlipMasts =dyeingSlipService.getDyeingSlipByApprovedId(id);
            if(!dyeingSlipMasts.isEmpty())
                throw new Exception("can't delete this record");

            approveByDao.deleteApprovedById(id);
            return true;

    }

    public void saveDepartment(Department c) throws Exception {

        Department exist = departmentDao.getDepartmentByName(c.getName());
        if (exist != null)
            throw new Exception("department already exist");

        Department d=new Department(c);
        departmentDao.save(d);


    }

    public boolean deleteDepartmentById(Long id) throws Exception {

            Department exist = departmentDao.getDepartmentById(id);
            if (exist == null)
                throw new Exception("no data found");
            List<UserData> userDataList = userService.getAllUserByDepartmentId(exist.getId());
            if(!userDataList.isEmpty())
                throw new Exception("can't delete the department");


            departmentDao.deleteDepartmentById(id);
            return true;


    }

    public List<Department> getAllDepartmentList() {
        return departmentDao.getAllDepartment();
    }

    public Company getCompanyById(Long id) throws Exception {

        Company c= companyDao.getCompanyById(id);
        if(c==null)
            throw new Exception("no company found");

        return c;
    }

    public void updateCompany(Company company) throws Exception {
        Company companyExist = companyDao.getCompanyById(company.getId());
        if(companyExist==null) {
            throw new Exception("no data found");
        }

        //List<UserData> userDataList =userService.getAllUserByCompany(companyExist.getName());
        companyDao.save(company);


    }

    public void updateApprovedBy(ApprovedBy approvedBy) throws Exception {
        ApprovedBy approvedExist = approveByDao.getApprovedById(approvedBy.getId());
        if (approvedExist==null)
            throw new Exception("no record found");

        List<DyeingSlipMast> dyeingSlipMasts = dyeingSlipService.getDyeingSlipByApprovedId(approvedBy.getId());
        approveByDao.save(approvedBy);

        for(DyeingSlipMast dyeingSlipMast:dyeingSlipMasts)
        {
            dyeingSlipService.updateDyeingSlipWithApproveById(approvedBy.getId(),dyeingSlipMast.getId());
        }

    }

    public ApprovedBy getApprovedById(Long id) throws Exception {
        ApprovedBy approvedByExist = approveByDao.getApprovedById(id);
        if(approvedByExist==null)
            throw new Exception("no data found");
        return approvedByExist;
    }

    public Department getDepartmentById(Long id) throws Exception {
        Department department = departmentDao.getDepartmentById(id);
        if(department==null) {
            throw new Exception("no data found");
        }
        return department;
    }

    public void updateDepartment(Department department) throws Exception {
        Department departmentExist = departmentDao.getDepartmentById(department.getId());
        if(departmentExist==null)
            throw new Exception("no record found");

        departmentDao.save(department);

    }

    public Boolean getApprovedByDeletable(Long id) throws Exception {
        ApprovedBy approvedByExist = approveByDao.getApprovedById(id);
        if (approvedByExist==null)
            throw new Exception("no approvedBy found");

        List<DyeingSlipMast> dyeingSlipMasts = dyeingSlipService.getDyeingSlipByApprovedId(id);
        if(dyeingSlipMasts.isEmpty())
            return true;
        else
            return false;
    }

    public Boolean getDepartmentIsDelatable(Long id) throws Exception {
        Department departmentExist = departmentDao.getDepartmentById(id);
        if(departmentExist==null)
            throw new Exception("no department found");

        List<UserData> userDataList = userService.getAllUserByDepartmentId(departmentExist.getId());

        if(userDataList.isEmpty())
            return true;
        else
            return false;

    }

    public Boolean getCompanyIsDelatable(Long id) throws Exception {
        Company companyExist = companyDao.getCompanyById(id);
        if(companyExist==null)
            throw new Exception("company data not found");

        List<UserData> userDataList = userService.getUserByCompanyId(companyExist.getId());
        if(userDataList.isEmpty())
            return true;
        else
            return false;
    }


    public void saveQualityName(QualityName qualityName) throws Exception {
        Optional<QualityName> qualityNameExist =qualityNameDao.getQualityNameDetailByNameAndId(qualityName.getQualityName(),0l);
        if(qualityNameExist.isPresent())
            throw new Exception("quality name is already exist");

        qualityNameDao.save(qualityName);

    }

    public void updateQualityName(QualityName qualityName) throws Exception {
        Optional<QualityName> qualityNameExist = qualityNameDao.getQualityNameDetailByNameAndId(qualityName.getQualityName(), qualityName.getId());
        if(qualityNameExist.isEmpty())
            throw new Exception("quality name is already exist");

        qualityNameDao.saveAndFlush(qualityName);

    }

    public void deleteQualityNameById(Long id) throws Exception {

        Optional<QualityName> qualityNameExist = qualityNameDao.getQualityNameDetailById(id);
        if(qualityNameExist.isEmpty())
            throw new Exception("quality name is present");

        Optional<List<Quality>> qualityList = qualityServiceImp.getQualityByQualityNameId(id);
        if(qualityList.isPresent())
            throw new Exception("remove the quality data first");

        qualityNameDao.deleteQualityNameById(id);
    }

    public void addInvoiceSequence(InvoiceSequence record) throws Exception {

        InvoiceSequence invoiceSequenceExist = invoiceSequenceDao.getSequence();
        if(invoiceSequenceExist!=null)
            throw new Exception("update the existing sequence");

        invoiceSequenceDao.save(record);
    }

    public InvoiceSequence getInvoiceSequence() throws Exception {
        InvoiceSequence invoiceSequenceExist = invoiceSequenceDao.getSequence();
        if(invoiceSequenceExist==null)
            throw new Exception("No data found");
        return invoiceSequenceExist;

    }

    public void updateInvoiceSequence(InvoiceSequence invoiceSequence) throws Exception {
        InvoiceSequence invoiceSequenceExist = invoiceSequenceDao.getSequenceById(invoiceSequence.getId());
        if(invoiceSequenceExist==null)
            throw new Exception("No data found");

        if(invoiceSequenceExist.getSequence()>invoiceSequence.getSequence())
            throw new Exception("please add greater sequence than previous one");

        invoiceSequenceDao.save(invoiceSequence);
    }

    public InvoiceSequence getInvoiceSequenceById(Long id) {
        return invoiceSequenceDao.getSequenceById(id);
    }

    public void addBatchSequence(BatchSequence record) throws Exception {
        BatchSequence batchSequence = batchSequneceDao.getBatchSequence();

        if(batchSequence!=null)
            throw new Exception("batch sequence found");

        batchSequneceDao.save(record);

    }

    public BatchSequence getBatchSequence() throws Exception {
        BatchSequence batchSequence = batchSequneceDao.getBatchSequence();

        if(batchSequence==null)
            throw new Exception("no batch sequence found");
        return batchSequence;

    }

    public BatchSequence getBatchSequenceById(Long id) throws Exception {
        BatchSequence batchSequence = batchSequneceDao.getBatchSequenceById(id);
        if(batchSequence==null) {
            throw new Exception("no batch sequence found");
        }
        return batchSequence;
    }

    public BatchSequence updateBatchSequence(BatchSequence record) throws Exception {
        BatchSequence batchSequence = batchSequneceDao.getBatchSequenceById(record.getId());
        if(batchSequence==null) {
            throw new Exception("no batch sequence found");
        }


        //check is exiting batchsequence is < coming batch seqeunce

        if(batchSequence.getSequence() > record.getSequence())
            return batchSequence;


        BatchSequence batch = new BatchSequence(batchSequence,record);
        batchSequneceDao.saveAndFlush(batch);
        return batchSequneceDao.getBatchSequence();
    }

    public void addReceiver(ReceiverBy record) throws Exception {
        ReceiverBy receiverExist = receiverByDao.getReceiverByNameExceptId(record.getName(),0l);

        if(receiverExist!=null)
            throw new Exception("receiver is already exist with name");

        receiverByDao.save(record);
    }

    public void updateReceiver(ReceiverBy record) throws Exception {

        ReceiverBy receiverByExistWithName = receiverByDao.getReceiverByNameExceptId(record.getName(),record.getId());
        if(receiverByExistWithName!=null)
            throw new Exception("receiver exist with name");

        receiverByDao.save(record);

    }

    public List<ReceiverBy> getAllReceiver() {
        return receiverByDao.getAllReceiver();
    }

    public ReceiverBy getReceiverById(Long id) {
        return receiverByDao.getReceiverById(id);
    }

    public void deleteReceiverById(Long id) throws Exception {
        ReceiverBy receiverByExist = receiverByDao.getReceiverById(id);
        if(receiverByExist==null)
            throw new Exception("no record found");

        List<Purchase> list = purchaseService.getPurchaseByReceiverId(id);

        if(!list.isEmpty())
            throw new Exception("remove the purchase record first");

        receiverByDao.deleteByReceiverId(id);
    }
}
