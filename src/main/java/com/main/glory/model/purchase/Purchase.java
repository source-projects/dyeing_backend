package com.main.glory.model.purchase;


import com.main.glory.model.employee.EmployeeData;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Purchase {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String material;
    Double amt;
    Long departmentId;
    Long approvedById;
    String remark;
    Long receiverById;
    Boolean checked=false;
    Long createdBy;
    Date createdDate;
    Long updatedBy;
    Date updatedDate;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "controlId", referencedColumnName = "id")
    List<MaterialPhotos> materialPhotosList;

    public Purchase(Purchase p) {
        this.id=p.getId() ;
        this.material=p.material;
        this.amt=p.amt;
        this.departmentId=p.departmentId;
        this.approvedById=p.approvedById;
        this.remark=p.remark;
        this.receiverById=p.receiverById;
        this.checked=p.checked;
        this.createdDate=p.createdDate;
        this.createdBy=p.createdBy;
        this.updatedDate=p.updatedDate;
        this.updatedBy=p.updatedBy;
        this.materialPhotosList=p.getMaterialPhotosList();
    }

    @PrePersist
    public void create()
    {
        this.createdDate=new Date(System.currentTimeMillis());
    }
    @PreUpdate
    public void update()
    {
        this.updatedDate=new Date(System.currentTimeMillis());
    }
}
