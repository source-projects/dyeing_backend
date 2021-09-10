package com.main.glory.controller;

import com.main.glory.Dao.SupplierDao;
import com.main.glory.config.ControllerConfig;
import com.main.glory.filters.FilterResponse;
import com.main.glory.model.ConstantFile;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.StockDataBatchData.request.GetBYPaginatedAndFiltered;
import com.main.glory.model.color.ColorAcknowledgement;
import com.main.glory.model.color.ColorBox;
import com.main.glory.model.color.ColorMast;
import com.main.glory.model.color.request.GetAllBox;
import com.main.glory.model.color.request.IssueBoxRequest;
import com.main.glory.model.color.responsemodals.ColorMastDetails;
import com.main.glory.model.color.responsemodals.SupplierItemWithLeftColorQty;
import com.main.glory.model.supplier.Supplier;
import com.main.glory.servicesImpl.ColorServiceImpl;
import com.main.glory.servicesImpl.LogServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ColorController extends ControllerConfig {


	ConstantFile constantFile;

	@Autowired
	LogServiceImpl logService;

	@Autowired
	HttpServletRequest request;

	@Value("${spring.application.debugAll}")
	Boolean debugAll;

	@Autowired
	SupplierDao supplierDao;

	@Autowired
	ColorServiceImpl colorService;

	@PostMapping("/color")
	public ResponseEntity<GeneralResponse<Boolean,Object>> addColor(@RequestBody ColorMast colorMast,@RequestHeader Map<String, String> headers){
		GeneralResponse<Boolean,Object> result ;
		try {
			Optional<Supplier> supplier = supplierDao.findById(colorMast.getSupplierId());
			if(supplier.isEmpty())
				result= new GeneralResponse<>(null, constantFile.Supplier_Not_Found+colorMast.getSupplierId(), false, System.currentTimeMillis(), HttpStatus.OK,colorMast);
			else {
				colorService.addColor(colorMast,headers.get("id"));
				result = new GeneralResponse<>(true, constantFile.Color_Found, true, System.currentTimeMillis(), HttpStatus.OK,colorMast);

			}
			logService.saveLog(result,request,debugAll);
		} catch (Exception e) {
			String msg = e.getMessage();
			String cause = e.getCause().getMessage();
			if(cause.equals("BR") || msg.contains("null"))
				result = new GeneralResponse<>(false, msg, false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,colorMast);
			else
			result =new GeneralResponse<>(false, msg, false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,colorMast);
			logService.saveLog(result,request,true);
		}
		return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
	}

	@GetMapping("/color/all/{getBy}/{id}")
	public ResponseEntity<GeneralResponse<List<ColorMastDetails>,Object>> getColor(@PathVariable(value="getBy")String getBy, @PathVariable(value="id")Long id){
		GeneralResponse<List<ColorMastDetails>,Object> result;
		try{
			List<ColorMastDetails> obj = null;
			switch (getBy) {
				case "own":
					//System.out.println(obj);
					obj = colorService.getAll(getBy,id);
					if(!obj.isEmpty()){
						result = new GeneralResponse<>(obj, constantFile.Color_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
					} else {
						result = new GeneralResponse<>(null, constantFile.Color_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
					}


					break;
				case "group":
					obj = colorService.getAll(getBy,id);
					if(!obj.isEmpty()){
						result = new GeneralResponse<>(obj, constantFile.Color_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
					} else {
						result = new GeneralResponse<>(null, constantFile.Color_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
					}
					break;
				case "all":
					obj = colorService.getAll(null,null);
					if(!obj.isEmpty()){
						result = new GeneralResponse<>(obj, constantFile.Color_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
					} else {
						result = new GeneralResponse<>(null, constantFile.Color_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
					}

					break;
				default:
					result = new GeneralResponse<>(null, constantFile.GetBy_String_Wrong, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());


			}

		} catch (Exception e) {
			e.printStackTrace();
			result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI());
			logService.saveLog(result,request,true);
		}
		logService.saveLog(result,request,debugAll);
		return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));

	}

	@PutMapping("/color")
	public ResponseEntity<GeneralResponse<Boolean,Object>> updateColor(@RequestBody ColorMast colorMast) {
		GeneralResponse<Boolean,Object> result;
		try {
			colorService.updateColor(colorMast);
			result = new GeneralResponse<>(true, constantFile.Color_Updated, true, System.currentTimeMillis(), HttpStatus.OK,colorMast);
			logService.saveLog(result,request,debugAll);
		} catch (Exception e) {
			e.printStackTrace();
			result = new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,colorMast);
			logService.saveLog(result,request,true);


		}
		return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
	}


	@PostMapping("/color/allPaginated")
	public ResponseEntity<GeneralResponse<FilterResponse<ColorMastDetails>,Object>> getColorPaginated(@RequestBody GetBYPaginatedAndFiltered requestParam,@RequestHeader Map<String,String> header){
		GeneralResponse<FilterResponse<ColorMastDetails>,Object> result;
		String id=header.get("id");
        if(id=="")id=null;

		try{
			FilterResponse<ColorMastDetails> obj = null;
			switch (requestParam.getGetBy()) {
				case "own":
					//System.out.println(obj);
					obj = colorService.getAllPaginated(requestParam, id);
					if(!obj.getData().isEmpty()){
						result = new GeneralResponse<>(obj, constantFile.Color_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
					} else {
						result = new GeneralResponse<>(null, constantFile.Color_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
					}


					break;
				case "group":
					obj = colorService.getAllPaginated(requestParam, id);
					if(!obj.getData().isEmpty()){
						result = new GeneralResponse<>(obj, constantFile.Color_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
					} else {
						result = new GeneralResponse<>(null, constantFile.Color_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
					}
					break;
				case "all":
					obj = colorService.getAllPaginated(requestParam, id);
					if(!obj.getData().isEmpty()){
						result = new GeneralResponse<>(obj, constantFile.Color_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
					} else {
						result = new GeneralResponse<>(null, constantFile.Color_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
					}

					break;
				default:
					result = new GeneralResponse<>(null, constantFile.GetBy_String_Wrong, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());


			}

		} catch (Exception e) {
			e.printStackTrace();
			result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI());
			logService.saveLog(result,request,true);
		}
		logService.saveLog(result,request,debugAll);
		return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));

	}



	@DeleteMapping(value = "/color/{id}")
	public ResponseEntity<GeneralResponse<Boolean,Object>> deleteColorById(@PathVariable(value = "id") Long id) {
		GeneralResponse<Boolean,Object> result;
		try{
			colorService.deleteColorById(id);
			result = new GeneralResponse<>(true, constantFile.Color_Deleted, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
			logService.saveLog(result,request,debugAll);
		} catch (Exception e) {
			e.printStackTrace();
			result = new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI());
			logService.saveLog(result,request,true);
		}
		return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
	}

	@GetMapping(value = "/color/{id}")
	public ResponseEntity<GeneralResponse<ColorMast,Object>> getColorDataById(@PathVariable(value = "id") Long id) {

		GeneralResponse<ColorMast,Object> result;
		try {
			if (id != null) {
				var colorData = colorService.getColorById(id);
				if (colorData != null) {
					result = new GeneralResponse<>(colorData.get(), constantFile.Color_Found, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI());
				} else
					result = new GeneralResponse<>(null, constantFile.Color_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI());
				logService.saveLog(result, request, debugAll);
			} else {
				result = new GeneralResponse<>(null, constantFile.Null_Record_Passed, false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, request.getRequestURI());
				logService.saveLog(result, request, true);
			}
		}catch (Exception e)
		{
			e.printStackTrace();
			result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST, request.getRequestURI());
			logService.saveLog(result, request, true);
		}
		return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
	}

	@GetMapping(value = "/color/box/all/{issued}")
	public ResponseEntity<GeneralResponse<List<ColorBox>,Object>> getColorBox(@PathVariable(value = "issued") Boolean issued){
		GeneralResponse<List<ColorBox>,Object> result;
		try {
			List<ColorBox> colorBoxes = colorService.getAllBox(issued);
			result = new GeneralResponse<>(colorBoxes, constantFile.Color_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
			logService.saveLog(result,request,debugAll);
		} catch (Exception e) {
			e.printStackTrace();
			result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI());
			logService.saveLog(result,request,true);
		}
		return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
	}

	@GetMapping(value = "/color/box/{issued}/{itemId}")
	public ResponseEntity<GeneralResponse<List<GetAllBox>,Object>> getColorBox(@PathVariable(value = "itemId") Long itemId,@PathVariable(value = "issued") Boolean issued){

		GeneralResponse<List<GetAllBox>,Object> result;
		try {
			List<GetAllBox> colorBoxes = colorService.getAllBoxNotIssuedBoxByItemId(itemId,issued);
			if(!colorBoxes.isEmpty()) {
				result = new GeneralResponse<>(colorBoxes, constantFile.Color_Found, true, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI());
			}
			else
			{
				result = new GeneralResponse<>(colorBoxes, constantFile.Color_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK, request.getRequestURI());
			}
			logService.saveLog(result,request,debugAll);
		} catch (Exception e) {
			e.printStackTrace();
			result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI());
			logService.saveLog(result,request,true);
		}
		return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
	}

	@GetMapping(value = "/color/supplierList/getSupplierItemWithAvailableStock")
	public ResponseEntity<GeneralResponse<List<SupplierItemWithLeftColorQty>,Object>> getSupplierItemWithAvailableStock(){

		GeneralResponse<List<SupplierItemWithLeftColorQty>,Object> result ;
		try {
			List<SupplierItemWithLeftColorQty> records = colorService.getSupplierItemWithAvailableStock();
			if(records.isEmpty())
				result = new GeneralResponse<>(null, constantFile.Color_Not_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
			else
			result = new GeneralResponse<>(records, constantFile.Color_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
			logService.saveLog(result,request,debugAll);
		} catch (Exception e) {
			e.printStackTrace();
			result = new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI());
			logService.saveLog(result,request,true);
		}
		return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
	}


	@PostMapping("/color/box/issue")
	public ResponseEntity<GeneralResponse<Boolean,Object>> issueBox(@RequestBody IssueBoxRequest issueBoxRequest){
		GeneralResponse<Boolean,Object> result;
		try {
			colorService.issueBox(issueBoxRequest);
			result = new GeneralResponse<>(true, constantFile.Color_Issue, true, System.currentTimeMillis(), HttpStatus.OK,issueBoxRequest);
			logService.saveLog(result,request,debugAll);
		} catch (Exception e) {
			e.printStackTrace();
			result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,issueBoxRequest);
			logService.saveLog(result,request,true);
		}
		return  new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
	}
	@PostMapping("/color/box/issueWithList")
	public ResponseEntity<GeneralResponse<Boolean,Object>> issueBox(@RequestBody List<IssueBoxRequest> issueBoxRequest){
		GeneralResponse<Boolean,Object> result;
		try {
			colorService.issueBoxList(issueBoxRequest);
			result = new GeneralResponse<>(true, constantFile.Color_Issue, true, System.currentTimeMillis(), HttpStatus.OK,issueBoxRequest);
			logService.saveLog(result,request,debugAll);
		} catch (Exception e) {
			e.printStackTrace();
			result= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,issueBoxRequest);
			logService.saveLog(result,request,true);
		}
		return  new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
	}

	//get all color boxec
	@GetMapping("/color/box/all")
	public ResponseEntity<GeneralResponse<List<GetAllBox>,Object>> getAllBoxes(){
		GeneralResponse<List<GetAllBox>,Object> response;
		try {
			List<GetAllBox> list = colorService.getAllColorBoxes();
			if(!list.isEmpty())
			response= new GeneralResponse<>(list, constantFile.Color_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());
			else
				response= new GeneralResponse<>(list, constantFile.Color_Found, false, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());

			logService.saveLog(response,request,debugAll);
		} catch (Exception e) {
			e.printStackTrace();
			response= new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI());
			logService.saveLog(response,request,true);
		}
		return new ResponseEntity<>(response,HttpStatus.valueOf(response.getStatusCode()));
	}

	//get the color stock update aknowledgement
	@PutMapping("/color/box/changeExistingColorStock")
	public ResponseEntity<GeneralResponse<Boolean,Object>> changeExistingColorStock(@RequestBody ColorAcknowledgement record){
		GeneralResponse<Boolean,Object> result;
		try {

			colorService.changeExistingColorStock(record);
			result= new GeneralResponse<>(true, constantFile.Color_Found, true, System.currentTimeMillis(), HttpStatus.OK,request.getRequestURI());


			logService.saveLog(result,request,debugAll);
		} catch (Exception e) {
			e.printStackTrace();
			result= new GeneralResponse<>(false, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.BAD_REQUEST,request.getRequestURI());
			logService.saveLog(result,request,true);
		}
		return new ResponseEntity<>(result,HttpStatus.valueOf(result.getStatusCode()));
	}




}
