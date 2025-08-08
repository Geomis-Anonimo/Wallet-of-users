package br.com.wallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import br.com.wallet.model.Wallet;
import jakarta.persistence.LockModeType;
import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Optional<Wallet> findByUserId(Long userId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Wallet> findWithLockingByUserId(Long userId);
}
