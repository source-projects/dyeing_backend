package com.main.glory.servicesImpl;

import com.main.glory.Dao.admin.*;
import com.main.glory.Dao.quality.QualityNameDao;
import com.main.glory.Dao.task.ReportTypeDao;
import com.main.glory.model.ConstantFile;
import com.main.glory.model.admin.*;
import com.main.glory.model.admin.request.DepartmentResponse;
import com.main.glory.model.dyeingSlip.DyeingSlipMast;
import com.main.glory.model.purchase.Purchase;
import com.main.glory.model.quality.Quality;
import com.main.glory.model.quality.QualityName;
import com.main.glory.model.task.ReportType;
import com.main.glory.model.task.TaskMast;
import com.main.glory.model.user.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("adminServiceImpl")
public class AdminServciceImpl {

    @Autowired
    AuthorizeDao authorizeDao;

    @Autowired
    TaskServiceImpl taskService;

    @Autowired
    ReportTypeDao reportTypeDao;

    @Autowired
    PurchaseImpl purchaseService;

   /* @Autowired
    ReceiverByDao receiverByDao;*/
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

   /* @Autowired
    ApproveByDao approveByDao;
*/
    @Autowired
    UserServiceImpl userService;

    ConstantFile constantFile;

    public void saveCompanyName(Company company) throws Exception {

        Company companyExist = companyDao.findByCompanyName(company.getName());
        System.out.println(company.getId());
        if(companyExist!=null)
            throw new Exception(constantFile.Company_Name_Exist);

        Company companyX=new Company(company.getName());

        companyDao.save(companyX);


    }

   /* public void saveApprovedBy(ApprovedBy data) throws Exception {

        ApprovedBy approvedBy = approveByDao.findByApprovedByName(data.getName());
        if(approvedBy!=null)
            throw new Exception("already data exist");
        approveByDao.save(data);
    }
*/
    public List<Authorize> getApprovedByList() {
        return authorizeDao.getAllAuthorizeByType("approve");
    }

    public List<Company> getAllCompany() {
        return companyDao.getAllCompany();
    }


    public boolean deleteCompanyById(Long id) throws Exception {

            Company companyExist = companyDao.getCompanyById(id);
            if (companyExist == null)
                throw new Exception(constantFile.Company_Not_Found);

            //check the company is assign to user or not

            List<UserData> userData = userService.getUserByCompanyId(companyExist.getId());
            if(!userData.isEmpty())
                throw new Exception(constantFile.User_Exist);

            companyDao.deleteByCompanyId(id);
            return true;

    }

    public void saveDepartment(Department c) throws Exception {

        Department exist = departmentDao.getDepartmentByName(c.getName());
        if (exist != null)
            throw new Exception(constantFile.Department_Name_Exist);

        Department d=new Department(c);
      /*  if(c.getIsMaster()==null)
            c.setIsMaster(false);*/

        departmentDao.save(d);


    }

    /*public Boolean deleteApprovedById(Long id) throws Exception {

        ApprovedBy approvedByExist = approveByDao.getApprovedById(id);
        if (approvedByExist == null)
            throw new Exception("no data found");

        List<DyeingSlipMast> dyeingSlipMasts =dyeingSlipService.getDyeingSlipByApprovedId(id);
        if(!dyeingSlipMasts.isEmpty())
            throw new Exception("can't delete this record");

        approveByDao.deleteApprovedById(id);
        return true;

    }*/

    public boolean deleteDepartmentById(Long id) throws Exception {

            Department exist = departmentDao.getDepartmentById(id);
            if (exist == null)
                throw new Exception(constantFile.Department_Not_Found);
            List<UserData> userDataList = userService.getAllUserByDepartmentId(exist.getId());
            if(!userDataList.isEmpty())
                throw new Exception(constantFile.User_Exist);


            departmentDao.deleteDepartmentById(id);
            return true;


    }

    public List<Department> getAllDepartmentList() {
        return departmentDao.getAllDepartment();
    }

    public Company getCompanyById(Long id) throws Exception {

        Company c= companyDao.getCompanyById(id);
        if(c==null)
            throw new Exception(constantFile.Company_Not_Found);

        return c;
    }

    public void updateCompany(Company company) throws Exception {
        Company companyExist = companyDao.getCompanyById(company.getId());
        if(companyExist==null) {
            throw new Exception(constantFile.Company_Not_Found);
        }

        //List<UserData> userDataList =userService.getAllUserByCompany(companyExist.getName());
        companyDao.save(company);


    }

   /* public void updateApprovedBy(ApprovedBy approvedBy) throws Exception {
        ApprovedBy approvedExist = approveByDao.getApprovedById(approvedBy.getId());
        if (approvedExist==null)
            throw new Exception("no record found");

        List<DyeingSlipMast> dyeingSlipMasts = dyeingSlipService.getDyeingSlipByApprovedId(approvedBy.getId());
        approveByDao.save(approvedBy);

        for(DyeingSlipMast dyeingSlipMast:dyeingSlipMasts)
        {
            dyeingSlipService.updateDyeingSlipWithApproveById(approvedBy.getId(),dyeingSlipMast.getId());
        }

    }*/

   /* public ApprovedBy getApprovedById(Long id) throws Exception {
        ApprovedBy approvedByExist = approveByDao.getApprovedById(id);
        if(approvedByExist==null)
            throw new Exception("no data found");
        return approvedByExist;
    }*/

    public DepartmentResponse getDepartmentById(Long id) throws Exception {
        DepartmentResponse department = departmentDao.getDepartmentResponseById(id);
        if(department==null) {
            throw new Exception(constantFile.Department_Not_Found);
        }
        return department;
    }

    public void updateDepartment(Department department) throws Exception {
        Department departmentExist = departmentDao.getDepartmentById(department.getId());
        if(departmentExist==null)
            throw new Exception(constantFile.Department_Not_Found);

        departmentDao.save(department);

    }

    /*public Boolean getApprovedByDeletable(Long id) throws Exception {
        ApprovedBy approvedByExist = approveByDao.getApprovedById(id);
        if (approvedByExist==null)
            throw new Exception("no approvedBy found");

        List<DyeingSlipMast> dyeingSlipMasts = dyeingSlipService.getDyeingSlipByApprovedId(id);
        if(dyeingSlipMasts.isEmpty())
            return true;
        else
            return false;
    }*/

    public Boolean getDepartmentIsDelatable(Long id) throws Exception {
        Department departmentExist = departmentDao.getDepartmentById(id);
        if(departmentExist==null)
            throw new Exception(constantFile.Department_Not_Found);

        List<UserData> userDataList = userService.getAllUserByDepartmentId(departmentExist.getId());

        if(userDataList.isEmpty())
            return true;
        else
            return false;

    }

    public Boolean getCompanyIsDelatable(Long id) throws Exception {
        Company companyExist = companyDao.getCompanyById(id);
        if(companyExist==null)
            throw new Exception(constantFile.Company_Not_Found);

        List<UserData> userDataList = userService.getUserByCompanyId(companyExist.getId());
        if(userDataList.isEmpty())
            return true;
        else
            return false;
    }


    public void saveQualityName(QualityName qualityName) throws Exception {
        Optional<QualityName> qualityNameExist =qualityNameDao.getQualityNameDetailByNameAndId(qualityName.getQualityName(),0l);
        if(qualityNameExist.isPresent())
            throw new Exception(constantFile.Quality_Name_Exist);

        qualityNameDao.save(qualityName);

    }

    public void updateQualityName(QualityName qualityName) throws Exception {
        Optional<QualityName> qualityNameExist = qualityNameDao.getQualityNameDetailByNameAndId(qualityName.getQualityName(), qualityName.getId());
        if(qualityNameExist.isEmpty())
            throw new Exception(constantFile.Quality_Name_Exist);

        qualityNameDao.saveAndFlush(qualityName);

    }

    public void deleteQualityNameById(Long id) throws Exception {

        Optional<QualityName> qualityNameExist = qualityNameDao.getQualityNameDetailById(id);
        if(qualityNameExist.isEmpty())
            throw new Exception(constantFile.Quality_Name_Not_Exist);

        Optional<List<Quality>> qualityList = qualityServiceImp.getQualityByQualityNameId(id);
        if(qualityList.isPresent())
            throw new Exception(constantFile.Quality_Data_Exist);

        qualityNameDao.deleteQualityNameById(id);
    }

    public void addInvoiceSequence(InvoiceSequence record) throws Exception {

        InvoiceSequence invoiceSequenceExist = invoiceSequenceDao.getSequence();
        if(invoiceSequenceExist!=null)
            throw new Exception(constantFile.Invoice_Sequence_Exist);

        invoiceSequenceDao.save(record);
    }

    public InvoiceSequence getInvoiceSequence() throws Exception {
        InvoiceSequence invoiceSequenceExist = invoiceSequenceDao.getSequence();
       /* if(invoiceSequenceExist==null)
            throw new Exception(commonMessage.Invoice_Sequence_Not_Found);*/
        return invoiceSequenceExist;

    }

    public void updateInvoiceSequence(InvoiceSequence invoiceSequence) throws Exception {
        InvoiceSequence invoiceSequenceExist = invoiceSequenceDao.getSequenceById(invoiceSequence.getId());
        if(invoiceSequenceExist==null)
            throw new Exception(constantFile.Invoice_Sequence_Not_Found);

        if(invoiceSequenceExist.getSequence()>invoiceSequence.getSequence())
            throw new Exception(constantFile.Invoice_Sequence_Greater);

        invoiceSequenceDao.save(invoiceSequence);
    }

    public InvoiceSequence getInvoiceSequenceById(Long id) {
        return invoiceSequenceDao.getSequenceById(id);
    }

    public void addBatchSequence(BatchSequence record) throws Exception {
        BatchSequence batchSequence = batchSequneceDao.getBatchSequence();

        if(batchSequence!=null)
            throw new Exception(constantFile.Batch_Sequence_Exist);

        batchSequneceDao.save(record);

    }

    public BatchSequence getBatchSequence(Boolean update) throws Exception {
        BatchSequence batchSequence = batchSequneceDao.getBatchSequence();
            if(batchSequence==null)
                return null;

        if(update==true) {
            batchSequence.setSequence(batchSequence.getSequence() + 1);
        }
        BatchSequence x= batchSequneceDao.saveAndFlush(batchSequence);
        return x;

    }

    public BatchSequence getBatchSequenceById(Long id) throws Exception {
        BatchSequence batchSequence = batchSequneceDao.getBatchSequenceById(id);
        if(batchSequence==null) {
            throw new Exception(constantFile.Batch_Sequence_Found);
        }
        return batchSequence;
    }

    public BatchSequence updateBatchSequence(BatchSequence record) throws Exception {
        BatchSequence batchSequence = batchSequneceDao.getBatchSequenceById(record.getId());
        if(batchSequence==null) {
            throw new Exception(constantFile.Batch_Sequence_Not_Found);
        }


        //check is exiting batchsequence is < coming batch seqeunce

        if(record.getSequence() < batchSequence.getSequence())
            throw new Exception(constantFile.Batch_Sequence_Greater);


        BatchSequence batch = new BatchSequence(batchSequence,record);
        batchSequneceDao.saveAndFlush(batch);
        return batchSequneceDao.getBatchSequence();
    }

   /* public void addReceiver(ReceiverBy record) throws Exception {
        ReceiverBy receiverExist = receiverByDao.getReceiverByNameExceptId(record.getName(),0l);

        if(receiverExist!=null)
            throw new Exception("receiver is already exist with name");

        receiverByDao.save(record);
    }*/

    /*public void updateReceiver(ReceiverBy record) throws Exception {

        ReceiverBy receiverByExistWithName = receiverByDao.getReceiverByNameExceptId(record.getName(),record.getId());
        if(receiverByExistWithName!=null)
            throw new Exception("receiver exist with name");

        receiverByDao.save(record);

    }*/

    public List<Authorize> getAllReceiver() {
        return authorizeDao.getAllAuthorizeByType("receive");
    }
/*
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
    }*/

    public void addReportType(ReportType record) throws Exception {
        ReportType reportTypeExist = reportTypeDao.getReportFormExist(record.getFormName());
        if(reportTypeExist!=null)
            throw new Exception(constantFile.Report_Type_Exist);
        reportTypeExist = new ReportType(record);

        reportTypeDao.save(reportTypeExist);
    }

    public List<ReportType> getAllReportType() {
        return reportTypeDao.getAllReportType();
    }

    public ReportType getReportTypeById(Long id) {
        return reportTypeDao.getReportTypeById(id);
    }

    public boolean updateReportType(ReportType record) {
        try {
            ReportType reportTypExist = reportTypeDao.getReportTypeById(record.getId());

            if (reportTypExist == null) {
                return false;
            }
            else
            {
                reportTypeDao.saveAndFlush(record);
                return true;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;

    }

    public void deleteReportTypeById(Long id) throws Exception {
        ReportType reportTypeExist = reportTypeDao.getReportTypeById(id);
        if(reportTypeExist==null)
            throw new Exception(constantFile.Report_Type_Not_Found);

        //check the report is assigned to any task or not
        List<TaskMast> taskMastListByReport = taskService.getTaskByReportId(id);
        if(!taskMastListByReport.isEmpty())
            throw new Exception(constantFile.Task_Data_Exist);

        reportTypeDao.deleteReportTypeById(id);


    }

    public List<DepartmentResponse> getAllDepartmentListByHeaderId(String id) {

        UserData userData = userService.getUserById(Long.parseLong(id));
        if(userData.getUserHeadId()==0)
        {
            return departmentDao.getDepartmentResponse();
        }
        else {

            return departmentDao.getDepartmentResponseByUserId(userData.getId());
        }
    }

    public void addAuthorize(Authorize authorize) throws Exception {
        //check the authorize exist with same name
        Authorize authorizeExist=authorizeDao.getAuthorizeByName(authorize.getName());
        if(authorizeExist!=null)
            throw new Exception(constantFile.Authorize_Name_Exist);

        authorizeDao.save(authorize);
    }

    public void updateAuthorize(Authorize authorize) throws Exception {
        Authorize authorizeExist = authorizeDao.getAuthorizeById(authorize.getId());
        if(authorizeExist==null)
            throw new Exception(constantFile.Authorize_Not_Found);

        authorizeDao.save(authorize);
    }

    public List<Authorize> getAllAuthorize() {
        return authorizeDao.getAllAuthorize();
    }

    public Authorize getAuthorizeById(Long id) {
        return authorizeDao.getAuthorizeById(id);
    }

    public void deleteAuthorizeById(Long id) throws Exception {

        //check the purchase record exist
        List<Purchase> purchaseList = purchaseService.getPurchaseByReceiverId(id);
        if(!purchaseList.isEmpty())
            throw new Exception(constantFile.Purchase_Data_Exist);

        List<DyeingSlipMast> dyeingSlipMasts = dyeingSlipService.getDyeingSlipByApprovedId(id);
        if(!dyeingSlipMasts.isEmpty())
            throw new Exception(constantFile.Dyeing_Slip_Data_Exist);

        authorizeDao.deleteByAuthorizeId(id);
    }
}
