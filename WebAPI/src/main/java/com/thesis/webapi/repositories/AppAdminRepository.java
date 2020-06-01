package com.thesis.webapi.repositories;

import com.thesis.webapi.entities.AppAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface AppAdminRepository extends JpaRepository<AppAdmin, UUID> {

    @Query(value = "SELECT salt FROM AppAdmin u WHERE u.username = :username")
    String getSalt(@Param("username") String username);

    @Query(value = "SELECT u FROM AppAdmin u WHERE u.username = :username AND u.password = :password")
    AppAdmin login(@Param("username") String username, @Param("password") String password);

    Optional<AppAdmin> findAdminByAuthenticationToken(UUID authenticationToken);

    AppAdmin getAppAdminByStaffId(UUID staffId);

    AppAdmin getAppAdminByUsername(String username);

    @Query(value = "SELECT username FROM AppAdmin u WHERE u.username = :username")
    String isUsernameTaken(@Param("username") String username);

}
