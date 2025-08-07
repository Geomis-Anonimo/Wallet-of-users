package br.com.wallet.DTOs;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DepositRequest {
    private Long userId;
    private BigDecimal amount;
}