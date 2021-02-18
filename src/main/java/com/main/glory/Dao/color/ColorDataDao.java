package com.main.glory.Dao.color;

import com.main.glory.model.color.ColorBox;
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

    @Query("select cb from ColorBox cb where cb.controlId IN (select c.id from ColorData c where c.itemId=:itemId) AND cb.issued=true AND cb.finished = false AND cb.quantityLeft > 0 ")
    List<ColorBox> findAllIssuedBoxByItemId(Long itemId);

    @Query("select MAX(c.id) from ColorBox c where c.controlId = (select MAX(cc.id) from ColorData cc where cc.itemId=:supplierItemId) AND c.issued=true")
    Long getLatestIssuedColorBoxByColorDataId(Long supplierItemId);
}

