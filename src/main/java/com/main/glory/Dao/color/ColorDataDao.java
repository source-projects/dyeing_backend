package com.main.glory.Dao.color;

import com.main.glory.model.color.ColorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ColorDataDao extends JpaRepository<ColorData, Long> {
    List<ColorData> findAllByControlId(Long controlId);

    @Query("select c from ColorData c where c.itemId=:itemId")
    List<ColorData> findByItemId(Long itemId);

    @Query("select c from ColorData c where c.id=:id")
    Optional<ColorData> findByColorDataId(Long id);
}

