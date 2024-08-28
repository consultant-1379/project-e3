package com.ericsson.graduates.projecte3.DAO;

import com.ericsson.graduates.projecte3.DTO.Sprint;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

public interface SprintDAO {
    Collection<Sprint> getAllSprintForAProject(int projectID);
    Sprint getSprint (int id);

    @Transactional
    int insertSprint(Sprint sprint);

    @Transactional
    Sprint updateSprint(Sprint sprint);

    @Transactional
    int deleteSprint(int id);
}
