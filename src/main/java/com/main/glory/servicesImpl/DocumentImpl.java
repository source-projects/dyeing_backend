package com.main.glory.servicesImpl;

import com.main.glory.Dao.PartyDao;
import com.main.glory.model.document.request.GetDocumentModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("documentImpl")
public class DocumentImpl {


    @Autowired
    PartyDao partyDao;

    public void getParty(GetDocumentModel documentModel) {



    }
}
