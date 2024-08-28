package com.ericsson.graduates.projecte3.DTO;

import com.ericsson.graduates.projecte3.ENUM.ItemCategory;
import com.ericsson.graduates.projecte3.ENUM.ItemLabel;
import com.ericsson.graduates.projecte3.ENUM.Priority;
import com.ericsson.graduates.projecte3.ENUM.Status;
import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
//        property = "id",
//        scope = Item.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int sprintID;

    @ManyToOne(optional = false)
    @JoinColumn(name = "sprint_id", nullable = false)
    @JsonIgnoreProperties("items")
    private Sprint sprint;

    @ManyToOne
    @JoinColumn
    @JsonBackReference(value = "parentItem")
    private Item parentItem;

    private String title;
    private String description;

    private int assignedTo;

    @ManyToOne
    @JoinColumn
//    @JsonBackReference(value = "memberAssignedTo")
    @JsonIgnoreProperties("team")
    private Member memberAssignedTo;

    private int createdBy;

    @ManyToOne
    @JoinColumn
//    @JsonBackReference(value = "memberCreatedBy")
    @JsonIgnoreProperties("team")
    private Member memberCreatedBy;

    private Status status;
    private ItemCategory category;
    private Priority priority;
    private ItemLabel label;
    
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "item")
    @JsonIgnoreProperties("item")
    private Set<Comment> comments = new HashSet<>();

    public Item(@JsonProperty Sprint sprint, @JsonProperty Item parentItem, @JsonProperty String title,
                @JsonProperty String description, @JsonProperty Member assignedTo, @JsonProperty Member createdBy,
                @JsonProperty Status status, @JsonProperty ItemCategory category, @JsonProperty Priority priority,
                @JsonProperty ItemLabel label) {
        this.sprint = sprint;
        this.parentItem = parentItem;
        this.title = title;
        this.description = description;
        this.memberAssignedTo = assignedTo;
        this.memberCreatedBy = createdBy;
        this.status = status;
        this.category = category;
        this.priority = priority;
        this.label = label;
    }

    public Item(@JsonProperty Sprint sprint, @JsonProperty Item parentItem, @JsonProperty String title,
                @JsonProperty String description, @JsonProperty Member assignedTo, @JsonProperty Member createdBy,
                @JsonProperty Status status, @JsonProperty ItemCategory category, @JsonProperty Priority priority,
                @JsonProperty ItemLabel label, @JsonProperty Set<Comment> comments) {
        this.sprint = sprint;
        this.parentItem = parentItem;
        this.title = title;
        this.description = description;
        this.memberAssignedTo = assignedTo;
        this.memberCreatedBy = createdBy;
        this.status = status;
        this.category = category;
        this.priority = priority;
        this.label = label;
        this.comments = comments;
    }

    public Item() {

    }

    public int getId() {
        return id;
    }

    public int getSprintID() {
        return sprintID;
    }

    public void setSprintID(int sprintID) {
        this.sprintID = sprintID;
    }

    public Item getParentItem() {
        return parentItem;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getAssignedTo() {
        return assignedTo;
    }

    public Member getMemberAssignedTo() {
        return memberAssignedTo;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public Member getMemberCreatedBy() {
        return memberCreatedBy;
    }

    public Status getStatus() {
        return status;
    }

    public ItemCategory getCategory() {
        return category;
    }

    public Priority getPriority() {
        return priority;
    }

    public ItemLabel getLabel() {
        return label;
    }
    public Sprint getSprint() {return sprint;}

    public Set<Comment> getComments() {return comments;}

    public void setId(int id) {
        this.id = id;
    }

    public void setParentItem(Item parentItem) {
        this.parentItem = parentItem;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAssignedTo(int assignedTo) {
        this.assignedTo = assignedTo;
    }

    public void setMemberAssignedTo(Member memberAssignedTo) {
        if (this.memberAssignedTo == null)
            this.assignedTo = memberAssignedTo.getId();
        else
            this.assignedTo = 0;

        this.memberAssignedTo = memberAssignedTo;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public void setMemberCreatedBy(Member memberCreatedBy) {
        if (this.memberCreatedBy == null)
            this.createdBy = memberCreatedBy.getId();
        else
            this.createdBy = 0;

        this.memberCreatedBy = memberCreatedBy;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setCategory(ItemCategory category) {
        this.category = category;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public void setLabel(ItemLabel label) {
        this.label = label;
    }

    public void setSprint(Sprint sprint) {
        if (this.sprint == null)
            this.sprintID = sprint.getId();
        else
            this.sprintID = 0;

        this.sprint = sprint;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", sprintID=" + sprintID +
                ", sprint=" + sprint +
                ", parentItem=" + parentItem +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", assignedTo=" + assignedTo +
                ", createdBy=" + createdBy +
                ", status=" + status +
                ", category=" + category +
                ", priority='" + priority + '\'' +
                ", label=" + label +
                ", comments=" + comments +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return id == item.id && sprintID == item.sprintID && Objects.equals(parentItem, item.parentItem) && Objects.equals(title, item.title) && Objects.equals(description, item.description) && Objects.equals(assignedTo, item.assignedTo) && Objects.equals(createdBy, item.createdBy) && status == item.status && category == item.category && priority == item.priority && label == item.label && Objects.equals(comments, item.comments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sprintID, parentItem, title, description, assignedTo, createdBy, status, category, priority, label, comments);
    }
}
