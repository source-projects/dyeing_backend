package com.main.glory.model.jet.responce;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetJetSlip {
    Long jetId;
    Long productionId;
    String batchId;
    Double batchWt;
    String partyShadeNo;
    String colorTone;
    Long stockId;
    Long qualityEntryId;
    String qualityId;
    List<GetJetSlipData> slipDataList;
}
