package com.main.glory.model.qualityProcess.request;

import com.main.glory.model.qualityProcess.QualityProcessData;
import com.main.glory.model.qualityProcess.QualityProcessMast;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateRequestQualityProcess {
    Long id;
    String processName;
    Long time;
    Long updatedBy;
    List<QualityProcessData> steps;
}
