package com.example.bidly.controllers;

import com.example.bidly.entity.AppUser;
import com.example.bidly.entity.Item;
import com.example.bidly.repositories.AppUserRepository;
import com.example.bidly.repositories.ItemRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ItemController {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private AppUserRepository appUserRepository;


    @GetMapping("/item/{id}")
    public String showItemDetails(@PathVariable("id") int id, HttpSession session, Model model) {
        Object user = session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid item ID: " + id));
        model.addAttribute("item", item);

        if (item.getWinner() != 0) {
            try {
                int winnerId = item.getWinner();
                AppUser winner = appUserRepository.findById(winnerId)
                        .orElse(null);
                model.addAttribute("winnerUsername", winner != null ? winner.getUsername() : "Unknown User");
            } catch (NumberFormatException e) {
                model.addAttribute("winnerUsername", "Invalid User ID");
            }
        } else {
            model.addAttribute("winnerUsername", "No Bidder");
        }
        return "item_details";
    }


    @PostMapping("/item/{id}")
    public String placeBid(@PathVariable("id") int id,
                           @RequestParam("bidAmount") double bidAmount,
                           HttpSession session, Model model) {
        AppUser user = (AppUser) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid item ID: " + id));

        if (bidAmount <= item.getStartingBid()) {
            model.addAttribute("item", item);
            model.addAttribute("error", "Your bid must be higher than the current bid of $" + item.getStartingBid());

            String winnerUsername = "No Bidder";
            if (item.getWinner() != 0) {
                AppUser winner = appUserRepository.findById(item.getWinner()).orElse(null);
                winnerUsername = (winner != null) ? winner.getUsername() : "Unknown User";
            }
            model.addAttribute("winnerUsername", winnerUsername);

            return "item_details";
        }

        item.setStartingBid(bidAmount);
        item.setWinner(user.getId());
        itemRepository.save(item);

        return "redirect:/item/" + id;
    }


    @PostMapping("/item/{id}/toggle-approval")
    public String toggleApproval(@PathVariable("id") int id, HttpSession session, Model model) {
        AppUser user = (AppUser) session.getAttribute("user");

        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid item ID: " + id));
        item.setApproved(!item.isApproved());
        itemRepository.save(item);

        return "redirect:/panel";
    }
    @PostMapping("/item/{id}/delete")
    public String delete(@PathVariable("id") int id, HttpSession session) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid item ID: " + id));
        itemRepository.delete(item);
        return "redirect:/panel";
    }

    @GetMapping("/profile")
    public String userProfile(HttpSession session, Model model) {
        AppUser user = (AppUser) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        List<Item> userWinningBids = itemRepository.findByWinner(user.getId());
        List<Item> userListedItems = itemRepository.findByOwner(user);

        model.addAttribute("userWinningBids", userWinningBids);
        model.addAttribute("userListedItems", userListedItems);

        return "profile";
    }


}