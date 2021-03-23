package com.main.glory.Dao.admin;

import com.main.glory.model.admin.ReceiverBy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ReceiverByDao extends JpaRepository<ReceiverBy,Long> {

    @Query("select x from ReceiverBy x where LOWER(x.name)=LOWER(:name)")
    ReceiverBy getReceiverByName(String name);

    @Query("select x from ReceiverBy x where LOWER(x.name)=LOWER(:name) AND x.id!=:l")
    ReceiverBy getReceiverByNameExceptId(String name, long l);

    @Query("select x from ReceiverBy x ")
    List<ReceiverBy> getAllReceiver();

    @Query("select x from ReceiverBy x where x.id=:id")
    ReceiverBy getReceiverById(Long id);

    @Modifying
    @Transactional
    @Query("delete from ReceiverBy r where r.id=:id")
    void deleteByReceiverId(Long id);
}
