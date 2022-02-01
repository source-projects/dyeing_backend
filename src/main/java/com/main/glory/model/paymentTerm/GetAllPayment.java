package com.main.glory.model.paymentTerm;

import com.main.glory.model.paymentTerm.PaymentMast;
import com.main.glory.model.paymentTerm.request.AddPaymentMast;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetAllPayment extends PaymentMast {
    String partyName;

    public GetAllPayment(PaymentMast paymentMast, String partyName) {
        super(paymentMast);
        this.partyName = partyName;
    }

    public GetAllPayment(PaymentMast e) {
        super(e);
        this.partyName = e.getParty()!=null?null:e.getParty().getPartyName();
    }
}
