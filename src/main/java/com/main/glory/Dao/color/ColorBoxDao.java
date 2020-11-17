package com.main.glory.Dao.color;

import com.main.glory.model.color.ColorBox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ColorBoxDao extends JpaRepository<ColorBox, Long> {
	List<ColorBox> findByIssued(Boolean aBoolean);
}
