package com.main.glory.servicesImpl;

import com.main.glory.Dao.designation.DesignationDao;
import com.main.glory.Dao.user.UserDao;
import com.main.glory.model.StockDataBatchData.StockMast;
import com.main.glory.model.designation.Designation;
import com.main.glory.model.quality.Quality;
import com.main.glory.model.user.Request.UserAddRequest;
import com.main.glory.model.user.Request.UserIdentification;
import com.main.glory.model.user.Request.UserUpdateRequest;
import com.main.glory.model.user.UserData;
import com.main.glory.model.user.UserPermission;
import com.main.glory.model.user.response.GetAllOperator;
import com.main.glory.model.user.response.getAllUserInfo;
import com.main.glory.services.UserServiceInterface;

import org.apache.catalina.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        var data = userDao.findById(id);
        if (data.isEmpty())
            return null;
        else
            return data.get();
    }


    public void createUser(UserAddRequest userDataDto) throws Exception {
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        UserData userData = modelMapper.map(userDataDto, UserData.class);

        Optional<UserData> data = userDao.findByUserName(userData.getUserName());

        if (!data.isPresent()) {
            Optional<Designation> designationData = designationService.getDesignationById(userDataDto.getDesignationId());
            if (!designationData.isPresent()) {
                throw new Exception("No such desgination found:" + userDataDto.getDesignationId());

            }
            userData.setDesignationId(designationData.get());
            //System.out.println(userData.toString());

            userDao.saveAndFlush(userData);


        } else {
            throw new Exception("User is already available with username:" + userData.getUserName());
        }


    }

    public UserData checkUser(String userName, String password) {
        return userDao.findByUserNameAndPassword(userName, password);
    }

    public List<getAllUserInfo> getAllHeadUser() {
        List<UserData> adminList = userDao.findByUserHeadId(0l);
        List<getAllUserInfo> userHeads = new ArrayList<>();

        for (UserData e : adminList) {
            //userHeads.add(modelMapper.map(e, getAllUserInfo.class));
            List<UserData> users = userDao.findByUserHeadId(e.getId());
            for (UserData ex : users) {
                getAllUserInfo userData = modelMapper.map(ex, getAllUserInfo.class);
                System.out.println(userData.getUserHeadId());
                userHeads.add(userData);
            }
        }
        return userHeads;
    }

    public boolean deleteUserById(Long id) {
        var userIndex = userDao.findById(id);
        if (!userIndex.isPresent())
            return false;
        else
            userDao.deleteById(id);
        return true;

    }

    //for All user
    public List<getAllUserInfo> getAllUser(String getBy, Long id) {
        List<UserData> userDataList = null;
        List<getAllUserInfo> getAllUserInfoList = new ArrayList<>();
        if (id == null) {
            userDataList = userDao.getAllUser();
            int i = 0;
            for (UserData e : userDataList) {
                getAllUserInfo userData = modelMapper.map(e, getAllUserInfo.class);
                getAllUserInfoList.add(userData);
            }
        } else if (getBy.equals("own")) {
            userDataList = userDao.findAllByCreatedBy(id);
            int i = 0;
            for (UserData e : userDataList) {
                getAllUserInfo userData = modelMapper.map(e, getAllUserInfo.class);
                getAllUserInfoList.add(userData);
            }
        } else if (getBy.equals("group")) {
            UserData userData = userDao.findUserById(id);

            if(userData.getUserHeadId()==0) {
                //master user
                userDataList = userDao.findAllByCreatedByAndUserHeadId(id,id);
                int i = 0;
                for (UserData e : userDataList) {
                    getAllUserInfo userData1 = modelMapper.map(e, getAllUserInfo.class);
                    getAllUserInfoList.add(userData1);
                }
            }
            else
            {
                userDataList = userDao.findAllByCreatedByAndUserHeadId(id,userData.getUserHeadId());
                int i = 0;
                for (UserData e : userDataList) {
                    getAllUserInfo userData1 = modelMapper.map(e, getAllUserInfo.class);
                    getAllUserInfoList.add(userData1);
                }
            }



        }
        return getAllUserInfoList;
    }

    public int isAvailable(UserUpdateRequest userData) throws Exception {

        var userData1 = userDao.findById(userData.getId());
        if (!userData1.isPresent()) {
            return 0;
        }
        UserData userData2 = modelMapper.map(userData, UserData.class);
        Optional<Designation> d = designationService.getDesignationById(userData.getDesignationId());
        if (d.isPresent()) {
            Optional<Designation> designation = designationDao.findById(userData.getDesignationId());
            if (designation.isPresent())
                userData1.get().setDesignationId(designation.get());
            else
                throw new Exception("Wrong designation id" + userData1.get().getDesignationId());

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
        } else
            throw new Exception("Wrong designation id" + userData2.getId());

        return 1;
    }

    public List<GetAllOperator> getAllOperator() throws Exception {

        List<GetAllOperator> getAllOperatorList = new ArrayList<>();
        Designation operatoDesoignation = designationDao.findDesignationOperator();

        if (operatoDesoignation == null)
            throw new Exception("no operator designation found");

        List<UserData> operatorList = userDao.findByDesignationId(operatoDesoignation.getId());

        if (operatorList.isEmpty())
            throw new Exception("no user found");


        for (UserData operator : operatorList) {
            GetAllOperator getAllOperator = new GetAllOperator(operator);
            getAllOperatorList.add(getAllOperator);
        }


        return getAllOperatorList;

    }

    public UserData getUserByUserHeadIdAndId(Long userHeadId, Long createdBy) throws Exception {
        UserData userData = userDao.findByUserHeadIdAndUserId(userHeadId, createdBy);
        if (userData == null)
            throw new Exception("no user found with head id");

        return userData;
    }


    public List<UserData> getAllUserByCompany(String name) {
        List<UserData> list = userDao.findByCompanyName(name);
        return list;
    }

    public void updateUserCompanyById(Long id, String name) throws Exception {
        UserData userData =userDao.getUserById(id);
        if(userData==null)
            throw new Exception("no user found");

        userDao.updateCompanyById(id,name);

    }

    public List<UserData> getAllUserByDepartment(String name) {
        List<UserData> userDataList = userDao.getAllUserByDepartment(name);
        return userDataList;
    }

    public void updateUserByDepartment(Long id, String name) {
        userDao.updateDepartmentById(id,name);
    }

    public UserIdentification getUserHeadDetail(Long id) throws Exception {

        UserIdentification userIdentification=null;

        UserData userData = userDao.getUserById(id);
        Designation designation = designationDao.getDesignationById(userData.getDesignationId().getId());
        if(userData==null)
            throw new Exception("no user data found");

        if(userData.getUserHeadId()==0)
        {
            userIdentification=new UserIdentification(userData,designation);
        }
        else
        {
           UserData userHead = userDao.getUserById(userData.getUserHeadId());
           Designation designationHead = designationDao.getDesignationById(userHead.getDesignationId().getId());
           userIdentification=new UserIdentification(userData,designation,userHead,designationHead);
           if(userHead.getUserHeadId()>0)
           {
               UserData superUser = userDao.getUserById(userHead.getUserHeadId());
               Designation designationSuper = designationDao.getDesignationById(superUser.getDesignationId().getId());
               userIdentification=new UserIdentification(userData,designation,userHead,designationHead,superUser,designationSuper);
           }

        }


        return userIdentification;
    }

    public List<UserData> getUserByDesignation(Long id) {
        List<UserData> list = userDao.getAllUserByDesignation(id);
        return list;
    }

    public void updateUserByDesignation(Designation designation) {
        userDao.updateUserByDesignation(designation);
    }
}
