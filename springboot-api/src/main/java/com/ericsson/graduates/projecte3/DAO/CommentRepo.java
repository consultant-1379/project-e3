package com.ericsson.graduates.projecte3.DAO;

import com.ericsson.graduates.projecte3.DTO.Comment;
import com.ericsson.graduates.projecte3.DTO.Item;
import com.ericsson.graduates.projecte3.DTO.QComment;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Collection;

@Repository
public class CommentRepo implements CommentDAO {
    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Collection<Comment> getAllCommentsForAnItem(int itemID) {
        QComment comment = QComment.comment;
        JPAQuery<?> query = new JPAQuery<>(entityManager);

        JPAQuery<Comment> comments = query.select(comment)
                .from(comment)
                .where(comment.item.id.eq(itemID)).fetchAll();

        return comments.fetch();
    }

    @Override
    public Comment getComment(int id) {
        return entityManager.find(Comment.class, id);
    }

    @Override
    public int insertComment(Comment comment) {
        entityManager.persist(comment);
        entityManager.flush();

        return comment.getId();
    }

    @Override
    public Comment updateComment(Comment comment) {
        Comment entity = entityManager.find(Comment.class, comment.getId());

        if (entity != null)
        {
            if (comment.getCommentBy() > 0)
                entity.setCommentBy(comment.getCommentBy());

            if (comment.getContent() != null)
                entity.setContent(comment.getContent());

            if (comment.getTopic() != null)
                entity.setTopic(comment.getTopic());

            if (comment.getItemID() > 0)
                entity.setItemID(comment.getItemID());

            if (comment.getCreatedOn() != null)
                entity.setCreatedOn(comment.getCreatedOn());
            
            return entity;
        }

        return null;
    }

    @Override
    public int deleteComment(int id) {
        Comment entity = entityManager.find(Comment.class, id);

        if (entity != null)
        {
            entityManager.remove(entity);
            return 1;
        }

        return 0;
    }
}
