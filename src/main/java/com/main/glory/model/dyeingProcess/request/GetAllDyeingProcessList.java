package com.main.glory.model.dyeingProcess.request;

import com.main.glory.model.dyeingProcess.DyeingProcessMast;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetAllDyeingProcessList {
    Long id;
    String dyeingProcessName;

    public GetAllDyeingProcessList(DyeingProcessMast d) {
        this.id=d.getId();
        this.dyeingProcessName=d.getProcessName();
    }
}
