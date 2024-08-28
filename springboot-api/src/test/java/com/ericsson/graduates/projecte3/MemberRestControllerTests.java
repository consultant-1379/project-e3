package com.ericsson.graduates.projecte3;

import com.ericsson.graduates.projecte3.DAO.MemberDAO;
import com.ericsson.graduates.projecte3.DTO.Member;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MemberRestControllerTests {
    @MockBean
    private MemberDAO dao;

    @Autowired
    private TestRestTemplate rest;

    private Member member;

    @BeforeEach
    void setUp() {
        Team team = new Team("Test team");

        member = new Member(team, "Test member", "test@member.com");
        member.setId(1);
    }

    @Test
    void testAddMember() {
        ResponseEntity<Member> response = addMember();
        Member responseMember = response.getBody();

        assertEquals(member, responseMember);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(dao).insertMember(member);
    }

    ResponseEntity<Member> addMember() {
        HttpEntity<Member> memberEntity = new HttpEntity<>(member);
        when(dao.insertMember(member)).thenReturn(member.getId());

        return rest.exchange("/member",
                HttpMethod.POST,
                memberEntity,
                Member.class);
    }

    @Test
    void testGetMemberByTeamID() {
        Collection<Member> teamMembers = new ArrayList<>();
        teamMembers.add(member);
        // A list of members is expected when getting members from a team
        when(dao.getAllMembersOfATeam(member.getTeam().getId())).thenReturn(teamMembers);

        ResponseEntity<Collection<Member>> response = rest.exchange("/member/team/" + member.getTeam().getId(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Member>>() {});

        Collection<Member> responseMembers = response.getBody();

        assertEquals(teamMembers, responseMembers);
        assertTrue(responseMembers.contains(member));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(dao).getAllMembersOfATeam(member.getTeam().getId());
    }

    @Test
    void testGetMemberByID() {
        when(dao.getMember(member.getId())).thenReturn(member);
        ResponseEntity<Member> response = rest.exchange("/member/" + member.getId(),
                HttpMethod.GET,
                null,
                Member.class);

        Member responseMember = response.getBody();

        assertEquals(member, responseMember);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(dao).getMember(member.getId());
    }

    @Test
    void testUpdateMember() {
        addMember();

        Member updateMember = member;
        updateMember.setName("Updated Name");
        updateMember.setEmail("new@member.com");

        HttpEntity<Member> memberEntity = new HttpEntity<>(updateMember);

        // Update should initially return the current member info
        when(dao.getMember(updateMember.getId())).thenReturn(member);

        // Updated member should only be returned after update
        when(dao.updateMember(updateMember)).thenReturn(updateMember);

        ResponseEntity<Member> response = rest.exchange("/member",
                HttpMethod.PUT,
                memberEntity,
                Member.class);

        Member responseMember = response.getBody();

        assertEquals(updateMember, responseMember);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(dao).getMember(member.getId());
        verify(dao).updateMember(updateMember);
    }

    @Test
    void testDeleteMember() {
        addMember();

        // 1 for successful delete
        when(dao.deleteMember(member.getId())).thenReturn(1);

        ResponseEntity<String> response = rest.exchange("/member/" + member.getId(),
                HttpMethod.DELETE,
                null,
                String.class);

        assertEquals("Success", response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(dao).deleteMember(member.getId());
    }
}
