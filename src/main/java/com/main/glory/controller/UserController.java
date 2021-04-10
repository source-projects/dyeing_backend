package com.main.glory.controller;


import com.main.glory.config.ControllerConfig;
import com.main.glory.model.DemoGeneral;
import com.main.glory.model.DemoGeneral;
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
    public ResponseEntity<DemoGeneral<UserData,String>> getUserById(@PathVariable(value = "id") Long id, @RequestHeader Map<String,String> headers) throws IllegalAccessException {
        UserData userObj=null;
        DemoGeneral<UserData,String> result;
        if(id!=null)
        {
            userObj=userService.getUserById(id);
            if(userObj!=null)
            {
                result = new DemoGeneral<>(userObj, "Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI().toString());

            }
            else
            result = new DemoGeneral<>(null, "No such id", false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI().toString());
            logService.saveLog(result,request,debugAll);
        }
        else {
            result = new DemoGeneral<>(null, "Null Id Passed!", false, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI());
            logService.saveLog(result,request,true);
        }

        ///logService.saveRequestResponse(request,result,headers,userObj);
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }
    @GetMapping("/user/getByDepartmentId")
    public ResponseEntity<DemoGeneral<List<UserData>,String>> getUserByDepartmentId(@RequestParam(name = "departmentId") Long departmentId)
    {
        DemoGeneral<List<UserData>,String> result;
        if(departmentId!=null)
        {
            List<UserData> userObj=userService.getAllUserByDepartmentId(departmentId);
            if(userObj!=null)
            {
                result = new DemoGeneral<>(userObj, "Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            }
            else
                result = new DemoGeneral<>(null, "No such record found", false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,debugAll);
        }
        else {
            result = new DemoGeneral<>(null, "Null Id Passed!", false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,true);
        }

        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }
    @GetMapping("/user/getAllOperator/all")
    public ResponseEntity<DemoGeneral<List<GetAllOperator>,String>> getAllOperator() throws Exception {

        DemoGeneral<List<GetAllOperator>,String> result;
        List<GetAllOperator> userObj=userService.getAllOperator();
            if(userObj!=null)
            {
                result = new DemoGeneral<>(userObj, "Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
                logService.saveLog(result,request,debugAll);
            }
            else {
                result = new DemoGeneral<>(null, "Null Id Passed!", false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
                logService.saveLog(result,request,true);
            }
            return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping("/userHead")
    public ResponseEntity<DemoGeneral<List<getAllUserInfo>,String>> getAllHead(@RequestHeader Map<String, String> headers)
    {
        DemoGeneral<List<getAllUserInfo>,String> result;
        try {
            var data = userService.getAllHeadUser(headers.get("id"));
            if (data != null) {
                result = new DemoGeneral<>(data, "User Head Fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            } else {
                result = new DemoGeneral<>(null, "User Head Not Available ", false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            }
            logService.saveLog(result,request,debugAll);
        }catch (Exception e)
        {
            result = new DemoGeneral<>(null, "User Head Not Available ", false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }
    @GetMapping("/user/{username}/{id}")
    public ResponseEntity<DemoGeneral<Boolean,String>> getUserNameExist(@PathVariable(name = "username")String username,@PathVariable(name = "id")Long id,@RequestHeader Map<String,String> headers) throws IllegalAccessException {
        DemoGeneral<Boolean,String> result;
        try {
            Boolean data = userService.getUserNameExist(username,id);
            if (data) {
                result = new DemoGeneral<>(data, "Username found", true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            } else {
                result = new DemoGeneral<>(null, "Username not found ", false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            }
            logService.saveLog(result,request,debugAll);
        }catch (Exception e)
        {
            e.printStackTrace();
            result = new DemoGeneral<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,true);
        }

        //logService.saveLog(result,request);
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping("/user/AllUsers/{getBy}/{id}")
    public ResponseEntity<DemoGeneral<List<getAllUserInfo>,String>> getAllUser(@PathVariable(value = "getBy")String getBy, @PathVariable(value = "id")Long id,@RequestHeader Map<String, String> headers)
    {
        DemoGeneral<List<getAllUserInfo>,String> result;
        List<getAllUserInfo> users = null;
        try{
            switch (getBy) {
                case "own":

                        users = userService.getAllUser(getBy, id,headers.get("id"));
                        if(!users.isEmpty())
                            result = new DemoGeneral<>(users, "User Fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
                        else
                            result = new DemoGeneral<>(null, "No data found", false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());

                        break;

                case "group":

                        users = userService.getAllUser(getBy, id,headers.get("id"));
                        if(!users.isEmpty())
                            result = new DemoGeneral<>(users, "User Fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
                        else
                            result = new DemoGeneral<>(null, "No data found", false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());


                        break;
                case "all":
                        users = userService.getAllUser(null, null,headers.get("id"));
                        if(!users.isEmpty())
                            result = new DemoGeneral<>(users, "User Fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
                        else
                            result = new DemoGeneral<>(null, "No data added yet", false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());

                        break;

                default:
                    result = new DemoGeneral<>(null, "GetBy string is wrong", false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
                    logService.saveLog(result,request,debugAll);
            }
        }catch(Exception e){
            result = new DemoGeneral<>(null, "Internal Server Error", false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @PostMapping("/user")
    public ResponseEntity<DemoGeneral<Boolean,Object>> createUser(@RequestBody UserAddRequest userData,@RequestHeader Map<String, String> headers) throws Exception{

        DemoGeneral<Boolean,Object> result;
        try{
            userService.createUser(userData,headers.get("id"));
            result = new DemoGeneral<>(true,"User created successfully", true, System.currentTimeMillis(), HttpStatus.OK,userData);
            logService.saveLog(result,request,debugAll);
        }
        catch (Exception e){
            e.printStackTrace();
            result = new DemoGeneral<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,userData);

            logService.saveLog(result,request,true);
        }


        //logService.saveLog(result,request);

        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));

    }

    @PostMapping("/login")
    public ResponseEntity<DemoGeneral<LoginResponse,Object>> login(@RequestBody UserRequest userData, @RequestHeader Map<String, String> headers) throws Exception{

        DemoGeneral<LoginResponse,Object> result;
        try{
            var user = userService.checkUser(userData.getUserName(),userData.getPassword());
            if(user!=null){
                LoginResponse loginResponse = new LoginResponse();
                var token = jwtUtil.generateToken(user, "accessToken");
                loginResponse.setAccessToken(token);
                token = jwtUtil.generateToken(user, "refreshToken");
                loginResponse.setRefreshToken(token);
                result = new DemoGeneral<>(loginResponse,"successfully logged in", true, System.currentTimeMillis(), HttpStatus.OK,userData);
                /*System.out.println(headers.toString());
                System.out.println(request.getRequestURL());*/
                //logService.saveRequestResponse(request,result,headers,null);

            }
            else
            {
                result = new DemoGeneral<>(null,"Wrong Creds", false, System.currentTimeMillis(), HttpStatus.OK,userData);
            }
            logService.saveLog(result,request,debugAll);
        }
        catch (Exception e){
            e.printStackTrace();
            result = new DemoGeneral<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,userData);
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }



    @PutMapping("/user")
    public ResponseEntity<DemoGeneral<Boolean,Object>> updateUser(@RequestBody UserUpdateRequest userData) throws Exception{

        DemoGeneral<Boolean,Object> result;
        try{
            int flag = userService.isAvailable(userData);

            if(flag==1){
                result = new DemoGeneral<>(true,"User Updated successfully", true, System.currentTimeMillis(), HttpStatus.OK,userData);
            }
            else
            {
                result = new DemoGeneral<>(true,"User not Updated ", true, System.currentTimeMillis(), HttpStatus.OK,userData);
            }
            logService.saveLog(result,request,debugAll);
        }
        catch (Exception e){
            e.printStackTrace();
            result = new DemoGeneral<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,userData);
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }


    @DeleteMapping(value="/user/{id}")
    public ResponseEntity<DemoGeneral<Boolean,Object>> deleteUserDetailsByID(@PathVariable(value = "id") Long id) throws Exception {
        DemoGeneral<Boolean,Object> result;
        try {
            if (id != null) {
                boolean flag = userService.deleteUserById(id);
                if (flag) {
                    result = new DemoGeneral<>(true, "Deleted successfully", true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
                } else {
                    result = new DemoGeneral<>(false, "no such id found", false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
                }
            } else
                result = new DemoGeneral<>(false, "Null party object", false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,debugAll);
        }
        catch (Exception e)
        {
            result = new DemoGeneral<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(result,request,true);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }


    //identify the user
    @GetMapping(value="/user/getUserHeadDetailById/{id}")
    public ResponseEntity<DemoGeneral<UserIdentification,Object>> getUserHeadDetailById(@PathVariable(value = "id") Long id)
    {
        DemoGeneral<UserIdentification,Object> response;
        try {

            UserIdentification userIdentification = userService.getUserHeadDetail(id);
            response= new DemoGeneral<>(userIdentification, "fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(response,request,debugAll);

        }
        catch (Exception e)
        {
            response = new DemoGeneral<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
            logService.saveLog(response,request,true);
        }
        return new ResponseEntity<>(response,HttpStatus.valueOf(response.getStatusCode()));
    }

}
