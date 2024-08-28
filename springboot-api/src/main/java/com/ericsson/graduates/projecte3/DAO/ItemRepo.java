package com.ericsson.graduates.projecte3.DAO;

import com.ericsson.graduates.projecte3.DTO.Item;
import com.ericsson.graduates.projecte3.DTO.QItem;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Collection;

@Repository
public class ItemRepo implements ItemDAO{
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Collection<Item> getAllItemsForASprint(int sprintID) {
        QItem item = QItem.item;
        JPAQuery<?> query = new JPAQuery<>(entityManager);

        JPAQuery<Item> items = query.select(item)
                .from(item)
                .where(item.sprint.id.eq(sprintID)).fetchAll();

        return items.fetch();
    }

    @Override
    public Item getItem(int id) {
        return entityManager.find(Item.class, id);
    }

    @Override
    public int insertItem(Item item) {
        entityManager.persist(item);
        entityManager.flush();

        return item.getId();
    }

    @Override
    public Item updateItem(Item item) {
        Item entity = entityManager.find(Item.class, item.getId());

        if (entity != null)
        {
            if (item.getCreatedBy() > 0)
                entity.setCreatedBy(item.getCreatedBy());

            if (item.getLabel() != null)
                entity.setLabel(item.getLabel());

            if (item.getParentItem() != null)
                entity.setParentItem(item.getParentItem());

            if (item.getAssignedTo() > 0)
                entity.setAssignedTo(item.getAssignedTo());

            if (item.getCategory() != null)
                entity.setCategory(item.getCategory());

            if (item.getLabel() != null)
                entity.setLabel(item.getLabel());

            if (item.getTitle() != null)
                entity.setTitle(item.getTitle());

            if (item.getDescription() != null)
                entity.setDescription(item.getDescription());

            if (item.getStatus() != null)
                entity.setStatus(item.getStatus());

            if (item.getPriority() != null)
                entity.setPriority(item.getPriority());

            if (item.getComments() != null)
                entity.setComments(item.getComments());

            return entity;
        }

        return null;
    }

    @Override
    public int deleteItem(int id) {
        Item entity = entityManager.find(Item.class, id);

        if (entity != null)
        {
            entityManager.remove(entity);
            return 1;
        }

        return 0;
    }
}
