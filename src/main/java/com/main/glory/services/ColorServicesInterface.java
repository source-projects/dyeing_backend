package com.main.glory.services;

import com.main.glory.model.color.ColorMast;

import java.util.List;

public interface ColorServicesInterface {
	public Boolean addColor(ColorMast colorMast);
	public List<ColorMast >getAll();
}
