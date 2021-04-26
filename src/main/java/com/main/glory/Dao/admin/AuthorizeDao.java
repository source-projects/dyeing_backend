package com.main.glory.Dao.admin;

import com.main.glory.model.admin.Authorize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface AuthorizeDao extends JpaRepository<Authorize,Long> {
    @Query("select x from Authorize x where LOWER(x.name)=LOWER(:name)")
    Authorize getAuthorizeByName(String name);

    @Query("select x from Authorize x where x.id=:id")
    Authorize getAuthorizeById(Long id);

    @Query("select x from Authorize x")
    List<Authorize> getAllAuthorize();

    @Modifying
    @Transactional
    @Query("delete from Authorize x where x.id=:id")
    void deleteByAuthorizeId(Long id);



    @Query("select x from Authorize x where x.type=:receive")
    List<Authorize> getAllAuthorizeByType(String receive);

   /* @Query("select x from Authorize x wheere")
    Authorize getAuthorizeByNameAndType(String name, String type);*/
}
