package com.main.glory.model.color.responsemodals;

import com.main.glory.model.color.ColorData;
import com.main.glory.model.color.ColorMast;
import lombok.*;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ColorMastDetails extends ColorMast {
	String SupplierName;

	public ColorMastDetails(Long id, Long supplierId, String billNo, Date billDate, String chlNo, Date chlDate, Double billAmount, Long userId, String remark, Date createdDate, List<ColorData> colorDataList) {
		super(id, supplierId, billNo, billDate, chlNo, chlDate, billAmount, userId, remark, createdDate, colorDataList);
	}

	public ColorMastDetails(ColorMast colorMast) {
		super(colorMast.getId(), colorMast.getSupplierId(), colorMast.getBillNo(), colorMast.getBillDate(), colorMast.getChlNo(), colorMast.getChlDate(), colorMast.getBillAmount(), colorMast.getUserId(), colorMast.getRemark(), colorMast.getCreatedDate(), colorMast.getColorDataList());
	}
}
