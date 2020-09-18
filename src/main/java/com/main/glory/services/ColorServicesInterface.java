package com.main.glory.services;

import com.main.glory.model.color.ColorMast;
import com.main.glory.model.color.responsemodals.ColorMastDetails;

import java.util.List;

public interface ColorServicesInterface {
	public void addColor(ColorMast colorMast);
	public List<ColorMastDetails> getAll();
}
