package com.main.glory.servicesImpl;

import com.main.glory.Dao.log.APILogDao;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.log.APILog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.Map;

@Service("logServiceImpl")
public class LogServiceImpl<T,D> {

    @Autowired
    APILogDao APILogDao;

    public void saveRequestResponse(HttpServletRequest request, GeneralResponse<T> result, Map<String, String> headers,D record) throws IllegalAccessException {
        APILog errorLog = new APILog(request,result,headers);

        String requestObject="";
            //fields
            Field[] fields = record.getClass().getDeclaredFields();
            for(Field field:fields)
            {
                field.setAccessible(true);
                Object value = field.get(record);
                requestObject +=field.getName()+"-"+value;
            }

            errorLog.setRequestBody(requestObject);
            APILogDao.save(errorLog);
        }



}
