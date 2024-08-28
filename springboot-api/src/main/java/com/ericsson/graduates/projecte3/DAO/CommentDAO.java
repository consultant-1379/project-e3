package com.ericsson.graduates.projecte3.DAO;


import com.ericsson.graduates.projecte3.DTO.Comment;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

public interface CommentDAO {
    Collection<Comment> getAllCommentsForAnItem(int itemID);

    Comment getComment(int id);

    @Transactional
    int insertComment(Comment comment);

    @Transactional
    Comment updateComment(Comment comment);

    @Transactional
    int deleteComment(int comment);
}
