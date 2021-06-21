package com.main.glory.Dao.dyeingProcess.TagDyeingProcess;

import com.main.glory.model.dyeingProcess.TagDyeingProcess.TagDyeingProcessMast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface TagDyeingProcessMastDao extends JpaRepository<TagDyeingProcessMast,Long> {

    @Query("select x from TagDyeingProcessMast x where LOWER(x.tagProcessName)=LOWER(:tagProcessName) AND x.id!=:id")
    TagDyeingProcessMast getTagDyeingProcessNameExistExceptId(String tagProcessName, Long id);

    @Query("select x from TagDyeingProcessMast x where x.id=:id")
    TagDyeingProcessMast getTagDyeingProcessById(Long id);

    @Query("select x from TagDyeingProcessMast x")
    List<TagDyeingProcessMast> getAllTagDyeingProcessMast();

    @Modifying
    @Transactional
    @Query("delete from TagDyeingProcessMast x where x.id=:id")
    void deleteTagDyeingProcessById(Long id);
}
