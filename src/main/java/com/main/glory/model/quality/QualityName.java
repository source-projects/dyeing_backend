package com.main.glory.model.quality;

import com.main.glory.model.supplier.Supplier;
import com.main.glory.model.supplier.SupplierRate;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@ToString
public class QualityName {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    String qualityName;//should be unique
    Long createdBy;
    Date createdDate;
    Long updatedBy;
    Date updatedDate;
    @ColumnDefault("0")
    Double rate;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "qualityNameId", referencedColumnName = "id")
    @ApiModelProperty(hidden = true)
    List<Supplier> supplierList;
    @PrePersist
    public void create()
    {
        this.createdDate = new Date(System.currentTimeMillis());
    }
    @PreUpdate
    public void update()
    {
        this.updatedDate = new Date(System.currentTimeMillis());
    }

}
