package com.thesis.webapi.services;

import com.thesis.webapi.entities.Message;
import com.thesis.webapi.repositories.MessageRepository;
import com.thesis.webapi.services.impl.MessageServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MessageServiceTest {

    @Mock
    MessageRepository messageRepository;

    @InjectMocks
    MessageServiceImpl messageService;

    private UUID userId;

    private Message message;

    private ArrayList<Message> messageList;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        userId = UUID.randomUUID();
        Message firstMessage = new Message(UUID.randomUUID(), userId, "First");
        Message secondMessage = new Message(UUID.randomUUID(), userId, "Second");
        message = new Message(UUID.randomUUID(), UUID.randomUUID(), "Seen Method Test");
        messageList = new ArrayList<>();
        messageList.add(firstMessage);
        messageList.add(secondMessage);
    }

    @Test
    public void whenGetMessagesByUserIdIsCalled_WithExistingId_ThenReturnCorrectList() {
        //Arrange
        Mockito.when(messageRepository.getMessagesByUserId(userId)).thenReturn(messageList);

        //Act
        ResponseEntity<List<Message>> messages = messageService.getMessagesByUserId(userId);

        //Assert
        Assertions.assertThat(messages).isNotNull();
        Assertions.assertThat(messages.getBody().size()).isEqualTo(2);
        Assertions.assertThat(messages.getBody().get(0)).isEqualToComparingFieldByFieldRecursively(messageList.get(0));
        Assertions.assertThat(messages.getBody().get(1)).isEqualToComparingFieldByFieldRecursively(messageList.get(1));
        Assertions.assertThat(messages.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    }

    @Test
    public void whenGetMessagesByUserIdIsCalled_WithNonexistentId_ThenReturnEmptyList() {
        //Arrange
        Mockito.when(messageRepository.getMessagesByUserId(userId)).thenReturn(messageList);

        //Act
        ResponseEntity<List<Message>> messages = messageService.getMessagesByUserId(UUID.randomUUID());

        //Assert
        Assertions.assertThat(messages).isNotNull();
        Assertions.assertThat(messages.getBody().size()).isEqualTo(0);
        Assertions.assertThat(messages.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    }

    @Test
    public void whenMarkMessageAsSeenIsCalled_WithExistingId_ThenReturnCorrectResponse() {
        //Arrange
        Mockito.when(messageRepository.getUnseenMessagesByUserId(message.getId())).thenReturn(messageList);

        //Act
        ResponseEntity<String> result = messageService.markMessagesAsSeen(userId);

        //Assert
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getBody()).isEqualTo("Successfully marked messages as seen.");
        Assertions.assertThat(result.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    }

    @Test
    public void whenMarkMessageAsSeenIsCalled_WithNonexistentId_ThenReturnCorrectResponse() {
        //Arrange

        //Act
        ResponseEntity<String> result = messageService.markMessagesAsSeen(UUID.randomUUID());

        //Assert
        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.getBody()).isEqualTo("Successfully marked messages as seen.");
        Assertions.assertThat(result.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    }

    @After
    public void tearDown() {
        userId = null;
        messageList = null;
        message = null;
    }

}
