package com.ericsson.graduates.projecte3.DAO;

import com.ericsson.graduates.projecte3.DTO.Item;
import com.ericsson.graduates.projecte3.DTO.Team;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class TeamRepo implements TeamDAO{
    @PersistenceContext
    protected EntityManager entityManager;

    public Team getTeam (int teamID) throws DataAccessException{
        return entityManager.find(Team.class, teamID);
    }

    @Override
    public int insertTeam(Team team) {
        entityManager.persist(team);
        entityManager.flush();

        return team.getId();
    }

    @Override
    public Team updateTeam(Team team) {
        Team entity = entityManager.find(Team.class, team.getId());

        if (entity != null)
        {
            if (team.getName() != null)
                entity.setName(team.getName());

            return entity;
        }

        return null;
    }

    @Override
    public int deleteTeam(int id) {
        Team entity = entityManager.find(Team.class, id);

        if (entity != null)
        {
            entityManager.remove(entity);
            return 1;
        }

        return 0;
    }
}
