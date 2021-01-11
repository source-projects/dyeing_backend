package com.main.glory.controller;

import com.main.glory.Dao.SupplierDao;
import com.main.glory.config.ControllerConfig;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.batch.BatchMast;
import com.main.glory.model.color.ColorBox;
import com.main.glory.model.color.ColorMast;
import com.main.glory.model.color.request.IssueBoxRequest;
import com.main.glory.model.color.responsemodals.ColorMastDetails;
import com.main.glory.model.fabric.FabStockMast;
import com.main.glory.model.party.Party;
import com.main.glory.model.supplier.Supplier;
import com.main.glory.servicesImpl.ColorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ColorController extends ControllerConfig {

	@Autowired
	SupplierDao supplierDao;

	@Autowired
	ColorServiceImpl colorService;

	@PostMapping("/color")
	public GeneralResponse<Boolean> addColor(@RequestBody ColorMast colorMast){
		try {
			Optional<Supplier> supplier = supplierDao.findById(colorMast.getSupplierId());
			if(supplier.isEmpty())
				return new GeneralResponse<Boolean>(null, "No supplier found with id: "+colorMast.getSupplierId(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
			colorService.addColor(colorMast);
			return new GeneralResponse<>(true, "Data Saved Successfully", true, System.currentTimeMillis(), HttpStatus.OK);
		} catch (Exception e) {
			String msg = e.getMessage();
			String cause = e.getCause().getMessage();
			if(cause.equals("BR") || msg.contains("null"))
				return new GeneralResponse<Boolean>(false, msg, false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
			return new GeneralResponse<Boolean>(false, msg, false, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/color/all/{getBy}/{id}")
	public GeneralResponse<List<ColorMastDetails>> getColor(@PathVariable(value="getBy")String getBy, @PathVariable(value="id")Long id){
		try{
			List<ColorMastDetails> obj = null;
			switch (getBy) {
				case "own":
					//System.out.println(obj);
					obj = colorService.getAll(getBy,id);
					if(!obj.isEmpty()){
						return new GeneralResponse<>(obj, "Data Fetched Successfully", true, System.currentTimeMillis(), HttpStatus.OK);
					} else {
						return new GeneralResponse<>(null, "No data found", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
					}

				case "group":
					obj = colorService.getAll(getBy,id);
					if(!obj.isEmpty()){
						return new GeneralResponse<>(obj, "Data Fetched Successfully", true, System.currentTimeMillis(), HttpStatus.OK);
					} else {
						return new GeneralResponse<>(null, "No data found", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
					}

				case "all":
					obj = colorService.getAll(null,null);
					if(!obj.isEmpty()){
						return new GeneralResponse<>(obj, "Data Fetched Successfully", true, System.currentTimeMillis(), HttpStatus.OK);
					} else {
						return new GeneralResponse<>(null, "No data added yet", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
					}

				default:
					return new GeneralResponse<>(null, "GetBy string is wrong", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {
			return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
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

	@PostMapping("/box/issue")
	public GeneralResponse<Boolean> issueBox(@RequestBody IssueBoxRequest issueBoxRequest){
		try {
			colorService.issueBox(issueBoxRequest);
			return new GeneralResponse<>(true, "Box issued successfully", true, System.currentTimeMillis(), HttpStatus.OK);
		} catch (Exception e) {
			return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
		}
	}

}
