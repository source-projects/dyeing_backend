package com.main.glory.controller;

import com.main.glory.Dao.TestingDao;
import com.main.glory.config.ControllerConfig;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.testing.Testing;
import com.main.glory.model.user.Request.UserAddRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class TestingController extends ControllerConfig {

    @Autowired
    TestingDao testingDao;

    @PutMapping("/testing/{records}")
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

}
