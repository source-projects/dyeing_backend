package com.main.glory.controller;

import com.main.glory.Dao.PartyDao;
import com.main.glory.Dao.QualityDao;
import com.main.glory.config.ControllerConfig;
import com.main.glory.model.GeneralResponse;
import com.main.glory.model.basic.PartyQuality;
import com.main.glory.model.basic.QualityParty;
import com.main.glory.model.quality.Quality;
import com.main.glory.model.shade.ShadeMast;
import com.main.glory.servicesImpl.BasicServiceImpl;
import com.main.glory.servicesImpl.QualityServiceImp;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class BasicController extends ControllerConfig {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    private QualityServiceImp qualityServiceImp;

    @Autowired
    private PartyDao partyDao;

    @Autowired
    private BasicServiceImpl basicService;

    @GetMapping("/party/ByQuality/{id}")
    public GeneralResponse<QualityParty> ByQuality(@PathVariable(value = "id") Long id){
        try{

           // String quality="sndkjabn";
            QualityParty qualityParties = qualityServiceImp.getAllQualityWithParty(id);
            if(qualityParties != null){
                return new GeneralResponse<>(qualityParties, "fetched successfully", true, System.currentTimeMillis(), HttpStatus.FOUND);
            }else{
                return new GeneralResponse<>(null, "No data found for given id", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
            }
        }catch (Exception e){
            e.printStackTrace();
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/Quality/ByParty/{id}")
    public GeneralResponse<PartyQuality> ByParty(@PathVariable(value = "id") Long partyId){
        try{

            // String quality="sndkjabn";
            PartyQuality partyQualities = qualityServiceImp.getAllPartyWithQuality(partyId);
            if(partyQualities != null){
                return new GeneralResponse<>(partyQualities, "fetched successfully", true, System.currentTimeMillis(), HttpStatus.FOUND);
            }else{
                return new GeneralResponse<>(null, "No data found for given id", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
            }
        }catch (Exception e){
            e.printStackTrace();
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @GetMapping("/QualityAndParty/ByMaster/{userHeadId}")
    public GeneralResponse<List<PartyQuality>> ByMaster(@PathVariable(value = "userHeadId") Long userHeadId){
        try{


            List<PartyQuality> partyQualities = qualityServiceImp.getAllPartyWithQualityByMaster(userHeadId);
            if(partyQualities != null){
                return new GeneralResponse<>(partyQualities, "fetched successfully", true, System.currentTimeMillis(), HttpStatus.FOUND);
            }else{
                return new GeneralResponse<>(null, "No shade data found for given id", false, System.currentTimeMillis(), HttpStatus.NOT_FOUND);
            }
        }catch (Exception e){
            e.printStackTrace();
            return new GeneralResponse<>(null, e.getMessage(), false, System.currentTimeMillis(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }



}
