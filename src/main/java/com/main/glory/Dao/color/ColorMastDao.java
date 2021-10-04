package com.main.glory.Dao.color;

import com.main.glory.Dao.FilterDao;
import com.main.glory.model.color.ColorMast;
import com.main.glory.model.color.responsemodals.ColorMastDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;

@EnableJpaRepositories
public interface ColorMastDao extends FilterDao<ColorMast> {

	@Query(value = "Select cm.*, (Select cd.* from color_data as cd where cd.purchase_id = cm.id), (Select cb.* from color_box as cb where cb.control_id = cd.id where cb.issued = 0) from color_mast", nativeQuery = true)
	List<ColorMast> getAllActiveData();

	List<ColorMast> getAllByCreatedBy(Long createdBy);

	@Query("select c from ColorMast c where c.createdBy.id=:userHeadId OR c.userHeadData.id=:userHeadId")
	List<ColorMast> getAllByUserHeadId(Long userHeadId);

	@Query("select c from ColorMast c")
    List<ColorMast> getAllColorList();

	@Query("select s from ColorMast s where s.createdBy.id=:id OR s.userHeadData.id=:userHeadId")
	List<ColorMast> getAllByCreatedByAndHeadId(Long id, Long userHeadId);
}
