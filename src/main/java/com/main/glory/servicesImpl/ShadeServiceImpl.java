package com.main.glory.servicesImpl;

import com.main.glory.Dao.PartyDao;
import com.main.glory.Dao.QualityDao;
import com.main.glory.Dao.ShadeDataDao;
import com.main.glory.Dao.ShadeMastDao;
import com.main.glory.model.Quality;
import com.main.glory.model.shade.ShadeData;
import com.main.glory.model.shade.ShadeMast;
import com.main.glory.model.shade.requestmodals.UpdateShadeMastRequest;
import com.main.glory.model.shade.responsemodals.ShadeMastWithDetails;
import com.main.glory.services.ShadeServicesInterface;
import io.swagger.annotations.ApiModelProperty;
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
	ShadeMastDao shadeMastDao;

	@Autowired
	ShadeDataDao shadeDataDao;

	@Autowired
	QualityDao qualityDao;

	@Autowired
	PartyDao partyDao;

	@Transactional
	public void saveShade(ShadeMast shadeMast){
		Date dt = new Date(System.currentTimeMillis());
		shadeMast.setCreatedDate(dt);
		shadeMast.setIsActive(true);
		ShadeMast x = shadeMastDao.save(shadeMast);
		shadeMast.getShadeDataList().forEach(e -> {
			e.setIsActive(true);
			e.setState("original");
			e.setControlId(x.getId());
			e.setCreatedDate(dt);
		});
		shadeDataDao.saveAll(shadeMast.getShadeDataList());
	}

	@Override
	public List<ShadeMast> getAllActiveData() {
		List<ShadeMast> data = shadeMastDao.findAll();
		data.forEach(e -> {
			e.setShadeDataList(shadeDataDao.findByIsActiveAndControlId(true, e.getId()));
		});
		return data;
	}

	@Override
	public List<ShadeMast> getAllOriginalData() {
		List<ShadeMast> data = shadeMastDao.findAll();
		data.forEach(e -> {
			e.setShadeDataList(shadeDataDao.findByStateAndControlId("original",e.getId()));
		});
		return data;
	}

	@Override
	public List<ShadeMastWithDetails> getShadeMastList() {
		List<ShadeMast> shadeMast = shadeMastDao.findByIsActive(true);
		List<ShadeMastWithDetails> data = new ArrayList<>();
		System.out.println(shadeMast);
		shadeMast.forEach(e -> {
			try {
				ShadeMastWithDetails t = new ShadeMastWithDetails(e);
				t.setPartyName(partyDao.getPartyNameByPartyId(e.getPartyId()));

				Optional<Quality> q = qualityDao.findById(e.getQualityId());
				t.setQualityType(q.get().getQualityType());
				t.setQualityName(q.get().getQualityName());
				System.out.println(t);
				data.add(t);
			} catch (Exception x) {
				System.out.println(x.getMessage());
			}
		});
		return data;
	}

	@Override
	public ShadeMast getActiveShadeData(Long id) {
		ShadeMast data = shadeMastDao.findByIdAndIsActive(id, true);
		if(data != null)
			data.setShadeDataList(shadeDataDao.findByIsActiveAndControlId(true, id));
		return data;
	}

	@Override
	public ShadeMast getOriginalShadeData(Long id) {
		ShadeMast data = shadeMastDao.findByIdAndIsActive(id, true);
		if(data != null)
			data.setShadeDataList(shadeDataDao.findByStateAndControlId("original", id));
		return data;
	}

	@Override
	public void deleteData(Long aLong) {
		shadeMastDao.setInactiveById(aLong);
		shadeDataDao.setInactiveByControlId(aLong);
	}

	@Override
	public void updateShadeMast(UpdateShadeMastRequest updateShadeMastRequest) {
//		shadeMastDao.setInactiveById(updateShadeMastRequest.getId());
		ShadeMast temp = shadeMastDao.findById(updateShadeMastRequest.getId()).get();
		ShadeMast updated = updateShadeMastRequest.getShadeMast();
		if(temp != null) {
			temp.setUpdatedDate(new Date(System.currentTimeMillis()));
			temp.setPartyShadeNo(updated.getPartyShadeNo());
			temp.setProcessId(updated.getProcessId());
			temp.setQualityId(updated.getQualityId());
			temp.setPartyId(updated.getPartyId());
			temp.setColorTone(updated.getColorTone());
			temp.setUpdatedBy(updated.getUpdatedBy());
			temp.setUserHeadId(updated.getUserHeadId());
			temp.setCuttingId(updated.getCuttingId());
			temp.setRemark(updated.getRemark());
			temp.setCategory(updated.getCategory());
			temp.setLabColorNo(updated.getLabColorNo());
			shadeMastDao.save(temp);
		}

	}

	@Override
	public void updateShadeData(List<ShadeData> shadeDataList) {

	}
}
