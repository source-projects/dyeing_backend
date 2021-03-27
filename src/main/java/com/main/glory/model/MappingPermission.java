package com.main.glory.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Getter
@Setter
public class MappingPermission {

    Map<String,String> mapping ;

    public Map<String, String> getMapping() {
        return mapping;
    }

    public MappingPermission(){
        mapping = new HashMap<String,String>();

        mapping.put("pa","party");
        mapping.put("qu","quality");
        mapping.put("u","user");
        mapping.put("sb","stockBatch");
        //mapping.put("prg","program");
        mapping.put("sh","shade");
        mapping.put("su","supplier");
        mapping.put("sr","supplier/rate");
        mapping.put("cs","color");
        mapping.put("pr","dyeingProcess");
        mapping.put("pp","productionPlan");
        mapping.put("jp","jet");
        mapping.put("pt","paymentTerm");
        mapping.put("d","dispatch");
        mapping.put("bf","batch");//bf==batch finish mtr
        mapping.put("ip","machine");//machine=all machine except jet
        //mapping.put("wt","waterJet");
        mapping.put("ad","admin");
        mapping.put("ds","dyeingSlip");
        mapping.put("emp","employee");
        mapping.put("attnds","attendance");


    }


}
