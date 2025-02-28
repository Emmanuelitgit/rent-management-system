package com.rent_management_system.fileManager;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rent_management_system.apartment.Apartment;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "apartmentFile_tb")
public class ApartmentFile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column
    private Long id;
    @Column
    public String fileName;
    @Column
    public String fileId;
    @Column
    public String fileType;
    @ManyToOne
    @JoinColumn(name = "apartment_id")
    @JsonIgnore
    public Apartment apartment;

    @PrePersist
    public void prePersist() {
        this.apartment = getApartment();

    }
}
