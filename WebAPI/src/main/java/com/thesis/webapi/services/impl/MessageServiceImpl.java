package com.thesis.webapi.services.impl;

import com.thesis.webapi.entities.Message;
import com.thesis.webapi.repositories.MessageRepository;
import com.thesis.webapi.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    @Autowired
    public MessageServiceImpl(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public ResponseEntity<List<Message>> getMessagesByUserId(UUID userId) {
        return new ResponseEntity<>(messageRepository.getMessagesByUserId(userId), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> markMessageAsSeen(UUID messageId) {
        Message message = messageRepository.getMessageById(messageId);
        if(message != null) {
            message.setSeen(true);
            messageRepository.save(message);
            return new ResponseEntity<>("Successfully marked message as seen.", HttpStatus.OK);
        }
        return new ResponseEntity<>("No message found with given id.", HttpStatus.NOT_FOUND);
    }

}
