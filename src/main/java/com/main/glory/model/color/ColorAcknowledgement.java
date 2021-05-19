package com.main.glory.model.color;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class ColorAcknowledgement {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    Long itemId;
    Long createdBy;
    Long updatedBy;
    Date createdDate;
    Date updatedDate;
    Double existingQty;
    Double visibleQty;
    Boolean issue;//record belong to issue color if it true else belong to unissued color

    @PrePersist
    public void create()
    {
        this.createdDate = new Date(System.currentTimeMillis());
    }

    public void update()
    {
        this.updatedDate = new Date(System.currentTimeMillis());
    }

}
