package br.com.wallet.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserWithWalletDTO {
    private Long id;
    private String name;
    private String email;
    private BigDecimal balance;

    public UserWithWalletDTO(Long id, String name, String email, BigDecimal balance) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.balance = balance;
    }
}
