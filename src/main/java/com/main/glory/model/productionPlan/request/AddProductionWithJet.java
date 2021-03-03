package com.main.glory.model.productionPlan.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddProductionWithJet {
    String batchId;
    Long stockId;
    Long partyId;
    Long qualityEntryId;
    Long shadeId;
    Long jetId;
}
