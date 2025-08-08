package br.com.wallet.service;

import br.com.wallet.dto.UserWithWalletDTO;
import br.com.wallet.model.User;
import br.com.wallet.model.Wallet;
import br.com.wallet.repository.UserRepository;
import br.com.wallet.repository.WalletRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepo;
    private final WalletRepository walletRepo;

    public UserService(UserRepository userRepo, WalletRepository walletRepo) {
        this.userRepo = userRepo;
        this.walletRepo = walletRepo;
    }

    @Transactional
    public User createUser(User user) {
        if (userRepo.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email já cadastrado.");
        }

        User saved = userRepo.save(user);

        if (saved.getWallet() == null) {
            Wallet wallet = Wallet.builder()
                    .user(saved)
                    .balance(BigDecimal.ZERO)
                    .build();

            walletRepo.save(wallet);
            saved.setWallet(wallet);
        }

        return saved;
    }

    @Transactional(readOnly = true)
    public List<UserWithWalletDTO> findAll() {
        List<User> users = userRepo.findAllWithWallet();
        return users.stream()
                .map(user -> new UserWithWalletDTO(user.getId(), user.getName(), user.getEmail(), user.getWallet().getBalance()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<UserWithWalletDTO> findById(Long id) {
        Optional<User> user = userRepo.findByIdWithWallet(id);
        return user.map(u -> new UserWithWalletDTO(u.getId(), u.getName(), u.getEmail(), u.getWallet().getBalance()));
    }

    @Transactional
    public User update(Long id, User newData) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        if (newData.getEmail() != null) {
            String newEmail = newData.getEmail();
            if (!newEmail.equalsIgnoreCase(user.getEmail()) && userRepo.existsByEmail(newEmail)) {
                throw new RuntimeException("Email já cadastrado.");
            }
            user.setEmail(newEmail);
        }

        if (newData.getName() != null) {
            user.setName(newData.getName());
        }

        if (newData.getPassword() != null) {
            user.setPassword(newData.getPassword());
        }

        return userRepo.save(user);
    }

    @Transactional
    public void delete(Long id) {
        if (!userRepo.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado.");
        }
        userRepo.deleteById(id);
    }
}
