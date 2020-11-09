package com.main.glory.controller;


import com.main.glory.config.ControllerConfig;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.batch.BatchMast;
import com.main.glory.model.user.UserData;
import com.main.glory.model.user.UserRequest;
import com.main.glory.model.user.response.LoginResponse;
import com.main.glory.servicesImpl.UserServiceImpl;
import com.main.glory.utils.JwtUtil;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController extends ControllerConfig {

    private UserServiceImpl userService;

    private JwtUtil jwtUtil;

    @Autowired
    public UserController(UserServiceImpl userService, JwtUtil jwtUtil){
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/user/{id}")
    public GeneralResponse<UserData> getUserById(@PathVariable(value = "id") Long id)
    {
        if(id!=null)
        {
            var userObj=userService.getUserById(id);
            if(userObj!=null)
            {
                return new GeneralResponse<UserData>(userObj, "Fetch Success", true, System.currentTimeMillis(), HttpStatus.FOUND);
            }
            return new GeneralResponse<UserData>(null, "No such id", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
        }
        return new GeneralResponse<>(null, "Null Id Passed!", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/user")
    public GeneralResponse<Boolean> createUser(@RequestBody UserData userData) throws Exception{

        try{
            int flag = userService.createUser(userData);
            if(flag==1){
            return new GeneralResponse<>(true,"User created successfully", true, System.currentTimeMillis(), HttpStatus.OK);
            }
            else
            {
                return new GeneralResponse<>(true,"User not created successfully", true, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
            }
            }
        catch (Exception e){
            e.printStackTrace();
            return new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public GeneralResponse<LoginResponse> checkUser(@RequestBody UserRequest userData) throws Exception{

        try{
            var user = userService.checkUser(userData.getUserName(),userData.getPassword());
            if(user!=null){
                LoginResponse loginResponse = new LoginResponse();
                var token = jwtUtil.generateToken(user, "accessToken");
                loginResponse.setAccessToken(token);
                token = jwtUtil.generateToken(user, "refreshToken");
                loginResponse.setRefreshToken(token);
                return new GeneralResponse<>(loginResponse,"successfully logged in", true, System.currentTimeMillis(), HttpStatus.OK);
            }
            else
            {
                return new GeneralResponse<>(null,"Wrong Creds", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
        }
    }


}
