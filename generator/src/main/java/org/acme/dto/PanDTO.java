package org.acme.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.jackson.Jacksonized;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Jacksonized
public class PanDTO {

    private String bin;

    private String accountNumber;

    private String checkDigit;

    private LocalDate createdAt;
}
