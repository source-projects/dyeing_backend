package com.main.glory.Dao.dyeingSlip;

import com.main.glory.model.dyeingSlip.DyeingSlipItemData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface DyeingSlipItemDataDao extends JpaRepository<DyeingSlipItemData,Long> {


    @Query("select dt from DyeingSlipItemData dt where dt.controlId=(select dd.id from DyeingSlipData dd where dd.controlId=:id AND dd.processType='addition')")
    List<DyeingSlipItemData> getDyeingItemByMastId(Long id);

    @Transactional
    @Modifying
    @Query("delete from DyeingSlipItemData dd where dd.controlId = (select d.id from DyeingSlipData d where d.controlId=:id AND d.processType='addition')")
    void deleteByDyeingSlipId(Long id);

    @Transactional
    @Modifying
    @Query("delete from DyeingSlipItemData dd where dd.controlId=:id")
    void deleteDyeingSlipItemByDyeingSlipDataId(Long id);

    @Transactional
    @Modifying
    @Query("update DyeingSlipItemData dd set dd.isColor=:b where dd.itemId=:itemId ")
    void updateIsColorByItemId(Long itemId, boolean b);

    @Query("select s from DyeingSlipItemData s where s.itemId=:e")
    List<DyeingSlipItemData> getAllItemByItemId(Long e);
}
