package com.main.glory.model.color.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ItemWithLeftQty {

    Long itemId;
    Double availableQty;
}
