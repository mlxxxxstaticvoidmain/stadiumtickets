package com.example.stadiumtickets.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.stadiumtickets.model.Role;
import com.example.stadiumtickets.service.RoleService;

@Controller
@RequestMapping("/admin/roles")
@PreAuthorize("hasAuthority('Администратор')")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    public String listRoles(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String name,
            Model model) {
        
        Page<Role> rolePage;
        
        if (id != null) {
            Role role = roleService.findById(id);
            List<Role> roleList = role != null ? List.of(role) : List.of();
            rolePage = new PageImpl<>(roleList);
            model.addAttribute("search", "ID: " + id);
        } else if (name != null && !name.isEmpty()) {
            rolePage = roleService.findByName(name, page, size);
            model.addAttribute("search", "Название: " + name);
        } else {
            rolePage = roleService.findAll(page, size);
        }
        
        model.addAttribute("roles", rolePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", rolePage.getTotalPages());
        model.addAttribute("totalItems", rolePage.getTotalElements());
        model.addAttribute("size", size);
        
        return "admin/roles/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("role", new Role());
        return "admin/roles/add";
    }

    @PostMapping("/add")
    public String addRole(@RequestParam String name,
                          @RequestParam(required = false) String permissions) {
        Role role = new Role();
        role.setName(name);
        role.setPermissions(permissions != null ? permissions : "");
        roleService.save(role);
        return "redirect:/admin/roles";
    }

        @GetMapping("/new")
    public String showAddFormNew(Model model) {
        model.addAttribute("role", new Role());
        return "admin/roles/add";
    }
    
    @PostMapping("/new")
    public String addRoleNew(@RequestParam String name,
                            @RequestParam(required = false) String permissions) {
        Role role = new Role();
        role.setName(name);
        role.setPermissions(permissions != null ? permissions : "");
        roleService.save(role);
        return "redirect:/admin/roles";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Role role = roleService.findById(id);
        model.addAttribute("role", role);
        return "admin/roles/edit";
    }

    @PostMapping("/edit")
    public String updateRole(@RequestParam int id, 
                             @RequestParam String name,
                             @RequestParam(required = false) String permissions) {
        Role role = roleService.findById(id);
        role.setName(name);
        role.setPermissions(permissions != null ? permissions : "");
        roleService.save(role);
        return "redirect:/admin/roles";
    }

    @GetMapping("/delete/{id}")
    public String deleteRole(@PathVariable int id, Model model) {
        boolean canDelete = roleService.canDeleteRole(id);
        if (!canDelete) {
            String accounts = roleService.getAccountsWithRole(id);
            model.addAttribute("error", "Невозможно удалить роль, она используется в аккаунтах: " + accounts);
            Page<Role> rolePage = roleService.findAll(0, 10);
            model.addAttribute("roles", rolePage.getContent());
            model.addAttribute("totalPages", rolePage.getTotalPages());
            return "admin/roles/list";
        }
        roleService.deleteRole(id);
        return "redirect:/admin/roles";
    }
}