package com.main.glory.servicesImpl;

import com.main.glory.Dao.designation.DesignationDao;
import com.main.glory.Dao.user.UserDao;
import com.main.glory.model.designation.Designation;
import com.main.glory.model.quality.Quality;
import com.main.glory.model.user.Request.UserAddRequest;
import com.main.glory.model.user.Request.UserUpdateRequest;
import com.main.glory.model.user.UserData;
import com.main.glory.model.user.UserPermission;
import com.main.glory.model.user.response.getAllUserInfo;
import com.main.glory.services.UserServiceInterface;

import org.apache.catalina.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service("userServiceImpl")
public class UserServiceImpl implements UserServiceInterface {

    @Autowired
    UserDao userDao;

    @Autowired
    DesignationDao designationDao;

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


    public void createUser(UserAddRequest userDataDto) throws Exception {
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        UserData userData =  modelMapper.map(userDataDto, UserData.class);

        Optional<UserData> data = userDao.findByUserName(userData.getUserName());

        if(!data.isPresent()) {
            Optional<Designation> designationData = designationService.getDesignationById(userDataDto.getDesignationId());
            if(!designationData.isPresent())
            {
                throw new Exception("No such desgination found:"+userDataDto.getDesignationId());


            }
            userData.setDesignationId(designationData.get());
            //System.out.println(userData.toString());

            userDao.saveAndFlush(userData);



        }
       else
        {
            throw new Exception("User is already available with username:"+userData.getUserName());
        }


    }

    public UserData checkUser(String userName,String password) {
        return userDao.findByUserNameAndPassword(userName, password);
    }

    public List<getAllUserInfo> getAllHeadUser() {
        List<UserData> adminList = userDao.findByUserHeadId(0l);
        List<getAllUserInfo> userHeads = new ArrayList<>();

        for (UserData e : adminList) {
            userHeads.add(modelMapper.map(e, getAllUserInfo.class));
            List<UserData> users = userDao.findByUserHeadId(e.getId());
            for (UserData ex : users) {
                getAllUserInfo userData = modelMapper.map(ex, getAllUserInfo.class);
                userHeads.add(userData);
            }
        }
        return userHeads;
    }

    public boolean deleteUserById(Long id) {
        var userIndex= userDao.findById(id);
        if(!userIndex.isPresent())
            return false;
        else
            userDao.deleteById(id);
        return true;

    }

    //for All user
    public List<getAllUserInfo> getAllUser(String getBy, Long id) {
        List<UserData> userDataList = null;
        List<getAllUserInfo> getAllUserInfoList = new ArrayList<>();
        if(id == null){
            userDataList = userDao.findAll();
            int i=0;
            for (UserData e : userDataList) {
                getAllUserInfo userData = modelMapper.map(e, getAllUserInfo.class);
                getAllUserInfoList.add(userData);
            }
        }
        else if(getBy.equals("own")){
            userDataList = userDao.findAllByCreatedBy(id);
            int i=0;
            for (UserData e : userDataList) {
                getAllUserInfo userData = modelMapper.map(e, getAllUserInfo.class);
                getAllUserInfoList.add(userData);
            }
        }
        else if(getBy.equals("group")){
            userDataList = userDao.findAllByUserHeadId(id);
            int i=0;
            for (UserData e : userDataList) {
                getAllUserInfo userData = modelMapper.map(e, getAllUserInfo.class);
                getAllUserInfoList.add(userData);
            }
        }
        return getAllUserInfoList;
    }

    public int isAvailable(UserUpdateRequest userData) throws Exception {

        var userData1 = userDao.findById(userData.getId());
        if(!userData1.isPresent())
        {
            return 0;
        }
        UserData userData2 =  modelMapper.map(userData, UserData.class);
        Optional<Designation> d = designationService.getDesignationById(userData.getDesignationId());
        if(d.isPresent()) {
            Optional<Designation> designation = designationDao.findById(userData.getDesignationId());
            if(designation.isPresent())
                userData1.get().setDesignationId(designation.get());
            else
                throw new Exception("Wrong designation id"+userData1.get().getDesignationId());
            userData1.get().setUserName(userData.getUserName());
            userData1.get().setFirstName(userData.getFirstName());
            userData1.get().setLastName(userData.getLastName());
            userData1.get().setEmail(userData.getEmail());
            userData1.get().setContact(userData.getContact());
            userData1.get().setCompany(userData.getCompany());
            userData1.get().setDepartment(userData.getDepartment());
            userData1.get().setUserHeadId(userData.getUserHeadId());
            userData1.get().setUpdatedBy(userData.getUpdatedBy());
            userData1.get().setUserPermissionData(userData.getUserPermissionData());
            userDao.save(userData1.get());
        }
        else
            throw new Exception("Wrong designation id"+userData2.getId());

        return 1;
    }
}
