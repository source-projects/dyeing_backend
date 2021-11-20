package com.main.glory.Dao.Jet;

import com.main.glory.model.jet.JetMast;
import com.main.glory.model.jet.responce.GetAllJetMast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface JetMastDao  extends JpaRepository<JetMast,Long> {

    @Query("select j from JetMast j where id=: controlId")
    Optional<JetMast> findByControlId(Long controlId);

    @Query("select j from JetMast j ORDER BY j.id ASC")
    List<JetMast> getAll();

    @Query("select j from JetMast j where j.name=:name")
    Optional<JetMast> findByName(String name);

    @Query("select j from JetMast j where j.id = :jetId")
    Optional<JetMast> getJetById(Long jetId);

    @Modifying
    @Transactional
    @Query("delete from JetMast j where j.id=:id")
    void deleteByJetId(Long id);

    @Query("select new com.main.glory.model.jet.responce.GetAllJetMast(x.id,x.name,x.capacity) from JetMast x")
    List<GetAllJetMast> getAllJetMast();

    @Query("select x from JetMast x where x.id IN (:array)")
    List<JetMast> getAllJetByIds(List<Long> array);
}
