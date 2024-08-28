package com.ericsson.graduates.projecte3.DAO;

import com.ericsson.graduates.projecte3.DTO.Item;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

public interface ItemDAO {
    Collection<Item> getAllItemsForASprint(int sprintID);

    Item getItem(int id);

    @Transactional
    int insertItem(Item item);

    @Transactional
    Item updateItem(Item item);

    @Transactional
    int deleteItem(int id);
}
