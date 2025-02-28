package com.rent_management_system.fileManager;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.rent_management_system.apartment.Apartment;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MainFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileId;  // Google Drive File ID
    private String fileName;
    private String fileType;

    @OneToOne
    @JsonBackReference
    @JoinColumn(name = "apartment_id")
    Apartment apartment;
}
