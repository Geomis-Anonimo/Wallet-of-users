package br.com.wallet.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.wallet.Model.User;


public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
}
