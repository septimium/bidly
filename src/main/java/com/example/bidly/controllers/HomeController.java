package com.example.bidly.controllers;

import com.example.bidly.entity.Item;
import com.example.bidly.repositories.AppUserRepository;
import com.example.bidly.repositories.ItemRepository;
import jakarta.servlet.http.HttpSession;
import com.example.bidly.entity.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class HomeController {
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private AppUserRepository appUserRepository;

    @GetMapping("/")
    public String home(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);

        LocalDateTime now = LocalDateTime.now();
        Map<Integer, String> winnerUsernames = new HashMap<>();

        items.forEach(item -> {
            if (item.isActive() && item.getEndTime() != null && item.getEndTime().isBefore(now)) {
                item.setActive(false);
                itemRepository.save(item);
            }

            if (item.getWinner() != 0) {
                int winnerId = item.getWinner();
                AppUser winner = appUserRepository.findById(winnerId).orElse(null);
                winnerUsernames.put(item.getItemId(), winner != null ? winner.getUsername() : "Unknown User");
            } else {
                winnerUsernames.put(item.getItemId(), "No Bidder");
            }
        });

        model.addAttribute("winnerUsernames", winnerUsernames);

        return "home";
    }


    @GetMapping("/image/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Integer id) {
        Item item = itemRepository.findById(id).orElse(null);
        if (item != null && item.getImage() != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(item.getImage(), headers, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }

    @GetMapping("/sell")
    public String sellPage(HttpSession session) {
        Object user = session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        return "sell";
    }
    @PostMapping("/sell")
    public String postItem(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("startingBid") Double startingBid,
            @RequestParam("image") MultipartFile imageFile,
            @RequestParam("endTime") LocalDateTime endTime,
            HttpSession session) throws IOException {

        Item item = new Item();
        item.setName(name);
        item.setDescription(description);
        item.setStartingBid(startingBid);
        item.setEndTime(endTime);
        item.setActive(true);
        item.setApproved(false);

        AppUser user = (AppUser) session.getAttribute("user");
        if (user != null) {
            item.setOwner(user);
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            item.setImage(imageFile.getBytes());
        }

        itemRepository.save(item);
        return "redirect:/";
    }


    @GetMapping("/about")
    public String about() {
        return "about";
    }


    @GetMapping("/panel")
    public String panelPage(HttpSession session, Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        LocalDateTime now = LocalDateTime.now();
        items.forEach(item -> {
            if (item.isActive() && item.getEndTime() != null && item.getEndTime().isBefore(now)) {
                item.setActive(false);
                itemRepository.save(item);
            }
        });
        AppUser user = (AppUser) session.getAttribute("user");
        if (user != null && "admin".equals(user.getUsername())) {
            return "/panel";
        }
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

}