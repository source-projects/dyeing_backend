package com.main.glory.model.log;

import com.google.gson.JsonObject;
import com.main.glory.model.DemoGeneral;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.user.response.LoginResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class APILog<T> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String apiName;
    String apiUrl;
    String userType;
    String deviceType;
    String device;
    String userId;
    @Column(columnDefinition = "varchar(10000)")
    String requestBody;
    @Column(columnDefinition = "varchar(10000)")
    String responseBody;
    String responseMsg;
    String method;
    Date createdDate;

    public <T,D> APILog(GeneralResponse<T,D> result, HttpServletRequest request) {
        this.apiUrl = request.getRequestURI();
        this.apiUrl =  request.getRequestURI();
        this.deviceType = request.getHeader("User-Agent");
        this.userId = request.getHeader("id")==null?null:request.getHeader("id").toString() ;
        this.method = request.getMethod();
        this.responseMsg = result.getMsg();
        this.responseBody = result.getData()==null?null:result.getData().toString();
        this.requestBody = result.getRequestBody()==null?null:result.getRequestBody().toString();
    }

    /*public <T, D> APILog(DemoGeneral<T,D> result, HttpServletRequest request) {
        this.apiUrl = request.getRequestURI();
        this.apiUrl =  request.getRequestURI();
        this.deviceType = request.getHeader("User-Agent");
        this.userId = request.getHeader("id")==null?null:request.getHeader("id").toString() ;
        this.method = request.getMethod();
        this.responseMsg = result.getMsg();
        this.responseBody = result.getData()==null?null:result.getData().toString();
        this.requestBody = result.getRequestBody().toString();
    }*/



  /*  public <T> APILog(HttpServletRequest request, GeneralResponse<T> result, Map<String, String> headers) {
        this.apiUrl = request.getRequestURI();
        this.device = headers.get("user-agent");
        this.responseBody = result.getData()==null?null:result.getData().toString();
        this.responseMsg=result.getMsg();

    }*/
    //Date updatedDate;

    @PrePersist
    public void create()
    {
        this.createdDate=new Date(System.currentTimeMillis());
    }

}
