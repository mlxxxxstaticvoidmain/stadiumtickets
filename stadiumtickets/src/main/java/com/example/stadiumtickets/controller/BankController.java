package com.example.stadiumtickets.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.stadiumtickets.model.Bank;
import com.example.stadiumtickets.service.BankService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin/banks")
public class BankController {

    @Autowired
    private BankService bankService;

    @GetMapping
    public String listBanks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) String bankName,
            Model model) {
        
        Page<Bank> bankPage;
        
        if (id != null) {
            Bank bank = bankService.findById(id);
            List<Bank> bankList = bank != null ? List.of(bank) : List.of();
            bankPage = new PageImpl<>(bankList);
            model.addAttribute("search", "ID: " + id);
        } else if (bankName != null && !bankName.isEmpty()) {
            bankPage = bankService.findByBankName(bankName, page, size);
            model.addAttribute("search", "Название: " + bankName);
        } else {
            bankPage = bankService.findAll(page, size);
        }
        
        model.addAttribute("banks", bankPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", bankPage.getTotalPages());
        model.addAttribute("totalItems", bankPage.getTotalElements());
        model.addAttribute("size", size);
        
        return "admin/banks/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("bank", new Bank());
        return "admin/banks/add";
    }

    @PostMapping("/add")
    public String addBank(@Valid @ModelAttribute Bank bank, 
                          BindingResult result) {
        if (result.hasErrors()) {
            return "admin/banks/add";
        }
        bankService.save(bank);
        return "redirect:/admin/banks";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Bank bank = bankService.findById(id);
        if (bank != null) {
            model.addAttribute("bank", bank);
            return "admin/banks/edit";
        }
        return "redirect:/admin/banks";
    }

    @PostMapping("/edit")
    public String updateBank(@Valid @ModelAttribute Bank bank,
                             BindingResult result) {
        if (result.hasErrors()) {
            return "admin/banks/edit";
        }
        bankService.save(bank);
        return "redirect:/admin/banks";
    }

    @GetMapping("/delete/{id}")
    public String deleteBank(@PathVariable int id) {
        bankService.deleteBank(id);
        return "redirect:/admin/banks";
    }
}