package com.main.glory.model.user.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class getAllUserInfo {

    Long id;
    String userName;
    String firstName;
    String lastName;
    Long userHeadId;
    String company;
    String designation;

}
