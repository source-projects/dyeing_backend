package com.main.glory.model.purchase;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ResponsePurchase extends PurchaseOrder{
    String userName;

    public ResponsePurchase(PurchaseOrder p ,String userName){
        super(p);
        this.userName = userName;
    }
}
