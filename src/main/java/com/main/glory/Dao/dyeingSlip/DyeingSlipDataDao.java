package com.main.glory.Dao.dyeingSlip;

import com.main.glory.model.dyeingSlip.DyeingSlipData;
import com.main.glory.model.dyeingSlip.DyeingSlipItemData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DyeingSlipDataDao extends JpaRepository<DyeingSlipData,Long> {

    @Query("select x from DyeingSlipData x where x.controlId=:id")
    List<DyeingSlipData> findByControlId(Long id);

    @Query("select s from DyeingSlipData s where s.controlId=(select dd.id from DyeingSlipData dd where dd.controlId=:id AND dd.processType='addition' )")
    List<DyeingSlipItemData> getDyeingExistingSlipItemList(Long id);


    @Transactional
    @Modifying
    @Query("delete from DyeingSlipData dd where dd.processType='addition' AND dd.controlId=:id")
    void deleteAdditionalSlipDataByDyeingSlipId(Long id);

    @Query("select d from DyeingSlipData d where d.controlId=:id AND d.processType='addition'")
    DyeingSlipData getOnlyAdditionalSlipMastById(Long id);

    @Transactional
    @Modifying
    @Query("delete from DyeingSlipData dd where dd.id=:id")
    void deleteDyeingSlipDataById(Long id);

    @Query("select d from DyeingSlipData d where d.controlId=:id AND d.processType='Dyeing'")
    DyeingSlipData getOnlyDyeingProcessByMastId(Long id);

    @Query("select d from DyeingSlipData d where d.controlId=:id AND d.processType='directDyeing'")
    DyeingSlipData getOnlyDirectSlipMastById(Long id);

    @Query("select d from DyeingSlipData d where d.controlId=:id AND d.processType='Re-Dyeing'")
    DyeingSlipData getOnlyReDyeingSlipMastById(Long id);
}
