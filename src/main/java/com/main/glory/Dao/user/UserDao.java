package com.main.glory.Dao.user;



import com.main.glory.model.user.UserData;
import org.apache.catalina.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


public interface UserDao extends JpaRepository<UserData, Long> {

    UserData findByUserNameAndPassword(String userName, String password);

    List<UserData> findByUserHeadIdGreaterThan(Long id);

    List<UserData> findByUserHeadId(Long id);

    Optional<UserData> findByUserName(String userName);

    List<UserData>findAllByUserHeadId(Long userHeadId);
    List<UserData>findAllByCreatedBy(Long createdBy);

    @Query("select u from UserData u where u.designationId.id=:id")
    List<UserData> findByDesignationId(Long id);

    @Query("select u from UserData u")
    List<UserData> getAllUser();

    @Query("select u from UserData u where u.userHeadId=:userHeadId AND u.id=:createdBy")
    UserData findByUserHeadIdAndUserId(Long userHeadId, Long createdBy);
}
