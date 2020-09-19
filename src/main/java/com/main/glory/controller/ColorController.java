package com.main.glory.controller;

import com.main.glory.model.GeneralResponse;
import com.main.glory.model.color.ColorMast;
import com.main.glory.model.color.responsemodals.ColorMastDetails;
import com.main.glory.servicesImpl.ColorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ColorController {

	@Autowired
	ColorServiceImpl colorService;

	@PostMapping("/color")
	public GeneralResponse<Boolean> addColor(@RequestBody ColorMast colorMast){
		try{
			return new GeneralResponse<Boolean>(true, "Color data added successfully", true, System.currentTimeMillis(), HttpStatus.OK);

		} catch (Exception e) {
			return new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/color/all")
	public GeneralResponse<List<ColorMastDetails>> getColor(){
		try{
			List<ColorMastDetails> obj = colorService.getAll();
			if(obj != null){
				return new GeneralResponse<>(obj, "Data Fetched Successfully", true, System.currentTimeMillis(), HttpStatus.OK);
			} else {
				return new GeneralResponse<>(null, "No Such Data Found", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new GeneralResponse<>(null, "Internal Server Error", true, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
