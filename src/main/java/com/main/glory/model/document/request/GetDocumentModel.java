package com.main.glory.model.document.request;

import lombok.*;

import java.util.List;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GetDocumentModel {

    Long fromRow;
    Long toRow;
    String exportType;
    String moduleName;
    String fromEmail;
    String subjectEmail;
    String sendText;
    List<ToEmailList> toEmailList;


}
