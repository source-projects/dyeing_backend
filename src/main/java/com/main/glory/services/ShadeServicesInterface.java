package com.main.glory.services;

import com.main.glory.model.shade.ShadeData;
import com.main.glory.model.shade.ShadeMast;
import com.main.glory.model.shade.requestmodals.UpdateShadeMastRequest;

import java.util.List;

public interface ShadeServicesInterface {
	public void saveShade(ShadeMast shadeMast);
	public List<ShadeMast> getAllActiveData();
	public List<ShadeMast> getAllOriginalData();
	public List<ShadeMast> getShadeMastList();
	public ShadeMast getActiveShadeData(Long aLong);
	public ShadeMast getOriginalShadeData(Long aLong);
	public void deleteData(Long aLong);
	public void updateShadeMast(UpdateShadeMastRequest updateShadeMastRequest);
	public void updateShadeData(List<ShadeData> shadeDataList);
}
