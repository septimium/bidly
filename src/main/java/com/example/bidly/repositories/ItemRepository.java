package com.example.bidly.repositories;

import com.example.bidly.entity.AppUser;
import com.example.bidly.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {

    List<Item> findByOwner(AppUser user);
    List<Item> findByWinner(int winnerId);

}
