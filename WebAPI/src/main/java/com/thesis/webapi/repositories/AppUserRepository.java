package com.thesis.webapi.repositories;

import com.thesis.webapi.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface AppUserRepository extends JpaRepository<AppUser, UUID> {

    @Query(value = "SELECT salt FROM AppUser u WHERE u.username = :username")
    String getSalt(@Param("username") String username);

    @Query(value = "SELECT u FROM AppUser u WHERE u.username = :username AND u.password = :password")
    AppUser login(@Param("username") String username, @Param("password") String password);

    @Query(value = "SELECT username FROM AppUser u WHERE u.username = :username")
    String isUsernameTaken(String username);

    @Query(value = "SELECT email FROM AppUser u WHERE u.email = :email")
    String isEmailTaken(String email);

    Optional<AppUser> findUserByAuthenticationToken(UUID authenticationToken);

}
