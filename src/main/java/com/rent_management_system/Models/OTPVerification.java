package com.rent_management_system.Models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "otp_tb")
public class OTPVerification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;
    @Column
    public int otp;
    @Column
    public Date createdAt;
    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    public boolean status;
    @OneToOne
    @JoinColumn(name = "user_id")
    public User user;
}
