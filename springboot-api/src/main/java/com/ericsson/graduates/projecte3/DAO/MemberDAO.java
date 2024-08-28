package com.ericsson.graduates.projecte3.DAO;

import com.ericsson.graduates.projecte3.DTO.Member;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

public interface MemberDAO {
    Collection<Member> getAllMembersOfATeam(int teamID);
    Member getMember(int id);

    @Transactional
    int insertMember(Member member);

    @Transactional
    Member updateMember(Member member);

    @Transactional
    int deleteMember(int id);
}
