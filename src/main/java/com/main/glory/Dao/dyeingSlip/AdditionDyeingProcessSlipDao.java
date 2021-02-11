package com.main.glory.Dao.dyeingSlip;

import com.main.glory.model.dyeingSlip.AdditionDyeingProcessSlip;
import com.main.glory.model.dyeingSlip.request.AddAdditionDyeingSlipModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AdditionDyeingProcessSlipDao extends JpaRepository<AdditionDyeingProcessSlip,Long> {

    @Query("select s from AdditionDyeingProcessSlip s")
    List<AdditionDyeingProcessSlip> getAllAdditional();
}
