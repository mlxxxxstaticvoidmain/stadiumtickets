package com.example.stadiumtickets.controller;

import com.example.stadiumtickets.model.Account;
import com.example.stadiumtickets.model.Role;
import com.example.stadiumtickets.service.AccountService;
import com.example.stadiumtickets.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
}
