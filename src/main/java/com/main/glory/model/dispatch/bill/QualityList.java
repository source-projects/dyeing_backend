package com.main.glory.model.dispatch.bill;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class QualityList {

    Long qualityEntryId;
    String qulityId;
    Double rate;
    String batchId;
    Double totalFinishMtr;
    Double totalMtr;
    Double amt;

}
