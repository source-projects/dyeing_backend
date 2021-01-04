package com.main.glory.model.dispatch.response;

import com.main.glory.model.dispatch.request.InvoiceWithBatch;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InvoiceListBasedOnFilter {
    Long partyId;
    String partyName;
    List<InvoiceWithBatch> invoiceWithBatchList;
}
