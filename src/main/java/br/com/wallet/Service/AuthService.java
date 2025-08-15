package br.com.wallet.service;

import br.com.wallet.infra.exceptions.CustomException;
import br.com.wallet.model.User;
import br.com.wallet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepo;

    public User authenticate(String email, String password) {
        User user = userRepo.findByEmail(email)
            .orElseThrow(
                () -> new CustomException(HttpStatus.UNAUTHORIZED.value(), "E-mail ou senha incorretos")
        );

        if (!password.equals(user.getPassword())) {
            throw new CustomException(401, "E-mail ou senha incorretos");
        }

        return user;
    }
}
