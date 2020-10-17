//package com.main.glory.Dao;
//
//import com.main.glory.Lookup.grLookUp.GrLooKUpDetails;
//import com.main.glory.model.FabricInRecord;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//
//import javax.transaction.Transactional;
//import java.util.List;
//
////@EnableJpaRepositories
////
////public interface GrLookUpDao extends JpaRepository <GrLooKUpDetails,Long>{
////    @Query(value = ("SELECT fs.gr,fs.wt,fm.lot_no from fabstock as fs inner join fabric_master as fm on fm.id=fs.control_id order by 1"), nativeQuery = true)
////   public  List<GrLooKUpDetails> getGrDetailsByQualityId();
////}
