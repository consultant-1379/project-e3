package com.ericsson.graduates.projecte3.controllers;

import com.ericsson.graduates.projecte3.DAO.TeamDAO;
import com.ericsson.graduates.projecte3.DTO.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/team")
public class TeamRestController {
    @Autowired
    private TeamDAO dao;

    @GetMapping(value = "/{id}", produces = {"application/json","application/xml"})
    public ResponseEntity<Team> getTeam(@PathVariable int id) {
        Team team = dao.getTeam(id);

        if (team != null)
            return ResponseEntity.ok().body(team);
        else
            return ResponseEntity.notFound().build();
    }

    @PostMapping(consumes = {"application/json","application/xml"},
            produces = {"application/json","application/xml"})
    public Team addTeam(@RequestBody Team team)
    {
        int id = dao.insertTeam(team);
        team.setId(id);

        return team;
    }

    @PutMapping(consumes = {"application/json","application/xml"},
            produces = {"application/json","application/xml"})
    public ResponseEntity<Team> updateTeam(@RequestBody Team team)
    {
        Team entity = dao.getTeam(team.getId());

        if (entity != null)
            return ResponseEntity.ok().body(dao.updateTeam(team));
        else
            return ResponseEntity.notFound().build();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteTeam(@PathVariable int id)
    {
        int status = dao.deleteTeam(id);

        if (status == 0)
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok().body("Success");
    }
}
