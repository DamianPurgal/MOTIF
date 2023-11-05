package com.deemor.motif.user.repository;

import com.deemor.motif.user.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByUsername(String username);

    void deleteAppUserByUsername(String username);
}
