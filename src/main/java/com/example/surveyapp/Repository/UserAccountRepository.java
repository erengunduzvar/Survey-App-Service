package com.example.surveyapp.Repository;

import com.example.surveyapp.Model.Entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    // Spring Security login işlemi için kritik
    Optional<UserAccount> findByEmail(String email);
}