package com.main.glory.model.paymentTerm.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
public class GetAllBank {
    String name;

    public GetAllBank(String name) {
        this.name = name;
    }
}
