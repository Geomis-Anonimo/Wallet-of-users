package br.com.wallet.dto;

import lombok.Getter;

@Getter
public class JwtResponse {
    private String tokenType;
    private String accessToken;

    public JwtResponse(String tokenType, String accessToken) {
        this.tokenType = tokenType;
        this.accessToken = accessToken;
    }
}
