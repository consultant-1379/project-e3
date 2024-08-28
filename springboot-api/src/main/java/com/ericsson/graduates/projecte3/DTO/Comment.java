package com.ericsson.graduates.projecte3.DTO;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
//        property = "id",
//        scope = Comment.class)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int itemID;

    @ManyToOne(optional = false)
    @JoinColumn(name = "item_id", nullable = false)
    @JsonIgnoreProperties("comments")
    private Item item;

    private int commentBy;
    private String topic;
    private String content;
    private LocalDateTime createdOn;

    public Comment() {
    }

    public Comment(int id, int itemID, int commentBy, String topic, String content, LocalDateTime createdOn) {
        this.id = id;
        this.itemID = itemID;
        this.commentBy = commentBy;
        this.topic = topic;
        this.content = content;
        this.createdOn = createdOn;
    }

    public int getId() {
        return id;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.itemID = item.getId();
        this.item = item;
    }

    public int getItemID() {
        return itemID;
    }

    public int getCommentBy() {
        return commentBy;
    }

    public String getTopic() {
        return topic;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public void setCommentBy(int commentBy) {
        this.commentBy = commentBy;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", itemID=" + itemID +
                ", commentBy=" + commentBy +
                ", topic='" + topic + '\'' +
                ", content='" + content + '\'' +
                ", createdOn=" + createdOn +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Comment comment = (Comment) o;
        return id == comment.id && itemID == comment.itemID && commentBy == comment.commentBy && Objects.equals(topic, comment.topic) && Objects.equals(content, comment.content) && Objects.equals(createdOn, comment.createdOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, itemID, commentBy, topic, content, createdOn);
    }
}
