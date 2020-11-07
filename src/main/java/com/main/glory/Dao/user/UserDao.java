package com.main.glory.Dao.user;



import com.main.glory.model.user.UserData;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserDao extends JpaRepository<UserData, Long> {
    UserData findByUserNameAndPassword(String userName, String password);

    //@Query("Select new user(b.id, b.qualityId, b.remark, b.createdBy, b.createdDate, b.updatedDate, b.updatedBy, b.userHeadId, b.isProductionPlaned) from BatchMast b")
    //public List<User> findAllByIdWithoutData();
}
