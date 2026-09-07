package com.example.stadiumtickets.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.stadiumtickets.model.Employee;
import com.example.stadiumtickets.service.AccountService;
import com.example.stadiumtickets.service.BankService;
import com.example.stadiumtickets.service.EmployeeService;

@Controller
@RequestMapping("/admin/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private BankService bankService;

    @GetMapping
    public String listEmployees(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Integer id,
            @RequestParam(required = false) Integer bankId,
            Model model) {
        
        Page<Employee> employeePage;
        
        if (id != null) {
            Employee employee = employeeService.findById(id);
            List<Employee> employeeList = employee != null ? List.of(employee) : List.of();
            employeePage = new PageImpl<>(employeeList);
            model.addAttribute("search", "ID: " + id);
            model.addAttribute("searchId", id);
        } else if (bankId != null) {
            employeePage = employeeService.findByBankId(bankId, page, size);
            String bankName = bankService.findById(bankId) != null ? 
                              bankService.findById(bankId).getBankName() : String.valueOf(bankId);
            model.addAttribute("search", "Банк: " + bankName);
            model.addAttribute("searchBankId", bankId);
        } else {
            employeePage = employeeService.findAll(page, size);
        }
        
        List<Employee> employees = employeePage.getContent();
        for (Employee employee : employees) {
            int accountId = employee.getAccount() != null ? employee.getAccount().getId() : -1;
            if (accountId > 0 && accountService.findById(accountId) != null) {
                var account = accountService.findById(employee.getAccount() != null ? employee.getAccount().getId() : -1);
                employee.setEmployeeName(account.getFirstName() + " " + account.getLastName());
            }
            int empBankId = employee.getBank() != null ? employee.getBank().getId() : -1;
            if (empBankId > 0 && bankService.findById(empBankId) != null) {
                employee.setBankName(bankService.findById(empBankId).getBankName());
            }
        }
        
        model.addAttribute("employees", employees);
        model.addAttribute("banks", bankService.findAll());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", employeePage.getTotalPages());
        model.addAttribute("totalItems", employeePage.getTotalElements());
        model.addAttribute("size", size);
        
        return "admin/employees/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute("accounts", accountService.findAll());
        model.addAttribute("banks", bankService.findAll());
        return "admin/employees/add";
    }

    @PostMapping("/add")
    public String addEmployee(@RequestParam int accountId,
                              @RequestParam int bankId,
                              @RequestParam String hireDate,
                              @RequestParam double salary,
                              RedirectAttributes redirectAttributes) {
        
        try {
            if (accountService.findById(accountId) != null) {
                redirectAttributes.addFlashAttribute("error", "Сотрудник с таким аккаунтом уже существует!");
                return "redirect:/admin/employees/add";
            }
            
            Employee employee = new Employee();
            employee.setAccount(accountService.findById(accountId));
            employee.setBank(bankService.findById(bankId));
            employee.setHireDate(LocalDate.parse(hireDate));
            employee.setSalary(salary);
            
            employeeService.save(employee);
            redirectAttributes.addFlashAttribute("success", "Сотрудник успешно добавлен!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при добавлении: " + e.getMessage());
        }
        
        return "redirect:/admin/employees";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model, RedirectAttributes redirectAttributes) {
        Employee employee = employeeService.findById(id);
        if (employee == null) {
            redirectAttributes.addFlashAttribute("error", "Сотрудник не найден!");
            return "redirect:/admin/employees";
        }
        
        model.addAttribute("employee", employee);
        model.addAttribute("accounts", accountService.findAll());
        model.addAttribute("banks", bankService.findAll());
        return "admin/employees/edit";
    }

    @PostMapping("/edit")
    public String updateEmployee(@RequestParam int id,
                                 @RequestParam int accountId,
                                 @RequestParam int bankId,
                                 @RequestParam String hireDate,
                                 @RequestParam double salary,
                                 RedirectAttributes redirectAttributes) {
        
        try {
            Employee employee = employeeService.findById(id);
            if (employee == null) {
                redirectAttributes.addFlashAttribute("error", "Сотрудник не найден!");
                return "redirect:/admin/employees";
            }
            
            employee.setAccount(accountService.findById(accountId));
            employee.setBank(bankService.findById(bankId));
            employee.setHireDate(LocalDate.parse(hireDate));
            employee.setSalary(salary);
            
            employeeService.save(employee);
            redirectAttributes.addFlashAttribute("success", "Сотрудник успешно обновлен!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при обновлении: " + e.getMessage());
        }
        
        return "redirect:/admin/employees";
    }

    @PostMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable int id,
                                 @RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "10") int size,
                                 @RequestParam(required = false) Integer searchId,
                                 @RequestParam(required = false) Integer searchBankId,
                                 RedirectAttributes redirectAttributes) {
        
        try {
            employeeService.deleteEmployee(id);
            redirectAttributes.addFlashAttribute("success", "Сотрудник успешно удален!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при удалении: " + e.getMessage());
        }
        
        StringBuilder redirect = new StringBuilder("redirect:/admin/employees?page=" + page + "&size=" + size);
        if (searchId != null) {
            redirect.append("&id=").append(searchId);
        }
        if (searchBankId != null) {
            redirect.append("&bankId=").append(searchBankId);
        }
        
        return redirect.toString();
    }
}