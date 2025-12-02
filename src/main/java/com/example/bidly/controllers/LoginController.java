package com.example.bidly.controllers;

import com.example.bidly.entity.AppUser;
import com.example.bidly.services.AppUserService;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private AppUserService appUserService;

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@RequestParam String email,
                               @RequestParam String password,
                               Model model,
                               HttpSession session) {
        AppUser user = appUserService.authenticateUser(email, password);
        if (user != null) {
            session.setAttribute("user", user);
            return "redirect:/?loginSuccess=true";
        } else {
            model.addAttribute("error", "Invalid email or password.");
            return "login";
        }
    }

}
