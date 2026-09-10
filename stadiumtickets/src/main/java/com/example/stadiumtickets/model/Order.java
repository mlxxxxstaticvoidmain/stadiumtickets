package com.example.stadiumtickets.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "Orders")
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @NotNull(message = "Пользователь обязателен")
    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;
    
    @NotNull(message = "Билет обязателен")
    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = false, unique = true)
    private Ticket ticket;
    
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
    
    @PastOrPresent(message = "Дата заказа не может быть в будущем")
    private LocalDateTime orderDate;
    
    @NotBlank(message = "Способ оплаты обязателен")
    @Pattern(regexp = "card|cash", message = "Способ оплаты должен быть: card или cash")
    private String paymentMethod;
    
    @PositiveOrZero(message = "Сумма не может быть отрицательной")
    private double totalAmount;
    
    @Transient
    private String accountName;
    
    @Transient
    private String ticketInfo;
    
    @Transient
    private String employeeName;
    
    public Order() {
        this.orderDate = LocalDateTime.now();
    }
    
    public Order(int id, Account account, Ticket ticket, Employee employee, 
                 LocalDateTime orderDate, String paymentMethod, double totalAmount) {
        this.id = id;
        this.account = account;
        this.ticket = ticket;
        this.employee = employee;
        this.orderDate = orderDate;
        this.paymentMethod = paymentMethod;
        this.totalAmount = totalAmount;
    }
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }
    
    public Ticket getTicket() { return ticket; }
    public void setTicket(Ticket ticket) { this.ticket = ticket; }
    
    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }
    
    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
    
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    
    public String getAccountName() { return accountName; }
    public void setAccountName(String accountName) { this.accountName = accountName; }
    
    public String getTicketInfo() { return ticketInfo; }
    public void setTicketInfo(String ticketInfo) { this.ticketInfo = ticketInfo; }
    
    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }
    
    public String getPaymentMethodRu() {
        return "card".equals(paymentMethod) ? "Карта" : "Наличные";
    }
}