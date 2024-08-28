package com.ericsson.graduates.projecte3.DAO;

import com.ericsson.graduates.projecte3.DTO.Team;
import org.springframework.transaction.annotation.Transactional;

public interface TeamDAO {
    Team getTeam(int teamID);

    @Transactional
    int insertTeam(Team team);

    @Transactional
    Team updateTeam(Team team);

    @Transactional
    int deleteTeam(int id);
}
