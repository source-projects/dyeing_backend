package com.main.glory.servicesImpl;

import com.main.glory.Dao.machine.ThermopackDao;
import com.main.glory.model.machine.AddMachineInfo.AddThermopackInfo;
import com.main.glory.model.machine.MachineMast;
import com.main.glory.model.machine.Thermopack;
import com.main.glory.model.machine.request.GetRecordBasedOnFilter;
import com.main.glory.model.machine.request.ThermopackRecordBasedOnShift;
import com.main.glory.model.machine.response.ThermopackFilterRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("thermopackImpl")
public class ThermopackImpl {

    @Autowired
    ThermopackDao thermopackDao;

    @Autowired
    MachineServiceImpl machineService;

    public void saveThermopackRecord(List<AddThermopackInfo> thermopackRecord) throws Exception {


        List<Thermopack> list=new ArrayList<>();
        int i=0;
        for(AddThermopackInfo thermopack:thermopackRecord)
        {
            //check the machine is available or not
            MachineMast machineMastExist= machineService.getMachineByMachineId(thermopack.getControlId());

            Thermopack thermopackData=new Thermopack(thermopack);

            list.add(thermopackData);
            i++;
        }


        thermopackDao.saveAll(list);
    }

    public List<Thermopack> getAllMachineRecord() throws Exception{

        List<Thermopack> thermopackList = thermopackDao.getAllThermopack();
        if(thermopackList.isEmpty())
            throw new Exception("no data found");

        return thermopackList;

    }

    public List<ThermopackFilterRecord> getDataBasedOnFilter(GetRecordBasedOnFilter record) throws Exception {


        List<ThermopackFilterRecord> thermopackFilterRecordList=new ArrayList<>();

        String attribute = record.getAttribute();//based on attribute
        SimpleDateFormat datetimeFormatter1 = new SimpleDateFormat(
                "yyyy-MM-dd");

        Date fromDate = datetimeFormatter1.parse(record.getFromDate());

        Date toDate = datetimeFormatter1.parse(record.getToDate());

        List<Thermopack> thermopackFilterRecords;

        switch (attribute)
        {
            case "forwardTemp":

                thermopackFilterRecords=thermopackDao.findByControlId(record.getControlId());

                if(thermopackFilterRecords.isEmpty())
                    throw new Exception("no data found ");


                for(Thermopack thermopack:thermopackFilterRecords)
                {
                    if(thermopack.getDateToEnter().getTime()<=toDate.getTime() && thermopack.getDateToEnter().getTime()>=fromDate.getTime() && thermopack.getTimeOf()<=record.getToTime() && thermopack.getTimeOf()>=record.getFromTime())
                    if(thermopack.getForwardTemp()!=null)
                    {
                        ThermopackFilterRecord thermopackFilterRecord=new ThermopackFilterRecord(thermopack,thermopack.getForwardTemp());
                        thermopackFilterRecordList.add(thermopackFilterRecord);

                    }

                }
                return thermopackFilterRecordList;
            case "returnTemp":

                thermopackFilterRecords=thermopackDao.findByControlId(record.getControlId());

                if(thermopackFilterRecords.isEmpty())
                    throw new Exception("no data found ");


                for(Thermopack thermopack:thermopackFilterRecords)
                {
                    if(thermopack.getDateToEnter().getTime()<=toDate.getTime() && thermopack.getDateToEnter().getTime()>=fromDate.getTime() && thermopack.getTimeOf()<=record.getToTime() && thermopack.getTimeOf()>=record.getFromTime())
                    if(thermopack.getReturnTemp()!=null)
                    {
                        ThermopackFilterRecord thermopackFilterRecord=new ThermopackFilterRecord(thermopack,thermopack.getReturnTemp());
                        thermopackFilterRecordList.add(thermopackFilterRecord);

                    }

                }
                return thermopackFilterRecordList;
            case "stackTemp":

                thermopackFilterRecords=thermopackDao.findByControlId(record.getControlId());

                if(thermopackFilterRecords.isEmpty())
                    throw new Exception("no data found ");


                for(Thermopack thermopack:thermopackFilterRecords)
                {
                    if(thermopack.getDateToEnter().getTime()<=toDate.getTime() && thermopack.getDateToEnter().getTime()>=fromDate.getTime() && thermopack.getTimeOf()<=record.getToTime() && thermopack.getTimeOf()>=record.getFromTime())
                    if(thermopack.getStackTemp()!=null)
                    {
                        ThermopackFilterRecord thermopackFilterRecord=new ThermopackFilterRecord(thermopack,thermopack.getStackTemp());
                        thermopackFilterRecordList.add(thermopackFilterRecord);


                    }

                }
                return thermopackFilterRecordList;

            case "furnaceTemp":

                thermopackFilterRecords=thermopackDao.findByControlId(record.getControlId());

                if(thermopackFilterRecords.isEmpty())
                    throw new Exception("no data found ");


                for(Thermopack thermopack:thermopackFilterRecords)
                {
                    if(thermopack.getDateToEnter().getTime()<=toDate.getTime() && thermopack.getDateToEnter().getTime()>=fromDate.getTime() && thermopack.getTimeOf()<=record.getToTime() && thermopack.getTimeOf()>=record.getFromTime())
                    if(thermopack.getFurnaceTemp()!=null)
                    {
                        ThermopackFilterRecord thermopackFilterRecord=new ThermopackFilterRecord(thermopack,thermopack.getFurnaceTemp());
                        thermopackFilterRecordList.add(thermopackFilterRecord);
                    }

                }
                return thermopackFilterRecordList;
            case "pumpData":

                thermopackFilterRecords=thermopackDao.findByControlId(record.getControlId());

                if(thermopackFilterRecords.isEmpty())
                    throw new Exception("no data found ");


                for(Thermopack thermopack:thermopackFilterRecords)
                {
                    if(thermopack.getDateToEnter().getTime()<=toDate.getTime() && thermopack.getDateToEnter().getTime()>=fromDate.getTime() && thermopack.getTimeOf()<=record.getToTime() && thermopack.getTimeOf()>=record.getFromTime())
                    if(thermopack.getPumpData()!=null)
                    {
                        ThermopackFilterRecord thermopackFilterRecord=new ThermopackFilterRecord(thermopack,thermopack.getPumpData());
                        thermopackFilterRecordList.add(thermopackFilterRecord);

                    }

                }
                return thermopackFilterRecordList;
            case "idFan":

                thermopackFilterRecords=thermopackDao.findByControlId(record.getControlId());

                if(thermopackFilterRecords.isEmpty())
                    throw new Exception("no data found ");


                for(Thermopack thermopack:thermopackFilterRecords)
                {
                    if(thermopack.getDateToEnter().getTime()<=toDate.getTime() && thermopack.getDateToEnter().getTime()>=fromDate.getTime() && thermopack.getTimeOf()<=record.getToTime() && thermopack.getTimeOf()>=record.getFromTime())
                    if(thermopack.getIdFan()!=null)
                    {
                        ThermopackFilterRecord thermopackFilterRecord=new ThermopackFilterRecord(thermopack,thermopack.getIdFan());
                        thermopackFilterRecordList.add(thermopackFilterRecord);


                    }

                }
                return thermopackFilterRecordList;
            case "fdFan":

                thermopackFilterRecords=thermopackDao.findByControlId(record.getControlId());

                if(thermopackFilterRecords.isEmpty())
                    throw new Exception("no data found ");


                for(Thermopack thermopack:thermopackFilterRecords)
                {
                    if(thermopack.getDateToEnter().getTime()<=toDate.getTime() && thermopack.getDateToEnter().getTime()>=fromDate.getTime() && thermopack.getTimeOf()<=record.getToTime() && thermopack.getTimeOf()>=record.getFromTime())
                    if(thermopack.getFdFan()!=null)
                    {
                        ThermopackFilterRecord thermopackFilterRecord=new ThermopackFilterRecord(thermopack,thermopack.getFdFan());
                        thermopackFilterRecordList.add(thermopackFilterRecord);

                    }

                }
                return thermopackFilterRecordList;

            case "screwFeeder":

                thermopackFilterRecords=thermopackDao.findByControlId(record.getControlId());

                if(thermopackFilterRecords.isEmpty())
                    throw new Exception("no data found ");


                for(Thermopack thermopack:thermopackFilterRecords)
                {
                    if(thermopack.getDateToEnter().getTime()<=toDate.getTime() && thermopack.getDateToEnter().getTime()>=fromDate.getTime() && thermopack.getTimeOf()<=record.getToTime() && thermopack.getTimeOf()>=record.getFromTime())
                    if(thermopack.getScrewFeeder()!=null)
                    {
                        ThermopackFilterRecord thermopackFilterRecord=new ThermopackFilterRecord(thermopack,thermopack.getScrewFeeder());
                        thermopackFilterRecordList.add(thermopackFilterRecord);


                    }

                }
                return thermopackFilterRecordList;



            default:throw new Exception("no data found for iven attribute");
        }

    }

    public List<Thermopack> getRecordBasedOnShift(ThermopackRecordBasedOnShift record) throws Exception  {


            List<Thermopack> list = new ArrayList<>();
            SimpleDateFormat datetimeFormatter1 = new SimpleDateFormat(
                    "yyyy-MM-dd");

            Date fromDate = datetimeFormatter1.parse(record.getDate());
            String shift = record.getShift();
            if (shift.isEmpty())
                throw new Exception("shift can't be empty");

            MachineMast thermopackExist = machineService.getMachineByMachineId(record.getThermopackId());

            Long fromTime;
            Long toTime;

            switch (shift) {
                case "day":
                    fromTime = 10l;
                    toTime = 20l;
                    list = thermopackDao.findRecordBasedOnFilter(record.getThermopackId(), fromDate, fromTime, toTime);
                    break;
                case "night":

                    fromTime = 22l;
                    toTime = 8l;
                    list = thermopackDao.findRecordBasedOnFilterNight(record.getThermopackId(), fromDate, fromTime, toTime);
                    break;
                default:
                    throw new Exception("no data found");


            }
            if(list.isEmpty())
                throw new Exception("no data found");

            return list;


    }
}
