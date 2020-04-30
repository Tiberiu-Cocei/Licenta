package com.thesis.webapi.controllers;

import com.thesis.webapi.entities.Message;
import com.thesis.webapi.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/secure/messages")
public class MessageController {

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping(value = "/user/{id}")
    public ResponseEntity<List<Message>> getReportsByUserId(@PathVariable("id") UUID userId) {
        return messageService.getMessagesByUserId(userId);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<String> markMessageAsSeen(@PathVariable("id") UUID messageId) {
        return messageService.markMessageAsSeen(messageId);
    }

}
