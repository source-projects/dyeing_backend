package com.main.glory.services;

import com.main.glory.model.shade.ShadeMast;
import com.main.glory.model.shade.requestmodals.AddShadeMast;

import java.util.List;
import java.util.Optional;

public interface ShadeServicesInterface {
	public void saveShade(AddShadeMast shadeMast)throws Exception;
	public List<ShadeMast> getAllShadeMast() throws Exception;
	public Optional<ShadeMast> getShadeMastById(Long id);
	public Boolean updateShade(ShadeMast shadeMast);
}
