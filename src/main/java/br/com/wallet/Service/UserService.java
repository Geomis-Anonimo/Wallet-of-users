package br.com.wallet.Service;

import br.com.wallet.Model.User;
import br.com.wallet.Model.Wallet;
import br.com.wallet.Repository.UserRepository;
import br.com.wallet.Repository.WalletRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

        Wallet wallet = Wallet.builder()
                .user(saved)
                .build();
        walletRepo.save(wallet);

        saved.setWallet(wallet);
        return saved;
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepo.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepo.findById(id);
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
        // Remoção em cascata deve estar configurada no relacionamento (User -> Wallet) se quiser remover a wallet junto.
        // Se não estiver, remova explicitamente a wallet antes:
        // walletRepo.findByUserId(id).ifPresent(w -> walletRepo.deleteById(w.getId()));

        if (!userRepo.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado.");
        }
        userRepo.deleteById(id);
    }
}
