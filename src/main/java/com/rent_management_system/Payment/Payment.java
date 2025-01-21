package com.rent_management_system.Payment;

import com.rent_management_system.Apartment.Apartment;
import com.rent_management_system.User.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;
    public int amount;
    public String status;
    public String currency;
    public String channel;
    public LocalDateTime transactionDate;
    public String reference;
    @ManyToOne
    @JoinColumn(name = "user_id")
    public User user;
    @OneToOne
    @JoinColumn(name = "apartment_id")
    public Apartment apartment;
}
