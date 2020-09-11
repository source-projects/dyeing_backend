package com.main.glory.controller;

import com.main.glory.model.color.ColorMast;
import com.main.glory.servicesImpl.ColorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ColorController {

	@Autowired
	ColorServiceImpl colorService;

	@PostMapping("/color")
	public Boolean addColor(@RequestBody ColorMast colorMast){
		return colorService.addColor(colorMast);
	}

	@GetMapping("/color-all")
	public List<ColorMast> getColor(){
		return colorService.getAll();
	}
}
