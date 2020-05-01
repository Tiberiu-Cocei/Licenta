package com.thesis.webapi.services;

import com.thesis.webapi.dtos.AppTransactionCreateDto;
import com.thesis.webapi.dtos.AppTransactionPreviewDto;
import com.thesis.webapi.dtos.AppTransactionUpdateDto;
import com.thesis.webapi.entities.AppTransaction;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface AppTransactionService {

    ResponseEntity<List<AppTransaction>> getAppTransactionsByUserId(UUID userId);

    ResponseEntity<String> createTransaction(AppTransactionCreateDto appTransactionCreateDto);

    ResponseEntity<String> finalizeTransaction(AppTransactionUpdateDto appTransactionUpdateDto);

    ResponseEntity<String> previewTransaction(AppTransactionPreviewDto appTransactionPreviewDto);

}
