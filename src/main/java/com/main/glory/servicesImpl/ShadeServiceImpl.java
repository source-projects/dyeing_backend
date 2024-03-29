package com.main.glory.servicesImpl;

import com.main.glory.Dao.APCDao;
import com.main.glory.Dao.PartyDao;
import com.main.glory.Dao.ShadeDataDao;
import com.main.glory.Dao.ShadeMastDao;
import com.main.glory.Dao.productionPlan.ProductionPlanDao;
import com.main.glory.Dao.quality.QualityDao;
import com.main.glory.Dao.quality.QualityNameDao;
import com.main.glory.Dao.user.UserDao;
import com.main.glory.filters.Filter;
import com.main.glory.filters.FilterResponse;
import com.main.glory.filters.QueryOperator;
import com.main.glory.filters.SpecificationManager;
import com.main.glory.model.ConstantFile;
import com.main.glory.model.StockDataBatchData.request.GetBYPaginatedAndFiltered;
import com.main.glory.model.dyeingProcess.DyeingProcessMast;
import com.main.glory.model.party.Party;
import com.main.glory.model.productionPlan.ProductionPlan;
import com.main.glory.model.quality.Quality;
import com.main.glory.model.quality.QualityName;
import com.main.glory.model.shade.APC;
import com.main.glory.model.shade.ShadeData;
import com.main.glory.model.shade.ShadeMast;
import com.main.glory.model.shade.requestmodals.*;
import com.main.glory.model.supplier.SupplierRate;
import com.main.glory.model.user.Permissions;
import com.main.glory.model.user.UserData;
import com.main.glory.model.user.UserPermission;
import com.main.glory.services.FilterService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;

import static java.util.Comparator.comparingLong;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

@Service("ShadeServiceImpl")
public class ShadeServiceImpl {
	QualityServiceImp qualityServiceImp;

	@Autowired
	QualityNameDao qualityNameDao;

	@Autowired
	SpecificationManager<ShadeMast> specificationManager;
	@Autowired
	FilterService<ShadeMast, ShadeMastDao> filterService;

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

	public void saveShade(AddShadeMast addShadeMast, String id) throws Exception {
		// consider we have data and add directlt
		Quality quality=qualityDao.findById(addShadeMast.getQualityEntryId()).get();
		Party party=partyDao.findById(addShadeMast.getPartyId()).get();
		UserData userHeadData = null;
		UserData createdBy = userDao.getUserById(addShadeMast.getCreatedBy());
		userHeadData = createdBy.getUserHeadId()==0 || createdBy.getIsMaster()==false?party.getUserHeadData():userDao.getUserById(createdBy.getUserHeadId());
		DyeingProcessMast dyeingProcessMast = dyeingProcessService.getDyeingProcessMastById(addShadeMast.getProcessId());

		ShadeMast shadeMast = new ShadeMast(addShadeMast, createdBy, userHeadData,createdBy,quality,party);
		shadeMast.setShadeDataList(addShadeMast.getShadeDataList());
		System.out.println("ShadeMast object created");

		if(dyeingProcessMast == null)
		throw new Exception(ConstantFile.DyeingProcess_Data_Not_Exist);

		if (shadeMast.getQuality() == null)
			throw new Exception(ConstantFile.Quality_Data_Not_Exist);

		if (shadeMast.getParty() == null)
			throw new Exception(ConstantFile.Party_Not_Exist);

		if (quality == null) {
			throw new Exception(ConstantFile.Quality_Data_Not_Found + shadeMast.getQuality().getId());
		}
		System.out.println("Quality object created");

		// check the dyeing process for the shade is available or not

		//DyeingProcessMast processMastExist = dyeingProcessService.getDyeingProcessById(shadeMast.getProcessId());

		System.out.println("DyeingProcessMast object created");
		// ShadeMast shadeData = new ShadeMast(shadeMast);

		// shadeMast.setQualityEntryId(quality.get().getId());

		if (shadeMast.getShadeDataList() == null || shadeMast.getShadeDataList().isEmpty()) {
			shadeMast.setShadeDataList(null);
			shadeMast.setPending(true);
			if (shadeMast.getCategory() == null)
				throw new Exception("Select shade category");
		} else {

			shadeMast.setPending(false);
			shadeMast.setShadeDataList(shadeMast.getShadeDataList());
			shadeMast = setShadeCatogory(shadeMast);
		}

		// check that the shade add by data entry or what
		UserData userData = userDao.getUserById(Long.parseLong(id));
		if (userData.getIsMaster() == false) {
			// get the party record
			if (party == null)
				throw new Exception(ConstantFile.Party_Not_Exist);

			shadeMast.setUserHeadData(party.getUserHeadData());
		}

		System.out.println("UserData object created");

		// check that shade is exist or not
		ShadeExistWithPartyShadeAndQualityId record = new ShadeExistWithPartyShadeAndQualityId(
				shadeMast.getId() == null ? 0 : shadeMast.getId(), shadeMast.getPartyShadeNo(),
				shadeMast.getQuality().getId());
		ShadeMast shadeMastExist = getShadeExistWithPartyShadeNoAndQualityEntryId(record);

		System.out.println("ShadeExistWithPartyShadeAndQualityId object created");

		if (shadeMastExist != null)
			throw new Exception(ConstantFile.Shade_Exist_Quality_And_PartyShade);

		shadeMastDao.save(shadeMast);

	}

	private ShadeMast setShadeCatogory(ShadeMast shadeData) {
		// check the total concentration
		Double totalConcentration = 0.0;
		Boolean specialFlag = false;

		for (ShadeData shadeItem : shadeData.getShadeDataList()) {
			totalConcentration += shadeItem.getConcentration() == null ? 0 : shadeItem.getConcentration();
			Optional<SupplierRate> supplierRate = supplierService.getItemById(shadeItem.getSupplierItemId());
			if (supplierRate.get().getItemName().equalsIgnoreCase("Blue SR")
					|| supplierRate.get().getItemName().equalsIgnoreCase("Blue GRL")) {
				specialFlag = true;
			}

		}

		if (specialFlag == true) {
			shadeData.setCategory("SPECIAL");
		} else {
			if (totalConcentration >= 0 && totalConcentration <= 1)
				shadeData.setCategory("LIGHT");
			else if (totalConcentration > 1 && totalConcentration <= 2.5)
				shadeData.setCategory("MEDIUM");
			else if (totalConcentration > 2.5)
				shadeData.setCategory("DARK");
		}
		System.out.println(totalConcentration + "-" + shadeData.getCategory());
		return shadeData;
	}

	public List<ShadeMast> getAllShadeMast() throws Exception {
		List<ShadeMast> shadeMastList = shadeMastDao.getAllShadeMast();
		if (shadeMastList.isEmpty())
			throw new Exception(ConstantFile.Shade_Not_Found);
		else {
			return shadeMastList;
		}
	}

	public Optional<ShadeMast> getShadeMastById(Long id) throws Exception {
		Optional<ShadeMast> shadeMastList = shadeMastDao.findById(id);

		/*
		 * if(shadeMastList.isPresent() && shadeMastList.get().getPartyId()!=null &&
		 * shadeMastList.get().getQualityEntryId()!=null) return shadeMastList; else{
		 * throw new Exception("shade data not found"); }
		 */
		return shadeMastList;
	}

	public Boolean updateShade(AddShadeMast addShadeMast) throws Exception {
		Boolean setCategory = false;
		UserData userHeadData =userDao.getUserById(addShadeMast.getUserHeadId());
		UserData createdBy = userDao.getUserById(addShadeMast.getCreatedBy());
		UserData updatedBy = userDao.getUserById(addShadeMast.getUpdatedBy());
		Quality quality=qualityDao.findById(addShadeMast.getQualityEntryId()).get();
		Party party=partyDao.findById(addShadeMast.getPartyId()).get();


		ShadeMast shadeMast = new ShadeMast(addShadeMast, createdBy, userHeadData,updatedBy,quality,party);
		shadeMast.setShadeDataList(addShadeMast.getShadeDataList());

		if (shadeMast.getQuality() == null)
			throw new Exception(ConstantFile.Quality_Data_Not_Exist);

		if (shadeMast.getParty() == null)
			throw new Exception(ConstantFile.Party_Not_Exist);

		if (shadeMast.getShadeDataList() == null || shadeMast.getShadeDataList().isEmpty()) {
			shadeMast.setPending(true);
			setCategory = false;
			if (shadeMast.getCategory() == null)
				throw new Exception("Select shade category");
		} else {
			shadeMast.setPending(false);
			setCategory = true;
		}

		ShadeMast shadeIndex = shadeMastDao.getShadeMastById(shadeMast.getId());
		if (shadeIndex == null)
			return false;
		else {

			// System.out.println(shadeMast);
			List<ProductionPlan> productionPlansList = productionPlanService.getProductionByShadeId(shadeMast.getId());
			if(setCategory==true)
			shadeMast = setShadeCatogory(shadeMast);

			// check that shade is exist or not
			ShadeExistWithPartyShadeAndQualityId record = new ShadeExistWithPartyShadeAndQualityId(
					shadeMast.getId() == null ? 0 : shadeMast.getId(), shadeMast.getPartyShadeNo(),
					shadeMast.getQuality().getId());
			ShadeMast shadeMastExist = getShadeExistWithPartyShadeNoAndQualityEntryId(record);

			if (shadeMastExist != null)
				throw new Exception(ConstantFile.Shade_Exist_Quality_And_PartyShade);
			ShadeMast x = shadeMastDao.save(shadeMast);
			/*
			 * for(ProductionPlan p :productionPlansList) {
			 * productionPlanDao.updateProductionWithShadeId(p.getId(),x.getId()); }
			 */
			// shadeDataDao.saveAll(shadeMast.getShadeDataList());

		}
		return true;
	}



	public boolean deleteShadeById(Long id) throws Exception{
		Optional<ShadeMast> shadeMast = shadeMastDao.findById(id);
		// check if this is present in the database
		if (shadeMast.isEmpty()) {
			throw new Exception(ConstantFile.Shade_Not_Exist + id);
		}
		// check the production is available or not
		List<ProductionPlan> productionPlans = productionPlanDao.getAllProductionByShadeId(id);
		if (!productionPlans.isEmpty())
			throw new Exception(ConstantFile.Production_Record_Exist);
		shadeMastDao.deleteById(id);

		return true;
	}

	public List<GetAllShade> getAllShadesInfo(String getBy, Long id) throws Exception {
		List<ShadeMast> shadeMastList = null;
		List<GetAllShade> getAllShadesList = new ArrayList<>();
		if (id == null) {
			shadeMastList = shadeMastDao.getAllShadeMast();
			for (ShadeMast e : shadeMastList) {

				if (e.getParty() != null && e.getQuality().getId() != null) {
					DyeingProcessMast dyeingProcessMast = dyeingProcessService.getDyeingProcessById(e.getProcessId());

					Party party = e.getParty();
					Optional<Quality> qualityName = qualityDao.findById(e.getQuality().getId());

					if (dyeingProcessMast == null)
						continue;

					if (!qualityName.isPresent())
						continue;
					if (party==null)
						continue;

					if (e.getShadeDataList() == null || e.getShadeDataList().isEmpty()
							|| e.getShadeDataList().get(0).getSupplierItemId() == null)
						continue;
					QualityName qualityName1 = qualityName.get().getQualityName();

					if (qualityName1==null)
						continue;

					getAllShadesList.add(new GetAllShade(e, party, qualityName, dyeingProcessMast, qualityName1));

				}

			}
		} else if (getBy.equals("own")) {
			shadeMastList = shadeMastDao.findAllByCreatedBy(id);
			for (ShadeMast e : shadeMastList) {

				if (e.getParty() != null && e.getQuality().getId() != null) {
					DyeingProcessMast dyeingProcessMast = dyeingProcessService.getDyeingProcessById(e.getProcessId());

					Party party = e.getParty();
					Optional<Quality> qualityName = qualityDao.findById(e.getQuality().getId());

					if (dyeingProcessMast == null)
						continue;

					if (!qualityName.isPresent())
						continue;
					if (party==null)
						continue;
					if (e.getShadeDataList() == null || e.getShadeDataList().isEmpty()
							|| e.getShadeDataList().get(0).getSupplierItemId() == null)
						continue;

					QualityName qualityName1 = qualityName.get().getQualityName();

					if (qualityName1==null)
						continue;

					getAllShadesList.add(new GetAllShade(e, party, qualityName, dyeingProcessMast, qualityName1));

				}
			}
		} else if (getBy.equals("group")) {
			UserData userData = userDao.findUserById(id);

			if (userData.getUserHeadId().equals(userData.getId())) {
				// master user
				shadeMastList = shadeMastDao.findAllByCreatedByAndHeadId(id, id);
				for (ShadeMast e : shadeMastList) {

					if (e.getParty() != null && e.getQuality().getId() != null) {
						DyeingProcessMast dyeingProcessMast = dyeingProcessService
								.getDyeingProcessById(e.getProcessId());

						Party party = e.getParty();
						Optional<Quality> qualityName = qualityDao.findById(e.getQuality().getId());

						if (dyeingProcessMast == null)
							continue;

						if (!qualityName.isPresent())
							continue;
						if (party==null)
							continue;

						if (e.getShadeDataList() == null || e.getShadeDataList().isEmpty()
								|| e.getShadeDataList().get(0).getSupplierItemId() == null)
							continue;

						QualityName qualityName1 = qualityName.get().getQualityName();

						if (qualityName1==null)
							continue;

						getAllShadesList
								.add(new GetAllShade(e, party, qualityName, dyeingProcessMast, qualityName1));

					}
				}
			} else {
				UserData userOperator = userDao.getUserById(id);
				shadeMastList = shadeMastDao.findAllByCreatedByAndHeadId(userOperator.getUserHeadId(),
						userOperator.getUserHeadId());
				for (ShadeMast e : shadeMastList) {

					if (e.getParty() != null && e.getQuality().getId() != null) {
						DyeingProcessMast dyeingProcessMast = dyeingProcessService
								.getDyeingProcessById(e.getProcessId());

						Party party = e.getParty();
						Optional<Quality> qualityName = qualityDao.findById(e.getQuality().getId());

						if (dyeingProcessMast == null)
							continue;

						if (!qualityName.isPresent())
							continue;
						if (party==null)
							continue;
						if (e.getShadeDataList() == null || e.getShadeDataList().isEmpty()
								|| e.getShadeDataList().get(0).getSupplierItemId() == null)
							continue;
						QualityName qualityName1 =qualityName.get().getQualityName();

						if (qualityName1==null)
							continue;

						getAllShadesList
								.add(new GetAllShade(e, party, qualityName, dyeingProcessMast, qualityName1));

					}
				}

			}

		}

		/*
		 * if(getAllShadesList.isEmpty()) throw new Exception("no data found");
		 */
		return getAllShadesList;
	}

	public FilterResponse<GetAllShade> getAllShadesInfoPaginated(GetBYPaginatedAndFiltered requestParam, String id)
			throws Exception {
		List<ShadeMast> shadeMastList = null;
		String getBy = requestParam.getGetBy();
		Pageable pageable = filterService.getPageable(requestParam.getData());
		List<Filter> filtersParam = requestParam.getData().getParameters();
		HashMap<String, List<String>> subModelCase = new HashMap<String, List<String>>();
		subModelCase.put("userHeadId", new ArrayList<String>(Arrays.asList("userHeadData", "id")));
		subModelCase.put("createdBy", new ArrayList<String>(Arrays.asList("createdBy", "id")));
		subModelCase.put("userHeadName", new ArrayList<String>(Arrays.asList("userHeadData", "userName")));
		subModelCase.put("createdByName", new ArrayList<String>(Arrays.asList("createdBy", "userName")));
		subModelCase.put("qualityId", new ArrayList<String>(Arrays.asList("quality", "qualityId")));
		subModelCase.put("partyName", new ArrayList<String>(Arrays.asList("party", "partyName")));
		Page queryResponse = null;
		Specification<ShadeMast> filterSpec = specificationManager.getSpecificationFromFilters(filtersParam,
				requestParam.getData().isAnd, subModelCase);

		List<GetAllShade> getAllShadesList = new ArrayList<>();
		if (id == null || getBy.equals("all")) {
			queryResponse = shadeMastDao.findAll(filterSpec, pageable);
		} else if (getBy.equals("own")) {
			UserData userData = userDao.findUserById(Long.parseLong(id));

			if (userData.getUserHeadId().equals(0)) {
				queryResponse = shadeMastDao.findAll(filterSpec, pageable);
			} else {
				List<Filter> filters = new ArrayList<Filter>();
				filters.add(new Filter(new ArrayList<String>(Arrays.asList("createdBy")), QueryOperator.EQUALS, id));
				Specification<ShadeMast> spec = specificationManager.getSpecificationFromFilters(filters, true,
						subModelCase);
				spec = spec.and(filterSpec);
				queryResponse = shadeMastDao.findAll(spec, pageable);
			}

		} else if (getBy.equals("group")) {

			UserData userData = userDao.findUserById(Long.parseLong(id));
			if (userData.getUserHeadId().equals(0)) {
				queryResponse = shadeMastDao.findAll(filterSpec, pageable);

			} else if (userData.getUserHeadId().equals(userData.getId())) {
				// master user
				List<Filter> filters = new ArrayList<Filter>();
				filters.add(new Filter(new ArrayList<String>(Arrays.asList("createdBy")), QueryOperator.EQUALS, id));
				filters.add(new Filter(new ArrayList<String>(Arrays.asList("userHeadId")), QueryOperator.EQUALS, id));
				Specification<ShadeMast> spec = specificationManager.getSpecificationFromFilters(filters, false,
						subModelCase);
				spec = spec.and(filterSpec);
				queryResponse = shadeMastDao.findAll(spec, pageable);

			} else {
				List<Filter> filters = new ArrayList<Filter>();

				UserData userOperator = userDao.getUserById(Long.parseLong(id));
				filters.add(new Filter(new ArrayList<String>(Arrays.asList("createdBy")), QueryOperator.EQUALS,
						Long.toString(userOperator.getUserHeadId())));
				Specification<ShadeMast> spec = specificationManager.getSpecificationFromFilters(filters, true,
						subModelCase);
				spec = spec.and(filterSpec);
				queryResponse = shadeMastDao.findAll(spec, pageable);

			}

		}
		shadeMastList = queryResponse.getContent();

		for (ShadeMast e : shadeMastList) {

			if (e.getParty() != null && e.getQuality().getId() != null) {
				DyeingProcessMast dyeingProcessMast = dyeingProcessService.getDyeingProcessById(e.getProcessId());

				Party party = e.getParty();
				Optional<Quality> qualityName = qualityDao.findById(e.getQuality().getId());

				if (dyeingProcessMast == null)
					continue;

				if (!qualityName.isPresent())
					continue;
				if (party==null)
					continue;

				if (e.getShadeDataList() == null || e.getShadeDataList().isEmpty()
						|| e.getShadeDataList().get(0).getSupplierItemId() == null)
					continue;
				QualityName qualityName1 = qualityName.get().getQualityName();

				if (qualityName1==null)
					continue;

				getAllShadesList.add(new GetAllShade(e, party, qualityName, dyeingProcessMast, qualityName1));

			}

		}

		FilterResponse<GetAllShade> response = new FilterResponse<GetAllShade>(getAllShadesList,
				queryResponse.getNumber(), queryResponse.getNumberOfElements(), (int) queryResponse.getTotalElements());

		return response;

	}

	public List<GetShadeByPartyAndQuality> getShadesByQualityAndPartyId(String qualityId, String partyId, String id)
			throws Exception {

		// get the user record first
		Long userId = Long.parseLong(id);

		UserData userData = userDao.getUserById(userId);
		Long userHeadId = null;

		UserPermission userPermission = userData.getUserPermissionData();
		Permissions permissions = new Permissions(userPermission.getSh().intValue());

		List<GetShadeByPartyAndQuality> list = new ArrayList<>();
		List<GetShadeByPartyAndQuality> shadeByPartyAndQualities = new ArrayList<>();

		if (!partyId.contains(",")) {
			// filter the record
			if (permissions.getViewAll()) {
				userId = null;
				userHeadId = null;
				shadeByPartyAndQualities = shadeMastDao.findByQualityEntryIdAndPartyId(Long.parseLong(qualityId),
						Long.parseLong(partyId));
			} else if (permissions.getViewGroup()) {
				// check the user is master or not ?
				// admin
				if (userData.getUserHeadId() == 0) {
					userId = null;
					userHeadId = null;
					shadeByPartyAndQualities = shadeMastDao.findByQualityEntryIdAndPartyId(Long.parseLong(qualityId),
							Long.parseLong(partyId));
				} else if (userData.getUserHeadId() > 0) {
					// check weather master or operator
					UserData userHead = userDao.getUserById(userData.getUserHeadId());
					userId = userData.getId();
					userHeadId = userHead.getId();
					shadeByPartyAndQualities = shadeMastDao.findByQualityEntryIdAndPartyId(Long.parseLong(qualityId),
							Long.parseLong(partyId), userId, userHeadId);

				}
			} else if (permissions.getView()) {
				userId = userData.getId();
				userHeadId = null;
				shadeByPartyAndQualities = shadeMastDao.findByQualityEntryIdAndPartyId(Long.parseLong(qualityId),
						Long.parseLong(partyId), userId, userHeadId);
			}
		} else {
			// perform split operation
			String[] parties = partyId.split(",");
			String[] qualities = qualityId.split(",");
			for (int i = 0; i < parties.length; i++) {
				List<GetShadeByPartyAndQuality> record = new ArrayList<>();

				if (permissions.getViewAll()) {
					userId = null;
					userHeadId = null;
					record = shadeMastDao.findByQualityEntryIdAndPartyId(Long.parseLong(qualities[i]),
							Long.parseLong(parties[i]));
				} else if (permissions.getViewGroup()) {
					// check the user is master or not ?
					// admin
					if (userData.getUserHeadId() == 0) {
						userId = null;
						userHeadId = null;
						record = shadeMastDao.findByQualityEntryIdAndPartyId(Long.parseLong(qualities[i]),
								Long.parseLong(parties[i]));
					} else if (userData.getUserHeadId() > 0) {
						// check weather master or operator
						UserData userHead = userDao.getUserById(userData.getUserHeadId());
						userId = userData.getId();
						userHeadId = userHead.getId();
						record = shadeMastDao.findByQualityEntryIdAndPartyId(Long.parseLong(qualities[i]),
								Long.parseLong(parties[i]), userId, userHeadId);

					}
				} else if (permissions.getView()) {
					userId = userData.getId();
					userHeadId = null;
					record = shadeMastDao.findByQualityEntryIdAndPartyId(Long.parseLong(qualities[i]),
							Long.parseLong(parties[i]), userId, userHeadId);
				}
				if (!record.isEmpty())
					shadeByPartyAndQualities.addAll(record);
			}

		}

		for (GetShadeByPartyAndQuality getShadeByPartyAndQuality : shadeByPartyAndQualities) {
			List<ShadeData> shadeData = shadeDataDao.findByControlId(getShadeByPartyAndQuality.getId());// shade id
			if (shadeData == null || shadeData.isEmpty() || shadeData.get(0).getSupplierItemId() == null)
				continue;

			list.add(getShadeByPartyAndQuality);
		}
		/*
		 * if(list.isEmpty()) throw new Exception("shade data not found");
		 */

		//remove duplicate id record
		list = list.stream()
				.collect(collectingAndThen(toCollection(() -> new TreeSet<>(comparingLong(GetShadeByPartyAndQuality::getId))),
						ArrayList::new));
		return list;

	}

	public ShadeMast getShadesByShadeAndQualityAndPartyId(Long shadeId, Long qualityEntryId, Long partyId)
			throws Exception {
		Optional<ShadeMast> shadeMast = shadeMastDao.findById(shadeId);
		if (shadeMast.isEmpty())
			throw new Exception("Shade not found for quality or party");

		return shadeMast.get();
	}

	public List<GetShadeByPartyAndQuality> getAllShadeByPartyAndQuality(Long partyId, Long qualityId) throws Exception {

		Optional<Quality> quality = qualityDao.findById(qualityId);
		if (quality.isEmpty())
			throw new Exception(ConstantFile.Quality_Data_Not_Found);

		List<GetShadeByPartyAndQuality> list = new ArrayList<>();

		List<GetShadeByPartyAndQuality> shadeByPartyAndQualities = shadeMastDao
				.findByQualityEntryIdAndPartyId(qualityId, partyId);
		if (shadeByPartyAndQualities.isEmpty())
			throw new Exception(ConstantFile.Shade_Not_Found);

		return shadeByPartyAndQualities;

	}

	public GetAPC getAPCNumber() {

		Long number = acpDao.getAcpNumber();
		if (number == null) {
			APC acp = new APC(1l);
			APC x = acpDao.save(acp);
			GetAPC getAcp = new GetAPC(x);
			return getAcp;

		} else {
			GetAPC x = new GetAPC(++number);
			return x;
		}

	}

	public Boolean isAPCExist(String number) {

		Long data = Long.parseLong(number.substring(3));

		APC flag = acpDao.getAcpNumberExist(data);
		if (flag == null)
			return true;
		else
			return false;
	}

	public List<GetAllPendingShade> getAllPendingShade() throws Exception {
		List<GetAllPendingShade> dataList = new ArrayList<>();
		List<ShadeMast> list = shadeMastDao.getAllPendingShadeMast();
		for (ShadeMast s : list) {
			Party party = s.getParty();
			Optional<Quality> quality = qualityDao.findById(s.getQuality().getId());
			DyeingProcessMast dyeingProcessMast = dyeingProcessService.getDyeingProcessById(s.getProcessId());
			if (s.getShadeDataList() == null || s.getShadeDataList().isEmpty()
					|| s.getShadeDataList().get(0).getSupplierItemId() == null) {
				s.setPending(true);
				s.setProcessName(dyeingProcessMast.getProcessName());
				dataList.add(new GetAllPendingShade(s, party, quality.get()));
			}
		}

		return dataList;
	}

	public List<ShadeMast> getAllShadeMastByProcessId(Long id) throws Exception {
		List<ShadeMast> list = shadeMastDao.getAllShadeByProcessId(id);
		if (list.isEmpty())
			throw new Exception("no shade found for process");
		return list;
	}

	public void saveAllShade(List<ShadeMast> getAllShade) {
		shadeMastDao.saveAll(getAllShade);
	}

	public void saveShadeData(ShadeMast s) {
		shadeMastDao.save(s);
	}

	public void updateShadeProcessId(Long shadeId, Long processId) {
		shadeMastDao.updateProcessId(shadeId, processId);
	}

	public ShadeMast getColorToneByProductionId(Long productionId) {

		String colorTone = "";

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

	public List<ShadeMast> getAllShadeMastByProcessIdForDeleteProcess(Long id) {
		List<ShadeMast> list = shadeMastDao.getAllShadeByProcessId(id);
		return list;
	}

	public ShadeMast getShadeById(Long shadeId) {
		return shadeMastDao.getShadeMastById(shadeId);
	}

	public List<GetShadeByPartyAndQuality> getShadeByPartyAndWithAndWithoutQuality(Long partyId, Long qualityId)
			throws Exception {

		Party partyExist = partyDao.findByPartyId(partyId);
		if (partyExist == null)
			throw new Exception(ConstantFile.Party_Not_Exist);
		List<GetShadeByPartyAndQuality> shadeByPartyAndQualities = null;
		List<GetShadeByPartyAndQuality> list = new ArrayList<>();
		if (qualityId == null) {
			List<Quality> qualityList = qualityDao.getQualityListByPartyIdId(partyId);
			for (Quality quality : qualityList) {
				if (quality == null)
					continue;
				shadeByPartyAndQualities = shadeMastDao.findByQualityEntryIdAndPartyId(quality.getId(), partyId);
				if (shadeByPartyAndQualities.isEmpty())
					continue;
				list.addAll(shadeByPartyAndQualities);
			}

		} else {
			Optional<Quality> qualityExistWithParty = qualityDao.getQualityByEntryIdAndPartyId(qualityId, partyId);
			if (qualityExistWithParty.isEmpty())
				throw new Exception("quality not found");
			list = shadeMastDao.findByQualityEntryIdAndPartyId(qualityId, partyId);

		}

		return list;
	}

	public List<ShadeMast> getShadeByCreatedByAndUserHeadId(Long id) {
		return shadeMastDao.getAllShadeByCreatedByAndUserHeadId(id, id);
	}

	public ShadeMast getShadeExistWithPartyShadeNoAndQualityEntryId(ShadeExistWithPartyShadeAndQualityId record) {

		if (record.getShadeId() == null || record.getShadeId().equals(0))
			return shadeMastDao.getShadeByPartyShadeNoAndQualityEntryIdWithExceptShadeId(record.getQualityEntryId(),
					record.getPartyShadeNo(), 0l);
		else
			return shadeMastDao.getShadeByPartyShadeNoAndQualityEntryIdWithExceptShadeId(record.getQualityEntryId(),
					record.getPartyShadeNo(), record.getShadeId());
	}

	public AddShadeMast getById(Long id) {
		Optional<ShadeMast> shadeMast = shadeMastDao.findById(id);
		AddShadeMast addShadeMast = null;
		if(shadeMast.isPresent())
		{

			addShadeMast = new AddShadeMast(shadeMast.get());
		}
		return addShadeMast;

	}

	public ShadeMast getShadeExistWithFactoryShadeNo(Long shadeId,String factoryShadeNo) {
		return shadeMastDao.getShadeMastByFactoryShadeNoExceptShadeId(shadeId,factoryShadeNo);
	}

	public GetShadeByPartyAndQuality getShadeByFactoryShadeNo(String factoryShadeNo) {
		GetShadeByPartyAndQuality getShadeByPartyAndQuality=null;

		getShadeByPartyAndQuality = shadeMastDao.getShadeDetailForProductionPlanByFactoryShadeNo(factoryShadeNo);

		return  getShadeByPartyAndQuality;
	}
}
