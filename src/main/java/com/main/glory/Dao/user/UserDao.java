package com.main.glory.Dao.user;



import com.main.glory.model.user.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface UserDao extends JpaRepository<UserData, Long> {

    UserData findByUserNameAndPassword(String userName, String password);

    List<UserData> findByUserHeadIdGreaterThan(Long id);

    //@Query("Select new user(b.id, b.qualityId, b.remark, b.createdBy, b.createdDate, b.updatedDate, b.updatedBy, b.userHeadId, b.isProductionPlaned) from BatchMast b")
    //public List<User> findAllByIdWithoutData();
    List<UserData> findByUserHeadId(Long id);
}
