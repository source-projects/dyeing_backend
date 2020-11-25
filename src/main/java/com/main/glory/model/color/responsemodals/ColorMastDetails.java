package com.main.glory.model.color.responsemodals;

import com.main.glory.model.color.ColorData;
import com.main.glory.model.color.ColorMast;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
public class ColorMastDetails extends ColorMast {
	String SupplierName;

	public ColorMastDetails(Long id, Long supplierId, String billNo, Date billDate, String chlNo, Date chlDate, Double billAmount, Long createdBy, Long updatedBy, Long userHeadId, String remark, List<ColorData> colorDataList) {
		super(id, supplierId, billNo, billDate, chlNo, chlDate, billAmount, createdBy, updatedBy, userHeadId, remark, colorDataList);
	}

	public ColorMastDetails(ColorMast colorMast) {
		super(colorMast.getId(), colorMast.getSupplierId(), colorMast.getBillNo(), colorMast.getBillDate(), colorMast.getChlNo(), colorMast.getChlDate(), colorMast.getBillAmount(), colorMast.getCreatedBy(), colorMast.getUpdatedBy(), colorMast.getUserHeadId(), colorMast.getRemark(), colorMast.getColorDataList());
	}
}
