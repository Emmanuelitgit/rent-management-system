package com.rent_management_system.RentInfo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.rent_management_system.Apartment.Apartment;
import com.rent_management_system.User.User;
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
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
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
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
}