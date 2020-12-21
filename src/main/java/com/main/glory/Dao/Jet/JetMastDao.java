package com.main.glory.Dao.Jet;

import com.main.glory.model.jet.JetMast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface JetMastDao  extends JpaRepository<JetMast,Long> {

    @Query("select j from JetMast j where id=: controlId")
    Optional<JetMast> findByControlId(Long controlId);
}
