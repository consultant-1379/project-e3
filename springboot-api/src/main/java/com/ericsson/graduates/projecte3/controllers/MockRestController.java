package com.ericsson.graduates.projecte3.controllers;

import com.ericsson.graduates.projecte3.DTO.*;
import com.ericsson.graduates.projecte3.ENUM.ItemCategory;
import com.ericsson.graduates.projecte3.ENUM.ItemLabel;
import com.ericsson.graduates.projecte3.ENUM.Priority;
import com.ericsson.graduates.projecte3.ENUM.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/mock")
public class MockRestController {

    @Autowired
    private RestTemplate rest;

    private static final String baseURL = "http://localhost:8080";

    @GetMapping(produces = {"application/json","application/xml"})
    public ResponseEntity<String> generateMockData() throws ParseException {

        Team mockTeam = new Team("Mock team");
        ResponseEntity<Team> teamResponse = insertMockItem(mockTeam, Team.class, baseURL + "/team");

        // Get updated ID
        mockTeam = teamResponse.getBody();


        // ================ ADD MEMBERS START ================

        // Lucy
        Member lucy = new Member(mockTeam, "Lucy Kerrigan", "lucy@member.com");
        ResponseEntity<Member> memberResponse = insertMockItem(lucy, Member.class, baseURL + "/member");

        //Get updated ID
        lucy = memberResponse.getBody();

        // Moey
        Member moey = new Member(mockTeam, "Jing Sheng Moey", "moey@member.com");
        memberResponse = insertMockItem(moey, Member.class, baseURL + "/member");

        //Get updated ID
        moey = memberResponse.getBody();

        // Chris
        Member chris = new Member(mockTeam, "Christopher Boland", "chris@member.com");
        memberResponse = insertMockItem(chris, Member.class, baseURL + "/member");

        //Get updated ID
        chris = memberResponse.getBody();

        // Cameron
        Member cameron = new Member(mockTeam, "Cameron Scholes", "cameron@member.com");
        memberResponse = insertMockItem(cameron, Member.class, baseURL + "/member");

        //Get updated ID
        cameron = memberResponse.getBody();

        // ================ ADD MEMBERS END ================

        // ================ ADD PROJECTS START ================
        Project mockProject1 = new Project(mockTeam, "Test Project 1");
        ResponseEntity<Project> projectResponse = insertMockItem(mockProject1, Project.class, baseURL + "/project");

        //Get updated ID
        mockProject1 = projectResponse.getBody();

        Project mockProject2 = new Project(mockTeam, "Test Project 2");
        projectResponse = insertMockItem(mockProject2, Project.class, baseURL + "/project");

        //Get updated ID
        mockProject2 = projectResponse.getBody();

        // ================ ADD PROJECTS END ================

        // ================ ADD SPRINTS START ================
        Date date = new Date();

        String dateString = "10-Nov-2022";
        DateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        Date futureDate = df.parse(dateString);

        Sprint mockSprint = new Sprint(mockProject1, date, futureDate, null, Status.IN_PROGRESS);
        ResponseEntity<Sprint> sprintResponse = insertMockItem(mockSprint, Sprint.class, baseURL + "/sprint");

        //Get updated ID
        mockSprint = sprintResponse.getBody();

        Sprint mockSprint2 = new Sprint(mockProject1, date, futureDate, null, Status.COMPLETED);
        sprintResponse = insertMockItem(mockSprint2, Sprint.class, baseURL + "/sprint");

        //Get updated ID
        mockSprint2 = sprintResponse.getBody();

        Sprint mockSprint3 = new Sprint(mockProject2, date, futureDate, null, Status.COMPLETED);
        sprintResponse = insertMockItem(mockSprint3, Sprint.class, baseURL + "/sprint");

        //Get updated ID
        mockSprint3 = sprintResponse.getBody();

        // ================ ADD SPRINTS END ================

        // ================ ADD ITEMS START ================
        Item mockItem = new Item(mockSprint2, null, "Test Item", "Test Item Description", lucy,
                chris, Status.TBD, ItemCategory.MAD, Priority.LOW, ItemLabel.TASK);

        ResponseEntity<Item> itemResponse = insertMockItem(mockItem, Item.class, baseURL + "/item");

        Item mockItem2 = new Item(mockSprint2, null, "Test Item Two", "Test Item Description", moey,
                chris, Status.IN_PROGRESS, ItemCategory.MAD, Priority.LOW, ItemLabel.TASK);

        insertMockItem(mockItem2, Item.class, baseURL + "/item");

        Item mockItem3 = new Item(mockSprint2, null, "Test Item Three", "Test Item Description", chris,
                chris, Status.COMPLETED, ItemCategory.MAD, Priority.LOW, ItemLabel.TASK);

        insertMockItem(mockItem3, Item.class, baseURL + "/item");

        // ================ ADD ITEMS END ================

        if (memberResponse.getStatusCode() == HttpStatus.OK &&
                itemResponse.getStatusCode() == HttpStatus.OK &&
                projectResponse.getStatusCode() == HttpStatus.OK &&
                sprintResponse.getStatusCode() == HttpStatus.OK &&
                teamResponse.getStatusCode() == HttpStatus.OK)
            return ResponseEntity.ok().body("Success");
        else
            return ResponseEntity.internalServerError().build();
    }

    private <T> ResponseEntity<T> insertMockItem(T mock, Class<T> mockClass, String url) {
        HttpEntity<T> entity = new HttpEntity<>(mock);

        return rest.exchange(url,
                HttpMethod.POST,
                entity,
                mockClass);
    }
}
