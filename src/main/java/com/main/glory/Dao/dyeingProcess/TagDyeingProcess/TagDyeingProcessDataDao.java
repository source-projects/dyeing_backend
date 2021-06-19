package com.main.glory.Dao.dyeingProcess.TagDyeingProcess;

import com.main.glory.model.dyeingProcess.TagDyeingProcess.TagDyeingProcessData;
import com.main.glory.model.dyeingProcess.TagDyeingProcess.TagDyeingProcessMast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface TagDyeingProcessDataDao extends JpaRepository<TagDyeingProcessData,Long> {

    @Modifying
    @Transactional
    @Query("delete from TagDyeingProcessData x where x.controlId=:id")
    void deleteTagProcessDataByControlId(Long id);
}
