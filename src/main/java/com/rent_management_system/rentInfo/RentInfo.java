package com.rent_management_system.rentInfo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rent_management_system.apartment.Apartment;
import com.rent_management_system.payment.Payment;
import com.rent_management_system.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rentInfo_tb")
public class RentInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "rented_apartment_tb",
            joinColumns = @JoinColumn(name = "rent_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<Apartment> apartment;
    @OneToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    public Payment payment;
}