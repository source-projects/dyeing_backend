package com.main.glory.Dao.color;

import com.main.glory.model.color.ColorData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ColorDataDao extends JpaRepository<ColorData, Long> {
    List<ColorData> findAllByControlId(Long controlId);
}

