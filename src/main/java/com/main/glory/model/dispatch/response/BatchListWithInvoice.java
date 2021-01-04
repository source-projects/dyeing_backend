package com.main.glory.model.dispatch.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BatchListWithInvoice {

    @JsonIgnore
    Long batchEntryId;
    String batchId;
    Long stockId;
    String invoicNo;
    Date toDate;

}
