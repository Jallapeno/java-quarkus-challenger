package org.acme.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pans")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PanEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String bin;

    private String accountNumber;

    private String checkDigit;

    private LocalDate createdAt;

}
