package com.ericsson.graduates.projecte3.controllers;

import com.ericsson.graduates.projecte3.DAO.ProjectDAO;
import com.ericsson.graduates.projecte3.DAO.TeamDAO;
import com.ericsson.graduates.projecte3.DTO.Project;
import com.ericsson.graduates.projecte3.DTO.Sprint;
import com.ericsson.graduates.projecte3.DTO.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@CrossOrigin
@RestController
@RequestMapping("/project")
public class ProjectRestController {
    @Autowired
    private ProjectDAO dao;

    @Autowired
    private TeamDAO teamDAO;

    @GetMapping("/{id}")
    public ResponseEntity<Project> get(@PathVariable int id){
        Project p = dao.getProject(id);
        if(p != null)
            return ResponseEntity.ok().body(p);
        else
            return ResponseEntity.notFound().build();
    }

    @GetMapping("/team/{teamId}")
    public ResponseEntity<Collection<Project>> getProjectByTeamId(@PathVariable int teamId){
        Collection<Project> projects = dao.getAllProjectsForATeam(teamId);
        if(projects.isEmpty())
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok().body(projects);
    }

    @PostMapping()
    public Project create(@RequestBody Project project){
        // If only the id was sent, map the correct team
        if (project.getTeamID() > 0 && project.getTeam() == null)
        {
            Team team = teamDAO.getTeam(project.getTeamID());
            project.setTeam(team);
        }

        int id = dao.insertProject(project);
        project.setId(id);
        return project;
    }

    @PutMapping()
    public ResponseEntity<Project> update(@RequestBody Project project){

        Project p = dao.getProject(project.getId());
        if(p!=null){
            return ResponseEntity.ok().body(dao.updateProject(project));
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id){
        int status = dao.deleteProject(id);

        if(status == 0)
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok().body("Success");
    }
}
