package com.main.glory.servicesImpl;

import com.main.glory.Dao.machine.*;
import com.main.glory.model.machine.*;
import com.main.glory.model.machine.AddMachineInfo.AddMachineInfo;
import com.main.glory.model.machine.AddMachineInfo.AddMachineRecord;
import com.main.glory.model.machine.category.AddCategory;
import com.main.glory.model.machine.response.GetAllCategory;
import com.main.glory.model.machine.response.GetAllMachine;
import com.main.glory.model.machine.response.GetAllMachineRecord;
import com.main.glory.model.machine.response.GetMachineByIdWithFilter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

@Service("machineServiceImpl")
public class MachineServiceImpl {

    @Autowired
    private MachineDao machineDao;

    @Autowired
    private MachineCategoryDao machineCategoryDao;

    @Autowired
    private MachineRecordDao machineRecordDao;


    @Autowired
    BoilerMachineRecordDao boilerMachineRecordDao;

    @Autowired
    ThermopackDao thermopackDao;
    @Autowired
    ModelMapper modelMapper;




    public void saveMachine(AddMachineInfo machine) throws Exception{

            modelMapper.getConfiguration().setAmbiguityIgnored(true);

            MachineMast machineMast = modelMapper.map(machine, MachineMast.class);

            Optional<MachineMast> machineData =machineDao.findByMachineName(machineMast.getMachineName());

            if(machineData.isPresent())
            {
                throw new Exception("Machine Data is already Exits for machine name:"+machine.getMachineName());
            }

            Optional<MachineCategory> machineCategory = machineCategoryDao.findById(machine.getControlId());
            if(!machineCategory.isPresent())
                throw new Exception("Not such category is available with id:"+machine.getControlId());

            machineDao.save(machineMast);


        }


    public void saveMachineRecord(AddMachineRecord machine) {


    }

    public void saveMachineRecord(String name, Double speed) throws Exception{

        Optional<MachineMast> machineMast = machineDao.findByMachineName(name);

        if(!machineMast.isPresent())
        {
            throw new Exception("No such machine found with name:"+name);

        }

        //get the last record of that machine
        /*
        MachineRecord machineRecord =  new MachineRecord();
        machineRecord.setControlId(machineMast.get().getId());
        machineRecord.setSpeed(speed);
        machineRecord.setControlId(machineMast.get().getId());

*/

        MachineRecord machineRecordExist = machineRecordDao.getLastMachineRecordByControlId(machineMast.get().getId());

        MachineRecord machineRecord;
        if(machineRecordExist!=null)
        {
            machineRecord = new MachineRecord(machineMast.get(),machineRecordExist,speed);

        }
        else {
            machineRecord =new MachineRecord(machineMast.get(),speed);
        }
        machineRecordDao.save(machineRecord);





    }

    public List<GetAllMachine> getAllMachine() throws Exception
    {

        List<GetAllMachine> getAllMachines=new ArrayList<>();
        List<MachineMast> machineMasts=machineDao.getAllMachineMast();

        for(MachineMast m:machineMasts)
        {
            MachineCategory machineCategory = machineCategoryDao.getCategoryById(m.getControlId());
            GetAllMachine getAllMachine=new GetAllMachine();
            getAllMachine.setId(m.getId());
            getAllMachine.setMachineName(m.getMachineName());
            getAllMachine.setControlId(machineCategory.getId());
            getAllMachine.setCategoryName(machineCategory.getName());

            List<GetAllMachineRecord> machineRecordList=new ArrayList<>();
            int i=0;
            for(MachineRecord mr : m.getMachineRecords())
            {

                GetAllMachineRecord gr=new GetAllMachineRecord();
                gr.setId(mr.getId());
                gr.setControlId(mr.getControlId());
                gr.setSpeed(mr.getSpeed());
                gr.setCreatedDate(mr.getCreatedDate());
                gr.setUpdatedDate(mr.getUpdatedDate());

                Double mtr = mr.getSpeed()/6;
                gr.setMtr(mtr);

                machineRecordList.add(gr);


            }


            getAllMachine.setGetAllMachineRecords(machineRecordList);

            getAllMachines.add(getAllMachine);
        }

        if(getAllMachines.isEmpty())
            throw new Exception("no data found");

        return getAllMachines;
    }

    public GetAllMachine getMachineById(Long id) throws Exception {

        Optional<MachineMast> machineMast = machineDao.findById(id);
        if(!machineMast.isPresent())
        {
            throw new Exception("No machine found for id:"+id);
        }
        GetAllMachine machine = new GetAllMachine();

        machine.setId(machineMast.get().getId());
        machine.setMachineName(machineMast.get().getMachineName());

        MachineRecord machineRecord = machineRecordDao.getLastMachineRecordByControlId(machineMast.get().getId());

        List<GetAllMachineRecord> getAllMachineRecordList = new ArrayList<>();

        if(machineRecord==null)
            throw new Exception("no record found");
            GetAllMachineRecord getAllMachineRecord=new GetAllMachineRecord();
            getAllMachineRecord.setId(machineRecord.getId());
            getAllMachineRecord.setControlId(machineRecord.getControlId());
            getAllMachineRecord.setSpeed(machineRecord.getSpeed());
            getAllMachineRecord.setUpdatedDate(machineRecord.getUpdatedDate());
            getAllMachineRecord.setCreatedDate(machineRecord.getCreatedDate());
            getAllMachineRecord.setMtr(machineRecord.getTotalMtr());
            getAllMachineRecordList.add(getAllMachineRecord);




        machine.setGetAllMachineRecords(getAllMachineRecordList);



        return machine;

    }

    public boolean deleteMachineById(Long id) throws Exception {

        Optional<MachineMast> machineMast = machineDao.findById(id);
        if(!machineMast.isPresent())
        {
            throw new Exception("No machine found for id:"+id);
        }

        List<MachineRecord> machineRecords = machineRecordDao.getMachineRecordByControlId(id);
        if(!machineRecords.isEmpty())
            throw new Exception("remove the machine record first");

        List<BoilerMachineRecord> boilerMachineRecords = boilerMachineRecordDao.getAllBoilerRecordByControlId(id);
        if(!boilerMachineRecords.isEmpty())
            throw new Exception("remove the machine record first");

        List<Thermopack> thermopackList = thermopackDao.getAllThermopackRecordByControlId(id);
        if(!thermopackList.isEmpty())
            throw new Exception("remove the machine record first");

        machineDao.deleteById(machineMast.get().getId());
        return true;
    }

    public void saveMachineCategory(AddCategory machine) throws Exception{

        MachineCategory machineCategory=new MachineCategory();
        machineCategory.setName(machine.getName());
        machineCategoryDao.save(machineCategory);
    }

    public List<GetAllCategory> getAllCategory() {

        List<GetAllCategory> getAllCategoryList = new ArrayList<>();
        List<MachineCategory> machineCategories =  machineCategoryDao.getAllMahineCategory();
        for(MachineCategory m:machineCategories)
        {
            GetAllCategory category=new GetAllCategory();
            category.setId(m.getId());
            category.setName(m.getName());

            getAllCategoryList.add(category);

        }
        return getAllCategoryList;

    }

    public List<GetAllMachine> getAllMachineByCategory(Long id) throws Exception{

        Optional<MachineCategory> machineCategory = machineCategoryDao.findById(id);
        if(!machineCategory.isPresent())
            throw new Exception("category not found with id:"+id);

        List<GetAllMachine> getAllMachineList = new ArrayList<>();
        List<MachineMast> machineMast = machineDao.findByControlId(id);
        for(MachineMast mm:machineMast)
        {
            GetAllMachine machine=new GetAllMachine();
            machine.setId(mm.getId());
            machine.setMachineName(mm.getMachineName());
            machine.setCategoryName(machineCategory.get().getName());
            machine.setControlId(id);

            List<GetAllMachineRecord> machineRecordList=new ArrayList<>();
            int i=0;
            for(MachineRecord mr : mm.getMachineRecords())
            {

                GetAllMachineRecord gr=new GetAllMachineRecord();
                gr.setId(mr.getId());
                gr.setControlId(mr.getControlId());
                gr.setSpeed(mr.getSpeed());
                gr.setCreatedDate(mr.getCreatedDate());
                gr.setUpdatedDate(mr.getUpdatedDate());

                Double mtr = mr.getSpeed()/6;
                gr.setMtr(mtr);

                machineRecordList.add(gr);


            }
            machine.setGetAllMachineRecords(machineRecordList);
            getAllMachineList.add(machine);

        }
        return getAllMachineList;


    }

    public GetAllMachine getMachineByIdWithFilter(GetMachineByIdWithFilter getMachine) throws Exception, ParseException {

        SimpleDateFormat formatter ;

        Date d1=null;
        Date date2=null;
        Date d1Time=null;
        Date d2Time=null;
        Date d2=null;

        GetAllMachine getAllMachine;
        Optional<MachineMast> machineMast = machineDao.findById(getMachine.getId());

        if(!machineMast.isPresent())
            throw new Exception("Not data found for id:"+getMachine.getId());


        //Ready for date filter
        if(getMachine.getFromDate()!="" && getMachine.getToDate()!="")
        {
            if(getMachine.getToTime()!="" && getMachine.getToTime()!="") {

                    getAllMachine = new GetAllMachine();
                    getAllMachine.setId(machineMast.get().getId());
                    getAllMachine.setMachineName(machineMast.get().getMachineName());

                    List<GetAllMachineRecord> getAllMachineRecordList = new ArrayList<>();

                    for (MachineRecord machineRecord : machineMast.get().getMachineRecords()) {
                        GetAllMachineRecord getAllMachineRecord = new GetAllMachineRecord(machineRecord);

                        //given fromDate
                        formatter = new SimpleDateFormat("yyyy-MM-dd");
                        d1 = formatter.parse(getMachine.getFromDate());

                        //given to date
                        d2 = formatter.parse(getMachine.getToDate());

                        //machine record date
                        Date date = formatter.parse(getAllMachineRecord.getCreatedDate().toString());
                        //compare
                        if (!date.before(d1) && !date.after(d2)) {
                            formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                            date = formatter.parse(machineRecord.getCreatedDate().toString());//formatter.parse(getMachine.getFromDate());



                            LocalTime localTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).toLocalTime();
                            LocalTime fromTime = LocalTime.parse(getMachine.getFromTime());
                            LocalTime toTime = LocalTime.parse(getMachine.getToTime());

                            System.out.println(localTime + "-" + fromTime + "-" + toTime);
                            int value = localTime.compareTo(fromTime);
                            if (value > 0) {
                                value = localTime.compareTo(toTime);
                                if (value < 0) {
                                    getAllMachineRecordList.add(getAllMachineRecord);
                                }


                            }


                        }
                        System.out.println("Date to check date:" + date);
                        System.out.println("++" + d1);
                        System.out.println("++" + d2);

                    }
                    if(getAllMachineRecordList.isEmpty())
                        throw new Exception("No record found for given time or date");

                    getAllMachine.setGetAllMachineRecords(getAllMachineRecordList);

                    return getAllMachine;

            }
            else{

            getAllMachine = new GetAllMachine();
            getAllMachine.setId(machineMast.get().getId());
            getAllMachine.setMachineName(machineMast.get().getMachineName());

            List<GetAllMachineRecord> getAllMachineRecordList = new ArrayList<>();

            for(MachineRecord machineRecord : machineMast.get().getMachineRecords())
            {
                GetAllMachineRecord getAllMachineRecord=new GetAllMachineRecord(machineRecord);

                //given fromDate
                formatter =  new SimpleDateFormat("yyyy-MM-dd");
                d1=formatter.parse(getMachine.getFromDate());

                //given to date
                d2=formatter.parse(getMachine.getToDate());

                //machine record date
                Date date = formatter.parse(getAllMachineRecord.getCreatedDate().toString());
                //compare
                if(!date.before(d1) && !date.after(d2))
                {
                   if(getMachine.getShift()==1)
                    {
                        date = formatter.parse(machineRecord.getCreatedDate().toString());//formatter.parse(getMachine.getFromDate());

                        LocalTime localTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).toLocalTime();
                        LocalTime fromTime = LocalTime.of(00,00,00,0);
                        LocalTime toTime = LocalTime.of(12,00,00,0);

                        System.out.println(localTime + "-" + fromTime + "-" + toTime);
                        int value = localTime.compareTo(fromTime);
                        if (value >= 0) {
                            value = localTime.compareTo(toTime);
                            if (value <= 0) {
                                getAllMachineRecordList.add(getAllMachineRecord);
                            }
                        }


                    }
                    else if(getMachine.getShift()==2)
                    {
                        date = formatter.parse(machineRecord.getCreatedDate().toString());
                        date2= formatter.parse(machineRecord.getCreatedDate().toString());
                        date2.setDate(date2.getDate());
                        //formatter.parse(getMachine.getFromDate());



                        LocalTime localTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).toLocalTime();
                        LocalTime fromTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).toLocalTime();
                        fromTime.isAfter(LocalTime.of(18,0,0,0));
                        LocalTime toTime = LocalDateTime.ofInstant(date2.toInstant(), ZoneId.systemDefault()).toLocalTime();
                        toTime.isBefore(LocalTime.of(22,0,0,0));


                        System.out.println(localTime + "-" + fromTime + "-" + toTime);
                        int value = localTime.compareTo(fromTime);
                        if (value >= 0) {
                            value = localTime.compareTo(toTime);
                            if (value <= 0) {
                                getAllMachineRecordList.add(getAllMachineRecord);
                            }
                        }

                    }


                }
                else throw new Exception("No record found");

                System.out.println("Date to check:"+date);
                System.out.println("++"+date2);
                System.out.println("++"+d2);

            }
            getAllMachine.setGetAllMachineRecords(getAllMachineRecordList);

            if(getAllMachine.getGetAllMachineRecords()==null)
                throw new Exception("no record found");
            return getAllMachine;}





        }

        if(getMachine.getFromTime()!="" && getMachine.getToTime()!="") {
            if (getMachine.getFromDate() != ""  && getMachine.getToDate() != "") {
                getAllMachine = new GetAllMachine();
                getAllMachine.setId(machineMast.get().getId());
                getAllMachine.setMachineName(machineMast.get().getMachineName());

                List<GetAllMachineRecord> getAllMachineRecordList = new ArrayList<>();

                for (MachineRecord machineRecord : machineMast.get().getMachineRecords()) {
                    GetAllMachineRecord getAllMachineRecord = new GetAllMachineRecord(machineRecord);

                    //given fromDate
                    formatter = new SimpleDateFormat("yyyy-MM-dd");
                    d1 = formatter.parse(getMachine.getFromDate());

                    //given to date
                    d2 = formatter.parse(getMachine.getToDate());

                    //machine record date
                    Date date = formatter.parse(getAllMachineRecord.getCreatedDate().toString());
                    //compare
                    if (!date.before(d1) && !date.after(d2)) {
                        formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
                        date = formatter.parse(machineRecord.getCreatedDate().toString());//formatter.parse(getMachine.getFromDate());
                        date.setHours(date.getHours() - 11);


                        LocalTime localTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).toLocalTime();
                        LocalTime fromTime = LocalTime.parse(getMachine.getFromTime());
                        LocalTime toTime = LocalTime.parse(getMachine.getToTime());

                        System.out.println(localTime + "-" + fromTime + "-" + toTime);
                        int value = localTime.compareTo(fromTime);
                        if (value >= 0) {
                            value = localTime.compareTo(toTime);
                            if (value <= 0) {
                                getAllMachineRecordList.add(getAllMachineRecord);
                            }
                        }


                    }
                    System.out.println("Date to check:" + date);
                    System.out.println("++" + d1);
                    System.out.println("++" + d2);

                }
                getAllMachine.setGetAllMachineRecords(getAllMachineRecordList);

                return getAllMachine;
            }
            else if(getMachine.getFromDate() =="" && getMachine.getToDate() == "")
            {
                getAllMachine = new GetAllMachine();
                getAllMachine.setId(machineMast.get().getId());
                getAllMachine.setMachineName(machineMast.get().getMachineName());

                List<GetAllMachineRecord> getAllMachineRecordList = new ArrayList<>();

                for (MachineRecord machineRecord : machineMast.get().getMachineRecords()) {
                    GetAllMachineRecord getAllMachineRecord = new GetAllMachineRecord(machineRecord);

                    formatter = new SimpleDateFormat("yyyy-MM-dd");
                    d1 = new java.sql.Date(System.currentTimeMillis());

                    //given to date
                    d2 = new java.sql.Date(System.currentTimeMillis());

                    Date date = formatter.parse(getAllMachineRecord.getCreatedDate().toString());
                    //compare
                    if (!date.before(d1) && !date.after(d2)) {

                        //LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).toLocalTime();
                        LocalTime localTime = LocalTime.parse(getMachine.getFromTime());
                        LocalTime fromTime = LocalTime.parse(getMachine.getFromTime());
                        LocalTime toTime = LocalTime.parse(getMachine.getToTime());

                        System.out.println(localTime + "-" + fromTime + "-" + toTime);
                        int value = localTime.compareTo(fromTime);
                        if ((value > 0) | (value == 0)) {
                            value = localTime.compareTo(toTime);
                            if ((value < 0) | (value == 0)) {
                                getAllMachineRecordList.add(getAllMachineRecord);
                            }
                        }
                    }


                  //  }
                    System.out.println("Date to check12:" + date);


                }
                getAllMachine.setGetAllMachineRecords(getAllMachineRecordList);

                return getAllMachine;
            }
        }


        return null;

    }
//for store the temp data of machine record not recommedated
    public void addTempMachineRecord(String name, long date) {

        try {
            Optional<MachineMast> machineMast = machineDao.findByMachineName(name);
            long retryDate = date;
            int sec = 600;
            Double speed = 90.0;
            for (int i = 0; i < 10; i++) {

                Timestamp original = new Timestamp(retryDate);
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(original.getTime());
                cal.add(Calendar.SECOND, sec);
                Timestamp later = new Timestamp(cal.getTime().getTime());


                MachineRecord machineRecord = new MachineRecord();
                machineRecord.setControlId(machineMast.get().getId());
                machineRecord.setSpeed(speed);
                speed += 10;
                machineRecord.setCreatedDate(later);
                machineRecordDao.save(machineRecord);
                retryDate = later.getTime();
                //System.out.println(later);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }


    }

    public MachineMast getMachineByMachineId(Long controlId)throws Exception {
        Optional<MachineMast> machineMast = machineDao.findById(controlId);
        if(machineMast.isEmpty())
            throw new Exception("no data found for id:"+controlId);
        return machineMast.get();
    }

    public boolean deleteMachineCategoryById(Long id) throws Exception {
        MachineCategory machineCategory = machineCategoryDao.getCategoryById(id);
        if (machineCategory==null)
        {
            throw new Exception("no machine category found");
        }

        List<MachineMast> machineMasts = machineDao.findByControlId(id);
        if(!machineMasts.isEmpty())
        {
            throw new Exception("delete the machine first");
        }
        machineCategoryDao.deleteCategoryById(id);
        return true;
    }

    public boolean updateMachineCategory(MachineCategory machineCategory) throws Exception {
        MachineCategory machineCategoryExist = machineCategoryDao.getCategoryById(machineCategory.getId());
        if(machineCategoryExist==null)
            throw new Exception("no machine category found");

        machineCategoryExist=new MachineCategory(machineCategory);

        machineCategoryDao.save(machineCategoryExist);

        return true;

    }

    public boolean updateMachineMast(MachineMast machineMast) throws Exception {

        MachineMast machineMastExist = machineDao.getMachineById(machineMast.getId());
        if(machineMastExist==null)
            throw new Exception("no machine found");



        MachineMast x = machineDao.save(machineMast);
        //update the respecte record by id of machine mast

        List<MachineRecord> machineRecords = machineRecordDao.getMachineRecordByControlId(x.getId());
        if(!machineRecords.isEmpty())
        {
            for(MachineRecord machineRecord:machineRecords)
            {
                machineRecordDao.updateMachineRecord(machineRecord.getId(),x.getId());
            }

        }

        List<BoilerMachineRecord> boilerMachineRecords =boilerMachineRecordDao.getAllBoilerRecordByControlId(machineMastExist.getId());
        if(!boilerMachineRecords.isEmpty())
        {
            for(BoilerMachineRecord boilerMachineRecord:boilerMachineRecords)
            {
                boilerMachineRecordDao.updateBoilerRecord(boilerMachineRecord.getId(),x.getId());
            }
        }

        //thermopack

        List<Thermopack> thermopackList = thermopackDao.getAllThermopackRecordByControlId(x.getId());
        if(!thermopackList.isEmpty())
        {
            for(Thermopack t :thermopackList)
            {
                thermopackDao.udpateThermopackRecord(t.getId(),x.getId());
            }
        }

        return true;
    }

    public boolean getCategoryIsDeletble(Long id) throws Exception {
        MachineCategory machineCategoryExist = machineCategoryDao.getCategoryById(id);

        if(machineCategoryExist==null)
            throw new Exception("machine category not found");

        List<MachineMast> machineMasts = machineDao.findByControlId(id);

        if(machineMasts.isEmpty())
            return true;
        else
            return false;


    }

    public boolean getMachineIsDeletable(Long id) throws Exception {
        MachineMast machineMastExist = machineDao.getMachineById(id);
        if(machineMastExist==null)
            throw new Exception("machine not found");

        //check the record is available or not
        List<BoilerMachineRecord> boilerMachineRecords = boilerMachineRecordDao.getAllBoilerRecordByControlId(id);
        List<MachineRecord> machineRecords = machineRecordDao.getMachineRecordByControlId(id);
        List<Thermopack> thermopackList = thermopackDao.getAllThermopackRecordByControlId(id);

        if(thermopackList.isEmpty() && boilerMachineRecords.isEmpty() && machineRecords.isEmpty() )
            return true;
        else
            return false;

    }
}
