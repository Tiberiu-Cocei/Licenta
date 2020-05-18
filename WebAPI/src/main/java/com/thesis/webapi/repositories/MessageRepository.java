package com.thesis.webapi.repositories;

import com.thesis.webapi.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {

    List<Message> getMessagesByUserId(UUID userId);

    @Query(value = "SELECT u FROM Message u WHERE u.seen = false AND u.userId = :userId")
    List<Message> getUnseenMessagesByUserId(@Param("userId") UUID userId);

}