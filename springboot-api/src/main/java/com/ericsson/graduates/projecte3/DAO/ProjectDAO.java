package com.ericsson.graduates.projecte3.DAO;

import com.ericsson.graduates.projecte3.DTO.Project;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

public interface ProjectDAO {
    Collection<Project> getAllProjectsForATeam(int teamID);
    Project getProject(int id);

    @Transactional
    int insertProject(Project project);

    @Transactional
    Project updateProject(Project project);

    @Transactional
    int deleteProject(int id);
}
