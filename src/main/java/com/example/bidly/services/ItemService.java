package com.example.bidly.services;

import com.example.bidly.entity.AppUser;
import com.example.bidly.entity.Item;

import java.util.List;

public interface ItemService {
    Item addItem(Item item, AppUser owner);
    List<Item> getItemsByUser(AppUser user);
}
