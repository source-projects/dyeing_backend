package com.main.glory.Dao.quality;

import com.main.glory.model.quality.QualityName;
import com.main.glory.model.quality.request.AddQualityName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface QualityNameDao extends JpaRepository<QualityName,Long> {

    @Query("Select c from QualityName c where LOWER(c.qualityName)=LOWER(:qualityName) ")
    QualityName getQualityNameDetailByName(String qualityName);

    @Query("select q from QualityName q where q.id=:id")
    Optional<QualityName> getQualityNameDetailById(Long id);

    @Modifying
    @Transactional
    @Query("delete from QualityName q where q.id=:id ")
    void deleteQualityNameById(Long id);

    @Query("select s from QualityName s ")
    Optional<List<QualityName>> getAllQualityName();

    @Query("Select c from QualityName c where LOWER(c.qualityName)=LOWER(:qualityName) AND c.id!=:id ")
    Optional<QualityName> getQualityNameDetailByNameAndId(String qualityName, Long id);

    @Query("select x.qualityName from QualityName x where x.id=(select q.qualityName.id from Quality q where q.id=:qualityId)")
    String getQualityNameDetailByQualitytEntryId(Long qualityId);

    /*@Query("select new com.main.glory.model.quality.request.AddQualityName(x,(select ss from Supplier ss where ss.qualityNameId=x.id)) from QualityName x")
    Optional<List<AddQualityName>> getAllQualityNameWithSupplier();*/
}
