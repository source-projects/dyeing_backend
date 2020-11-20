package com.main.glory.model.shade.requestmodals;

import com.main.glory.model.quality.Quality;
import com.main.glory.model.shade.ShadeData;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddShadeMast {

    String partyShadeNo;
    Long processId;
    Long qualityId;
    Long partyId;
    String colorTone;
    String createdBy;
    String updatedBy;
    Long userHeadId;
    Long cuttingId;
    String remark;
    String category;
    String labColorNo;
    String processName;

    List<ShadeData> shadeDataList;

}
