package com.rent_management_system.Authentiication;

import com.rent_management_system.User.User;
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
public class OTP {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;
    @Column
    public Long otp;
    @Column
    public Date createdAt;
    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    public boolean status;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
