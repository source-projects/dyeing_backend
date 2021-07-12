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

    public DyeingChemicalData(DyeingChemicalData dyeingChemicalData,String itemName)
    {
        this.id = dyeingChemicalData.getId();
        this.controlId = dyeingChemicalData.getControlId();
        this.itemId = dyeingChemicalData.getItemId();
        this.byChemical = dyeingChemicalData.getByChemical();
        this.concentration = dyeingChemicalData.getConcentration();
        this.shadeType = dyeingChemicalData.getShadeType();
        this.itemName = itemName;
    }



}
