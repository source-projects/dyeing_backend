package com.main.glory.servicesImpl;

import com.main.glory.Dao.employee.AttendanceDao;
import com.main.glory.model.ConstantFile;
import com.main.glory.model.employee.Attendance;
import com.main.glory.model.employee.EmployeeMast;
import com.main.glory.model.employee.request.Filter;
import com.main.glory.model.employee.request.FilterAttendance;
import com.main.glory.model.employee.request.GetLatestAttendance;
import com.main.glory.model.employee.response.EmployeeAttendanceResponse;
import com.main.glory.model.employee.response.EmployeeWithAttendance;
import com.main.glory.model.employee.response.GetAllEmployee;
import com.main.glory.model.employee.response.MonthlyAttendanceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service("attendanceServiceImpl")
public class AttendanceServiceImpl {

    /*

        * always get the record entry id by EmpId coming from FE and then map entry with child record attendance record


     */

    ConstantFile constantFile;

    @Autowired
    EmployeeServiceImpl employeeService;
    @Autowired
    AttendanceDao attendanceDao;

    public Attendance saveAttendance(Attendance record) throws Exception {
        EmployeeMast employeeMast  =employeeService.getEmployeeByEmpId(record.getControlId());
        if(employeeMast==null)
            throw new Exception(constantFile.Employee_Not_Found);

        record.setControlId(employeeMast.getId());
        Attendance attendance = attendanceDao.save(record);
        return attendance;
    }

    public Attendance updateAttendance(Attendance record) throws Exception {

        EmployeeMast employeeMast  =employeeService.getEmployeeByEmpId(record.getControlId());
        if(employeeMast==null)
            throw new Exception(constantFile.Employee_Not_Found);

        record.setControlId(employeeMast.getId());

        Attendance attendanceExist = attendanceDao.getAttendanceById(record.getId());
        /*if(attendanceExist==null)
            throw new Exception("no record found");

        if(attendanceExist.getShift()!=record.getShift())
            throw new Exception("please select right shift");*/

        Attendance x= attendanceDao.saveAndFlush(record);
        return x;

    }

    public List<Attendance> getAttendanceByEmployeeId(Long id) throws Exception {
        //check the employee exist

        EmployeeMast employeeMast =employeeService.getEmployeeByEmpId(id);

        if(employeeMast==null)
            throw new Exception(constantFile.Employee_Not_Found);

        return attendanceDao.getAllAttendanceByEmployeeId(employeeMast.getId());


    }

    public Attendance getAttendanceById(Long id) {
        return  attendanceDao.getAttendanceById(id);
    }

    public EmployeeWithAttendance getLatestAttendanceRecordByEmployeeId(Long id) throws Exception {
        EmployeeWithAttendance employeeWithAttendance=null;
        EmployeeMast employeeMastExist = employeeService.getEmployeeByEmpId(id);
        if(employeeMastExist==null)
            throw new Exception(constantFile.Employee_Not_Exist);

        Attendance attendance = attendanceDao.getLatestAttendanceRecordByEmployeeId(employeeMastExist.getId());

        if(attendance==null || (attendance.getInTime()!=null && attendance.getOutTime()!=null))
        {
            employeeWithAttendance =new EmployeeWithAttendance(employeeMastExist,new Attendance());
        }
        else
        {
            employeeWithAttendance=new EmployeeWithAttendance(employeeMastExist,attendance);
        }
        return  employeeWithAttendance;

    }

    public List<EmployeeAttendanceResponse> getAttendanceRecordBasedOnFilter(FilterAttendance filterAttendance) throws Exception {
        List<EmployeeAttendanceResponse> list=new ArrayList<>();




        if(filterAttendance.getControlId()==null)
        {
            List<GetAllEmployee> employeeMastList = employeeService.getAllEmployee();
            for(EmployeeMast employeeMast:employeeMastList)
            {
                EmployeeAttendanceResponse employeeAttendanceResponse = attendanceDao.getAttendanceBasedOnFilter(employeeMast.getId(),filterAttendance.getFromDate(),filterAttendance.getToDate());
                if(employeeAttendanceResponse!=null)
                list.add(new EmployeeAttendanceResponse(employeeAttendanceResponse,filterAttendance,employeeMast));
            }
        }
        else
        {
            //check the employee is exist or not

            EmployeeMast employeeMastExist = employeeService.getEmployeeByEmpId(filterAttendance.getControlId());
            if(employeeMastExist==null)
                throw new Exception(constantFile.Employee_Not_Found);

            EmployeeAttendanceResponse employeeAttendanceResponse = attendanceDao.getAttendanceBasedOnFilter(employeeMastExist.getId(),filterAttendance.getFromDate(),filterAttendance.getToDate());
            if(employeeAttendanceResponse!=null)
                list.add(new EmployeeAttendanceResponse(employeeAttendanceResponse,filterAttendance,employeeMastExist));

        }

        return list;
    }

    public EmployeeWithAttendance getLatestAttendanceRecordByEmployeeIdDateAndShift(GetLatestAttendance record) throws Exception {
        EmployeeWithAttendance employeeWithAttendance=null;
        /*EmployeeMast employeeMastExist = employeeService.getEmployeeByEmpId(record.getId());
        if(employeeMastExist==null)
            throw new Exception(constant.Employee_Not_Exist);


       *//* if(record.getShift()==false)
        {
            Calendar cal =Calendar.getInstance();
            cal.setTime(record.getDate());
            cal.add(Calendar.DATE,-1);
            record.setDate(cal.getTime());
        }*//*
        Attendance attendance = attendanceDao.getAttendanceByIdDateAndShift(employeeMastExist.getId(),record.getShift(),record.getDate());

        ObjectMapper objectMapper =new ObjectMapper();
        if(attendance==null)
        {

            attendance  = new Attendance();
            System.out.println(objectMapper.writeValueAsString(attendance));
            employeeWithAttendance =new EmployeeWithAttendance(employeeMastExist,attendance);
        }
        else
        {
            System.out.println(objectMapper.writeValueAsString(attendance));
            employeeWithAttendance=new EmployeeWithAttendance(employeeMastExist,attendance);
        }
        System.out.println(objectMapper.writeValueAsString(employeeWithAttendance));
        */
        return  employeeWithAttendance;
    }

    //***********************   AFTER CHANGES IN Attendance MODULE   *************************************

    public EmployeeWithAttendance getLatestAttendanceRecordByEmployeeIdDateAndSaveFlag(GetLatestAttendance record) throws Exception {

        EmployeeWithAttendance employeeWithAttendance=null;

        /*

        * check that the employee exist or not
        * and FE always send the empId we have to get the empId from record object and convert it into the record id and then check
        with existing record

        */

        EmployeeMast employeeMast=employeeService.getEmployeeByEmpId(record.getEmpId());
        if(employeeMast==null)
            throw new Exception(ConstantFile.Employee_Not_Exist);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(record.getDate());


        //minus by 1 date
        if(record.getSaveFlag()==true)
        {
            //return the record only
            calendar.add(Calendar.DATE,-1);
            Attendance previousAttendance = attendanceDao.getAttendanceByDateAndIdDescendingOrder(calendar.getTime(),employeeMast.getId());

            if(previousAttendance!=null)
            {
                if(previousAttendance.getOutTime()==null)
                {
                    //if null then auto patch the out time
                    //check for the out time is stored or not if not then auto patch the out time on that attendance
                    // if time is < current date time


                    Long diff =record.getDate().getTime() - calendar.getTime().getTime();
                    if((diff / (60 * 60 * 1000))<12) //to convert diff in hour
                    {
                        previousAttendance.setOutTime(record.getDate());
                        previousAttendance.setOutUrl(record.getUrl()==null?"":record.getUrl());
                        employeeWithAttendance = new EmployeeWithAttendance(employeeMast,attendanceDao.saveAndFlush(previousAttendance));

                        /*return employeeWithAttendance;*/

                    }
                    else {

                        calendar.setTime(previousAttendance.getInTime());
                        calendar.add(Calendar.HOUR, ConstantFile.inOutTimeAuto);
                        previousAttendance.setOutTime(calendar.getTime());
                        previousAttendance.setOutUrl(record.getUrl()==null?"":record.getUrl());
                        employeeWithAttendance = new EmployeeWithAttendance(employeeMast, attendanceDao.saveAndFlush(previousAttendance));

                    }



                    // for in time
                    calendar.setTime(record.getDate());
                    Attendance currentAttendance = attendanceDao.getAttendanceByDateAndIdDescendingOrder(calendar.getTime(),employeeMast.getId());
                    if(currentAttendance==null || (currentAttendance.getInTime()!=null && currentAttendance.getOutTime()!=null)) {
                        //change the record
                        currentAttendance=new Attendance(record,employeeMast);
                        currentAttendance.setUrl(record.getUrl());
                        employeeWithAttendance = new EmployeeWithAttendance(employeeMast, attendanceDao.saveAndFlush(currentAttendance));
                    }
                    else
                    {
                        currentAttendance.setOutTime(record.getDate());
                        currentAttendance.setOutUrl(record.getUrl()==null?"":record.getUrl());
                        employeeWithAttendance = new EmployeeWithAttendance(employeeMast,attendanceDao.saveAndFlush(currentAttendance));
                    }





                }
                else
                {
                    //fot in time

                    //check for the today is any attendance is exist
                    calendar.setTime(record.getDate());
                    Attendance currentAttendanceExist =attendanceDao.getAttendanceByDateAndIdDescendingOrder(calendar.getTime(),employeeMast.getId());
                    if (currentAttendanceExist==null || (currentAttendanceExist.getOutTime()!=null && currentAttendanceExist.getInTime()!=null))
                    {
                        currentAttendanceExist = new Attendance(record,employeeMast);
                        currentAttendanceExist.setUrl(record.getUrl());
                        employeeWithAttendance = new EmployeeWithAttendance(employeeMast,attendanceDao.saveAndFlush(currentAttendanceExist));
                    }
                    else
                    {
                        if(currentAttendanceExist.getOutTime() == null)
                        {
                            currentAttendanceExist.setOutTime(record.getDate());
                            currentAttendanceExist.setOutUrl(record.getUrl()==null?"":record.getUrl());
                            employeeWithAttendance = new EmployeeWithAttendance(employeeMast,attendanceDao.saveAndFlush(currentAttendanceExist));
                        }
                    }
                }

            }
            else
            {
                calendar.setTime(record.getDate());
                Attendance currentAttendance = attendanceDao.getAttendanceByDateAndIdDescendingOrder(calendar.getTime(),employeeMast.getId());
                if(currentAttendance==null || (currentAttendance.getInTime()!=null && currentAttendance.getOutTime()!=null)) {
                    //in time
                    currentAttendance = new Attendance(record,employeeMast);
                    currentAttendance.setUrl(record.getUrl());
                    employeeWithAttendance = new EmployeeWithAttendance(employeeMast,attendanceDao.saveAndFlush(currentAttendance));
                }
                else
                {
                    //save the out time which is coming
                    currentAttendance.setOutTime(record.getDate());
                    currentAttendance.setOutUrl(record.getUrl()==null?"":record.getUrl());
                    employeeWithAttendance = new EmployeeWithAttendance(employeeMast,attendanceDao.saveAndFlush(currentAttendance));
                }
            }


        }
        else
        {
            //then only send the object as per the condition
            //return the record only
            calendar.add(Calendar.DATE,-1);
            Attendance previousAttendance = attendanceDao.getAttendanceByDateAndIdDescendingOrder(calendar.getTime(),employeeMast.getId());

            if(previousAttendance!=null)
            {
                if(previousAttendance.getOutTime()==null)
                {
                    //if null then check as per the condition
                    Long diff =record.getDate().getTime() - calendar.getTime().getTime();
                    if((diff / (60 * 60 * 1000))<12)
                    {
                        previousAttendance.setOutTime(record.getDate());
                        previousAttendance.setOutUrl(record.getUrl()==null?"":record.getUrl());
                        employeeWithAttendance = new EmployeeWithAttendance(employeeMast,attendanceDao.saveAndFlush(previousAttendance));

                        /*return employeeWithAttendance;*/

                    }
                    else {

                        calendar.setTime(previousAttendance.getInTime());
                        calendar.add(Calendar.HOUR, ConstantFile.inOutTimeAuto);
                        previousAttendance.setOutTime(calendar.getTime());
                        previousAttendance.setOutUrl(record.getUrl()==null?"":record.getUrl());
                        employeeWithAttendance = new EmployeeWithAttendance(employeeMast, attendanceDao.saveAndFlush(previousAttendance));

                    }



                    // for in time
                    calendar.setTime(record.getDate());
                    Attendance currentAttendance = attendanceDao.getAttendanceByDateAndIdDescendingOrder(calendar.getTime(),employeeMast.getId());
                    if(currentAttendance==null || (currentAttendance.getInTime()!=null && currentAttendance.getOutTime()!=null)) {
                        employeeWithAttendance = new EmployeeWithAttendance(employeeMast, new Attendance());
                    }
                    else
                    {
                        employeeWithAttendance = new EmployeeWithAttendance(employeeMast,currentAttendance);
                    }
                }
                else
                {
                    calendar.setTime(record.getDate());
                    Attendance currentAttendance = attendanceDao.getAttendanceByDateAndIdDescendingOrder(calendar.getTime(),employeeMast.getId());
                    if(currentAttendance==null || (currentAttendance.getInTime()!=null && currentAttendance.getOutTime()!=null)) {
                        employeeWithAttendance = new EmployeeWithAttendance(employeeMast, new Attendance());
                    }
                    else
                    {
                        employeeWithAttendance = new EmployeeWithAttendance(employeeMast,currentAttendance);
                    }
                }

            }
            else
            {
                calendar.setTime(record.getDate());
                Attendance currentAttendance = attendanceDao.getAttendanceByDateAndIdDescendingOrder(calendar.getTime(),employeeMast.getId());
                if(currentAttendance==null || (currentAttendance.getInTime()!=null && currentAttendance.getOutTime()!=null)) {
                    employeeWithAttendance = new EmployeeWithAttendance(employeeMast, new Attendance());
                }
                else
                {
                    employeeWithAttendance = new EmployeeWithAttendance(employeeMast,currentAttendance);
                }

            }

        }


        return employeeWithAttendance;



    }

    public List<MonthlyAttendanceResponse> getAttendanceMonthlyReport(Filter filter) throws ParseException {

        List<MonthlyAttendanceResponse> list = new ArrayList<>();

        Date from=null;
        Date to=null;
        //add one day because of timestamp issue
        Calendar c = Calendar.getInstance();

        SimpleDateFormat datetimeFormatter1 = new SimpleDateFormat(
                "yyyy-MM-dd");


        if(!filter.getFrom().isEmpty())
            from = datetimeFormatter1.parse(filter.getFrom());
        if(!filter.getTo().isEmpty()) {
            to = datetimeFormatter1.parse(filter.getTo());
            c.setTime(to);
            c.add(Calendar.DATE, 1);//don;t + date because on Palsana, server it is working,but not working on EC2 because of timezone
            to=c.getTime();
        }


       list = employeeService.getAllEmployeeNameWithId();

        /*
         for counting something
        long countBigCustomers = customers
                .stream()
                .filter(c -> c.getPoints() > 100)
                .count();*/


        Date finalFrom = from;
        Date finalTo = to;
        list.forEach(e->{
            e.setApproved(attendanceDao.getApprovedFlagAttendanceByIdAndDateFilter(finalFrom, finalTo,e.getId(),true));
            e.setNotApproved(attendanceDao.getApprovedFlagAttendanceByIdAndDateFilter(finalFrom, finalTo,e.getId(),false));
            e.setTotalPresent(e.getApproved()+e.getNotApproved());
            e.setTotalAbsent(TimeUnit.DAYS.convert(finalTo.getTime()-finalFrom.getTime(), TimeUnit.MILLISECONDS) - e.getTotalPresent());
        });
        return list;

    }

    public EmployeeWithAttendance getLatestAttendanceRecordByEmployeeIdDateAndSaveFlagV2(GetLatestAttendance record) throws Exception {
        EmployeeWithAttendance employeeWithAttendance=null;

        /*

        * check that the employee exist or not
        * and FE always send the empId we have to get the empId from record object and convert it into the record id and then check
        with existing record

        */

        EmployeeMast employeeMast=employeeService.getEmployeeByEmpId(record.getEmpId());
        if(employeeMast==null)
            throw new Exception(ConstantFile.Employee_Not_Exist);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(record.getDate());


        //minus by 1 date
        if(record.getSaveFlag()==true)
        {
            //return the record only
            calendar.add(Calendar.DATE,-1);
            Attendance previousAttendance = attendanceDao.getAttendanceByDateAndIdDescendingOrder(calendar.getTime(),employeeMast.getId());

            if(previousAttendance!=null)
            {
                if(previousAttendance.getOutTime()==null)
                {
                    //if null then auto patch the out time
                    //check for the out time is stored or not if not then auto patch the out time on that attendance
                    // if time is < current date time


                    Long diff =record.getDate().getTime() - calendar.getTime().getTime();
                    if((diff / (60 * 60 * 1000))<12) //to convert diff in hour
                    {
                        previousAttendance.setOutTime(record.getDate());
                        previousAttendance.setOutUrl(record.getUrl()==null?"":record.getUrl());
                        employeeWithAttendance = new EmployeeWithAttendance(employeeMast,attendanceDao.saveAndFlush(previousAttendance));

                        /*return employeeWithAttendance;*/

                    }
                    else {

                        calendar.setTime(previousAttendance.getInTime());
                        calendar.add(Calendar.HOUR, ConstantFile.inOutTimeAuto);
                        previousAttendance.setOutTime(calendar.getTime());
                        previousAttendance.setOutUrl(record.getUrl()==null?"":record.getUrl());
                        employeeWithAttendance = new EmployeeWithAttendance(employeeMast, attendanceDao.saveAndFlush(previousAttendance));

                    }



                    // for in time
                    calendar.setTime(record.getDate());
                    Attendance currentAttendance = attendanceDao.getAttendanceByDateAndIdDescendingOrder(calendar.getTime(),employeeMast.getId());
                    if(currentAttendance==null || (currentAttendance.getInTime()!=null && currentAttendance.getOutTime()!=null)) {
                        //change the record
                        currentAttendance=new Attendance(record,employeeMast);
                        currentAttendance.setUrl(record.getUrl());
                        employeeWithAttendance = new EmployeeWithAttendance(employeeMast, attendanceDao.saveAndFlush(currentAttendance));
                    }
                    else
                    {
                        currentAttendance.setOutTime(record.getDate());
                        currentAttendance.setOutUrl(record.getUrl()==null?"":record.getUrl());
                        employeeWithAttendance = new EmployeeWithAttendance(employeeMast,attendanceDao.saveAndFlush(currentAttendance));
                    }





                }
                else
                {
                    //fot in time

                    //check for the today is any attendance is exist
                    calendar.setTime(record.getDate());
                    Attendance currentAttendanceExist =attendanceDao.getAttendanceByDateAndIdDescendingOrder(calendar.getTime(),employeeMast.getId());
                    if (currentAttendanceExist==null || (currentAttendanceExist.getOutTime()!=null && currentAttendanceExist.getInTime()!=null))
                    {
                        currentAttendanceExist = new Attendance(record,employeeMast);
                        currentAttendanceExist.setUrl(record.getUrl());
                        employeeWithAttendance = new EmployeeWithAttendance(employeeMast,attendanceDao.saveAndFlush(currentAttendanceExist));
                    }
                    else
                    {
                        if(currentAttendanceExist.getOutTime() == null)
                        {
                            currentAttendanceExist.setOutTime(record.getDate());
                            currentAttendanceExist.setOutUrl(record.getUrl()==null?"":record.getUrl());
                            employeeWithAttendance = new EmployeeWithAttendance(employeeMast,attendanceDao.saveAndFlush(currentAttendanceExist));
                        }
                    }
                }

            }
            else
            {
                calendar.setTime(record.getDate());
                Attendance currentAttendance = attendanceDao.getAttendanceByDateAndIdDescendingOrder(calendar.getTime(),employeeMast.getId());
                if(currentAttendance==null || (currentAttendance.getInTime()!=null && currentAttendance.getOutTime()!=null)) {
                    //in time
                    currentAttendance = new Attendance(record,employeeMast);
                    currentAttendance.setUrl(record.getUrl());
                    employeeWithAttendance = new EmployeeWithAttendance(employeeMast,attendanceDao.saveAndFlush(currentAttendance));
                }
                else
                {
                    //save the out time which is coming
                    currentAttendance.setOutTime(record.getDate());
                    currentAttendance.setOutUrl(record.getUrl()==null?"":record.getUrl());
                    employeeWithAttendance = new EmployeeWithAttendance(employeeMast,attendanceDao.saveAndFlush(currentAttendance));
                }
            }


        }
        else
        {
            //then only send the object as per the condition
            //return the record only
            calendar.add(Calendar.DATE,-1);
            Attendance previousAttendance = attendanceDao.getAttendanceByDateAndIdDescendingOrder(calendar.getTime(),employeeMast.getId());

            if(previousAttendance!=null)
            {
                if(previousAttendance.getOutTime()==null)
                {
                    //if null then check as per the condition
                    Long diff =record.getDate().getTime() - calendar.getTime().getTime();
                    if((diff / (60 * 60 * 1000))<12)
                    {
                        previousAttendance.setOutTime(record.getDate());
                        previousAttendance.setOutUrl(record.getUrl()==null?"":record.getUrl());
                        employeeWithAttendance = new EmployeeWithAttendance(employeeMast,attendanceDao.saveAndFlush(previousAttendance));

                        /*return employeeWithAttendance;*/

                    }
                    else {

                        calendar.setTime(previousAttendance.getInTime());
                        calendar.add(Calendar.HOUR, ConstantFile.inOutTimeAuto);
                        previousAttendance.setOutTime(calendar.getTime());
                        previousAttendance.setOutUrl(record.getUrl()==null?"":record.getUrl());
                        employeeWithAttendance = new EmployeeWithAttendance(employeeMast, attendanceDao.saveAndFlush(previousAttendance));

                    }



                    // for in time
                    calendar.setTime(record.getDate());
                    Attendance currentAttendance = attendanceDao.getAttendanceByDateAndIdDescendingOrder(calendar.getTime(),employeeMast.getId());
                    if(currentAttendance==null || (currentAttendance.getInTime()!=null && currentAttendance.getOutTime()!=null)) {
                        employeeWithAttendance = new EmployeeWithAttendance(employeeMast, new Attendance());
                    }
                    else
                    {
                        employeeWithAttendance = new EmployeeWithAttendance(employeeMast,currentAttendance);
                    }
                }
                else
                {
                    calendar.setTime(record.getDate());
                    Attendance currentAttendance = attendanceDao.getAttendanceByDateAndIdDescendingOrder(calendar.getTime(),employeeMast.getId());
                    if(currentAttendance==null || (currentAttendance.getInTime()!=null && currentAttendance.getOutTime()!=null)) {
                        employeeWithAttendance = new EmployeeWithAttendance(employeeMast, new Attendance());
                    }
                    else
                    {
                        employeeWithAttendance = new EmployeeWithAttendance(employeeMast,currentAttendance);
                    }
                }

            }
            else
            {
                calendar.setTime(record.getDate());
                Attendance currentAttendance = attendanceDao.getAttendanceByDateAndIdDescendingOrder(calendar.getTime(),employeeMast.getId());
                if(currentAttendance==null || (currentAttendance.getInTime()!=null && currentAttendance.getOutTime()!=null)) {
                    employeeWithAttendance = new EmployeeWithAttendance(employeeMast, new Attendance());
                }
                else
                {
                    employeeWithAttendance = new EmployeeWithAttendance(employeeMast,currentAttendance);
                }

            }

        }


        return employeeWithAttendance;


    }
}
