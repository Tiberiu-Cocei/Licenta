package com.thesis.webapi.repositories;

import com.thesis.webapi.entities.AppTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AppTransactionRepository extends JpaRepository<AppTransaction, UUID> {

    List<AppTransaction> getAppTransactionsByUserId(UUID userId);

}
