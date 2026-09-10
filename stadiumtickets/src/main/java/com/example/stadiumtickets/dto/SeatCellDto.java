package com.example.stadiumtickets.dto;

import com.example.stadiumtickets.model.Seat;
import com.example.stadiumtickets.model.Ticket;

public class SeatCellDto {

    private final Seat seat;
    private final Ticket ticket;

    public SeatCellDto(Seat seat, Ticket ticket) {
        this.seat = seat;
        this.ticket = ticket;
    }

    public Seat getSeat() { return seat; }

    public Ticket getTicket() { return ticket; }

    public boolean isAvailable() {
        return ticket != null && "available".equals(ticket.getStatus());
    }

    public String getCssClass() {
        if (ticket == null) {
            return "seat-none";
        }
        String cssClass = "seat-" + ticket.getStatus();
        if (ticket.isVipAccess()) {
            cssClass += " seat-vip";
        }
        return cssClass;
    }

    public String getTooltip() {
        String place = "Ряд " + seat.getRowNumber() + ", место " + seat.getSeatNumber();
        if (ticket == null) {
            return place + " — не продаётся";
        }
        String status;
        switch (ticket.getStatus()) {
            case "available": status = "свободно"; break;
            case "reserved": status = "забронировано"; break;
            case "sold": status = "продано"; break;
            default: status = ticket.getStatus();
        }
        String vip = ticket.isVipAccess() ? " (VIP)" : "";
        return place + vip + " — " + ticket.getPrice() + " ₽ (" + status + ")";
    }
}
