package br.com.wallet.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.wallet.dto.UserWithWalletDTO;
import br.com.wallet.model.User;
import br.com.wallet.model.Wallet;
import br.com.wallet.service.UserService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        if (user.getWallet() == null) {
            Wallet wallet = new Wallet();
            wallet.setBalance(BigDecimal.ZERO);
            wallet.setUser(user);
            user.setWallet(wallet);
        }
        
        User saved = userService.createUser(user);
        return ResponseEntity.status(201).body(saved);
    }

    @GetMapping
    public ResponseEntity<List<UserWithWalletDTO>> getAllUsers() {
        List<UserWithWalletDTO> users = this.userService.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserWithWalletDTO> getUserById(@PathVariable Long id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User newData) {
        return ResponseEntity.ok(userService.update(id, newData));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
