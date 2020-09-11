package com.main.glory.servicesImpl;

import com.main.glory.Dao.ColorBoxDao;
import com.main.glory.Dao.ColorMastDao;
import com.main.glory.model.color.ColorBox;
import com.main.glory.model.color.ColorMast;
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
	ColorBoxDao colorBoxDao;

	@Override
	@Transactional
	public Boolean addColor(ColorMast colorMast) {
		try {
			ColorMast colorMast1 = colorMastDao.save(colorMast);
			List<ColorBox> colorBoxes = new ArrayList<>();
			colorMast1.getColorDataList().forEach(e -> {
				for (int i = 0; i < e.getNo_of_box(); i++) {
					ColorBox temp = new ColorBox();
					temp.setControl_id(e.getId());
					temp.setIssued(false);
					colorBoxes.add(temp);
				}
			});
			colorBoxDao.saveAll(colorBoxes);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public List<ColorMast> getAll() {
		return colorMastDao.findAll();
	}
}
