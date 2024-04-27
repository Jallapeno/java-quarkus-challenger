package org.acme.dto;

import java.math.BigInteger;
import java.time.LocalDateTime;

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

    private String createdAt;
}
