package com.main.glory.servicesImpl;

import com.main.glory.Dao.employee.AttendanceDao;
import com.main.glory.Dao.employee.EmployeeDataDao;
import com.main.glory.Dao.employee.EmployeeMastDao;
import com.main.glory.model.employee.Attendance;
import com.main.glory.model.employee.EmployeeData;
import com.main.glory.model.employee.EmployeeMast;
import com.main.glory.model.employee.response.EmployeeAttendanceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service("employeeServiceImpl")
public class EmployeeServiceImpl {

    @Autowired
    EmployeeDataDao employeeDataDao;

    @Autowired
    EmployeeMastDao employeeMastDao;

    @Autowired
    AttendanceDao attendanceDao;

    @Transactional
    public Long addEmployeeRecord(EmployeeMast record) throws Exception {


         if(record.getEmployeeDocumentList().isEmpty())
             throw new Exception("employee document can't be null");


        EmployeeMast employeeMastExistWithAdhar = employeeMastDao.getEmployeeByAadhaarExceptId(record.getAadhaar(),record.getId());

        if(employeeMastExistWithAdhar!=null)
            throw new Exception("employee exist with aadhaar number");


      /*  //process the image and store to the cloudniary

      onlye the image name is coming from FE process that record
        record.getEmployeeDataList().forEach(e->{

        });

*//*

        //set the file url and store in system
        record.getEmployeeDataList().forEach(e->{
            FileUpload fileUpload = new FileUpload();
            String url = fileUpload.uploadFile(e.getFile());
        });*/

        EmployeeMast x = employeeMastDao.save(record);

        //employeeDataDao.saveAll(record.getEmployeeDocumentList());
        return x.getId();
    }



    public Long updateEmployeeRecord(EmployeeMast record) throws Exception {
        //find the record

        EmployeeMast employeeMastExist = employeeMastDao.getEmployeeByAadhaarExceptId(record.getAadhaar(),record.getId());
        if(employeeMastExist!=null)
            throw new Exception("employee exist with aadhaar number");

        EmployeeMast x = employeeMastDao.saveAndFlush(record);

        //employeeDataDao.saveAll(record.getEmployeeDocumentList());
        return x.getId();

    }

    public EmployeeMast getEmployeeById(Long id) {
        return employeeMastDao.getEmployeeById(id);
    }

    public List<EmployeeMast> getAllEmployee() {
        return employeeMastDao.getAllEmployee();
    }

    public void deleteEmployeeById(Long id) throws Exception {
        EmployeeMast employeeMast = employeeMastDao.getEmployeeById(id);
        if(employeeMast==null)
            throw new Exception("no employee found");

        List<Attendance> attendances = attendanceDao.getAllAttendanceByEmployeeId(id);
        if(!attendances.isEmpty())
            throw new Exception("remove the record of attendace");

        employeeMastDao.deleteByEmployeeId(id);
    }

    public Long addEmployeeDataRecord(EmployeeData record) {
        EmployeeData employeeData = employeeDataDao.saveAndFlush(record);
        return employeeData.getId() ;
    }


}
