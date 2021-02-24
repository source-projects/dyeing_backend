package com.main.glory.servicesImpl;

import com.main.glory.Dao.*;
import com.main.glory.Dao.productionPlan.ProductionPlanDao;
import com.main.glory.Dao.user.UserDao;
import com.main.glory.model.dyeingProcess.DyeingProcessMast;
import com.main.glory.model.party.Party;
import com.main.glory.model.productionPlan.ProductionPlan;
import com.main.glory.model.quality.Quality;
import com.main.glory.model.shade.APC;
import com.main.glory.model.shade.ShadeData;
import com.main.glory.model.shade.ShadeMast;
import com.main.glory.model.shade.requestmodals.*;
import com.main.glory.model.user.UserData;
import com.main.glory.services.ShadeServicesInterface;
import org.hibernate.engine.jdbc.env.spi.QualifiedObjectNameFormatter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service("ShadeServiceImpl")
public class ShadeServiceImpl implements ShadeServicesInterface {


	@Autowired
	ProductionPlanDao productionPlanDao;

	@Autowired
	ProductionPlanImpl productionPlanService;
	@Autowired
	APCDao acpDao;
	
	@Autowired
	UserDao userDao;

	@Autowired
	ShadeMastDao shadeMastDao;

	@Autowired
	ShadeDataDao shadeDataDao;

	@Autowired
	SupplierServiceImpl supplierService;

	@Autowired
	QualityDao qualityDao;

	@Autowired
	PartyDao partyDao;

	@Autowired
	DyeingProcessServiceImpl dyeingProcessService;

	@Autowired
	ModelMapper modelMapper;


	@Transactional

	public void saveShade(AddShadeMast shadeMast) throws Exception{

		if (shadeMast.getShadeDataList()==null || shadeMast.getShadeDataList().isEmpty())
		{
			Optional<Quality> quality=qualityDao.findByQualityIdAndQualityName(shadeMast.getQualityId(),shadeMast.getQualityName());
			if(!quality.isPresent())
			{
				throw new Exception("Quality Not Found with QualityId:"+shadeMast.getQualityId()+" and QualityName:"+shadeMast.getQualityName());
			}

			//check the dyeing process for the shade is available or not

			DyeingProcessMast processMastExist = dyeingProcessService.getDyeingProcessById(shadeMast.getProcessId());

			ShadeMast shadeData =  new ShadeMast(shadeMast);
			shadeData.setPending(true);

			shadeData.setQualityEntryId(quality.get().getId());
			shadeData.setShadeDataList(null);

			//check the ACP number

			/*APC numberExist =acpDao.getAcpNumberExist(Long.parseLong(shadeMast.getApcNo()));

			if(numberExist!=null)
				throw new Exception("APC number is already available");

			APC apc=new APC(Long.parseLong(shadeMast.getApcNo()));
			acpDao.save(apc);*/
			shadeMastDao.save(shadeData);





		}
		else {
			//consider we have data and add directlt
			Optional<Quality> quality=qualityDao.findByQualityIdAndQualityName(shadeMast.getQualityId(),shadeMast.getQualityName());
			if(!quality.isPresent())
			{
				throw new Exception("Quality Not Found with QualityId:"+shadeMast.getQualityId()+" and QualityName:"+shadeMast.getQualityName());
			}

			//check the dyeing process for the shade is available or not

			DyeingProcessMast processMastExist = dyeingProcessService.getDyeingProcessById(shadeMast.getProcessId());

			ShadeMast shadeData =  new ShadeMast(shadeMast);
			shadeData.setPending(false);
			shadeData.setQualityEntryId(quality.get().getId());
			shadeData.setShadeDataList(shadeMast.getShadeDataList());
			//check the ACP number

			/*APC numberExist =acpDao.getAcpNumberExist(Long.parseLong(shadeMast.getApcNo()));

			if(numberExist!=null)
				throw new Exception("APC number is already available");

			APC apc=new APC(Long.parseLong(shadeMast.getApcNo()));
			acpDao.save(apc);*/
			shadeMastDao.save(shadeData);



		}



	}


	@Override
	public List<ShadeMast> getAllShadeMast() throws Exception{
		List<ShadeMast> shadeMastList = shadeMastDao.getAllShadeMast();
		if(shadeMastList.isEmpty())
			throw new Exception("no party shade found");
		else{
			return shadeMastList;
		}
	}

	@Override
	public Optional<ShadeMast> getShadeMastById(Long id) throws Exception {
		Optional<ShadeMast> shadeMastList = shadeMastDao.findById(id);

		if(shadeMastList.isPresent() && shadeMastList.get().getPartyId()!=null && shadeMastList.get().getQualityEntryId()!=null)
			return shadeMastList;
		else{
			throw new Exception("shade data not found");
		}
	}

	@Override
	public Boolean updateShade(ShadeMast shadeMast) {
		if(shadeMast.getShadeDataList()==null || shadeMast.getShadeDataList().isEmpty())
			shadeMast.setPending(true);
		else
			shadeMast.setPending(false);

		ShadeMast shadeIndex = shadeMastDao.getShadeMastById(shadeMast.getId());
		if(shadeIndex==null)
			return false;
		else{
			try{
				//System.out.println(shadeMast);
				List<ProductionPlan> productionPlansList =productionPlanService.getProductionByShadeId(shadeMast.getId());
				ShadeMast x = shadeMastDao.save(shadeMast);
				for(ProductionPlan p :productionPlansList)
				{
					productionPlanDao.updateProductionWithShadeId(p.getId(),x.getId());
				}
				//shadeDataDao.saveAll(shadeMast.getShadeDataList());
			}catch(Exception e){
				e.printStackTrace();
				return false;
			}

		}
		return true;
	}

	@Transactional
	public boolean deleteShadeById(Long id) throws Exception{
		Optional<ShadeMast> shadeMast = shadeMastDao.findById(id);
		// check if this is present in the database
		if(shadeMast.isEmpty()){
			throw new Exception("shade data does not exist with id:"+id);
		}
		shadeMastDao.deleteById(id);

		return true;
	}

	public List<GetAllShade> getAllShadesInfo(String getBy, Long id) throws Exception {
		List<ShadeMast> shadeMastList = null;
		List<GetAllShade> getAllShadesList = new ArrayList<>();
		if(id == null){
			shadeMastList = shadeMastDao.getAllShadeMast();
			for (ShadeMast e : shadeMastList) {

				if(e.getPartyId()!=null && e.getQualityEntryId()!=null)
				{
					DyeingProcessMast dyeingProcessMast = dyeingProcessService.getDyeingProcessById(e.getProcessId());

					Optional<Party> party = partyDao.findById(e.getPartyId());
					Optional<Quality> qualityName=qualityDao.findById(e.getQualityEntryId());

					if(dyeingProcessMast==null)
						continue;

					if(!qualityName.isPresent())
						continue;
					if(!party.isPresent())
						continue;

					if(e.getShadeDataList()==null || e.getShadeDataList().isEmpty() || e.getShadeDataList().get(0).getSupplierItemId()==null)
						continue;

					getAllShadesList.add(new GetAllShade(e,party,qualityName,dyeingProcessMast));

				}

			}
		}
		else if(getBy.equals("own")){
			shadeMastList = shadeMastDao.findAllByCreatedBy(id);
			for (ShadeMast e : shadeMastList) {

				if(e.getPartyId()!=null && e.getQualityEntryId()!=null)
				{
					DyeingProcessMast dyeingProcessMast = dyeingProcessService.getDyeingProcessById(e.getProcessId());

					Optional<Party> party = partyDao.findById(e.getPartyId());
					Optional<Quality> qualityName=qualityDao.findById(e.getQualityEntryId());

					if(dyeingProcessMast==null)
						continue;

					if(!qualityName.isPresent())
						continue;
					if(!party.isPresent())
						continue;
					if(e.getShadeDataList()==null || e.getShadeDataList().isEmpty()|| e.getShadeDataList().get(0).getSupplierItemId()==null)
						continue;

					getAllShadesList.add(new GetAllShade(e,party,qualityName,dyeingProcessMast));

				}
			}
		}
		else if(getBy.equals("group")){
			UserData userData = userDao.findUserById(id);

			if(userData.getUserHeadId()==0) {
				//master user
				shadeMastList = shadeMastDao.findAllByCreatedByAndHeadId(id,id);
				for (ShadeMast e : shadeMastList) {

					if(e.getPartyId()!=null && e.getQualityEntryId()!=null)
					{
						DyeingProcessMast dyeingProcessMast = dyeingProcessService.getDyeingProcessById(e.getProcessId());

						Optional<Party> party = partyDao.findById(e.getPartyId());
						Optional<Quality> qualityName=qualityDao.findById(e.getQualityEntryId());

						if(dyeingProcessMast==null)
							continue;

						if(!qualityName.isPresent())
							continue;
						if(!party.isPresent())
							continue;

						if(e.getShadeDataList()==null || e.getShadeDataList().isEmpty()|| e.getShadeDataList().get(0).getSupplierItemId()==null)
							continue;

						getAllShadesList.add(new GetAllShade(e,party,qualityName,dyeingProcessMast));


					}
				}
			}
			else
			{
				shadeMastList = shadeMastDao.findAllByCreatedByAndHeadId(id,userData.getUserHeadId());
				for (ShadeMast e : shadeMastList) {

					if(e.getPartyId()!=null && e.getQualityEntryId()!=null)
					{
						DyeingProcessMast dyeingProcessMast = dyeingProcessService.getDyeingProcessById(e.getProcessId());

						Optional<Party> party = partyDao.findById(e.getPartyId());
						Optional<Quality> qualityName=qualityDao.findById(e.getQualityEntryId());

						if(dyeingProcessMast==null)
							continue;

						if(!qualityName.isPresent())
							continue;
						if(!party.isPresent())
							continue;
						if(e.getShadeDataList()==null || e.getShadeDataList().isEmpty()|| e.getShadeDataList().get(0).getSupplierItemId()==null)
							continue;

						getAllShadesList.add(new GetAllShade(e,party,qualityName,dyeingProcessMast));


					}
				}

			}


		}

		if(getAllShadesList.isEmpty())
			throw new Exception("no data found");
		return getAllShadesList;
	}

    public List<GetShadeByPartyAndQuality> getShadesByQualityAndPartyId(Long qualityId, Long partyId) throws Exception{
		List<GetShadeByPartyAndQuality> shadeByPartyAndQualities = shadeMastDao.findByQualityEntryIdAndPartyId(qualityId,partyId);
		if(shadeByPartyAndQualities.isEmpty())
			throw new Exception("shade data not found");

		return shadeByPartyAndQualities;

    }

	public ShadeMast getShadesByShadeAndQualityAndPartyId(Long shadeId, Long qualityEntryId, Long partyId) throws Exception{
		Optional<ShadeMast> shadeMast = shadeMastDao.findById(shadeId);
		if(shadeMast.isEmpty())
			throw new Exception("Shade not found for quality or party");

		return shadeMast.get();
	}

    public List<GetShadeByPartyAndQuality> getAllShadeByPartyAndQuality(Long partyId, Long qualityId) throws Exception{

		Optional<Quality> quality=qualityDao.findById(qualityId);
		if(quality.isEmpty())
			throw new Exception("no quality is found");

		List<GetShadeByPartyAndQuality> list=new ArrayList<>();

		List<GetShadeByPartyAndQuality> shadeByPartyAndQualities = shadeMastDao.findByQualityEntryIdAndPartyId(qualityId,partyId);
		if(shadeByPartyAndQualities.isEmpty())
			throw new Exception("shade data not found");

		return shadeByPartyAndQualities;


	}

	public GetAPC getAPCNumber() {
		
		Long number = acpDao.getAcpNumber();
		if(number==null)
		{
			APC acp =new APC(1l);
			APC x = acpDao.save(acp);
			GetAPC getAcp = new GetAPC(x);
			return getAcp;

		}
		else
		{
			GetAPC x=new GetAPC(++number);
			return x;
		}


	}

	public Boolean isAPCExist(String number) {

		Long data = Long.parseLong(number.substring(3));

		APC flag = acpDao.getAcpNumberExist(data);
		if(flag==null)
			return true;
		else
			return false;
	}

	public List<GetAllPendingShade> getAllPendingShade() {
		List<GetAllPendingShade> dataList=new ArrayList<>();
		List<ShadeMast> list = shadeMastDao.getAllPendingShadeMast();
		for(ShadeMast s:list)
		{
			Party party =partyDao.findByPartyId(s.getPartyId());
			Optional<Quality> quality =qualityDao.findById(s.getQualityEntryId());
			if(s.getShadeDataList()==null || s.getShadeDataList().isEmpty() || s.getShadeDataList().get(0).getSupplierItemId()==null) {
				s.setPending(true);
				dataList.add(new GetAllPendingShade(s,party,quality.get()));
			}
		}

		return dataList;
	}

	public List<ShadeMast> getAllShadeMastByProcessId(Long id) throws Exception {
		List<ShadeMast> list = shadeMastDao.getAllShadeByProcessId(id);
		if(list.isEmpty())
			throw new Exception("no shade found for process");
		return list;
	}

	public void saveAllShade(List<ShadeMast> getAllShade) {
		shadeMastDao.saveAll(getAllShade);
	}

	public void saveShadeData(ShadeMast s)
	{
		shadeMastDao.save(s);
	}

	public void updateShadeProcessId(Long shadeId, Long processId) {
		shadeMastDao.updateProcessId(shadeId,processId);
	}

    public ShadeMast getColorToneByProductionId(Long productionId) {

		String colorTone="";

		ShadeMast shadeMast = shadeMastDao.getShadeColorToneByProductionId(productionId);

		return shadeMast;
    }


    public List<ShadeMast> getShadeByPartyId(Long id) {
		List<ShadeMast> shadeMastList = shadeMastDao.getAllShadeByPartyId(id);
		return shadeMastList;

    }

	public List<ShadeMast> getAllShadeByQualityId(Long id) {
		List<ShadeMast> list = shadeMastDao.getAllShadeByQualityId(id);
		return list;
	}

	public List<ShadeData> getShadDataByItemId(Long e) {
		return shadeDataDao.getShadeDataByItemid(e);
	}

	public List<ShadeMast> getAllShadeByProcessId(Long id) {
		return shadeMastDao.getAllShadeByProcessId(id);
	}
}
