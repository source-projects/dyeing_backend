package com.main.glory.Dao.productionPlan;

import com.main.glory.model.StockDataBatchData.response.GetBatchDetailByProduction;
import com.main.glory.model.party.Party;
import com.main.glory.model.productionPlan.request.GetAllProductionWithShadeData;
import com.main.glory.model.productionPlan.ProductionPlan;
import com.main.glory.model.quality.Quality;
import com.main.glory.model.quality.QualityName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ProductionPlanDao extends JpaRepository<ProductionPlan,Long> {

   /* @Query("select new com.main.glory.model.productionPlan.request.GetAllProductionWithShadeData(p," +
            "(select c.colorTone from ShadeMast c where c.id=p.shadeId AND p.shadeId IS NOT NULL) AS colorTone," +
            "(select c.qualityName from Quality c where c.id=p.qualityEntryId)AS qualityName," +
            "(select c.qualityId from Quality c where c.id=p.qualityEntryId)AS qualityId," +
            "(select x.processName from DyeingProcessMast x where x.id = (select s.processId from ShadeMast s where s.id=p.shadeId)) AS processName," +
            "(select s.partyShadeNo from ShadeMast s where s.id=p.shadeId) AS partyShadeNo ," +
            "(select SUM(b.wt) from BatchData b where b.controlId=p.stockId AND b.batchId=p.batchId) AS WT," +
            "(select SUM(b.mtr) from BatchData b where b.controlId=p.stockId AND b.batchId=p.batchId)AS MTR," +
            "(select z.partyName from Party z where z.id=p.partyId) as partyName" +
            ") from ProductionPlan p where p.status=false AND p.isMergeBatchId=false")
    Optional<List<GetAllProductionWithShadeData>> getAllProductionWithColorTone();

    @Query("select new com.main.glory.model.productionPlan.request.GetAllProductionWithShadeData(p," +
            "(select c.colorTone from ShadeMast c where c.id=p.shadeId AND p.shadeId IS NOT NULL) AS colorTone," +
            "(select c.qualityName from Quality c where c.id=p.qualityEntryId)AS qualityName," +
            "(select c.qualityId from Quality c where c.id=p.qualityEntryId)AS qualityId," +
            "(select x.processName from DyeingProcessMast x where x.id = (select s.processId from ShadeMast s where s.id=p.shadeId)) AS processName," +
            "(select s.partyShadeNo from ShadeMast s where s.id=p.shadeId) AS partyShadeNo ," +
            "(select SUM(b.wt) from BatchData b where b.controlId=p.stockId AND b.batchId=p.batchId) AS WT," +
            "(select SUM(b.mtr) from BatchData b where b.controlId=p.stockId AND b.batchId=p.batchId)AS MTR," +
            "(select z.partyName from Party z where z.id=p.partyId) as partyName" +
            ") from ProductionPlan p where p.status=false AND p.stockId IN (select ss.id from StockMast ss where ss.createdBy=:userId OR ss.userHeadId=:userHeadId)")
    Optional<List<GetAllProductionWithShadeData>> getAllProductionWithColorTone(Long userId,Long userHeadId);*/

    @Query("select new com.main.glory.model.productionPlan.request.GetAllProductionWithShadeData(p,(select c.colorTone from ShadeMast c where c.id=p.shadeId )) from ProductionPlan p ")
    Optional<List<GetAllProductionWithShadeData>> getAllProduction();

    @Query("select p from ProductionPlan p where p.id=:id")
    ProductionPlan getByProductionId(Long id);

    /*@Query("select p from ProductionPlan p where p.partyId=:partyId AND p.qualityEntryId=:qualityEntryId AND p.status=false")
    List<ProductionPlan> getProductionByPartyAndQuality(Long partyId, Long qualityEntryId);
*/
   /* @Query("select p from ProductionPlan p where p.batchId=:batchId AND p.stockId=:controlId")
    ProductionPlan getProductionByBatchAndStockId(String batchId, Long controlId);*/

    @Query("select p from ProductionPlan p where p.id=:productionId AND p.batchId=:batchId")
    ProductionPlan getProductionByBatchAndProduction(String batchId, Long productionId);


    @Query("select p from ProductionPlan p ")
    List<ProductionPlan> getAllProductionWithoutFilter();

    @Query("select p from ProductionPlan p where p.shadeId=:shadeId")
    List<ProductionPlan> getAllProductionByShadeId(Long shadeId);

    @Modifying
    @Transactional
    @Query("update ProductionPlan p set p.shadeId=:shadeId where p.id=:id")
    void updateProductionWithShadeId(Long id, Long shadeId);

   /* @Query("select p from ProductionPlan p where p.qualityEntryId=:id")
    List<ProductionPlan> getAllProductionByQualityId(Long id);*/

    /*@Query("select p from ProductionPlan p where p.partyId=:id")
    List<ProductionPlan> getAllProuctionByPartyId(Long id);*/

    /*@Query("select p from ProductionPlan p where p.stockId=:id")
    List<ProductionPlan> getProductionByStockId(Long id);*/

    @Modifying
    @Transactional
    @Query("delete from ProductionPlan p where p.id=:id")
    void deleteProductionById(Long id);

   /* @Query("select p from Party p where p.id=(select pp.partyId from ProductionPlan pp where pp.id=:productionId)")
    Party getPartyByProductionId(Long productionId);
*/
   /* @Query("select x from Quality x where x.id = (select p.qualityEntryId from ProductionPlan p where p.id=:productionId)")
    Quality getQualityByProductionId(Long productionId);*/

   /* @Query("select z from QualityName z where z.id = (select x.qualityNameId from Quality x where x.id = (select p.qualityEntryId from ProductionPlan p where p.id=:productionId))")
    QualityName getQualityNameByProductionId(Long productionId);*/

    @Query("select p from ProductionPlan p where p.batchId=:batchId AND p.isMergeBatchId=:isMergeBatchIds")
    ProductionPlan getProductionDetailByBatchId(String batchId,Boolean isMergeBatchIds);

    /*Optional<List<GetAllProductionWithShadeData>> getAllProductionWithColorTone();
    Optional<List<GetAllProductionWithShadeData>> getAllProductionWithColorTone(Long userId,Long userHeadId);*/


    @Query("select p.batchId from ProductionPlan p where p.isMergeBatchId=false AND p.status=false")
    List<String> getAllBatchWithoutMergeBatch();

    @Query("select new com.main.glory.model.productionPlan.request.GetAllProductionWithShadeData(p," +
            "(select c.colorTone from ShadeMast c where c.id=p.shadeId AND p.shadeId IS NOT NULL) AS colorTone," +
            "(select qq.qualityName from QualityName qq where qq.id =(select c.qualityName.id from Quality c where c.id=:qualityId))AS qualityName," +
            "(select c.qualityId from Quality c where c.id=:qualityId)AS qualityIdRecord," +
            "(select x.processName from DyeingProcessMast x where x.id = (select s.processId from ShadeMast s where s.id=p.shadeId)) AS processName," +
            "(select s.partyShadeNo from ShadeMast s where s.id=p.shadeId) AS partyShadeNo ," +
            "(select SUM(b.wt) from BatchData b where b.batchId=:e AND b.mergeBatchId IS NULL) AS WT," +
            "(select SUM(b.mtr) from BatchData b where b.batchId=:e AND b.mergeBatchId IS NULL)AS MTR," +
            "(select z.partyName from Party z where z.id=:partyId) as partyName" +
            ") from ProductionPlan p where p.batchId=:e")
    GetAllProductionWithShadeData getProductionWithColorToneByBatchId(String e, Long partyId, Long qualityId);

    @Query("select x.batchId from ProductionPlan x where x.status=false AND x.isMergeBatchId=true")
    List<String> getAllProductionBasedOnMergeBatchId();

    @Query("select new com.main.glory.model.productionPlan.request.GetAllProductionWithShadeData(p," +
            "(select c.colorTone from ShadeMast c where c.id=p.shadeId AND p.shadeId IS NOT NULL) AS colorTone," +
            "(select qq.qualityName from QualityName qq where qq.id =(select c.qualityName.id from Quality c where c.id=:qualityId))AS qualityName," +
            "(select c.qualityId from Quality c where c.id=:qualityId)AS qualityIdRecord," +
            "(select x.processName from DyeingProcessMast x where x.id = (select s.processId from ShadeMast s where s.id=p.shadeId)) AS processName," +
            "(select s.partyShadeNo from ShadeMast s where s.id=p.shadeId) AS partyShadeNo ," +
            "(select SUM(b.wt) from BatchData b where b.batchId=:e AND b.mergeBatchId IS NOT NULL) AS WT," +
            "(select SUM(b.mtr) from BatchData b where b.batchId=:e AND b.mergeBatchId IS NOT NULL)AS MTR," +
            "(select z.partyName from Party z where z.id=:partyId) as partyName" +
            ") from ProductionPlan p where p.batchId=:e")
    GetAllProductionWithShadeData getProductionWithColorToneWithMergeBatchId(String e, Long partyId, Long qualityId);

    @Query("select c.colorTone from ShadeMast c where c.id = (select s.shadeId from ProductionPlan s where s.batchId=:e)")
    String getColorToneByBatchId(String e);

    @Query("select c.partyShadeNo from ShadeMast c where c.id=(select s.shadeId from ProductionPlan s where s.batchId=:e)")
    String getPartyShadenoByBatchId(String e);

    @Query("select c.processId from ShadeMast c where c.id=(select s.shadeId from ProductionPlan s where s.batchId=:e)")
    Long getDyeingProcessByBatchId(String e);

    @Query("select s.id from ProductionPlan s where s.batchId=:e")
    Long getProductionIdByBatchId(String e);

    @Query("select s.shadeId from ProductionPlan s where s.batchId=:e")
    Long getShadeIdByBatchId(String e);

    @Query("select x from ProductionPlan x where x.batchId=:e")
    ProductionPlan getProductionByBatchId(String e);

    @Query(value = "select * from production_plan as x where x.id=:productionId",nativeQuery = true)
    ProductionPlan getById(@Param("productionId") Long productionId);



    /*@Query("select p from ProductionPlan p where p.mergeBatchId=:batchId")
    ProductionPlan getProductionDetailByMergeBatchIdBatchId(String batchId);*/

   /* @Query("select new com.main.glory.model.productionPlan.request.GetAllProductionWithShadeData(p," +
            "(select c.colorTone from ShadeMast c where c.id=p.shadeId) AS colorTone," +
            "(select c.qualityName from Quality c where c.id=p.qualityEntryId)AS qualityName," +
            "(select c.qualityId from Quality c where c.id=p.qualityEntryId)AS qualityId," +
            "(select x.processName from DyeingProcessMast x where x.id = (select s.processId from ShadeMast s where s.id=p.shadeId)) AS processName," +
            "(select s.partyShadeNo from ShadeMast s where s.id=p.shadeId) AS partyShadeNo ," +
            "(select SUM(b.wt) from BatchData b where b.controlId=p.stockId AND b.batchId=p.batchId) AS WT," +
            "(select SUM(b.mtr) from BatchData b where b.controlId=p.stockId AND b.batchId=p.batchId)AS MTR " +
            ")from ProductionPlan p")
    Optional<List<GetAllProductionWithShadeData>> getAllProductionWithColorToneAndBatchDetail();
*/

}
