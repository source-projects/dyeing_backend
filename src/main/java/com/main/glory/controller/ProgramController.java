package com.main.glory.controller;

import com.main.glory.config.ControllerConfig;
import com.main.glory.model.Program;
import com.main.glory.servicesImpl.ProgramServiceImpl;
import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProgramController extends ControllerConfig {

    @Autowired
    private ProgramServiceImpl programServiceImpl;

    @PostMapping(value="/program")
    public boolean saveProgram(@RequestBody Program program) throws Exception {
        if(program==null)
        {
            return false;
        }
        boolean flag=programServiceImpl.saveProgram(program);
        if(!flag)
        {
            System.out.println("Something went wrong");
            return false;
        }else
            return true;
    }

    @GetMapping(value="/program/all")
    public List<Program> getProgramList() throws Exception {
        return programServiceImpl.getAllProgram();
    }

    @GetMapping(value="/program/{id}")
    public Program getProgramDetailsById(@PathVariable(value = "id") Long id) throws Exception {
        if(id!=null)
        {
            Program programObject=programServiceImpl.getProgramById(id);
            if(programObject!=null)
            {
                return programObject;
            }
        }
        return null;
    }

    @PutMapping(value="/program")
    public boolean  updateProgram(@RequestBody Program program) throws Exception
    {
        if(program!=null)
        {
            boolean flag=programServiceImpl.updateProgramByID(program);
            if(flag) {
                return true;
            }
        }
        return false;
    }

    @DeleteMapping(value="/program/{id}")
    public boolean deleteProgramDetailsByID(@PathVariable(value = "id") Long id) throws Exception {
        if(id!=null)
        {
            boolean flag=programServiceImpl.deleteProgramById(id);
            if(flag)
            {
                return true;
            }
        }
        return false;
    }
}
