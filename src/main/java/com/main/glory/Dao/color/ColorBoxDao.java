package com.main.glory.Dao.color;

import com.main.glory.model.color.ColorBox;
import com.main.glory.model.color.request.ItemWithLeftQty;
import com.main.glory.model.color.responsemodals.SupplierItemWithLeftColorQty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ColorBoxDao extends JpaRepository<ColorBox, Long> {
	List<ColorBox> findByIssued(Boolean aBoolean);
	List<ColorBox> findAllByControlId(Long controlId);

	@Query("select c from ColorBox c where c.controlId=:id AND c.issued=true AND c.finished=false")
    List<ColorBox> findAllByControlIdAndIssused(Long id);

	@Query("select c from ColorBox c where c.controlId =(select cc.id from ColorData cc where cc.itemId=:itemId) AND c.issued=false")
    List<ColorBox> getAllNotIssuedBoxByItemId(Long itemId);

	@Query("select c from ColorBox c where c.controlId=:id AND c.issued=false")
	List<ColorBox> getAllNotIssuedBoxByControlId(Long id);


	@Query("select new com.main.glory.model.color.request.ItemWithLeftQty((select cd.itemId from ColorData cd where cd.id=cb.controlId )as ItemId,SUM(cb.quantityLeft)) from ColorBox cb where cb.issued=true AND cb.finished=false AND cb.quantityLeft > 0 GROUP BY cb.controlId")
	List<ItemWithLeftQty> getAllLeftQtyItemList();
}
