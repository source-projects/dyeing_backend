package com.main.glory.controller;

import com.main.glory.config.ControllerConfig;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.shade.ShadeMast;
import com.main.glory.model.shade.responsemodals.ShadeMastWithDetails;
import com.main.glory.services.ShadeServicesInterface;
import com.main.glory.servicesImpl.ShadeServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ShadeController extends ControllerConfig {

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
			e.printStackTrace();
			return new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/shade/all")
	public GeneralResponse<List<ShadeMast>> getAllShades(){
		try{
			List<ShadeMast> shadeMast = shadeService.getAllShadeMast();
			if(shadeMast != null){
				return new GeneralResponse<List<ShadeMast>>(shadeMast, "fetched successfully", false, System.currentTimeMillis(), HttpStatus.FOUND);
			}else{
				return new GeneralResponse<>(null, "No shade data added yet", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
			}
		}catch (Exception e){
			e.printStackTrace();
			return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}



}
