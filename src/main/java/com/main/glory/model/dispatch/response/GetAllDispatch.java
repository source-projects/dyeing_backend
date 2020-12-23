package com.main.glory.model.dispatch.response;

import com.main.glory.model.dispatch.DispatchMast;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter

public class GetAllDispatch {
    Long id;
    Date createdDate;
    Boolean isSendToParty;

    public GetAllDispatch(DispatchMast dispatchMast) {
        this.id=dispatchMast.getId();
        this.createdDate=dispatchMast.getCreatedDate();
        this.isSendToParty=dispatchMast.getIsSendToParty();
    }
}
