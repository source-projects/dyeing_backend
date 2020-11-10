package com.main.glory.model.user;

import com.main.glory.model.designation.Designation;
import lombok.*;

import javax.persistence.*;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="user")
@ToString
public class UserData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String userName;
    String fistName;
    String lastName;
    String email;
    Long contact;
    String password;
    String company;

    String department;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userId", referencedColumnName = "id")
    private UserPermission userPermissionData;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "designationId", referencedColumnName = "id")
    private Designation designationData;
}
