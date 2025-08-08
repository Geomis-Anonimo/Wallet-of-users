package br.com.wallet.infra.exceptions;

public class CustomException extends RuntimeException {
    private final int statusCode;
    private final String message;

    public CustomException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}
