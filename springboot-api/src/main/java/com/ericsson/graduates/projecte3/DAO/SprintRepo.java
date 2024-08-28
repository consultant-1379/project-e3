package com.ericsson.graduates.projecte3.DAO;

import com.ericsson.graduates.projecte3.DTO.QItem;
import com.ericsson.graduates.projecte3.DTO.QSprint;
import com.ericsson.graduates.projecte3.DTO.Sprint;
import com.ericsson.graduates.projecte3.DTO.Team;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Collection;

@Repository
public class SprintRepo implements SprintDAO{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Collection<Sprint> getAllSprintForAProject(int projectID) {
        QSprint sprint = QSprint.sprint;
        JPAQuery<?> query = new JPAQuery<>(entityManager);

        JPAQuery<Sprint> sprints = query.select(sprint).from(sprint).where(sprint.project.id.eq(projectID)).fetchAll();

        return sprints.fetch();
    }

    @Override
    public Sprint getSprint(int id) {
        return entityManager.find(Sprint.class, id);
    }

//    @Override
//    public Sprint getSprint(int id) {
//        QSprint sprint = QSprint.sprint;
//        QItem item = QItem.item;
//        JPAQuery<?> query = new JPAQuery<>(entityManager);
//
//        JPAQuery<Sprint> sprintJPAQuery = query.select(sprint)
//                .from(sprint)
//                .where(sprint.id.eq(id))
//                .join(sprint.items, item);
//
//        return sprintJPAQuery.fetchFirst();
//    }

    @Override
    public int insertSprint(Sprint sprint) {
        entityManager.persist(sprint);
        entityManager.flush();

        return sprint.getId();
    }

    @Override
    public Sprint updateSprint(Sprint sprint) {
        Sprint entity = entityManager.find(Sprint.class, sprint.getId());

        if (entity != null)
        {
            if (sprint.getStartDate() != null)
                entity.setStartDate(sprint.getStartDate());

            if (sprint.getEndDate() != null)
                entity.setEndDate(sprint.getEndDate());

            if (sprint.getItems() != null)
                entity.setItems(sprint.getItems());

            if (sprint.getStatus() != null)
                entity.setStatus(sprint.getStatus());


            return entity;
        }

        return null;
    }

    @Override
    public int deleteSprint(int id) {
        Sprint entity = entityManager.find(Sprint.class, id);

        if (entity != null)
        {
            entityManager.remove(entity);
            return 1;
        }

        return 0;
    }
}
