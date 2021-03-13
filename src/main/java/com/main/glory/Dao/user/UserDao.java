package com.main.glory.Dao.user;



import com.main.glory.model.designation.Designation;
import com.main.glory.model.user.UserData;
import com.main.glory.model.user.response.getAllUserInfo;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


public interface UserDao extends JpaRepository<UserData, Long> {

    UserData findByUserNameAndPassword(String userName, String password);

    List<UserData> findByUserHeadIdGreaterThan(Long id);

    List<UserData> findByUserHeadId(Long id);

    @Query("select u from UserData u where u.userName=:userName")
    Optional<UserData> findByUserName(String userName);

    @Query("select u from UserData u where u.createdBy=:userHeadId OR u.userHeadId=:userHeadId")
    List<UserData>findAllByUserHeadId(Long userHeadId);

    @Query("select u from UserData u where u.createdBy=:createdBy")
    List<UserData>findAllByCreatedBy(Long createdBy);

    @Query("select u from UserData u where u.designationId.id=:id")
    List<UserData> findByDesignationId(Long id);

    @Query("select u from UserData u where u.userHeadId!=0 ")
    List<UserData> getAllUser();

    @Query("select u from UserData u where u.userHeadId=:userHeadId AND u.id=:createdBy")
    UserData findByUserHeadIdAndUserId(Long userHeadId, Long createdBy);

    @Query(value = "select * from user as u where u.id=id LIMIT 1",nativeQuery = true)
    UserData findUserById(Long id);

    @Query("select u from UserData u where u.createdBy=:id OR u.userHeadId=:userHeadId")
    List<UserData> findByUserAndHeadId(Long userHeadId, Long id);


    @Query("select u from UserData u where u.id = :id")
    UserData findByUserAdminId(Long id);

    @Query(value = "select * from user as u where u.id=:id LIMIT 1",nativeQuery = true)
    UserData getUserById(Long id);

    @Query("select s from UserData s where s.createdBy=:id OR s.userHeadId=:userHeadId")
    List<UserData> findAllByCreatedByAndUserHeadId(Long id, Long userHeadId);

    /*@Query("select u from UserData u where u.company=:name")
    List<UserData> findByCompanyName(String name);*/

    /*@Modifying
    @Transactional
    @Query("update UserData u set u.company=:name where u.id=:id")
    void updateCompanyById(Long id, String name);*/

    /*@Query("select u from UserData u where u.department=:name")
    List<UserData> getAllUserByDepartment(String name);*/

    /*@Modifying
    @Transactional
    @Query("update UserData u set u.department=:name where id=:id")
    void updateDepartmentById(Long id, String name);
*/
    @Query("select u from UserData u where u.designationId.id=:id")
    List<UserData> getAllUserByDesignation(Long id);


    @Modifying
    @Transactional
    @Query("update UserData u set u.designationId=:designation")
    void updateUserByDesignation(Designation designation);

    @Modifying
    @Transactional
    @Query("update UserData u set u.userHeadId=:id1 where u.id=:id")
    void updateUserHeadId(Long id, Long id1);

    @Query("select u from UserData u where u.id=u.userHeadId AND u.dataEntry=false")
    List<UserData> getAllUserHeadList();

  /*  @Query("select u from UserData u where u.company=:name")
    List<UserData> getAllUserByCompany(String name);*/


    @Query("select u from UserData u where u.userName=:username AND u.id!=:id")
    UserData getUserByUserNameWithId(String username, Long id);

    @Query("select u from UserData u where u.userName=:username")
    UserData getUserByUserName(String username);

    @Query("select s from UserData s where s.id!=:headerId")
    List<UserData> getAllUserExceptHeaderId(Long headerId);

    @Query("select s from UserData s where s.companyId=:id")
    List<UserData> getUserByCompanyId(Long id);

    @Query("select s from UserData s where s.departmentId=:id")
    List<UserData> getAllUserByDepartmentId(Long id);
}
