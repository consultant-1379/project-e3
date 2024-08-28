package com.ericsson.graduates.projecte3;

import com.ericsson.graduates.projecte3.DAO.TeamDAO;
import com.ericsson.graduates.projecte3.DTO.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TeamRestControllerTests {
    @MockBean
    private TeamDAO dao;

    @Autowired
    private TestRestTemplate rest;

    private Team team;

    @BeforeEach
    void setUp() {
        team = new Team("Test Team");
        team.setId(1);
    }

    @Test
    void testAddTeam() {
        ResponseEntity<Team> response = addTeam();
        Team responseTeam = response.getBody();

        assertEquals(team, responseTeam);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(dao).insertTeam(team);
    }

    ResponseEntity<Team> addTeam() {
        HttpEntity<Team> teamEntity = new HttpEntity<>(team);
        when(dao.insertTeam(team)).thenReturn(team.getId());

        return rest.exchange("/team",
                HttpMethod.POST,
                teamEntity,
                Team.class);
    }

    @Test
    void testGetTeamByID() {
        when(dao.getTeam(team.getId())).thenReturn(team);
        ResponseEntity<Team> response = rest.exchange("/team/" + team.getId(),
                HttpMethod.GET,
                null,
                Team.class);

        Team responseTeam = response.getBody();

        assertEquals(team, responseTeam);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(dao).getTeam(team.getId());
    }

    @Test
    void testUpdateTeam() {
        addTeam();

        Team updateTeam = team;
        updateTeam.setName("Updated team");

        HttpEntity<Team> teamEntity = new HttpEntity<>(updateTeam);

        // Get team should return the original team before updating
        when(dao.getTeam(updateTeam.getId())).thenReturn(team);

        // Updated team should only be returned if get is not null
        when(dao.updateTeam(updateTeam)).thenReturn(updateTeam);

        ResponseEntity<Team> response = rest.exchange("/team",
                HttpMethod.PUT,
                teamEntity,
                Team.class);
        Team responseTeam = response.getBody();

        assertEquals(updateTeam, responseTeam);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(dao).updateTeam(updateTeam);
    }

    @Test
    void testDeleteTeam() {
        addTeam();

        // 1 for successful delete
        when(dao.deleteTeam(team.getId())).thenReturn(1);

        ResponseEntity<String> response = rest.exchange("/team/" + team.getId(),
                HttpMethod.DELETE,
                null,
                String.class);

        assertEquals("Success", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(dao).deleteTeam(team.getId());
    }
}
