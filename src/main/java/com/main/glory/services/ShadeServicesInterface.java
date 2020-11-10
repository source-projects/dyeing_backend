package com.main.glory.services;

import com.main.glory.model.shade.ShadeMast;

import java.util.List;
import java.util.Optional;

public interface ShadeServicesInterface {
	public void saveShade(ShadeMast shadeMast);
	public List<ShadeMast> getAllShadeMast();
	public Optional<ShadeMast> getShadeMastById(Long id);
	public Boolean updateShade(ShadeMast shadeMast);
}
