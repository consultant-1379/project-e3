package com.ericsson.graduates.projecte3.controllers;

import com.ericsson.graduates.projecte3.DAO.MemberDAO;
import com.ericsson.graduates.projecte3.DAO.TeamDAO;
import com.ericsson.graduates.projecte3.DTO.Member;
import com.ericsson.graduates.projecte3.DTO.Team;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@CrossOrigin
@RestController
@RequestMapping("/member")
public class MemberRestController {
    @Autowired
    private MemberDAO dao;

    @Autowired
    private TeamDAO teamDAO;

    @GetMapping(value = "/team/{teamID}",
            produces = {"application/json","application/xml"})
    public ResponseEntity<Collection<Member>> getMembersOfATeam(@PathVariable int teamID)
    {
        Collection<Member> members = dao.getAllMembersOfATeam(teamID);

        if (members.size() >= 1)
            return ResponseEntity.ok().body(members);
        else
            return ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/{id}",
            produces = {"application/json","application/xml"})
    public ResponseEntity<Member> getMemberByID(@PathVariable int id)
    {
        Member member = dao.getMember(id);

        if (member != null)
            return ResponseEntity.ok().body(member);
        else
            return ResponseEntity.notFound().build();
    }

    @PostMapping(consumes = {"application/json","application/xml"},
            produces = {"application/json","application/xml"})
    public Member addMember(@RequestBody Member member)
    {
        // If only the id was sent, map the correct team
        if (member.getTeamID() > 0 && member.getTeam() == null)
        {
            Team team = teamDAO.getTeam(member.getTeamID());
            member.setTeam(team);
        }

        int id = dao.insertMember(member);
        member.setId(id);

        return member;
    }

    @PutMapping(consumes = {"application/json","application/xml"},
            produces = {"application/json","application/xml"})
    public ResponseEntity<Member> updateMember(@RequestBody Member member)
    {
        Member entity = dao.getMember(member.getId());

        if (entity != null)
            return ResponseEntity.ok().body(dao.updateMember(member));
        else
            return ResponseEntity.notFound().build();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteTeam(@PathVariable int id)
    {
        int status = dao.deleteMember(id);

        if (status == 0)
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok().body("Success");
    }
}
