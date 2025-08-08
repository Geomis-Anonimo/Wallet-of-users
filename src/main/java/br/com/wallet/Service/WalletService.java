package br.com.wallet.service;

import br.com.wallet.infra.exceptions.CustomException;
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
            throw new CustomException(400, "Valor de depósito deve ser positivo");

        Wallet wallet = walletRepo.findWithLockingByUserId(userId)
                .orElseThrow(() -> new CustomException(500, "Carteira não encontrada para o usuário " + userId));

        wallet.setBalance(wallet.getBalance().add(amount));
        walletRepo.save(wallet);
    }

    @Transactional
    public void transfer(Long fromUserId, Long toUserId, BigDecimal amount) {
        if (fromUserId.equals(toUserId))
            throw new CustomException(400, "Não é possível transferir para o mesmo usuário");
        if (amount == null || amount.signum() <= 0)
        throw new CustomException(400, "Valor da transferência deve ser positivo");

        Long first = fromUserId < toUserId ? fromUserId : toUserId;
        Long second = fromUserId < toUserId ? toUserId : fromUserId;

        Wallet firstW = walletRepo.findWithLockingByUserId(first)
                .orElseThrow(() -> new CustomException(400, "Carteira não encontrada para o usuário " + first));
                Wallet secondW = walletRepo.findWithLockingByUserId(second)
                .orElseThrow(() -> new CustomException(400, "Carteira não encontrada para o usuário " + second));

        Wallet sender = (first.equals(fromUserId)) ? firstW : secondW;
        Wallet receiver = (sender == firstW) ? secondW : firstW;

        if (sender.getBalance().compareTo(amount) < 0)
            throw new CustomException(400, "Saldo insuficiente");

        sender.setBalance(sender.getBalance().subtract(amount));
        receiver.setBalance(receiver.getBalance().add(amount));

        walletRepo.save(sender);
        walletRepo.save(receiver);
    }
}
