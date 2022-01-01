package com.main.glory.Dao.admin;


import com.main.glory.model.admin.RFSequence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface RFSequenceDao extends JpaRepository<RFSequence,Long> {

    @Query(value = "select * from rfsequence as x LIMIT 1",nativeQuery = true)
    RFSequence getSequence();

    @Query("select x from RFSequence x where x.id=:id")
    RFSequence getSequenceById(Long id);

    @Modifying
    @Transactional
    @Query("update RFSequence x set x.sequence=:l where x.id=:id")
    void RFSequence(Long id, long l);
}
