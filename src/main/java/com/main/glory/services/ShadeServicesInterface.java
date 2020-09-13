package com.main.glory.services;

import com.main.glory.model.shade.ShadeData;
import com.main.glory.model.shade.ShadeMast;

import java.util.List;

public interface ShadeServicesInterface {
	public void saveShade(ShadeMast shadeMast);
	public List<ShadeMast> getAllActiveData();
	public List<ShadeMast> getAllOriginalData();
	public List<ShadeMast> getShadeMastList();
	public ShadeMast getActiveShadeData(Long aLong);
	public ShadeMast getOriginalShadeData(Long aLong);
}
