package com.thesis.webapi.services;

import com.thesis.webapi.dtos.AppTransactionCreateDto;
import com.thesis.webapi.dtos.AppTransactionHistoryDto;
import com.thesis.webapi.dtos.AppTransactionPreviewDto;
import com.thesis.webapi.dtos.AppTransactionUpdateDto;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface AppTransactionService {

    ResponseEntity<List<AppTransactionHistoryDto>> getAppTransactionsByUserId(UUID userId);

    ResponseEntity<String> createTransaction(AppTransactionCreateDto appTransactionCreateDto);

    ResponseEntity<String> finalizeTransaction(AppTransactionUpdateDto appTransactionUpdateDto);

    ResponseEntity<Double> previewTransaction(AppTransactionPreviewDto appTransactionPreviewDto);

    void solveOverdueTransactions();

    void onScheduleCallSolveOverdueTransactions();

    void onStartupCallSolveOverdueTransactions();

}
