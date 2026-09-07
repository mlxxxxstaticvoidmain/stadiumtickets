package com.example.stadiumtickets.controller;

import com.example.stadiumtickets.model.Account;
import com.example.stadiumtickets.model.Role;
import com.example.stadiumtickets.service.AccountService;
import com.example.stadiumtickets.service.RoleService;
import com.example.stadiumtickets.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/admin/accounts")
@PreAuthorize("hasAuthority('Администратор')")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private BankService bankService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/new")
    public String redirectNewToAdd() {
        return "redirect:/admin/accounts/add";
    }

    @GetMapping
    public String listAccounts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            Model model) {

        Page<Account> accountPage;

        if (id != null) {
            Account account = accountService.findById(id);
            List<Account> accountList = account != null ? List.of(account) : List.of();
            accountPage = new PageImpl<>(accountList);
            model.addAttribute("search", "ID: " + id);
            model.addAttribute("searchId", id);
        } else if (username != null && !username.isEmpty()) {
            accountPage = accountService.findByUsername(username, page, size);
            model.addAttribute("search", "Username: " + username);
            model.addAttribute("searchUsername", username);
        } else if (email != null && !email.isEmpty()) {
            accountPage = accountService.findByEmail(email, page, size);
            model.addAttribute("search", "Email: " + email);
            model.addAttribute("searchEmail", email);
        } else {
            accountPage = accountService.findAll(page, size);
        }

        List<Account> accounts = accountPage.getContent();
        for (Account account : accounts) {
            if (account.getRole() != null) {
                account.setRoleName(account.getRole().getName());
            }
        }

        model.addAttribute("accounts", accounts);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", accountPage.getTotalPages());
        model.addAttribute("totalItems", accountPage.getTotalElements());
        model.addAttribute("size", size);
        model.addAttribute("roles", roleService.findAll());

        return "admin/accounts/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("account", new Account());
        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("banks", bankService.findAll());
        return "admin/accounts/add";
    }

    @PostMapping("/add")
    public String addAccount(@RequestParam String username,
                             @RequestParam String email,
                             @RequestParam String phoneNumber,
                             @RequestParam String firstName,
                             @RequestParam String lastName,
                             @RequestParam(required = false) String middleName,
                             @RequestParam String password,
                             @RequestParam int roleId,
                             @RequestParam(required = false) String bankName,
                             RedirectAttributes redirectAttributes) {

        if (accountService.findByUsername(username) != null) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: Имя пользователя '" + username + "' уже существует!");
            return "redirect:/admin/accounts/add";
        }

        if (accountService.findByEmail(email) != null) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: Email '" + email + "' уже существует!");
            return "redirect:/admin/accounts/add";
        }

        if (accountService.findByPhoneNumber(phoneNumber) != null) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: Номер телефона '" + phoneNumber + "' уже существует!");
            return "redirect:/admin/accounts/add";
        }

        try {
            Role role = roleService.findById(roleId);
            if (role == null) {
                redirectAttributes.addFlashAttribute("error", "Ошибка: Роль не найдена!");
                return "redirect:/admin/accounts/add";
            }

            Account account = new Account();
            account.setUsername(username);
            account.setEmail(email);
            account.setPhoneNumber(phoneNumber);
            account.setFirstName(firstName);
            account.setLastName(lastName);
            account.setMiddleName(middleName);
            account.setPassword(passwordEncoder.encode(password));
            account.setRole(role);
            account.setBankName(bankName);
            account.setCreatedAt(LocalDateTime.now());

            accountService.save(account);
            redirectAttributes.addFlashAttribute("success", "Пользователь успешно добавлен!");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при добавлении: " + e.getMessage());
        }

        return "redirect:/admin/accounts";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
        Account account = accountService.findById(id);
        if (account == null) {
            redirectAttributes.addFlashAttribute("error", "Пользователь не найден!");
            return "redirect:/admin/accounts";
        }
        model.addAttribute("account", account);
        model.addAttribute("roles", roleService.findAll());
        return "admin/accounts/edit";
    }

    @PostMapping("/edit")
    public String updateAccount(@RequestParam int id,
                                @RequestParam String username,
                                @RequestParam String email,
                                @RequestParam String phoneNumber,
                                @RequestParam String firstName,
                                @RequestParam String lastName,
                                @RequestParam(required = false) String middleName,
                                @RequestParam(required = false) String password,
                                @RequestParam int roleId,
                                @RequestParam(required = false) String bankName,
                                RedirectAttributes redirectAttributes) {

        Account existingAccount = accountService.findById(id);
        if (existingAccount == null) {
            redirectAttributes.addFlashAttribute("error", "Пользователь не найден!");
            return "redirect:/admin/accounts";
        }

        Account usernameCheck = accountService.findByUsername(username);
        if (usernameCheck != null && usernameCheck.getId() != id) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: Имя пользователя '" + username + "' уже существует!");
            return "redirect:/admin/accounts/edit/" + id;
        }

        Account emailCheck = accountService.findByEmail(email);
        if (emailCheck != null && emailCheck.getId() != id) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: Email '" + email + "' уже существует!");
            return "redirect:/admin/accounts/edit/" + id;
        }

        Account phoneCheck = accountService.findByPhoneNumber(phoneNumber);
        if (phoneCheck != null && phoneCheck.getId() != id) {
            redirectAttributes.addFlashAttribute("error", "Ошибка: Номер телефона '" + phoneNumber + "' уже существует!");
            return "redirect:/admin/accounts/edit/" + id;
        }

        try {
            Role role = roleService.findById(roleId);
            if (role == null) {
                redirectAttributes.addFlashAttribute("error", "Ошибка: Роль не найдена!");
                return "redirect:/admin/accounts/edit/" + id;
            }

            existingAccount.setUsername(username);
            existingAccount.setEmail(email);
            existingAccount.setPhoneNumber(phoneNumber);
            existingAccount.setFirstName(firstName);
            existingAccount.setLastName(lastName);
            existingAccount.setMiddleName(middleName);
            if (password != null && !password.isEmpty()) {
                existingAccount.setPassword(passwordEncoder.encode(password));
            }
            existingAccount.setRole(role);
            existingAccount.setBankName(bankName);

            accountService.save(existingAccount);
            redirectAttributes.addFlashAttribute("success", "Пользователь успешно обновлен!");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при обновлении: " + e.getMessage());
        }

        return "redirect:/admin/accounts";
    }

    @PostMapping("/delete/{id}")
    public String deleteAccount(@PathVariable int id,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size,
                                @RequestParam(required = false) Integer searchId,
                                @RequestParam(required = false) String searchUsername,
                                @RequestParam(required = false) String searchEmail,
                                RedirectAttributes redirectAttributes) {

        try {
            accountService.deleteAccount(id);
            redirectAttributes.addFlashAttribute("success", "Пользователь успешно удален!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при удалении: " + e.getMessage());
        }

        StringBuilder redirect = new StringBuilder("redirect:/admin/accounts?page=" + page + "&size=" + size);
        if (searchId != null) {
            redirect.append("&id=").append(searchId);
        }
        if (searchUsername != null && !searchUsername.isEmpty()) {
            redirect.append("&username=").append(searchUsername);
        }
        if (searchEmail != null && !searchEmail.isEmpty()) {
            redirect.append("&email=").append(searchEmail);
        }

        return redirect.toString();
    }

    @GetMapping("/delete/{id}")
    public String deleteAccountGet(@PathVariable int id, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "Используйте POST запрос для удаления!");
        return "redirect:/admin/accounts";
    }
}