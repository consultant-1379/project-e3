package com.ericsson.graduates.projecte3.controllers;

import com.ericsson.graduates.projecte3.DAO.ItemDAO;
import com.ericsson.graduates.projecte3.DAO.MemberDAO;
import com.ericsson.graduates.projecte3.DAO.SprintDAO;
import com.ericsson.graduates.projecte3.DTO.Item;
import com.ericsson.graduates.projecte3.DTO.Member;
import com.ericsson.graduates.projecte3.DTO.Sprint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@CrossOrigin
@RestController
@RequestMapping("/item")
public class ItemRestController {
    @Autowired
    private ItemDAO dao;

    @Autowired
    private SprintDAO sprintDAO;

    @Autowired
    private MemberDAO memberDAO;

    @PostMapping()
    public Item create(@RequestBody Item item){
        // If only the id was sent, map the correct sprint
        if (item.getSprintID() > 0 && item.getSprint() == null)
        {
            Sprint sprint = sprintDAO.getSprint(item.getSprintID());
            item.setSprint(sprint);
        }

        // If only the id was sent, map the correct member
        if (item.getAssignedTo() > 0 && item.getMemberAssignedTo() == null)
        {
            Member assignedTo = memberDAO.getMember(item.getAssignedTo());
            item.setMemberAssignedTo(assignedTo);
        }

        // If only the id was sent, map the correct member
        if (item.getCreatedBy() > 0 && item.getMemberCreatedBy() == null)
        {
            Member createdBy = memberDAO.getMember(item.getCreatedBy());
            item.setMemberCreatedBy(createdBy);
        }

        int id = dao.insertItem(item);
        item.setId(id);

        return item;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Item> getById(@PathVariable int id){
        Item i = dao.getItem(id);
        if(i != null)
            return ResponseEntity.ok().body(i);
        else
            return ResponseEntity.notFound().build();
    }

    @GetMapping("/sprint/{sprintId}")
    public ResponseEntity<Collection<Item>> getBySprint(@PathVariable int sprintId)
    {
        Collection<Item> items = dao.getAllItemsForASprint(sprintId);
        if(items.isEmpty())
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok().body(items);
    }

    @PutMapping()
    public ResponseEntity<Item> update(@RequestBody Item item){
        Item i = dao.getItem(item.getId());
        if(i!=null)
            return ResponseEntity.ok().body(dao.updateItem(item));
        else
            return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable int id){
        int status = dao.deleteItem(id);

        if(status == 0)
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok().body("Success");
    }

}
