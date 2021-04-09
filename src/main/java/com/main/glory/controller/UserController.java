package com.main.glory.controller;


import com.main.glory.config.ControllerConfig;
import com.main.glory.model.GeneralResponse;
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
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserController extends ControllerConfig {

    @Value("${spring.application.debugAll}")
    String debugAll;

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
    public ResponseEntity<GeneralResponse<UserData>> getUserById(@PathVariable(value = "id") Long id)
    {
        GeneralResponse<UserData> result;
        if(id!=null)
        {
            var userObj=userService.getUserById(id);
            if(userObj!=null)
            {
                result = new GeneralResponse<UserData>(userObj, "Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            }
            else
            result = new GeneralResponse<UserData>(null, "No such id", false, System.currentTimeMillis(), HttpStatus.OK);
        }
        else
        result = new GeneralResponse<>(null, "Null Id Passed!", false, System.currentTimeMillis(), HttpStatus.OK);


        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }
    @GetMapping("/user/getByDepartmentId")
    public ResponseEntity<GeneralResponse<List<UserData>>> getUserByDepartmentId(@RequestParam(name = "departmentId") Long departmentId)
    {
        GeneralResponse<List<UserData>> result;
        if(departmentId!=null)
        {
            List<UserData> userObj=userService.getAllUserByDepartmentId(departmentId);
            if(userObj!=null)
            {
                result = new GeneralResponse<>(userObj, "Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            }
            else
                result = new GeneralResponse<>(null, "No such record found", false, System.currentTimeMillis(), HttpStatus.OK);
        }
        else
            result = new GeneralResponse<>(null, "Null Id Passed!", false, System.currentTimeMillis(), HttpStatus.OK);


        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }
    @GetMapping("/user/getAllOperator/all")
    public ResponseEntity<GeneralResponse<List<GetAllOperator>>> getAllOperator() throws Exception {

        GeneralResponse<List<GetAllOperator>> result;
        List<GetAllOperator> userObj=userService.getAllOperator();
            if(userObj!=null)
            {
                result = new GeneralResponse<>(userObj, "Data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            }
            else {
                result = new GeneralResponse<>(null, "Null Id Passed!", false, System.currentTimeMillis(), HttpStatus.OK);
            }
            return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping("/userHead")
    public ResponseEntity<GeneralResponse<List<getAllUserInfo>>> getAllHead(@RequestHeader Map<String, String> headers)
    {
        GeneralResponse<List<getAllUserInfo>> result;
        try {
            var data = userService.getAllHeadUser(headers.get("id"));
            if (data != null) {
                result = new GeneralResponse<>(data, "User Head Fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            } else {
                result = new GeneralResponse<>(null, "User Head Not Available ", false, System.currentTimeMillis(), HttpStatus.OK);
            }
        }catch (Exception e)
        {
            result = new GeneralResponse<>(null, "User Head Not Available ", false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }
    @GetMapping("/user/{username}/{id}")
    public ResponseEntity<GeneralResponse<Boolean>> getUserNameExist(@PathVariable(name = "username")String username,@PathVariable(name = "id")Long id)
    {
        GeneralResponse<Boolean> result;
        try {
            Boolean data = userService.getUserNameExist(username,id);
            if (data) {
                result = new GeneralResponse<>(data, "Username found", true, System.currentTimeMillis(), HttpStatus.OK);
            } else {
                result = new GeneralResponse<>(null, "Username not found ", false, System.currentTimeMillis(), HttpStatus.OK);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @GetMapping("/user/AllUsers/{getBy}/{id}")
    public ResponseEntity<GeneralResponse<List<getAllUserInfo>>> getAllUser(@PathVariable(value = "getBy")String getBy, @PathVariable(value = "id")Long id,@RequestHeader Map<String, String> headers)
    {
        GeneralResponse<List<getAllUserInfo>> result;
        List<getAllUserInfo> users = null;
        try{
            switch (getBy) {
                case "own":

                        users = userService.getAllUser(getBy, id,headers.get("id"));
                        if(!users.isEmpty())
                            result = new GeneralResponse<>(users, "User Fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
                        else
                            result = new GeneralResponse<>(null, "No data found", false, System.currentTimeMillis(), HttpStatus.OK);

                        break;

                case "group":

                        users = userService.getAllUser(getBy, id,headers.get("id"));
                        if(!users.isEmpty())
                            result = new GeneralResponse<>(users, "User Fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
                        else
                            result = new GeneralResponse<>(null, "No data found", false, System.currentTimeMillis(), HttpStatus.OK);


                        break;
                case "all":
                        users = userService.getAllUser(null, null,headers.get("id"));
                        if(!users.isEmpty())
                            result = new GeneralResponse<>(users, "User Fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
                        else
                            result = new GeneralResponse<>(null, "No data added yet", false, System.currentTimeMillis(), HttpStatus.OK);

                        break;

                default:
                    result = new GeneralResponse<>(null, "GetBy string is wrong", false, System.currentTimeMillis(), HttpStatus.OK);
            }
        }catch(Exception e){
            result = new GeneralResponse<>(null, "Internal Server Error", false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }

    @PostMapping("/user")
    public ResponseEntity<GeneralResponse<Boolean>> createUser(@RequestBody UserAddRequest userData,@RequestHeader Map<String, String> headers) throws Exception{

        GeneralResponse<Boolean> result;
        try{
            userService.createUser(userData,headers.get("id"));
            result = new GeneralResponse<>(true,"User created successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            }
        catch (Exception e){
            e.printStackTrace();
            result = new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }


        logService.saveRequestResponse(request,result,headers,userData);
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));

    }

    @PostMapping("/login")
    public ResponseEntity<GeneralResponse<LoginResponse>> login(@RequestBody UserRequest userData, @RequestHeader Map<String, String> headers) throws Exception{

        GeneralResponse<LoginResponse> result;
        try{
            var user = userService.checkUser(userData.getUserName(),userData.getPassword());
            if(user!=null){
                LoginResponse loginResponse = new LoginResponse();
                var token = jwtUtil.generateToken(user, "accessToken");
                loginResponse.setAccessToken(token);
                token = jwtUtil.generateToken(user, "refreshToken");
                loginResponse.setRefreshToken(token);
                result = new GeneralResponse<>(loginResponse,"successfully logged in", true, System.currentTimeMillis(), HttpStatus.OK);
                /*System.out.println(headers.toString());
                System.out.println(request.getRequestURL());*/
                //logService.saveRequestResponse(request,result,headers,null);

            }
            else
            {
                result = new GeneralResponse<>(null,"Wrong Creds", false, System.currentTimeMillis(), HttpStatus.OK);
            }
        }
        catch (Exception e){
            e.printStackTrace();
            result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }



    @PutMapping("/user")
    public ResponseEntity<GeneralResponse<Boolean>> updateUser(@RequestBody UserUpdateRequest userData) throws Exception{

        GeneralResponse<Boolean> result;
        try{
            int flag = userService.isAvailable(userData);

            if(flag==1){
                result = new GeneralResponse<>(true,"User Updated successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            }
            else
            {
                result = new GeneralResponse<>(true,"User not Updated ", true, System.currentTimeMillis(), HttpStatus.OK);
            }
        }
        catch (Exception e){
            e.printStackTrace();
            result = new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }


    @DeleteMapping(value="/user/{id}")
    public ResponseEntity<GeneralResponse<Boolean>> deleteUserDetailsByID(@PathVariable(value = "id") Long id) throws Exception {
        GeneralResponse<Boolean> result;
        try {
            if (id != null) {
                boolean flag = userService.deleteUserById(id);
                if (flag) {
                    result = new GeneralResponse<Boolean>(true, "Deleted successfully", true, System.currentTimeMillis(), HttpStatus.OK);
                } else {
                    result = new GeneralResponse<Boolean>(false, "no such id found", false, System.currentTimeMillis(), HttpStatus.OK);
                }
            } else
                result = new GeneralResponse<Boolean>(false, "Null party object", false, System.currentTimeMillis(), HttpStatus.OK);
        }
        catch (Exception e)
        {
            result = new GeneralResponse<Boolean>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
    }


    //identify the user
    @GetMapping(value="/user/getUserHeadDetailById/{id}")
    public ResponseEntity<GeneralResponse<UserIdentification>> getUserHeadDetailById(@PathVariable(value = "id") Long id)
    {
        GeneralResponse<UserIdentification> response;
        try {

            UserIdentification userIdentification = userService.getUserHeadDetail(id);
            response= new GeneralResponse<>(userIdentification, "fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);

        }
        catch (Exception e)
        {
            response = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
        }
        return new ResponseEntity<>(response,HttpStatus.valueOf(response.getStatusCode()));
    }

}
