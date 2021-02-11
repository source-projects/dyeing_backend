package com.main.glory.model.shade.requestmodals;

import com.main.glory.model.shade.APC;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetAPC {
    String number;

    public GetAPC(APC x)
    {
        this.number = x.getPreFix()+x.getPostFix();
    }

    public GetAPC(Long number) {
        this.number = "ACP"+(number);
    }
}
