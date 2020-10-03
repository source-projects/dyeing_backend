package com.main.glory.servicesImpl;

import com.main.glory.Dao.color.ColorBoxDao;
import com.main.glory.Dao.color.ColorDataDao;
import com.main.glory.Dao.color.ColorMastDao;
import com.main.glory.Dao.SupplierDao;
import com.main.glory.model.color.ColorBox;
import com.main.glory.model.color.ColorMast;
import com.main.glory.model.color.responsemodals.ColorMastDetails;
import com.main.glory.services.ColorServicesInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class ColorServiceImpl implements ColorServicesInterface {

	@Autowired
	ColorMastDao colorMastDao;

	@Autowired
	ColorDataDao colorDataDao;

	@Autowired
	ColorBoxDao colorBoxDao;

	@Autowired
	SupplierDao supplierDao;

	@Override
	@Transactional
	public void addColor(ColorMast colorMast) {
			ColorMast colorMast1 = colorMastDao.save(colorMast);
			List<ColorBox> colorBoxes = new ArrayList<>();


			colorMast.getColorDataList().forEach(e -> {
				e.setPurchaseId(colorMast.getId());
				for (int i = 0; i < e.getNoOfBox(); i++) {
					ColorBox temp = new ColorBox();
					temp.setControlId(e.getId());
					temp.setIssued(false);
					colorBoxes.add(temp);
				}
			});
			colorDataDao.saveAll(colorMast.getColorDataList());
			colorBoxDao.saveAll(colorBoxes);
	}

	@Override
	public List<ColorMastDetails> getAll() {
		List<ColorMast> data = colorMastDao.findAll();
		List<ColorMastDetails> colorMastDetails = new ArrayList<>();
			data.forEach(e -> {
				try{
					ColorMastDetails x = new ColorMastDetails(e);
					x.setSupplierName(supplierDao.findById(e.getSupplierId()).get().getSupplierName());
					colorMastDetails.add(x);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			});
		return colorMastDetails;
	}

	public List<ColorMastDetails> getOne(Long id) {
		List<ColorMast> data = colorMastDao.findAll();
		List<ColorMastDetails> colorMastDetails = new ArrayList<>();
			data.forEach(e -> {
				try{
					ColorMastDetails x = new ColorMastDetails(e);
					x.setSupplierName(supplierDao.findById(e.getSupplierId()).get().getSupplierName());
					colorMastDetails.add(x);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			});
		return colorMastDetails;
	}
}
