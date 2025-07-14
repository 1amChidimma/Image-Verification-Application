package com.chidimma.image_verification_system.repository;

import com.chidimma.image_verification_system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
        Optional<User> findByUserName(String username);

        Optional<User> findByEmail(String email);

}
