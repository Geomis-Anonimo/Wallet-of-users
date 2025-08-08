package br.com.wallet.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.wallet.model.User;


public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}
