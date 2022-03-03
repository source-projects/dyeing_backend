package com.main.glory.model.paymentTerm.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
public class AdvancePaymentIdList {
    Long id;

    public AdvancePaymentIdList(Long id) {
        this.id = id;
    }
}
