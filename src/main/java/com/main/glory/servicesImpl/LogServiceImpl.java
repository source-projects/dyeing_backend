package com.main.glory.servicesImpl;

import aj.org.objectweb.asm.TypeReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.main.glory.Dao.log.APILogDao;
import com.main.glory.model.CommonMessage;
import com.main.glory.model.DemoGeneral;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.log.APILog;
import com.main.glory.model.user.UserData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("logServiceImpl")
public class LogServiceImpl<T,D> {

    @Autowired
    APILogDao apiLogDao;

    public void saveLog(GeneralResponse<T,D> result, HttpServletRequest request,Boolean debugAll)  {
        try {

            Date requestDate = new Date(Long.parseLong(request.getAttribute("requestTime").toString()));
            Date responseDate = new Date(System.currentTimeMillis());
            if (debugAll == true) {
                ObjectMapper mapper = new ObjectMapper();
                APILog apiLog = new APILog(result, request);
                String jsonRequestString = mapper.writeValueAsString(result.getData());
                apiLog.setResponseBody(jsonRequestString);
                jsonRequestString = mapper.writeValueAsString(result.getRequestBody());
                apiLog.setRequestBody(jsonRequestString);
                //System.out.println(result.getRequestBody().toString());
                apiLog.setResponseDate(responseDate);
                apiLog.setRequestDate(requestDate);
                apiLog.setServeInMicroSec(responseDate.getTime()- requestDate.getTime());
                apiLog.setServeInSec(((responseDate.getTime()- requestDate.getTime())/1000)%60);
                apiLogDao.save(apiLog);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    /*public void saveLog(DemoGeneral<T, D> result, HttpServletRequest request,Boolean debugAll) {
        if(debugAll==true) {
            APILog apiLog = new APILog(result, request);
            System.out.println(result.getRequestBody().toString());
            apiLogDao.save(apiLog);
        }
    }*/
}
