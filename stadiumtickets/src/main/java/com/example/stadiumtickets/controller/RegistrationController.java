package com.example.stadiumtickets.controller;

import com.example.stadiumtickets.model.Account;
import com.example.stadiumtickets.model.Role;
import com.example.stadiumtickets.service.AccountService;
import com.example.stadiumtickets.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class RegistrationController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String registrationPage(Model model) {
        List<Role> availableRoles = roleService.findAll().stream()
                .filter(r -> r.getName().equals("Пользователь"))
                .collect(Collectors.toList());
        model.addAttribute("roles", availableRoles);
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String email,
                           @RequestParam String firstName,
                           @RequestParam String lastName,
                           @RequestParam(required = false) String middleName,
                           @RequestParam String phoneNumber,
                           @RequestParam int roleId,
                           RedirectAttributes redirectAttributes) {

        System.out.println(">>> REGISTRATION ATTEMPT: " + username + ", roleId=" + roleId);

        if (accountService.findByUsername(username) != null) {
            System.out.println(">>> DUPLICATE USERNAME: " + username);
            redirectAttributes.addFlashAttribute("error", "Пользователь с таким логином уже существует!");
            return "redirect:/register";
        }

        if (accountService.findByEmail(email) != null) {
            System.out.println(">>> DUPLICATE EMAIL: " + email);
            redirectAttributes.addFlashAttribute("error", "Пользователь с таким email уже существует!");
            return "redirect:/register";
        }

        if (accountService.findByPhoneNumber(phoneNumber) != null) {
            System.out.println(">>> DUPLICATE PHONE: " + phoneNumber);
            redirectAttributes.addFlashAttribute("error", "Пользователь с таким номером телефона уже существует!");
            return "redirect:/register";
        }

        Role selectedRole = roleService.findById(roleId);
        if (selectedRole == null || !selectedRole.getName().equals("Пользователь")) {
            System.out.println(">>> INVALID ROLE: " + roleId);
            redirectAttributes.addFlashAttribute("error", "Нельзя выбрать эту роль!");
            return "redirect:/register";
        }

        System.out.println(">>> Using role: " + selectedRole.getName());

        try {
            Account account = new Account();
            account.setUsername(username);
            account.setPassword(passwordEncoder.encode(password));
            account.setEmail(email);
            account.setFirstName(firstName);
            account.setLastName(lastName);
            account.setMiddleName(middleName);
            account.setPhoneNumber(phoneNumber);
            account.setRole(selectedRole);
            account.setActive(true);
            account.setCreatedAt(LocalDateTime.now());

            System.out.println(">>> Saving account...");
            Account saved = accountService.save(account);
            System.out.println(">>> SUCCESS! Account id=" + saved.getId());

            redirectAttributes.addFlashAttribute("success", "Регистрация успешна! Войдите в систему.");

        } catch (Exception e) {
            System.out.println(">>> FAILED: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "Ошибка при регистрации: " + e.getMessage());
        }

        return "redirect:/login";
    }
}
