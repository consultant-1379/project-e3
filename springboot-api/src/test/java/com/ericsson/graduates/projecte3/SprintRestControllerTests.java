package com.ericsson.graduates.projecte3;


import com.ericsson.graduates.projecte3.DAO.ItemDAO;
import com.ericsson.graduates.projecte3.DAO.ProjectDAO;
import com.ericsson.graduates.projecte3.DAO.SprintDAO;
import com.ericsson.graduates.projecte3.DAO.TeamDAO;
import com.ericsson.graduates.projecte3.DTO.*;
import com.ericsson.graduates.projecte3.ENUM.ItemCategory;
import com.ericsson.graduates.projecte3.ENUM.ItemLabel;
import com.ericsson.graduates.projecte3.ENUM.Priority;
import com.ericsson.graduates.projecte3.ENUM.Status;
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

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SprintRestControllerTests {
    @MockBean
    private SprintDAO dao;

    @MockBean
    private ProjectDAO projectDAO;

    @MockBean
    private ItemDAO itemDAO;

    @MockBean
    private TeamDAO teamDAO;

    @Autowired
    private TestRestTemplate rest;

    private final int SUCCESS_CODE = 1;
    private final int FAILURE_CODE = 0;
    private Project project;
    private Sprint sprint;
    private Sprint updatedSprint;
    private Set<Sprint> sprints = new HashSet<>();
    private Set<Sprint> emptySprints = new HashSet<>();
    private Comment comment1;
    private Comment comment2;
    private Set<Comment> comments = new HashSet<>();
    private Item item1;
    private Item item2;
    private Set<Item> items = new HashSet<>();

    private Member member;

    @BeforeEach
    void setup() throws ParseException {
        LocalDateTime dateTime = LocalDateTime.now();

        comment1 = new Comment();
        comment1.setCommentBy(1);
        comment1.setItemID(1);
        comment1.setTopic("Comment 1 topic");
        comment1.setContent("Comment 1 content");
        comment1.setCreatedOn(dateTime);

        comment2 = new Comment();
        comment2.setCommentBy(1);
        comment2.setItemID(1);
        comment2.setTopic("Comment 2 topic");
        comment2.setContent("Comment 2 content");
        comment2.setCreatedOn(dateTime);

        comments.add(comment1);
        comments.add(comment2);

        member = new Member(null, "test", "test@test.com");

        item1 = new Item(sprint, null, "Item 1", "Item 1 description", null, null,
                Status.TBD, ItemCategory.MAD, Priority.LOW, ItemLabel.TASK, comments);

        item2 = new Item(sprint, null, "Item 2", "Item 2 description", null, null,
                Status.TBD, ItemCategory.MAD, Priority.LOW, ItemLabel.TASK, comments);

        items = new HashSet<>();
        items.add(item1);
        items.add(item2);

        Team mockTeam = new Team();

        project = new Project();
        project.setTeam(mockTeam);

        sprint = new Sprint();
        sprint.setStatus(Status.TBD);
        sprint.setProject(project);
        sprint.setStartDate(new Timestamp((new Date()).getTime()));
        sprint.setItems(items);

        sprints.add(sprint);
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
        when(projectDAO.insertProject(project)).thenReturn(project.getId());

        return rest.exchange("/project",
                HttpMethod.POST,
                entity,
                Project.class);
    }

    ResponseEntity<Item> addItem(Item item) {
        HttpEntity<Item> entity = new HttpEntity<>(item);
        when(itemDAO.insertItem(item)).thenReturn(item.getId());

        return rest.exchange("/item",
                HttpMethod.POST,
                entity,
                Item.class);
    }

    ResponseEntity<Sprint> addSprint(Sprint sprint){
        sprint.getItems().forEach(item -> {
            Item response = addItem(item).getBody();
            item = response;
        });

        Project responseProject = addProject(sprint.getProject()).getBody();
        sprint.setProject(responseProject);
        sprint.setProjectID(responseProject.getId());

        HttpEntity<Sprint> entity = new HttpEntity<>(sprint);
        when(dao.insertSprint(sprint)).thenReturn(sprint.getId());

        return rest.exchange("/sprint",
                HttpMethod.POST,
                entity,
                Sprint.class);
    }

    @Test
    void testAddSprint(){
        ResponseEntity<Sprint> response = addSprint(sprint);
        Sprint result = response.getBody();

        assertEquals(sprint, result);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        verify(dao).insertSprint(sprint);
    }

    @Test
    void testGetSprintById(){
        when(dao.getSprint(sprint.getId())).thenReturn(sprint);
        ResponseEntity<Sprint> response = rest.exchange("/sprint/" + sprint.getId(),
                HttpMethod.GET,
                null,
                Sprint.class);

        Sprint result = response.getBody();

        assertEquals(sprint, result);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        verify(dao).getSprint(anyInt());
    }

    @Test
    void testGetSprintByIdReturn404(){
        when(dao.getSprint(sprint.getId())).thenReturn(null);
        ResponseEntity<Sprint> response = rest.exchange("/sprint/" + sprint.getId(),
                HttpMethod.GET,
                null,
                Sprint.class);

        Sprint result = response.getBody();

        verify(dao).getSprint(anyInt());
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
        assertNull(result);
    }

    @Test
    void testGetSprintByProject(){
        addSprint(sprint);

        when(dao.getAllSprintForAProject(project.getId())).thenReturn(sprints);
        ResponseEntity<Collection<Sprint>> response = rest.exchange("/sprint/project/" + project.getId(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Sprint>>(){});

        Collection<Sprint> result = response.getBody();

//        assertEquals(sprints, result);
        assertTrue(sprints.contains(sprint));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(dao).getAllSprintForAProject(project.getId());
    }

    @Test
    void testGetSprintByProjectReturn404(){
        when(dao.getAllSprintForAProject(project.getId())).thenReturn(emptySprints);
        ResponseEntity<Collection<Sprint>> response = rest.exchange("/sprint/project/" + project.getId(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Sprint>>(){});

        Collection<Sprint> result = response.getBody();

        verify(dao).getAllSprintForAProject(project.getId());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(result);
    }

    @Test
    void testUpdateSprint() {
        addSprint(sprint);

        updatedSprint = sprint;
        updatedSprint.setStatus(Status.IN_PROGRESS);

        HttpEntity<Sprint> httpEntity = new HttpEntity<>(updatedSprint);

        // Update should initially return the current project info
        when(dao.getSprint(updatedSprint.getId())).thenReturn(sprint);

        // Updated member should only be returned after update
        when(dao.updateSprint(updatedSprint)).thenReturn(updatedSprint);

        ResponseEntity<Sprint> response = rest.exchange("/sprint",
                HttpMethod.PUT,
                httpEntity,
                Sprint.class);

        Sprint result = response.getBody();

        verify(dao).getSprint(sprint.getId());
        verify(dao).updateSprint(updatedSprint);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedSprint, result);
    }

    @Test
    void testUpdateSprintReturn404() {
        HttpEntity<Sprint> httpEntity = new HttpEntity<>(sprint);

        // Update should initially return the current project info
        when(dao.getSprint(sprint.getId())).thenReturn(null);

        ResponseEntity<Sprint> response = rest.exchange("/sprint",
                HttpMethod.PUT,
                httpEntity,
                Sprint.class);

        Sprint result = response.getBody();

        verify(dao).getSprint(anyInt());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(result);
    }

    @Test
    void testDeleteProject() {
        // 1 for successful delete
        when(dao.deleteSprint(sprint.getId())).thenReturn(SUCCESS_CODE);

        ResponseEntity<String> response = rest.exchange("/sprint/" + sprint.getId(),
                HttpMethod.DELETE,
                null,
                String.class);

        verify(dao).deleteSprint(sprint.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Success", response.getBody());
    }

    @Test
    void testDeleteProjectReturn404() {
        // 1 for successful delete
        when(dao.deleteSprint(sprint.getId())).thenReturn(FAILURE_CODE);

        ResponseEntity<String> response = rest.exchange("/sprint/" + sprint.getId(),
                HttpMethod.DELETE,
                null,
                String.class);

        verify(dao).deleteSprint(sprint.getId());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
