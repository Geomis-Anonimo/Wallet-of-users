package br.com.wallet.service;

import br.com.wallet.model.Wallet;
import br.com.wallet.repository.WalletRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class WalletService {

    private final WalletRepository walletRepo;

    public WalletService(WalletRepository walletRepo) {
        this.walletRepo = walletRepo;
    }

    @Transactional
    public void deposit(Long userId, BigDecimal amount) {
        if (amount == null || amount.signum() <= 0)
            throw new IllegalArgumentException("Valor de depósito deve ser positivo.");

        Wallet wallet = walletRepo.findWithLockingByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Carteira não encontrada para o usuário " + userId));

        wallet.setBalance(wallet.getBalance().add(amount));
        walletRepo.save(wallet);
    }

    @Transactional
    public void transfer(Long fromUserId, Long toUserId, BigDecimal amount) {
        if (fromUserId.equals(toUserId))
            throw new IllegalArgumentException("Não é possível transferir para o mesmo usuário.");
        if (amount == null || amount.signum() <= 0)
            throw new IllegalArgumentException("Valor da transferência deve ser positivo.");

        // Para evitar deadlock, sempre bloqueie na mesma ordem (menor id primeiro)
        Long first = fromUserId < toUserId ? fromUserId : toUserId;
        Long second = fromUserId < toUserId ? toUserId : fromUserId;

        Wallet firstW = walletRepo.findWithLockingByUserId(first)
                .orElseThrow(() -> new RuntimeException("Carteira não encontrada para o usuário " + first));
        Wallet secondW = walletRepo.findWithLockingByUserId(second)
                .orElseThrow(() -> new RuntimeException("Carteira não encontrada para o usuário " + second));

        Wallet sender = (first.equals(fromUserId)) ? firstW : secondW;
        Wallet receiver = (sender == firstW) ? secondW : firstW;

        if (sender.getBalance().compareTo(amount) < 0)
            throw new IllegalArgumentException("Saldo insuficiente.");

        sender.setBalance(sender.getBalance().subtract(amount));
        receiver.setBalance(receiver.getBalance().add(amount));

        walletRepo.save(sender);
        walletRepo.save(receiver);
    }
}
