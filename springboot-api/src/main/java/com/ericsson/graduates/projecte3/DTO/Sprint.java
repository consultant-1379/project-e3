package com.ericsson.graduates.projecte3.DTO;

import com.ericsson.graduates.projecte3.ENUM.Status;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id",
        scope = Sprint.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Sprint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int projectID;

    @ManyToOne
    private Project project;

    private Date startDate;

    private Date endDate;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "sprint", cascade = CascadeType.ALL)
//    @JoinTable(name = "sprint_items",
//            joinColumns = @JoinColumn(name = "sprint_id"),
//            inverseJoinColumns = @JoinColumn(name = "item_id"))
//    @Fetch(FetchMode.JOIN)
    @JsonIgnoreProperties({"sprint"})
    private Set<Item> items = new LinkedHashSet<>();

    private Status status;

    public Sprint() {
    }

    public Sprint(Project project, Date startDate, Date endDate, Set<Item> items, Status status) {
        this.project = project;
        this.startDate = startDate;
        this.endDate = endDate;
        this.items = items;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProjectID() {
        return projectID;
    }

    public void setProjectID(int projectID) {
        this.projectID = projectID;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.projectID = project.getId();
        this.project = project;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Set<Item> getItems() {
        return items;
    }

    public void setItems(Set<Item> items) {
        this.items = items;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Sprint{" +
                "id=" + id +
                ", project=" + project +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", items=" + items +
                ", status=" + status +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sprint sprint = (Sprint) o;
        return id == sprint.id && Objects.equals(project, sprint.project) && Objects.equals(startDate, sprint.startDate) && Objects.equals(endDate, sprint.endDate) && Objects.equals(items, sprint.items) && status == sprint.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, project, startDate, endDate, items, status);
    }
}
