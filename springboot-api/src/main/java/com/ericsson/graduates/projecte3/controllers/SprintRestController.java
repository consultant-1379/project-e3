package com.ericsson.graduates.projecte3.controllers;

import com.ericsson.graduates.projecte3.DAO.ProjectDAO;
import com.ericsson.graduates.projecte3.DAO.SprintDAO;
import com.ericsson.graduates.projecte3.DTO.Project;
import com.ericsson.graduates.projecte3.DTO.Sprint;
import com.ericsson.graduates.projecte3.DTO.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@CrossOrigin
@RestController
@RequestMapping("/sprint")
public class SprintRestController {
    @Autowired
    private SprintDAO dao;

    @Autowired
    private ProjectDAO projectDAO;

    @GetMapping("/{id}")
    public ResponseEntity<Sprint> get(@PathVariable int id){
        Sprint s = dao.getSprint(id);
        if(s != null)
            return ResponseEntity.ok().body(s);
        else
            return ResponseEntity.notFound().build();
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<Collection<Sprint>> getSprintsByProject(@PathVariable int projectId){
        Collection<Sprint> sprints = dao.getAllSprintForAProject(projectId);
        if(sprints.isEmpty())
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok().body(sprints);
    }

    @PostMapping()
    public Sprint create(@RequestBody Sprint sprint){
        // If only the id was sent, map the correct project
        if (sprint.getProjectID() > 0 && sprint.getProject() == null)
        {
            Project project = projectDAO.getProject(sprint.getProjectID());
            sprint.setProject(project);
        }

        int id = dao.insertSprint(sprint);
        sprint.setId(id);
        return sprint;
    }

    @PutMapping()
    public ResponseEntity<Sprint> update(@RequestBody Sprint sprint){
        Sprint s = dao.getSprint(sprint.getId());

        if(s != null)
            return ResponseEntity.ok().body(dao.updateSprint(sprint));
        else
            return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id){
        int status = dao.deleteSprint(id);

        if(status == 0)
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok().body("Success");
    }
}
