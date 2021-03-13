package com.main.glory.servicesImpl;

import com.main.glory.Dao.admin.CompanyDao;
import com.main.glory.Dao.admin.DepartmentDao;
import com.main.glory.Dao.designation.DesignationDao;
import com.main.glory.Dao.user.UserDao;
import com.main.glory.model.admin.Company;
import com.main.glory.model.admin.Department;
import com.main.glory.model.designation.Designation;
import com.main.glory.model.user.Request.UserAddRequest;
import com.main.glory.model.user.Request.UserIdentification;
import com.main.glory.model.user.Request.UserUpdateRequest;
import com.main.glory.model.user.UserData;
import com.main.glory.model.user.response.GetAllOperator;
import com.main.glory.model.user.response.getAllUserInfo;
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
    DepartmentDao departmentDao;

    @Autowired
    CompanyDao companyDao;


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


    public void createUser(UserAddRequest userDataDto, String headerId) throws Exception {
        //company and designation check

        Department departmentExist =departmentDao.getDepartmentById(userDataDto.getDepartmentId());
        if(departmentExist==null)
            throw new Exception("department not found");

        Company companyExist = companyDao.getCompanyById(userDataDto.getCompanyId());
        if(companyExist==null)
            throw new Exception("company not found");

        UserData userData = new UserData(userDataDto);


        Optional<UserData> data = userDao.findByUserName(userData.getUserName());

        if (!data.isPresent()) {
            Optional<Designation> designationData = designationService.getDesignationById(userDataDto.getDesignationId());
            if (!designationData.isPresent()) {
                throw new Exception("No such desgination found:" + userDataDto.getDesignationId());

            }
            userData.setDesignationId(designationData.get());
            userData.setUserPermissionData(userDataDto.getUserPermissionData());

            //System.out.println(userData.toString());


            Long id  = userData.getCreatedBy();
            UserData x = userDao.saveAndFlush(userData);

            Long headId=Long.parseLong(headerId);
            System.out.println(headId+":type:"+headId);
            //if master adding the opeartor then FE will send userHeadId= 0 then store the operator with userHeadID
            if(x.getUserHeadId()==0)
            {
                //then adding opeator by master and set from the header
                userDao.updateUserHeadId(x.getId(),headId);
                return;
            }

            //identify the user recently added was master/oprator or data-entry from admin
            UserData user = userDao.getUserById(x.getUserHeadId());

            if(user.getUserHeadId()==0)
            {
                //data entry
                if(x.getDataEntry()==true)
                {
                    userDao.updateUserHeadId(x.getId(),0l);
                    return;
                }

                //master
              userDao.updateUserHeadId(x.getId(), x.getId());

            }
            //else
            //remain the operator





        } else {
            throw new Exception("User is already available with username:" + userData.getUserName());
        }


    }

    public UserData checkUser(String userName, String password) {
        return userDao.findByUserNameAndPassword(userName, password);
    }

    public List<getAllUserInfo> getAllHeadUser(String id) throws Exception {

        List<getAllUserInfo> userHeads =new ArrayList<>();
        //identify the user is master/admin/operator
        UserData userData = userDao.getUserById(Long.parseLong(id));

        //admin requesting
        if(userData.getUserHeadId()==0)
        {

            List<UserData> list =userDao.getAllUserHeadList();

            for(UserData e : list)
            {
                Company company =companyDao.getCompanyById(e.getCompanyId());
                if(company==null)
                    continue;

                Department department= departmentDao.getDepartmentById(e.getDepartmentId());
                if(department==null)
                    continue;
                userHeads.add(new getAllUserInfo(e,company,department));
            }

        }
        else if(userData.getUserHeadId()==userData.getId())
        {
            //master

                Company company =companyDao.getCompanyById(userData.getCompanyId());
                Department department= departmentDao.getDepartmentById(userData.getDepartmentId());
                userHeads.add(new getAllUserInfo(userData,company,department));

        }
        else
        {
            //operator
            UserData userHead = userDao.getUserById(userData.getUserHeadId());
            Company company =companyDao.getCompanyById(userHead.getCompanyId());
            Department department= departmentDao.getDepartmentById(userHead.getDepartmentId());
            userHeads.add(new getAllUserInfo(userHead,company,department));


        }

        if(userHeads.isEmpty())
            throw new Exception("no user head found");


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
    public List<getAllUserInfo> getAllUser(String getBy, Long id, String s) {

        try {
            Long headerId = Long.parseLong(s);

            List<UserData> userDataList = null;
            List<getAllUserInfo> getAllUserInfoList = new ArrayList<>();
            if (id == null) {
                userDataList = userDao.getAllUserExceptHeaderId(headerId);
                int i = 0;
                for (UserData e : userDataList) {
                    if (e.getId() == headerId || e.getUserHeadId() == 0)
                        continue;
                    Company company = companyDao.getCompanyById(e.getCompanyId());
                    Department department = departmentDao.getDepartmentById(e.getDepartmentId());

                    if(company==null || department==null)
                        continue;

                    getAllUserInfo userData = new getAllUserInfo(e,company,department);
                    userData.setCompany(company.getName());
                    getAllUserInfoList.add(userData);
                }
            } else if (getBy.equals("own")) {
                userDataList = userDao.findAllByCreatedBy(id,headerId);
                int i = 0;
                for (UserData e : userDataList) {
                    if (e.getId() == headerId || e.getUserHeadId() == 0)
                        continue;
                    Company company = companyDao.getCompanyById(e.getCompanyId());
                    Department department = departmentDao.getDepartmentById(e.getDepartmentId());

                    if(company==null || department==null)
                        continue;

                    getAllUserInfo userData = new getAllUserInfo(e,company,department);
                    userData.setCompany(company.getName());
                    getAllUserInfoList.add(userData);
                }
            } else if (getBy.equals("group")) {
                UserData userData = userDao.findUserById(id);

                if (userData.getUserHeadId() == 0) {
                    //master user
                    userDataList = userDao.findAllByCreatedByAndUserHeadId(id, id,headerId);
                    int i = 0;
                    for (UserData e : userDataList) {
                        if (e.getId() == headerId)
                            continue;
                        Company company = companyDao.getCompanyById(e.getCompanyId());
                        Department department = departmentDao.getDepartmentById(e.getDepartmentId());

                        if(company==null || department==null)
                            continue;

                        getAllUserInfo userData1 = new getAllUserInfo(e,company,department);
                        getAllUserInfoList.add(userData1);
                    }
                } else {
                    userDataList = userDao.findAllByCreatedByAndUserHeadId(id, userData.getUserHeadId(),headerId);
                    int i = 0;
                    for (UserData e : userDataList) {
                        if (e.getId() == headerId)
                            continue;

                        Company company = companyDao.getCompanyById(e.getCompanyId());
                        Department department = departmentDao.getDepartmentById(e.getDepartmentId());

                        if(company==null || department==null)
                            continue;

                        getAllUserInfo userData1 = new getAllUserInfo(e,company,department);
                        getAllUserInfoList.add(userData1);
                    }
                }


            }
            return getAllUserInfoList;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public int isAvailable(UserUpdateRequest userData) throws Exception {

        var userData1 = userDao.findById(userData.getId());
        if (!userData1.isPresent()) {
            return 0;
        }
        //UserData userData2 = modelMapper.map(userData, UserData.class);
        Optional<Designation> d = designationService.getDesignationById(userData.getDesignationId());
        if (d.isPresent()) {
            Optional<Designation> designation = designationDao.findById(userData.getDesignationId());
            if (designation.isPresent())
                userData1.get().setDesignationId(designation.get());
            else
                throw new Exception("Wrong designation id" + userData1.get().getDesignationId());

            if(userData.getPassword()==null || userData.getPassword().isEmpty())
            {
                userData1.get().setPassword(userData1.get().getPassword());
            }
            else {
                userData1.get().setPassword(userData.getPassword());
            }
            Company company =companyDao.getCompanyById(userData.getCompanyId());
            if(company==null)
                throw new Exception("company not found");

            Department departmentExist =departmentDao.getDepartmentById(userData.getDepartmentId());
            if(departmentExist==null)
                throw new Exception("department not found");

            userData1.get().setUserName(userData.getUserName());
            userData1.get().setFirstName(userData.getFirstName());
            userData1.get().setLastName(userData.getLastName());
            userData1.get().setEmail(userData.getEmail());
            userData1.get().setContact(userData.getContact());
            userData1.get().setCompanyId(userData.getCompanyId());
            userData1.get().setDepartmentId(userData.getDepartmentId());
            userData1.get().setUserHeadId(userData.getUserHeadId());
            userData1.get().setUpdatedBy(userData.getUpdatedBy());
            userData1.get().setDataEntry(userData.getDataEntry());
            userData1.get().setUserPermissionData(userData.getUserPermissionData());

            UserData x = userDao.save(userData1.get());

            //check wether the last record update from admin and become the master or not
            UserData userAdmin = userDao.getUserById(x.getUserHeadId());

            if(userAdmin!=null)
            {
                if(userAdmin.getUserHeadId()==0)
                {
                    //update the recently updated record to master
                    userDao.updateUserHeadId(x.getId(),x.getId());
                }
            }

        } else
            throw new Exception("Wrong designation for user id" + userData.getId());

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


    /*public List<UserData> getAllUserByCompany(String name) {
        List<UserData> list = userDao.findByCompanyName(name);
        return list;
    }*/

    /*public void updateUserCompanyById(Long id, String name) throws Exception {
        UserData userData =userDao.getUserById(id);
        if(userData==null)
            throw new Exception("no user found");

        userDao.updateCompanyById(id,name);

    }*/

   /* public List<UserData> getAllUserByDepartment(String name) {
        List<UserData> userDataList = userDao.getAllUserByDepartment(name);
        return userDataList;
    }*/

   /* public void updateUserByDepartment(Long id, String name) {
        userDao.updateDepartmentById(id,name);
    }*/

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

   /* public List<UserData> getUserByCompany(String name) {
        return userDao.getAllUserByCompany(name);
    }*/

    public Boolean getUserNameExist(String username, Long id) {
        //id is null then insert request
        //else update request
        if(id==null)
        {
            UserData userData = userDao.getUserByUserName(username);
            if(userData!=null)
                return true;
            else
                return false;
        }
        else
        {
            UserData userData = userDao.getUserByUserNameWithId(username,id);
            if(userData!=null)
                return true;
            else
                return false;
        }
    }

    public List<UserData> getUserByCompanyId(Long id) {
        return userDao.getUserByCompanyId(id);
    }

    public List<UserData> getAllUserByDepartmentId(Long id) {
        return userDao.getAllUserByDepartmentId(id);
    }
}
