package com.main.glory.model.dispatch.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetPChallanByPartyAndRF {
    Long partyId;
    Boolean rfInvoiceFlag;
}
