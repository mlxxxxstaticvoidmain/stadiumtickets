package com.example.stadiumtickets.controller;

import com.example.stadiumtickets.model.Account;
import com.example.stadiumtickets.model.Role;
import com.example.stadiumtickets.service.AccountService;
import com.example.stadiumtickets.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/user-roles")
@PreAuthorize("hasAuthority('Администратор')")
public class AdminRoleController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private RoleService roleService;

    @GetMapping
    public String listAccountsByRole(
            @RequestParam(required = false) Integer roleId,
            Model model) {

        List<Account> accounts;
        if (roleId != null) {
            accounts = accountService.findByRoleId(roleId);
            model.addAttribute("selectedRoleId", roleId);
        } else {
            accounts = accountService.findAll();
        }

        for (Account account : accounts) {
            if (account.getRole() != null) {
                account.setRoleName(account.getRole().getName());
            }
        }

        model.addAttribute("accounts", accounts);
        model.addAttribute("roles", roleService.findAll());

        return "admin/user-roles/list";
    }

    @GetMapping("/{id}/edit")
    public String editRole(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
        Account account = accountService.findById(id);
        if (account == null) {
            redirectAttributes.addFlashAttribute("error", "Пользователь не найден!");
            return "redirect:/admin/user-roles";
        }
        model.addAttribute("account", account);
        model.addAttribute("roles", roleService.findAll());
        return "admin/user-roles/edit";
    }

    @PostMapping("/{id}/edit")
    public String updateRole(@PathVariable int id,
                             @RequestParam int roleId,
                             RedirectAttributes redirectAttributes) {

        Account account = accountService.findById(id);
        if (account == null) {
            redirectAttributes.addFlashAttribute("error", "Пользователь не найден!");
            return "redirect:/admin/user-roles";
        }

        Role newRole = roleService.findById(roleId);
        if (newRole == null) {
            redirectAttributes.addFlashAttribute("error", "Роль не найдена!");
            return "redirect:/admin/user-roles";
        }

        account.setRole(newRole);
        accountService.save(account);

        redirectAttributes.addFlashAttribute("success",
                "Роль пользователя '" + account.getUsername() + "' изменена на '" + newRole.getName() + "'");

        return "redirect:/admin/user-roles";
    }
}
