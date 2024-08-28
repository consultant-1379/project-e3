package com.ericsson.graduates.projecte3;

import com.ericsson.graduates.projecte3.DAO.ProjectDAO;
import com.ericsson.graduates.projecte3.DAO.TeamDAO;
import com.ericsson.graduates.projecte3.DTO.Member;
import com.ericsson.graduates.projecte3.DTO.Project;
import com.ericsson.graduates.projecte3.DTO.Sprint;
import com.ericsson.graduates.projecte3.DTO.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProjectRestControllerTests {
    @MockBean
    private ProjectDAO dao;

    @MockBean
    private TeamDAO teamDAO;

    @Autowired
    private TestRestTemplate rest;

    private final int SUCCESS_CODE = 1;
    private final int FAILURE_CODE = 0;
    private Project project;
    private Project project2;
    private Project updatedProject;
    private List<Project> projects = new ArrayList<>();
    private Team team;

    @BeforeEach
    void setup(){
        team = new Team();

        project = new Project();
        project.setName("old name");
        project.setTeamID(team.getId());
        project.setTeam(team);

        project2 = new Project();
        project2.setName("project 2");
        project2.setTeamID(team.getId());
        project.setTeam(team);

        updatedProject = project;
        updatedProject.setName("updated name");
        updatedProject.setTeamID(2);

        projects.add(project);
        projects.add(project2);
    }

    ResponseEntity<Team> addTeam(Team team) {
        HttpEntity<Team> entity = new HttpEntity<>(team);
        when(teamDAO.insertTeam(team)).thenReturn(team.getId());

        return rest.exchange("/team",
                HttpMethod.POST,
                entity,
                Team.class);
    }

    ResponseEntity<Project> addProject(Project project) {
        Team responseTeam = addTeam(project.getTeam()).getBody();
        project.setTeam(responseTeam);

        HttpEntity<Project> entity = new HttpEntity<>(project);
        when(dao.insertProject(project)).thenReturn(project.getId());

        return rest.exchange("/project",
                HttpMethod.POST,
                entity,
                Project.class);
    }

    @Test
    void testAddProject(){
        ResponseEntity<Project> response = addProject(project);
        Project result = response.getBody();

        verify(dao).insertProject(project);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(project, result);
    }

    @Test
    void testGetProjectById(){
        addProject(project);

        when(dao.getProject(project.getId())).thenReturn(project);
        ResponseEntity<Project> response = rest.exchange("/project/" + project.getId(),
                HttpMethod.GET,
                null,
                Project.class);

        Project result = response.getBody();

        verify(dao).getProject(anyInt());
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(project, result);
    }

    @Test
    void testGetProjectByIdReturn404(){
        when(dao.getProject(project.getId())).thenReturn(null);
        ResponseEntity<Project> response = rest.exchange("/project/" + project.getId(),
                HttpMethod.GET,
                null,
                Project.class);

        Project result = response.getBody();

        verify(dao).getProject(anyInt());
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
        assertNull(result);
    }

//    @Test
//    void testGetProjectByTeam(){
//        when(dao.getAllProjectsForATeam(project.getId())).thenReturn(projects);
//        ResponseEntity<Collection<Project>> response = rest.exchange("/project/team" + team.getId(),
//                HttpMethod.GET,
//                null,
//                new ParameterizedTypeReference<Collection<Project>>(){});
//
//        Collection<Project> result = response.getBody();
//
//        assertEquals(projects, result);
//        assertTrue(projects.contains(project));
//        assertTrue(projects.contains(project2));
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        verify(dao).getAllProjectsForATeam(team.getId());
//    }

    @Test
    void testUpdateProject() {
        addProject(project);

        HttpEntity<Project> httpEntity = new HttpEntity<>(updatedProject);

        // Update should initially return the current project info
        when(dao.getProject(updatedProject.getId())).thenReturn(project);

        // Updated member should only be returned after update
        when(dao.updateProject(updatedProject)).thenReturn(updatedProject);

        ResponseEntity<Project> response = rest.exchange("/project",
                HttpMethod.PUT,
                httpEntity,
                Project.class);

        Project result = response.getBody();

        verify(dao).getProject(anyInt());
        verify(dao).updateProject(any(Project.class));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedProject, result);
    }

    @Test
    void testUpdateProjectReturn404() {

        HttpEntity<Project> httpEntity = new HttpEntity<>(updatedProject);

        // Update should initially return the current project info
        when(dao.getProject(updatedProject.getId())).thenReturn(null);

        ResponseEntity<Project> response = rest.exchange("/project",
                HttpMethod.PUT,
                httpEntity,
                Project.class);

        Project result = response.getBody();

        verify(dao).getProject(anyInt());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(result);
    }

    @Test
    void testDeleteProject() {
        addProject(project);

        // 1 for successful delete
        when(dao.deleteProject(project.getId())).thenReturn(SUCCESS_CODE);

        ResponseEntity<String> response = rest.exchange("/project/" + project.getId(),
                HttpMethod.DELETE,
                null,
                String.class);

        verify(dao).deleteProject(project.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Success", response.getBody());
    }

    @Test
    void testDeleteProjectIDNotFoundReturn404() {
        when(dao.deleteProject(project.getId())).thenReturn(FAILURE_CODE);

        ResponseEntity<String> response = rest.exchange("/project/" + project.getId(),
                HttpMethod.DELETE,
                null,
                String.class);
        verify(dao).deleteProject(project.getId());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
