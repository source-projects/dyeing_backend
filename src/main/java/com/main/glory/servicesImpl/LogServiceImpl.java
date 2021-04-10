package com.main.glory.servicesImpl;

import com.main.glory.Dao.log.APILogDao;
import com.main.glory.model.DemoGeneral;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.log.APILog;
import com.main.glory.model.user.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service("logServiceImpl")
public class LogServiceImpl<T,D> {

    @Autowired
    APILogDao apiLogDao;

    public void saveLog(GeneralResponse<T> result, HttpServletRequest request,Boolean debugAll) {
        if(debugAll==true) {
            APILog apiLog = new APILog(result, request);
            //System.out.println(result.getRequestBody().toString());
            apiLogDao.save(apiLog);
        }
    }
    public void saveLog(DemoGeneral<T, D> result, HttpServletRequest request,Boolean debugAll) {
        if(debugAll==true) {
            APILog apiLog = new APILog(result, request);
            System.out.println(result.getRequestBody().toString());
            apiLogDao.save(apiLog);
        }
    }
}
