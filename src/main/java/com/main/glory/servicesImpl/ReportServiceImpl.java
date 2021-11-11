package com.main.glory.servicesImpl;

import com.main.glory.Dao.report.ReportMastDao;
import com.main.glory.model.ConstantFile;
import com.main.glory.model.report.ReportMast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("reportServiceImpl")
public class ReportServiceImpl {

    @Autowired
    ReportMastDao reportMastDao;


    public void addReportMast(ReportMast reportMast) throws Exception {
        ReportMast reportMastExistWithName = reportMastDao.getReportMastByName(reportMast.getName());
        if(reportMastExistWithName!=null)
            throw new Exception(ConstantFile.Report_Type_Exist);

        reportMastExistWithName = new ReportMast(reportMast);
        reportMastDao.save(reportMastExistWithName);


    }

    public void updateReportMast(ReportMast reportMast) throws Exception {

        ReportMast reportMastExistWithId = reportMastDao.getReportMastById(reportMast.getId());
        if(reportMastExistWithId==null)
            throw new Exception(ConstantFile.Report_Type_Not_Found);


        ReportMast reportMastExistWithNameExceptId = reportMastDao.getReportMastByNameExceptId(reportMast.getName(),reportMast.getId());
        if(reportMastExistWithNameExceptId!=null)
            throw new Exception(ConstantFile.Report_Type_Exist);

        reportMast = new ReportMast(reportMast);
        reportMastDao.save(reportMast);
    }

    public List<ReportMast> getAllReportMast() {
        return reportMastDao.getAllReportMast();
    }

    public ReportMast getReportMastById(Long id) {
        return reportMastDao.getReportMastById(id);
    }

    public void deleteReportMastById(Long id) throws Exception {
        //check record exist

        ReportMast reportMastExist = reportMastDao.getReportMastById(id);
        if(reportMastExist==null)
            throw new Exception(ConstantFile.Report_Type_Not_Found);

        reportMastDao.deleteById(id);

    }
}
