package com.ericsson.graduates.projecte3.DAO;

import com.ericsson.graduates.projecte3.DTO.Project;
import com.ericsson.graduates.projecte3.DTO.QProject;
import com.ericsson.graduates.projecte3.DTO.Sprint;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Collection;

@Repository
public class ProjectRepo implements ProjectDAO{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Collection<Project> getAllProjectsForATeam(int teamID) {
        QProject project = QProject.project;
        JPAQuery<?> query = new JPAQuery<>(entityManager);

        JPAQuery<Project> projects = query.select(project).from(project).where(project.team.id.eq(teamID)).fetchAll();
        return projects.fetch();
    }

    @Override
    public Project getProject(int id) {
        return entityManager.find(Project.class, id);
    }

    @Override
    public int insertProject(Project project) {
        entityManager.persist(project);
        entityManager.flush();

        return project.getId();
    }

    @Override
    public Project updateProject(Project project) {
        Project entity = entityManager.find(Project.class, project.getId());

        if (entity != null)
        {
            if (project.getName() != null)
                entity.setName(project.getName());

            return entity;
        }

        return null;
    }

    @Override
    public int deleteProject(int id) {
        Project entity = entityManager.find(Project.class, id);

        if (entity != null)
        {
            entityManager.remove(entity);
            return 1;
        }
        return 0;
    }
}
