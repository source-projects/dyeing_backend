package com.main.glory.servicesImpl;

import com.main.glory.Dao.machine.BoilerMachineRecordDao;
import com.main.glory.model.ConstantFile;
import com.main.glory.model.machine.AddMachineInfo.AddBoilerInfo;
import com.main.glory.model.machine.AddMachineInfo.AddBoilerMachineRecord;
import com.main.glory.model.machine.BoilerMachineRecord;
import com.main.glory.model.machine.MachineMast;
import com.main.glory.model.machine.UpdateMachineInfo.UpdateBoilerRecord;
import com.main.glory.model.machine.UpdateMachineInfo.UpdateBoilerRecordList;
import com.main.glory.model.machine.request.BoilerRecordBasedOnFilter;
import com.main.glory.model.machine.request.GetRecordBasedOnFilter;
import com.main.glory.model.machine.response.BoilerFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service("boilerServiceImpl")
public class BoilerRecordImpl {


    @Autowired
    MachineServiceImpl machineService;

    @Autowired
    BoilerMachineRecordDao boilerMachineRecordDao;

    ConstantFile constantFile;

    public void saveMachine(AddBoilerInfo boilerMachineRecordList) throws Exception {



        try {
            List<BoilerMachineRecord> list = new ArrayList<>();
            for (AddBoilerMachineRecord boilerMachineRecord : boilerMachineRecordList.getBoilerMachineRecord()) {
                //check the machine is exist or not
                MachineMast machineMastExist = machineService.getMachineByMachineId(boilerMachineRecord.getControlId());
                boilerMachineRecord.setJetRunning(boilerMachineRecordList.getJetRunning());


                //get time and
                BoilerMachineRecord boilerMachine = new BoilerMachineRecord(boilerMachineRecord);
                list.add(boilerMachine);

            }

            boilerMachineRecordDao.saveAll(list);

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public List<BoilerMachineRecord> getAllMachineRecord() throws Exception{

        List<BoilerMachineRecord> boilerMachineRecords = boilerMachineRecordDao.getAllBoilerRecord();
        if(boilerMachineRecords.isEmpty())
            throw new Exception(constantFile.Machine_Data_Found);

        return boilerMachineRecords;
    }

    public List<BoilerFilter> getDataBasedOnFilter(GetRecordBasedOnFilter record) throws Exception {

        String attribute = record.getAttribute();//based on attribute
        SimpleDateFormat datetimeFormatter1 = new SimpleDateFormat(
                "yyyy-MM-dd");

        Date fromDate = datetimeFormatter1.parse(record.getFromDate());

        Date toDate = datetimeFormatter1.parse(record.getToDate());
        List<BoilerFilter> boilerDataList = new ArrayList<>();

        List<BoilerMachineRecord> boilerMachineRecord;
        switch (attribute) {

                case "streamPressusre":

                    boilerMachineRecord = boilerMachineRecordDao.findByControlIdAndDate(record.getControlId(),record.getFromTime(),record.getToTime(),toDate,fromDate);

                    //List<BoilerMachineRecord> boilerMachineRecord = boilerMachineRecordDao.findByControlId(record.getControlId());

                    if(boilerMachineRecord.isEmpty())
                        throw new Exception("data not found");

                    for(BoilerMachineRecord boilerMachineRecord1:boilerMachineRecord)
                    {
                        if(boilerMachineRecord1.getStreamPressusre()!=null)
                        {

                            BoilerFilter boilerFilter=new BoilerFilter(boilerMachineRecord1,boilerMachineRecord1.getStreamPressusre());
                            boilerDataList.add(boilerFilter);
                        }

                    }

                    return boilerDataList;

                case "drumWaterLevel":
                    boilerMachineRecord = boilerMachineRecordDao.findByControlIdAndDate(record.getControlId(),record.getFromTime(),record.getToTime(),toDate,fromDate);

                    //List<BoilerMachineRecord> boilerMachineRecord = boilerMachineRecordDao.findByControlId(record.getControlId());

                    if(boilerMachineRecord.isEmpty())
                        throw new Exception("data not found");

                    for(BoilerMachineRecord boilerMachineRecord1:boilerMachineRecord)
                    {
                        if(boilerMachineRecord1.getDrumWaterLevel()!=null)
                        {
                            BoilerFilter boilerFilter=new BoilerFilter(boilerMachineRecord1,boilerMachineRecord1.getDrumWaterLevel());
                            boilerDataList.add(boilerFilter);
                        }

                    }

                    return boilerDataList;

                case "feedPump":
                    boilerMachineRecord = boilerMachineRecordDao.findByControlIdAndDate(record.getControlId(),record.getFromTime(),record.getToTime(),toDate,fromDate);

                //List<BoilerMachineRecord> boilerMachineRecord = boilerMachineRecordDao.findByControlId(record.getControlId());

                    if(boilerMachineRecord.isEmpty())
                        throw new Exception("data not found");

                    for(BoilerMachineRecord boilerMachineRecord1:boilerMachineRecord)
                    {
                        if(boilerMachineRecord1.getFeedPump()!=null)
                        {
                            BoilerFilter boilerFilter=new BoilerFilter(boilerMachineRecord1,boilerMachineRecord1.getFeedPump());
                            boilerDataList.add(boilerFilter);

                        }

                    }

                    return boilerDataList;

            case "flueGasTemp":
                boilerMachineRecord = boilerMachineRecordDao.findByControlIdAndDate(record.getControlId(),record.getFromTime(),record.getToTime(),toDate,fromDate);

                //List<BoilerMachineRecord> boilerMachineRecord = boilerMachineRecordDao.findByControlId(record.getControlId());

                if(boilerMachineRecord.isEmpty())
                    throw new Exception("data not found");

                for(BoilerMachineRecord boilerMachineRecord1:boilerMachineRecord)
                {
                    if(boilerMachineRecord1.getFlueGasTemp()!=null)
                    {
                        BoilerFilter boilerFilter=new BoilerFilter(boilerMachineRecord1,boilerMachineRecord1.getFlueGasTemp());
                        boilerDataList.add(boilerFilter);
                    }

                }

                return boilerDataList;

            case "bedTemp":
                boilerMachineRecord = boilerMachineRecordDao.findByControlIdAndDate(record.getControlId(),record.getFromTime(),record.getToTime(),toDate,fromDate);

                //List<BoilerMachineRecord> boilerMachineRecord = boilerMachineRecordDao.findByControlId(record.getControlId());

                if(boilerMachineRecord.isEmpty())
                    throw new Exception("data not found");

                for(BoilerMachineRecord boilerMachineRecord1:boilerMachineRecord)
                {
                    if(boilerMachineRecord1.getBedTemp()!=null)
                    {
                        BoilerFilter boilerFilter=new BoilerFilter(boilerMachineRecord1,boilerMachineRecord1.getBedTemp());
                        boilerDataList.add(boilerFilter);
                    }

                }

                return boilerDataList;

            case "draftPressure":
                boilerMachineRecord = boilerMachineRecordDao.findByControlIdAndDate(record.getControlId(),record.getFromTime(),record.getToTime(),toDate,fromDate);

                //List<BoilerMachineRecord> boilerMachineRecord = boilerMachineRecordDao.findByControlId(record.getControlId());

                if(boilerMachineRecord.isEmpty())
                    throw new Exception("data not found");

                for(BoilerMachineRecord boilerMachineRecord1:boilerMachineRecord)
                {
                    if(boilerMachineRecord1.getDraftPressure()!=null)
                    {
                        BoilerFilter boilerFilter=new BoilerFilter(boilerMachineRecord1,boilerMachineRecord1.getDraftPressure());
                        boilerDataList.add(boilerFilter);
                    }

                }

                return boilerDataList;

            case "idFan":
                boilerMachineRecord = boilerMachineRecordDao.findByControlIdAndDate(record.getControlId(),record.getFromTime(),record.getToTime(),toDate,fromDate);

                //List<BoilerMachineRecord> boilerMachineRecord = boilerMachineRecordDao.findByControlId(record.getControlId());

                if(boilerMachineRecord.isEmpty())
                    throw new Exception("data not found");

                for(BoilerMachineRecord boilerMachineRecord1:boilerMachineRecord)
                {
                    if(boilerMachineRecord1.getIdFan()!=null)
                    {
                        BoilerFilter boilerFilter=new BoilerFilter(boilerMachineRecord1,boilerMachineRecord1.getDaOne());
                        boilerDataList.add(boilerFilter);
                    }

                }

                return boilerDataList;

            case "jetRunning":
                boilerMachineRecord = boilerMachineRecordDao.findByControlIdAndDate(record.getControlId(),record.getFromTime(),record.getToTime(),toDate,fromDate);

                //List<BoilerMachineRecord> boilerMachineRecord = boilerMachineRecordDao.findByControlId(record.getControlId());

                if(boilerMachineRecord.isEmpty())
                    throw new Exception("data not found");

                for(BoilerMachineRecord boilerMachineRecord1:boilerMachineRecord)
                {
                    if(boilerMachineRecord1.getJetRunning()!=null)
                    {
                        BoilerFilter boilerFilter=new BoilerFilter(boilerMachineRecord1,boilerMachineRecord1.getJetRunning());
                        boilerDataList.add(boilerFilter);
                    }

                }

                return boilerDataList;

            case "daOne":
                boilerMachineRecord = boilerMachineRecordDao.findByControlIdAndDate(record.getControlId(),record.getFromTime(),record.getToTime(),toDate,fromDate);

                //List<BoilerMachineRecord> boilerMachineRecord = boilerMachineRecordDao.findByControlId(record.getControlId());

                if(boilerMachineRecord.isEmpty())
                    throw new Exception("data not found");

                for(BoilerMachineRecord boilerMachineRecord1:boilerMachineRecord)
                {
                    if(boilerMachineRecord1.getDaOne()!=null)
                    {
                        BoilerFilter boilerFilter=new BoilerFilter(boilerMachineRecord1,boilerMachineRecord1.getJetRunning());
                        boilerDataList.add(boilerFilter);
                    }

                }

                return boilerDataList;


            case "daTwo":
                boilerMachineRecord = boilerMachineRecordDao.findByControlIdAndDate(record.getControlId(),record.getFromTime(),record.getToTime(),toDate,fromDate);

                //List<BoilerMachineRecord> boilerMachineRecord = boilerMachineRecordDao.findByControlId(record.getControlId());

                if(boilerMachineRecord.isEmpty())
                    throw new Exception("data not found");

                for(BoilerMachineRecord boilerMachineRecord1:boilerMachineRecord)
                {
                    if(boilerMachineRecord1.getDaTwo()!=null)
                    {
                        BoilerFilter boilerFilter=new BoilerFilter(boilerMachineRecord1,boilerMachineRecord1.getDaTwo());
                        boilerDataList.add(boilerFilter);
                    }

                }

                return boilerDataList;

            case "daThree":
                boilerMachineRecord = boilerMachineRecordDao.findByControlIdAndDate(record.getControlId(),record.getFromTime(),record.getToTime(),toDate,fromDate);

                //List<BoilerMachineRecord> boilerMachineRecord = boilerMachineRecordDao.findByControlId(record.getControlId());

                if(boilerMachineRecord.isEmpty())
                    throw new Exception("data not found");

                for(BoilerMachineRecord boilerMachineRecord1:boilerMachineRecord)
                {
                    if(boilerMachineRecord1.getDaThree()!=null)
                    {
                        BoilerFilter boilerFilter=new BoilerFilter(boilerMachineRecord1,boilerMachineRecord1.getDaThree());
                        boilerDataList.add(boilerFilter);
                    }

                }

                return boilerDataList;


            case "screwFeeder":
                boilerMachineRecord = boilerMachineRecordDao.findByControlIdAndDate(record.getControlId(),record.getFromTime(),record.getToTime(),toDate,fromDate);

                //List<BoilerMachineRecord> boilerMachineRecord = boilerMachineRecordDao.findByControlId(record.getControlId());

                if(boilerMachineRecord.isEmpty())
                    throw new Exception("data not found");

                for(BoilerMachineRecord boilerMachineRecord1:boilerMachineRecord)
                {
                    if(boilerMachineRecord1.getScrewFeeder()!=null)
                    {
                        BoilerFilter boilerFilter=new BoilerFilter(boilerMachineRecord1,boilerMachineRecord1.getScrewFeeder());
                        boilerDataList.add(boilerFilter);
                    }

                }

                return boilerDataList;

            case "waterMeter":
                boilerMachineRecord = boilerMachineRecordDao.findByControlIdAndDate(record.getControlId(),record.getFromTime(),record.getToTime(),toDate,fromDate);

                //List<BoilerMachineRecord> boilerMachineRecord = boilerMachineRecordDao.findByControlId(record.getControlId());

                if(boilerMachineRecord.isEmpty())
                    throw new Exception("data not found");

                for(BoilerMachineRecord boilerMachineRecord1:boilerMachineRecord)
                {
                    if(boilerMachineRecord1.getWaterMeter()!=null)
                    {
                        BoilerFilter boilerFilter=new BoilerFilter(boilerMachineRecord1,boilerMachineRecord1.getWaterMeter());
                        boilerDataList.add(boilerFilter);
                    }

                }

                return boilerDataList;

            case "loadData":
                boilerMachineRecord = boilerMachineRecordDao.findByControlIdAndDate(record.getControlId(),record.getFromTime(),record.getToTime(),toDate,fromDate);

                //List<BoilerMachineRecord> boilerMachineRecord = boilerMachineRecordDao.findByControlId(record.getControlId());

                if(boilerMachineRecord.isEmpty())
                    throw new Exception("data not found");

                for(BoilerMachineRecord boilerMachineRecord1:boilerMachineRecord)
                {
                    if(boilerMachineRecord1.getLoadData()!=null)
                    {
                        BoilerFilter boilerFilter=new BoilerFilter(boilerMachineRecord1,boilerMachineRecord1.getLoadData());
                        boilerDataList.add(boilerFilter);
                    }

                }

                return boilerDataList;


            default:
                    throw new Exception("no data found");
            }


        }

    public List<BoilerMachineRecord> getBoilerRecordBasedOnFilter(BoilerRecordBasedOnFilter filter) throws Exception {

        try {
            List<BoilerMachineRecord> list = new ArrayList<>();
            SimpleDateFormat datetimeFormatter1 = new SimpleDateFormat(
                    "yyyy-MM-dd");

            Date fromDate = datetimeFormatter1.parse(filter.getDate());
            String shift = filter.getShift();
            if (shift.isEmpty())
                throw new Exception("shift can't be empty");

            MachineMast boilerExist = machineService.getMachineByMachineId(filter.getBoilerId());

            Long fromTime;
            Long toTime;

            switch (shift) {
                case "day":
                    fromTime = 10l;
                    toTime = 20l;
                    list = boilerMachineRecordDao.findRecordBasedOnFilter(filter.getBoilerId(), fromDate, fromTime, toTime);
                    break;
                case "night":

                    fromTime = 22l;
                    toTime = 8l;
                    list = boilerMachineRecordDao.findRecordBasedOnFilterForNight(filter.getBoilerId(), fromDate, fromTime, toTime);
                    break;
                default:
                    throw new Exception("no data found");


            }
            if(list.isEmpty())
                throw new Exception("no data found");

            return list;

        }catch (Exception e)
        {
            e.printStackTrace();
        }



        return null;
    }

    public boolean updateRecord(UpdateBoilerRecord record) throws Exception {

        List<BoilerMachineRecord> listToUpdate=new ArrayList<>();


        int i=0;
        for(UpdateBoilerRecordList updateBoilerRecord:record.getRecordList())
        {
            Optional<BoilerMachineRecord> recordExist = boilerMachineRecordDao.findById(updateBoilerRecord.getId());

            if(recordExist.isEmpty())
                throw new Exception("no record find with id:"+updateBoilerRecord.getId());

            if(new Date(System.currentTimeMillis()).getDate()==recordExist.get().getDateToEnter().getDate() && new Date(System.currentTimeMillis()).getMonth()==recordExist.get().getDateToEnter().getMonth())
            {
                //date + time + 30 minutes
                Long timeToCheck=recordExist.get().getDateToEnter().getTime()+TimeUnit.HOURS.toMillis(recordExist.get().getTimeOf())+TimeUnit.MINUTES.toMillis(30);

                //check the time with current time
                if(timeToCheck>System.currentTimeMillis())
                    throw new Exception("data can't updated because of time exceed");

                BoilerMachineRecord boilerMachineRecord=new BoilerMachineRecord(updateBoilerRecord,record.getJetRunning());
                boilerMachineRecord.setCreatedDate(recordExist.get().getCreatedDate());
                listToUpdate.add(boilerMachineRecord);
                i++;
            }
            else throw new Exception("data can't updated because of date exceed");


        }
        if(listToUpdate.isEmpty())
        {
            return false;
        }
        else
        {
            boilerMachineRecordDao.saveAll(listToUpdate);
            return true;
        }

    }
}


