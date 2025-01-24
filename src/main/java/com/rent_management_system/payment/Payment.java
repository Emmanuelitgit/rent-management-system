package com.rent_management_system.payment;

import com.rent_management_system.rentInfo.RentInfo;
import com.rent_management_system.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.Data;

@Entity
@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;
    public int amount;
    public String status;
    public String currency;
    public String channel;
    public String transactionDate;
    public String reference;
    @ManyToOne
    @JoinColumn(name = "user_id")
    public User user;
    @OneToOne
    @JoinColumn(name = "rentInfo_id")
    public RentInfo rentInfo;
}
