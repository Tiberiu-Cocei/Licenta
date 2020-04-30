package com.thesis.webapi.services;

import com.thesis.webapi.entities.Message;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface MessageService {

    ResponseEntity<List<Message>> getMessagesByUserId(UUID userId);

    ResponseEntity<String> markMessageAsSeen(UUID messageId);

}
