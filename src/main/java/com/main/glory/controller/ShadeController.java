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

	@GetMapping("/shade/current/all")
	public GeneralResponse<List<ShadeMast>> getAllCurrentShadeData(){
		try{
			var data = shadeService.getAllActiveData();
			return new GeneralResponse<>(data, "data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new GeneralResponse<>(null, "Internal Server Error",false,  System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/shade/original/all")
	public GeneralResponse<List<ShadeMast>> getAllOriginalShadeData(){
		try{
			var data = shadeService.getAllOriginalData();
			return new GeneralResponse<>(data, "data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new GeneralResponse<>(null, "Internal Server Error",false,  System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/shade/current/{id}")
	public GeneralResponse<ShadeMast> getCurrentShadeData(@PathVariable("id") Long id){
		try{
			var data = shadeService.getActiveShadeData(id);
			if(data == null){
				return new GeneralResponse<>(data, "No such active shade found", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
			}
			return new GeneralResponse<>(data, "data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new GeneralResponse<>(null, "Internal Server Error",false,  System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/shade/original/{id}")
	public GeneralResponse<ShadeMast> getOriginalShadeData(@PathVariable("id") Long id){
		try{
			var data = shadeService.getOriginalShadeData(id);
			if(data == null){
				return new GeneralResponse<>(data, "No such active shade found", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
			}
			return new GeneralResponse<>(data, "data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new GeneralResponse<>(null, "Internal Server Error",false,  System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/shade/all")
	public GeneralResponse<List<ShadeMastWithDetails>> getShadeMastList(){
		try{
			var data = shadeService.getShadeMastList();
			return new GeneralResponse<>(data, "data fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new GeneralResponse<>(null, "Internal Server Error",false,  System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@DeleteMapping("/shade/{id}")
	public GeneralResponse<Boolean> deleteShadeData(@PathVariable("id") Long id){
		try{
			shadeService.deleteData(id);
			return new GeneralResponse<>(true, "deleted successfully", false, System.currentTimeMillis(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new GeneralResponse<>(false, e.getMessage(),false, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
