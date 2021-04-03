package com.main.glory.Dao.purchase;

import com.main.glory.model.purchase.MaterialPhotos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface MaterialPhotosDao extends JpaRepository<MaterialPhotos,Long> {

    @Modifying
    @Transactional
    @Query("delete from MaterialPhotos x where x.id=:id")
    void deleteByMaterialId(Long id);
}
