package com.thesis.webapi.repositories;

import com.thesis.webapi.entities.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {

    List<Message> getMessagesByUserId(UUID userId);

    Message getMessageById(UUID messageId);

}