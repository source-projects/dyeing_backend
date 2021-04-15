package com.main.glory.controller;

import com.main.glory.Dao.TestingDao;
import com.main.glory.config.ControllerConfig;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.testing.Testing;
import com.main.glory.model.user.Request.UserAddRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TestingController extends ControllerConfig {

    @Autowired
    TestingDao testingDao;

    /*@PutMapping("/testing/{records}")
    public GeneralResponse<Boolean> createRecord(@PathVariable(name = "records") Long records) throws Exception{
        GeneralResponse<Boolean> result;
        for(int i=0;i<records;i++)
        {
            Testing testing = new Testing("test"+i);
            testingDao.save(testing);
        }
        result= new GeneralResponse<>(true, "added", true, System.currentTimeMillis(), HttpStatus.OK);

        return result;
    }

    @GetMapping("/testing")
    public GeneralResponse<List<Testing>> getRecords() throws Exception{

        return new GeneralResponse<>(testingDao.findAll(), "fetched", true, System.currentTimeMillis(), HttpStatus.OK);

    }

    @GetMapping("/testing/dateDemo")
    public GeneralResponse<Void> getRecordsDate() throws Exception{


        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date start= sdf.parse("2021-01-31");
        Date endDates= sdf.parse("2021-12-31");
        calendar.setTime(start);

        List<Integer> month = new ArrayList<>();
        month.add(2);
        month.add(4);
        month.add(6);
        month.add(9);
        month.add(11);
        for(Date d = calendar.getTime();d.before(endDates);calendar.add(Calendar.MONTH,1))
        {
            //calendar.getTime().getDate()

        }

        return null;

    }

    *//*@GetMapping("/testing/")
    public GeneralResponse<Boolean> callThePdf() throws Exception{
        GeneralResponse<Boolean> result;

        result= new GeneralResponse<>(true, "done", true, System.currentTimeMillis(), HttpStatus.OK);

        return result;
    }*//*
*/
}
