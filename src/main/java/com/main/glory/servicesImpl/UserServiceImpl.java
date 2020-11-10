package com.main.glory.servicesImpl;

import com.main.glory.Dao.user.UserDao;
import com.main.glory.model.user.UserData;
import com.main.glory.services.UserServiceInterface;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service("userServiceImpl")
public class UserServiceImpl implements UserServiceInterface {

    @Autowired
    UserDao userDao;


    public UserData getUserById(Long id) {
        var data=userDao.findById(id);
        if(data.isEmpty())
            return null;
        else
            return data.get();
    }


    public int createUser(UserData userData) {

        if(userData!=null) {System.out.println(userData.toString());
            userDao.saveAndFlush(userData);
            return 1;
        }
        else
        {
            return 0;
        }

    }

    public UserData checkUser(String userName,String password) {
        return userDao.findByUserNameAndPassword(userName, password);
    }

}
