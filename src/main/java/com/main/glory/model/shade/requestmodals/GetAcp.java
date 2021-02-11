package com.main.glory.model.shade.requestmodals;

import com.main.glory.model.shade.ACP;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetAcp {
    String number;

    public GetAcp(ACP x)
    {
        this.number = x.getPreFix()+x.getPostFix();
    }

    public GetAcp(Long number) {
        this.number = "ACP"+(number);
    }
}
