package com.main.glory.Dao;

import com.main.glory.model.quality.QualityWithPartyName;
import org.springframework.data.jpa.repository.JpaRepository;
import com.main.glory.model.quality.Quality;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;

import java.util.List;


@EnableJpaRepositories
public interface QualityDao extends JpaRepository<Quality, Long>  {

    @Query(value = "SELECT quality_id FROM quality as q WHERE q.quality_id = :qid", nativeQuery = true)
     String isQualityNameExist(@Param("qid") String quality_id);


    @Query(value = "Select * from quality as qa where qa.quality_id=:qid",nativeQuery = true)
    List<Quality> getQualityListById(@Param("qid") String quality_id);

    @Query("Select new com.main.glory.model.quality.QualityWithPartyName(q, (Select p.partyName from Party p where p.id = q.partyId)) from Quality q")
    List<QualityWithPartyName> findAllWithPartyName();
}


