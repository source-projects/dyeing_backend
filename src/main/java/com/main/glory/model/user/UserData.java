package com.main.glory.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.main.glory.model.designation.Designation;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="User")
@ToString
public class UserData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String userName;
    String firstName;
    String lastName;
    String email;
    Long contact;
    @JsonIgnore
    String password;
    String company;
    String department;
    Long userHeadId;
    Date createdDate;
    Date updatedDate;
    Long createdBy;
    Long updatedBy;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private UserPermission userPermissionData;

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "designationId", referencedColumnName = "id")
    private Designation designationId;


    @PrePersist
    protected void onCreate() {
        this.createdDate = new Date(System.currentTimeMillis());
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedDate = new Date(System.currentTimeMillis());
    }

}
