package com.main.glory.model.dyeingProcess;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DyeingChemicalData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    Long controlId;
    Long itemId;
    String itemName;
    String byChemical;
    Double concentration;
    @Column(columnDefinition = "varchar(255) default 'DEFAULT'")
    String shadeType;
    Date updatedDate;

    @PreUpdate
    protected void onUpdate()
    {
        this.updatedDate = new Date(System.currentTimeMillis());
    }



}
