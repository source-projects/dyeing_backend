package com.main.glory.controller;

import com.main.glory.config.ControllerConfig;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.batch.BatchMast;
import com.main.glory.model.color.ColorBox;
import com.main.glory.model.color.ColorMast;
import com.main.glory.model.color.responsemodals.ColorMastDetails;
import com.main.glory.model.fabric.FabStockMast;
import com.main.glory.servicesImpl.ColorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ColorController extends ControllerConfig {

	@Autowired
	ColorServiceImpl colorService;

	@PostMapping("/color")
	public GeneralResponse<Boolean> addColor(@RequestBody ColorMast colorMast){
		try {
			colorService.addColor(colorMast);
			return new GeneralResponse<>(true, "Data Saved Successfully", true, System.currentTimeMillis(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			String msg = e.getMessage();
			String cause = e.getCause().getMessage();
			if(cause.equals("BR") || msg.contains("null"))
				return new GeneralResponse<Boolean>(false, msg, false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
			return new GeneralResponse<Boolean>(false, msg, false, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		/*try{
			colorService.addColor(colorMast);
			return new GeneralResponse<Boolean>(true, "Color data added successfully", true, System.currentTimeMillis(), HttpStatus.OK);
		} catch (Exception e) {
			return new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
		}*/
	}

	@GetMapping("/color/all")
	public GeneralResponse<List<ColorMastDetails>> getColor(){
		try{
			List<ColorMastDetails> obj = colorService.getAll();
			if(obj != null){
				System.out.println(obj);
				return new GeneralResponse<>(obj, "Data Fetched Successfully", true, System.currentTimeMillis(), HttpStatus.OK);
			} else {
				return new GeneralResponse<>(null, "No Such Data Found", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new GeneralResponse<>(null, "Internal Server Error", true, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/color")
	public GeneralResponse<Boolean> updateColor(@RequestBody ColorMast colorMast) {
		try {
			colorService.updateColor(colorMast);
			return new GeneralResponse<>(true, "color updated successfully", true, System.currentTimeMillis(), HttpStatus.OK);
		} catch (Exception e) {
			return new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);

		}
	}

	@DeleteMapping(value = "/color/{id}")
	public GeneralResponse<Boolean> deleteColorById(@PathVariable(value = "id") Long id) {
		try{
			colorService.deleteColorById(id);
			return new GeneralResponse<>(true, "deleted successfully", true, System.currentTimeMillis(), HttpStatus.OK);
		} catch (Exception e) {
			return new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(value = "/color/{id}")
	public GeneralResponse<ColorMast> getColorDataById(@PathVariable(value = "id") Long id) {
		if (id != null) {
			var colorData = colorService.getColorById(id);
			if (colorData!=null) {
				return new GeneralResponse<>(colorData.get(), "Fetch Success", true, System.currentTimeMillis(), HttpStatus.FOUND);
			} else
				return new GeneralResponse<>(null, "No such id", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
		} else
			return new GeneralResponse<>(null, "Null Id Passed!", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);

	}

	@GetMapping(value = "/box/all/{issued}")
	public GeneralResponse<List<ColorBox>> getColorBox(@PathVariable(value = "issued") Boolean issued){
		try {
			List<ColorBox> colorBoxes = colorService.getAllBox(issued);
			return new GeneralResponse<>(colorBoxes, "fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
		} catch (Exception e) {
			return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
