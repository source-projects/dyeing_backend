package com.main.glory.servicesImpl;

import com.main.glory.Dao.designation.DesignationDao;
import com.main.glory.Dao.user.UserDao;
import com.main.glory.model.designation.Designation;
import com.main.glory.model.quality.Quality;
import com.main.glory.model.user.Request.UserAddRequest;
import com.main.glory.model.user.UserData;
import com.main.glory.services.UserServiceInterface;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service("userServiceImpl")
public class UserServiceImpl implements UserServiceInterface {

    @Autowired
    UserDao userDao;

    @Autowired
    private DesignationServiceImpl designationService;

    @Autowired
    ModelMapper modelMapper;


    public UserData getUserById(Long id) {
        var data=userDao.findById(id);
        if(data.isEmpty())
            return null;
        else
            return data.get();
    }


    public int createUser(UserAddRequest userDataDto) {
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        UserData userData =  modelMapper.map(userDataDto, UserData.class);

        Optional<UserData> data = userDao.findById(userData.getId());

        if(!data.isPresent()) {
            Optional<Designation> designationData = designationService.getDesignationById(userDataDto.getDesignationId());
            if(designationData.isPresent())
            {
                userData.setDesignationId(designationData.get());
                //System.out.println(userData.toString());
                userDao.saveAndFlush(userData);
                return 1;
            }


        }
        else
        {
            return 0;
        }

        return 0;
    }

    public UserData checkUser(String userName,String password) {
        return userDao.findByUserNameAndPassword(userName, password);
    }

    public List<UserData> getAllHeadUser() {
        List<UserData> adminList = userDao.findByUserHeadId(0l);
        List<UserData> userHeads = new ArrayList<>();
        adminList.forEach(e -> {
            List<UserData> users = userDao.findByUserHeadId(e.getId());
            userHeads.addAll(users);
        });
        return userHeads;
    }

    public int isAvailable(UserData userData) {

        var userData1 = userDao.findById(userData.getId());
        if(!userData1.isPresent())
        {
            return 0;
        }
        userDao.save(userData);
        return 1;

    }

    public boolean deletePartyById(Long id) {
        var userIndex= userDao.findById(id);
        if(!userIndex.isPresent())
            return false;
        else
            userDao.deleteById(id);
        return true;

    }

    public List<UserData> getAllUser() {

        return userDao.findAll();


    }
}
