package com.main.glory.model.purchase.response;

import com.main.glory.model.purchase.Purchase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PurchaseResponse extends Purchase {
    String departmentName;
    String receiverName;
    String approvedName;
    public PurchaseResponse(Purchase p,String depName,String rName,String aname)
    {
        super(p);
        this.departmentName=depName;
        this.receiverName=rName;
        this.approvedName=aname;
    }
}
