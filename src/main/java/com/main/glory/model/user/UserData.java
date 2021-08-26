package com.main.glory.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.main.glory.model.designation.Designation;
import com.main.glory.model.party.Party;
import com.main.glory.model.user.Request.UserAddRequest;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

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
    Long companyId;
    Long departmentId;
    Long userHeadId;
    Date createdDate;
    Date updatedDate;
    Long createdBy;
    Long updatedBy;
    @Column(columnDefinition = "boolean default true")
    Boolean isMaster;
   /* @Column(columnDefinition = "boolean default false")
    Boolean dataEntry;*/
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private UserPermission userPermissionData;

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "designationId", referencedColumnName = "id")
    private Designation designationId;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    @JoinColumn(name = "partyId", referencedColumnName = "id")
    private List<Party> parties;  

    public UserData(UserAddRequest userDataDto) {
        //this.dataEntry=userDataDto.getDataEntry();
        this.contact = userDataDto.getContact();
        this.userName=userDataDto.getUserName();
        this.firstName = userDataDto.getFirstName();
        this.lastName=userDataDto.getLastName();
        this.email=userDataDto.getEmail();
        this.password=userDataDto.getPassword();
        this.companyId =userDataDto.getCompanyId();
        this.departmentId= userDataDto.getDepartmentId() ;
        this.createdBy = userDataDto.getCreatedBy();
        this.userHeadId = userDataDto.getUserHeadId();
        this.isMaster = userDataDto.getIsMaster();

    }


    @PrePersist
    protected void onCreate() {
        this.createdDate = new Date(System.currentTimeMillis());
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedDate = new Date(System.currentTimeMillis());
    }

}
