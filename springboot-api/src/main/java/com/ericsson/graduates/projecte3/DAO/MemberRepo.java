package com.ericsson.graduates.projecte3.DAO;

import com.ericsson.graduates.projecte3.DTO.Member;
import com.ericsson.graduates.projecte3.DTO.Project;
import com.ericsson.graduates.projecte3.DTO.QMember;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Collection;

@Repository
public class MemberRepo implements MemberDAO{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Collection<Member> getAllMembersOfATeam(int teamID) {
        QMember member = QMember.member;
        JPAQuery<?> query = new JPAQuery<>(entityManager);

        JPAQuery<Member> members = query.select(member).from(member).where(member.team.id.eq(teamID)).fetchAll();
        return members.fetch();
    }

    @Override
    public Member getMember(int id) {
        return entityManager.find(Member.class, id);
    }

    @Override
    public int insertMember(Member member) {
        entityManager.persist(member);
        entityManager.flush();

        return member.getId();
    }

    @Override
    public Member updateMember(Member member) {
        Member entity = entityManager.find(Member.class, member.getId());

        if (entity != null)
        {
            if (member.getName() != null)
                entity.setName(member.getName());

            if (member.getEmail() != null)
                entity.setEmail(member.getEmail());

            return entity;
        }

        return null;
    }

    @Override
    public int deleteMember(int id) {
        Member entity = entityManager.find(Member.class, id);

        if (entity != null)
        {
            entityManager.remove(entity);
            return 1;
        }

        return 0;
    }
}
