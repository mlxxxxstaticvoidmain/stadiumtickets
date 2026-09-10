package com.example.stadiumtickets.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

@Entity
@Table(name = "Cart", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"account_id", "ticket_id"})
})
public class Cart {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    @NotNull(message = "Пользователь обязателен")
    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;
    
    @NotNull(message = "Билет обязателен")
    @OneToOne
    @JoinColumn(name = "ticket_id", nullable = false, unique = true)
    private Ticket ticket;
    
    @PastOrPresent(message = "Дата добавления не может быть в будущем")
    private LocalDateTime addedAt;
    
    @Transient
    private String accountName;
    
    @Transient
    private String ticketInfo;
    
    public Cart() {
        this.addedAt = LocalDateTime.now();
    }
    
    public Cart(int id, Account account, Ticket ticket, LocalDateTime addedAt) {
        this.id = id;
        this.account = account;
        this.ticket = ticket;
        this.addedAt = addedAt;
    }
    
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public Account getAccount() { return account; }
    public void setAccount(Account account) { this.account = account; }
    
    public Ticket getTicket() { return ticket; }
    public void setTicket(Ticket ticket) { this.ticket = ticket; }
    
    public LocalDateTime getAddedAt() { return addedAt; }
    public void setAddedAt(LocalDateTime addedAt) { this.addedAt = addedAt; }
    
    public String getAccountName() { return accountName; }
    public void setAccountName(String accountName) { this.accountName = accountName; }
    
    public String getTicketInfo() { return ticketInfo; }
    public void setTicketInfo(String ticketInfo) { this.ticketInfo = ticketInfo; }
}