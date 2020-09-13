package com.main.glory.controller;

import com.main.glory.model.GeneralResponse;
import com.main.glory.model.shade.ShadeMast;
import com.main.glory.services.ShadeServicesInterface;
import com.main.glory.servicesImpl.ShadeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ShadeController {

	@Autowired
	ShadeServiceImpl shadeService;

	@PostMapping("/shade")
	public GeneralResponse<Boolean> addShadeData(@RequestBody ShadeMast shadeMast){
		try {
			if(shadeMast == null){
				return new GeneralResponse<>(false, "No data passed, please send valid data", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
			}

			shadeService.saveShade(shadeMast);
			return new GeneralResponse<>(true, "Data Inserted Successfully", true, System.currentTimeMillis(), HttpStatus.CREATED);

		} catch (Exception e) {
			return new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
