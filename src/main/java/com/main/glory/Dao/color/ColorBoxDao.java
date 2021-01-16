package com.main.glory.Dao.color;

import com.main.glory.model.color.ColorBox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ColorBoxDao extends JpaRepository<ColorBox, Long> {
	List<ColorBox> findByIssued(Boolean aBoolean);
	List<ColorBox> findAllByControlId(Long controlId);

	@Query("select c from ColorBox c where c.controlId=:id AND c.issued=true AND c.finished=false")
    List<ColorBox> findAllByControlIdAndIssused(Long id);
}
