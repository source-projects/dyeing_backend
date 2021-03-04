package com.main.glory.controller;

import com.main.glory.Dao.SupplierDao;
import com.main.glory.config.ControllerConfig;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.batch.BatchMast;
import com.main.glory.model.color.ColorBox;
import com.main.glory.model.color.ColorMast;
import com.main.glory.model.color.request.GetAllBox;
import com.main.glory.model.color.request.IssueBoxRequest;
import com.main.glory.model.color.responsemodals.ColorMastDetails;
import com.main.glory.model.color.responsemodals.SupplierItemWithLeftColorQty;
import com.main.glory.model.fabric.FabStockMast;
import com.main.glory.model.party.Party;
import com.main.glory.model.supplier.Supplier;
import com.main.glory.servicesImpl.ColorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	public ResponseEntity<GeneralResponse<Boolean>> addColor(@RequestBody ColorMast colorMast){
		GeneralResponse<Boolean> result ;
		try {
			Optional<Supplier> supplier = supplierDao.findById(colorMast.getSupplierId());
			if(supplier.isEmpty())
				result= new GeneralResponse<Boolean>(null, "No supplier found with id: "+colorMast.getSupplierId(), false, System.currentTimeMillis(), HttpStatus.OK);
			colorService.addColor(colorMast);
			result= new GeneralResponse<>(true, "Data Saved Successfully", true, System.currentTimeMillis(), HttpStatus.OK);
		} catch (Exception e) {
			String msg = e.getMessage();
			String cause = e.getCause().getMessage();
			if(cause.equals("BR") || msg.contains("null"))
				result = new GeneralResponse<Boolean>(false, msg, false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
			result =new GeneralResponse<Boolean>(false, msg, false, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
	}

	@GetMapping("/color/all/{getBy}/{id}")
	public ResponseEntity<GeneralResponse<List<ColorMastDetails>>> getColor(@PathVariable(value="getBy")String getBy, @PathVariable(value="id")Long id){
		GeneralResponse<List<ColorMastDetails>> result;
		try{
			List<ColorMastDetails> obj = null;
			switch (getBy) {
				case "own":
					//System.out.println(obj);
					obj = colorService.getAll(getBy,id);
					if(!obj.isEmpty()){
						result = new GeneralResponse<>(obj, "Data Fetched Successfully", true, System.currentTimeMillis(), HttpStatus.OK);
					} else {
						result = new GeneralResponse<>(null, "No data found", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
					}

				case "group":
					obj = colorService.getAll(getBy,id);
					if(!obj.isEmpty()){
						result = new GeneralResponse<>(obj, "Data Fetched Successfully", true, System.currentTimeMillis(), HttpStatus.OK);
					} else {
						result = new GeneralResponse<>(null, "No data found", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
					}

				case "all":
					obj = colorService.getAll(null,null);
					if(!obj.isEmpty()){
						result = new GeneralResponse<>(obj, "Data Fetched Successfully", true, System.currentTimeMillis(), HttpStatus.OK);
					} else {
						result = new GeneralResponse<>(null, "No data added yet", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
					}

				default:
					result = new GeneralResponse<>(null, "GetBy string is wrong", false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
			}

		} catch (Exception e) {
			result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));

	}

	@PutMapping("/color")
	public ResponseEntity<GeneralResponse<Boolean>> updateColor(@RequestBody ColorMast colorMast) {
		GeneralResponse<Boolean> result;
		try {
			colorService.updateColor(colorMast);
			result = new GeneralResponse<>(true, "color updated successfully", true, System.currentTimeMillis(), HttpStatus.OK);
		} catch (Exception e) {
			result = new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);

		}
		return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
	}

	@DeleteMapping(value = "/color/{id}")
	public ResponseEntity<GeneralResponse<Boolean>> deleteColorById(@PathVariable(value = "id") Long id) {
		GeneralResponse<Boolean> result;
		try{
			colorService.deleteColorById(id);
			result = new GeneralResponse<>(true, "deleted successfully", true, System.currentTimeMillis(), HttpStatus.OK);
		} catch (Exception e) {
			result = new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
	}

	@GetMapping(value = "/color/{id}")
	public ResponseEntity<GeneralResponse<ColorMast>> getColorDataById(@PathVariable(value = "id") Long id) {

		GeneralResponse<ColorMast> result;
		if (id != null) {
			var colorData = colorService.getColorById(id);
			if (colorData!=null) {
				result = new GeneralResponse<>(colorData.get(), "Fetch Success", true, System.currentTimeMillis(), HttpStatus.OK);
			} else
				result = new GeneralResponse<>(null, "No such id", false, System.currentTimeMillis(), HttpStatus.OK);
		} else
			result = new GeneralResponse<>(null, "Null Id Passed!", false, System.currentTimeMillis(), HttpStatus.OK);

		return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
	}

	@GetMapping(value = "/color/box/all/{issued}")
	public ResponseEntity<GeneralResponse<List<ColorBox>>> getColorBox(@PathVariable(value = "issued") Boolean issued){
		GeneralResponse<List<ColorBox>> result;
		try {
			List<ColorBox> colorBoxes = colorService.getAllBox(issued);
			result = new GeneralResponse<>(colorBoxes, "fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
		} catch (Exception e) {
			result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
		}
		return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
	}

	@GetMapping(value = "/color/box/{issued}/{itemId}")
	public ResponseEntity<GeneralResponse<List<GetAllBox>>> getColorBox(@PathVariable(value = "itemId") Long itemId,@PathVariable(value = "issued") Boolean issued){

		GeneralResponse<List<GetAllBox>> result;
		try {
			List<GetAllBox> colorBoxes = colorService.getAllBoxNotIssuedBoxByItemId(itemId,issued);
			result = new GeneralResponse<>(colorBoxes, "fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
		} catch (Exception e) {
			result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.OK);
		}
		return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
	}

	@GetMapping(value = "/color/supplierList/getSupplierItemWithAvailableStock")
	public ResponseEntity<GeneralResponse<List<SupplierItemWithLeftColorQty>>> getSupplierItemWithAvailableStock(){

		GeneralResponse<List<SupplierItemWithLeftColorQty>> result ;
		try {
			List<SupplierItemWithLeftColorQty> records = colorService.getSupplierItemWithAvailableStock();
			if(records.isEmpty())
				result = new GeneralResponse<>(null, "data not found", false, System.currentTimeMillis(), HttpStatus.OK);
			else
			result = new GeneralResponse<>(records, "fetched successfully", true, System.currentTimeMillis(), HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
	}


	@PostMapping("/color/box/issue")
	public ResponseEntity<GeneralResponse<Boolean>> issueBox(@RequestBody IssueBoxRequest issueBoxRequest){
		GeneralResponse<Boolean> result;
		try {
			colorService.issueBox(issueBoxRequest);
			result = new GeneralResponse<>(true, "Box issued successfully", true, System.currentTimeMillis(), HttpStatus.OK);
		} catch (Exception e) {
			result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
		}
		return  new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
	}

	//get all color boxec
	@GetMapping("/color/box/all")
	public ResponseEntity<GeneralResponse<List<GetAllBox>>> getAllBoxes(){
		GeneralResponse<List<GetAllBox>> response;
		try {
			List<GetAllBox> list = colorService.getAllColorBoxes();
			response= new GeneralResponse<>(list, "Box issued successfully", true, System.currentTimeMillis(), HttpStatus.OK);
		} catch (Exception e) {
			response= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(response,HttpStatus.valueOf(response.getStatusCode()));
	}


}
