package com.main.glory.Dao.color;

import com.main.glory.model.color.ColorBox;
import com.main.glory.model.color.request.GetAllBox;
import com.main.glory.model.color.request.ItemWithLeftQty;
import com.main.glory.model.color.responsemodals.SupplierItemWithLeftColorQty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.bind.annotation.RequestParam;

import javax.transaction.Transactional;
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

	@Query("select c from ColorBox c where c.id=:boxId")
    ColorBox getColorBoxById(Long boxId);

	@Query("select c from ColorBox c where c.controlId=:id")
	List<ColorBox> getAllIssueBoxQty(Long id);

	@Query("select c from ColorBox c where c.controlId=:id")
	List<ColorBox> getAllBoxesByControlId(Long id);


	@Query("select c from ColorBox c where c.controlId=:id AND c.issued=:issued")
	List<ColorBox> getAllBoxByControlIdWithFlag(Long id, Boolean issued);

	@Modifying
	@Transactional
	@Query("delete from ColorBox c where c.controlId IS NULL")
    void deleteColorBoxWhichIsNUll();

	@Query(value = "select * from color_box as x where x.issued=:issued and x.controlId IN (select c.id from color_data as c where c.item_id=:itemId)  AND x.ORDER BY x.box_no DESC LIMTI 1",nativeQuery = true)
    ColorBox getLatestColorBoxByItemIdWithIssuseFlag(@RequestParam("itemId") Long itemId, @RequestParam("issued") Boolean issued);

	@Query("select sum(x.quantityLeft) from ColorBox x where x.controlId IN(select c.id from ColorData c where c.itemId=:itemId) AND x.issued=:issued")
	Double getTotalQtyLeftByItemIdWithIssueFlag(Long itemId, Boolean issued);
}
