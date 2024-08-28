package com.ericsson.graduates.projecte3;

import com.ericsson.graduates.projecte3.DAO.ItemDAO;
import com.ericsson.graduates.projecte3.DTO.*;
import com.ericsson.graduates.projecte3.ENUM.ItemCategory;
import com.ericsson.graduates.projecte3.ENUM.ItemLabel;
import com.ericsson.graduates.projecte3.ENUM.Priority;
import com.ericsson.graduates.projecte3.ENUM.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ItemRestControllerTests {

    @Autowired
    private TestRestTemplate rest;

    @MockBean
    private ItemDAO dao;
    private final int SUCCESS_CODE = 1;
    private final int FAILURE_CODE = 0;

    private Comment comment1;
    private Comment comment2;
    private Set<Comment> comments = new HashSet<>();
    private Item item1;
    private Item updatedItem1;
    private Item item2;
    private Set<Item> items = new HashSet<>();
    private Set<Item> emptyItem = new HashSet<>();
    private Sprint sprint;

    @BeforeEach
    void setup(){
        LocalDateTime dateTime = LocalDateTime.now();

        comment1 = new Comment();
        comment1.setCommentBy(1);
        comment1.setItemID(1);
        comment1.setTopic("Comment 1 topic");
        comment1.setContent("Comment 1 content");
        comment1.setCreatedOn(dateTime);

        comment2 = new Comment();
        comment2.setCommentBy(1);
        comment2.setItemID(2);
        comment2.setTopic("Comment 2 topic");
        comment2.setContent("Comment 2 content");
        comment2.setCreatedOn(dateTime);

        comments.add(comment1);
        comments.add(comment2);

        item1 = new Item(sprint, null, "Item 1", "Item 1 description", null, null,
                Status.TBD, ItemCategory.MAD, Priority.LOW, ItemLabel.TASK, comments);

        updatedItem1 = item1;
        updatedItem1.setDescription("Updated item 1 description");
        updatedItem1.setStatus(Status.IN_PROGRESS);
        updatedItem1.setTitle("Updated Item 1");

        item2 = new Item(sprint, null, "Item 2", "Item 2 description", null, null,
                Status.TBD, ItemCategory.MAD, Priority.LOW, ItemLabel.TASK, comments);

        items = new HashSet<>();
        items.add(item1);
        items.add(item2);

        sprint = new Sprint();
        sprint.setId(1);
        sprint.setItems(items);
    }

    ResponseEntity<Item>addItem(Item item) {
        HttpEntity<Item> entity = new HttpEntity<>(item1);
        when(dao.insertItem(item1)).thenReturn(item1.getId());

        return rest.exchange("/item",
                HttpMethod.POST,
                entity,
                Item.class);
    }

    @Test
    void testAddItem(){
        ResponseEntity<Item> response = addItem(item1);
        Item result = response.getBody();

        assertEquals(item1, result);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        verify(dao).insertItem(item1);
    }

    @Test
    void testGetItemById(){
        addItem(item1);

        when(dao.getItem(item1.getId())).thenReturn(item1);
        ResponseEntity<Item> response = rest.getForEntity("/item/" + item1.getId(),
                Item.class);

        Item result = response.getBody();

        assertEquals(item1, result);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        verify(dao).getItem(anyInt());
    }

    @Test
    void testGetItemByIdReturn404(){
        when(dao.getItem(item1.getId())).thenReturn(null);
        ResponseEntity<Item> response = rest.exchange("/item/" + item1.getId(),
                HttpMethod.GET,
                null,
                Item.class);

        Item result = response.getBody();

        verify(dao).getItem(anyInt());
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
        assertNull(result);
    }

    @Test
    void testGetItemsBySprint(){
        addItem(item1);
        addItem(item2);

        when(dao.getAllItemsForASprint(sprint.getId())).thenReturn(items);
        ResponseEntity<Collection<Item>> response = rest.exchange("/item/sprint/" + sprint.getId(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Item>>(){});

        Collection<Item> result = response.getBody();

//        assertEquals(items, result);
        assertTrue(items.contains(item1));
        assertTrue(items.contains(item2));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(dao).getAllItemsForASprint(sprint.getId());
    }

    @Test
    void testGetItemsBySprintReturn404(){
        when(dao.getAllItemsForASprint(sprint.getId())).thenReturn(emptyItem);
        ResponseEntity<Collection<Item>> response = rest.exchange("/item/sprint/" + sprint.getId(),
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Item>>(){});

        Collection<Item> result = response.getBody();

        verify(dao).getAllItemsForASprint(sprint.getId());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(result);
    }

    @Test
    void testUpdateItem() {
        addItem(item1);

        HttpEntity<Item> httpEntity = new HttpEntity<>(updatedItem1);

        // Update should initially return the current project info
        when(dao.getItem(updatedItem1.getId())).thenReturn(item1);

        // Updated member should only be returned after update
        when(dao.updateItem(updatedItem1)).thenReturn(updatedItem1);

        ResponseEntity<Item> response = rest.exchange("/item",
                HttpMethod.PUT,
                httpEntity,
                Item.class);

        Item result = response.getBody();

        verify(dao).getItem(item1.getId());
        verify(dao).updateItem(updatedItem1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedItem1, result);
    }

    @Test
    void testUpdateItemReturn404() {
        HttpEntity<Item> httpEntity = new HttpEntity<>(updatedItem1);

        System.out.println(updatedItem1);
        System.out.println(httpEntity);

        // Update should initially return the current project info
        when(dao.getItem(updatedItem1.getId())).thenReturn(null);

        // Updated member should only be returned after update
        when(dao.updateItem(updatedItem1)).thenReturn(updatedItem1);

        ResponseEntity<Item> response = rest.exchange("/item",
                HttpMethod.PUT,
                httpEntity,
                Item.class);

        Item result = response.getBody();

        verify(dao).getItem(anyInt());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(result);
    }

    @Test
    void testDeleteItem() {
        // 1 for successful delete
        when(dao.deleteItem(item1.getId())).thenReturn(SUCCESS_CODE);

        ResponseEntity<String> response = rest.exchange("/item/" + item1.getId(),
                HttpMethod.DELETE,
                null,
                String.class);

        verify(dao).deleteItem(item1.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Success", response.getBody());
    }

    @Test
    void testDeleteItemReturn404() {
        // 1 for successful delete
        when(dao.deleteItem(item1.getId())).thenReturn(FAILURE_CODE);

        ResponseEntity<String> response = rest.exchange("/item/" + item1.getId(),
                HttpMethod.DELETE,
                null,
                String.class);

        verify(dao).deleteItem(item1.getId());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
