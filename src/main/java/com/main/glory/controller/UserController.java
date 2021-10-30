package com.main.glory.controller;


import com.main.glory.config.ControllerConfig;
import com.main.glory.filters.FilterResponse;
import com.main.glory.model.ConstantFile;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.StockDataBatchData.request.GetBYPaginatedAndFiltered;
import com.main.glory.model.user.Request.UserAddRequest;
import com.main.glory.model.user.Request.UserIdentification;
import com.main.glory.model.user.Request.UserUpdateRequest;
import com.main.glory.model.user.UserData;
import com.main.glory.model.user.UserRequest;
import com.main.glory.model.user.response.GetAllOperator;
import com.main.glory.model.user.response.LoginResponse;
import com.main.glory.model.user.response.getAllUserInfo;
import com.main.glory.servicesImpl.LogServiceImpl;
import com.main.glory.servicesImpl.UserServiceImpl;
import com.main.glory.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController extends ControllerConfig {

    @Value("${spring.application.debugAll}")
    Boolean debugAll;


    @Autowired
    LogServiceImpl logService;


    @Autowired
    HttpServletRequest request;

    private UserServiceImpl userService;

    private JwtUtil jwtUtil;

    @Autowired
    public UserController(UserServiceImpl userService, JwtUtil jwtUtil){
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<GeneralResponse<UserData,String>> getUserById(@PathVariable(value = "id") Long id, @RequestHeader Map<String,String> headers) throws IllegalAccessException {
        UserData userObj=null;
        GeneralResponse<UserData,String> result;
        if(id!=null)
        {
            userObj=userService.getUserById(id);
            if(userObj!=null)
            {
                result = new GeneralResponse<>(userObj, ConstantFile.User_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());

            }
            else
            result = new GeneralResponse<>(null, ConstantFile.User_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,debugAll);
        }
        else {
            result = new GeneralResponse<>(null, ConstantFile.Null_Record_Passed, false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,true);
        }

        ///logService.saveRequestResponse(request,result,headers,userObj);
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }
    @GetMapping("/user/getByDepartmentId")
    public ResponseEntity<GeneralResponse<List<UserData>,String>> getUserByDepartmentId(@RequestParam(name = "departmentId") Long departmentId)
    {
        GeneralResponse<List<UserData>,String> result;
        try {

            if (departmentId != null) {
                List<UserData> userObj = userService.getAllUserByDepartmentId(departmentId);
                if (userObj != null) {
                    result = new GeneralResponse<>(userObj, ConstantFile.User_Exist, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI()+"?"+request.getQueryString());
                } else
                    result = new GeneralResponse<>(null, ConstantFile.User_Not_Exist, false, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI()+"?"+request.getQueryString());
                logService.saveLog(result, request, debugAll);
            } else {
               throw new Exception(ConstantFile.Null_Record_Passed);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result, request, true);
        }

        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }
    @GetMapping("/user/getAllOperator/all")
    public ResponseEntity<GeneralResponse<List<GetAllOperator>,String>> getAllOperator() throws Exception {

        GeneralResponse<List<GetAllOperator>,String> result;
        try {
            List<GetAllOperator> userObj = userService.getAllOperator();
            if (userObj != null) {
                result = new GeneralResponse<>(userObj, ConstantFile.User_Found, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI()+"?"+request.getQueryString());
                logService.saveLog(result, request, debugAll);
            } else {
                throw new Exception(ConstantFile.Null_Record_Passed);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result, request, true);
        }
            return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping("/userHead")
    public ResponseEntity<GeneralResponse<List<getAllUserInfo>,String>> getAllHead(@RequestHeader Map<String, String> headers)
    {
        GeneralResponse<List<getAllUserInfo>,String> result;
        try {
            List<getAllUserInfo> data = userService.getAllHeadUser(headers.get("id"));
            if (!data.isEmpty()) {
                result = new GeneralResponse<>(data, ConstantFile.User_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            } else {
                result = new GeneralResponse<>(null, ConstantFile.User_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            }
            logService.saveLog(result,request,debugAll);
        }catch (Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }
    @GetMapping("/user/{username}/{id}")
    public ResponseEntity<GeneralResponse<Boolean,String>> getUserNameExist(@PathVariable(name = "username")String username,@PathVariable(name = "id")Long id,@RequestHeader Map<String,String> headers) throws IllegalAccessException {
        GeneralResponse<Boolean,String> result;
        try {
            Boolean data = userService.getUserNameExist(username,id);
            if (data==true) {
                result = new GeneralResponse<>(data, ConstantFile.User_Exist, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            } else {
                result = new GeneralResponse<>(data, ConstantFile.User_Not_Exist, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            }
            logService.saveLog(result,request,debugAll);
        }catch (Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,true);
        }

        //logService.saveLog(result,request);
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping("/user/AllUsers/{getBy}/{id}")
    public ResponseEntity<GeneralResponse<List<getAllUserInfo>,String>> getAllUser(@PathVariable(value = "getBy")String getBy, @PathVariable(value = "id")Long id,@RequestHeader Map<String, String> headers)
    {
        GeneralResponse<List<getAllUserInfo>,String> result;
        List<getAllUserInfo> users = null;
        try{
            switch (getBy) {
                case "own":

                        users = userService.getAllUser(getBy, id,headers.get("id"));
                        if(!users.isEmpty())
                            result = new GeneralResponse<>(users, ConstantFile.User_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
                        else
                            result = new GeneralResponse<>(null, ConstantFile.User_Not_Added, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());

                        break;

                case "group":

                        users = userService.getAllUser(getBy, id,headers.get("id"));
                        if(!users.isEmpty())
                            result = new GeneralResponse<>(users, ConstantFile.User_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
                        else
                            result = new GeneralResponse<>(null, ConstantFile.User_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());


                        break;
                case "all":
                        users = userService.getAllUser(null, null,headers.get("id"));
                        if(!users.isEmpty())
                            result = new GeneralResponse<>(users, ConstantFile.User_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
                        else
                            result = new GeneralResponse<>(null, ConstantFile.User_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());

                        break;

                default:
                    result = new GeneralResponse<>(null, ConstantFile.GetBy_String_Wrong, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());

            }
        }catch(Exception e){
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,true);
        }
        logService.saveLog(result,request,debugAll);
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }


    @GetMapping("/user/AllUsers/allPaginated")
    public ResponseEntity<GeneralResponse<FilterResponse<getAllUserInfo>,String>> getAllUserPaginated(@RequestBody GetBYPaginatedAndFiltered requestParam,@RequestHeader Map<String, String> headers)
    {
        GeneralResponse<FilterResponse<getAllUserInfo>,String> result;
        FilterResponse<getAllUserInfo> users = null;
        String id=headers.get("id");
        String getBy=requestParam.getGetBy();
        try{
            switch (requestParam.getGetBy()) {
                case "own":

                        users = userService.getAllUserAllPaginated(requestParam, id);
                        if(!users.getData().isEmpty())
                            result = new GeneralResponse<>(users, ConstantFile.User_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
                        else
                            result = new GeneralResponse<>(null, ConstantFile.User_Not_Added, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());

                        break;

                case "group":

                        users = userService.getAllUserAllPaginated(requestParam, id);
                        if(!users.getData().isEmpty())
                            result = new GeneralResponse<>(users, ConstantFile.User_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
                        else
                            result = new GeneralResponse<>(null, ConstantFile.User_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());


                        break;
                case "all":
                        users = userService.getAllUserAllPaginated(requestParam, id);
                        if(!users.getData().isEmpty())
                            result = new GeneralResponse<>(users, ConstantFile.User_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
                        else
                            result = new GeneralResponse<>(null, ConstantFile.User_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());

                        break;

                default:
                    result = new GeneralResponse<>(null, ConstantFile.GetBy_String_Wrong, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());

            }
        }catch(Exception e){
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,true);
        }
        logService.saveLog(result,request,debugAll);
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }


    @PostMapping("/user")
    public ResponseEntity<GeneralResponse<Boolean,Object>> createUser(@RequestBody UserAddRequest userData,@RequestHeader Map<String, String> headers) throws Exception{

        GeneralResponse<Boolean,Object> result;
        try{
            userService.createUser(userData,headers.get("id"));
            result = new GeneralResponse<>(true, ConstantFile.User_Added, true, System.currentTimeMillis(), HttpStatus.OK,userData);
            logService.saveLog(result,request,debugAll);
        }
        catch (Exception e){
            e.printStackTrace();
            result = new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,userData);

            logService.saveLog(result,request,true);
        }


        //logService.saveLog(result,request);

        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));

    }

    @PostMapping("/login")
    public ResponseEntity<GeneralResponse<LoginResponse,Object>> login(@RequestBody UserRequest userData, @RequestHeader Map<String, String> headers) throws Exception{

        GeneralResponse<LoginResponse,Object> result;
        try{
            var user = userService.checkUser(userData.getUserName(),userData.getPassword());
            if(user!=null){
                LoginResponse loginResponse = new LoginResponse();
                var token = jwtUtil.generateToken(user, "accessToken");
                loginResponse.setAccessToken(token);
                token = jwtUtil.generateToken(user, "refreshToken");
                loginResponse.setRefreshToken(token);
                result = new GeneralResponse<>(loginResponse, ConstantFile.User_Found, true, System.currentTimeMillis(), HttpStatus.OK,userData);


            }
            else
            {
                result = new GeneralResponse<>(null, ConstantFile.User_Wrong_cred, false, System.currentTimeMillis(), HttpStatus.OK,userData);
            }
            logService.saveLog(result,request,debugAll);
        }
        catch (Exception e){
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,userData);
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }



    @PutMapping("/user")
    public ResponseEntity<GeneralResponse<Boolean,Object>> updateUser(@RequestBody UserUpdateRequest userData) throws Exception{

        GeneralResponse<Boolean,Object> result = null;
        try{
            int flag = userService.isAvailable(userData);

            if(flag==1){
                result = new GeneralResponse<>(true, ConstantFile.User_Updated, true, System.currentTimeMillis(), HttpStatus.OK,userData);
            }

            logService.saveLog(result,request,debugAll);
        }
        catch (Exception e){
            e.printStackTrace();
            result = new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,userData);
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }


    @DeleteMapping(value="/user/{id}")
    public ResponseEntity<GeneralResponse<Boolean,Object>> deleteUserDetailsByID(@PathVariable(value = "id") Long id) throws Exception {
        GeneralResponse<Boolean,Object> result;
        try {
            if (id != null) {
                boolean flag = userService.deleteUserById(id);
                if (flag) {
                    result = new GeneralResponse<>(true, ConstantFile.User_Deleted, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
                } else {
                    result = new GeneralResponse<>(false, ConstantFile.User_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
                }
            } else
                throw new Exception(ConstantFile.Null_Record_Passed);
                //result = new GeneralResponse<>(false, CommonMessage.Null_Record_Passed, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,debugAll);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }


    //identify the user
    @GetMapping(value="/user/getUserHeadDetailById/{id}")
    public ResponseEntity<GeneralResponse<UserIdentification,Object>> getUserHeadDetailById(@PathVariable(value = "id") Long id)
    {
        GeneralResponse<UserIdentification,Object> response;
        try {

            UserIdentification userIdentification = userService.getUserHeadDetail(id);
            if(userIdentification==null)
            {
                response= new GeneralResponse<>(userIdentification, ConstantFile.User_Not_Exist, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            }
            else
            response= new GeneralResponse<>(userIdentification, ConstantFile.User_Exist, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(response,request,debugAll);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            response = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI()+"?"+request.getQueryString());
            logService.saveLog(response,request,true);
        }
        return new ResponseEntity<>(response,HttpStatus.valueOf(response.getStatusCode()));
    }

}
