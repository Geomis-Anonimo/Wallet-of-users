package br.com.wallet.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.wallet.model.User;


public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    @Query("SELECT u FROM User u JOIN FETCH u.wallet")
    List<User> findAllWithWallet();

    @Query("SELECT u FROM User u JOIN FETCH u.wallet WHERE u.id = :id")
    Optional<User> findByIdWithWallet(Long id);

    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findByEmail(String email);
}
