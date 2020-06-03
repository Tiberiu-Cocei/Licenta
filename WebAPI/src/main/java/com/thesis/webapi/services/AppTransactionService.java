package com.thesis.webapi.services;

import com.thesis.webapi.dtos.AppTransactionCreateDto;
import com.thesis.webapi.dtos.AppTransactionHistoryDto;
import com.thesis.webapi.dtos.AppTransactionPreviewDto;
import com.thesis.webapi.dtos.AppTransactionUpdateDto;
import com.thesis.webapi.entities.AppTransaction;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface AppTransactionService {

    ResponseEntity<List<AppTransactionHistoryDto>> getAppTransactionsByUserId(UUID userId);

    ResponseEntity<List<AppTransactionHistoryDto>> getActiveAppTransactionByUserId(UUID userId);

    ResponseEntity<String> createTransaction(AppTransactionCreateDto appTransactionCreateDto);

    ResponseEntity<String> finalizeTransaction(AppTransactionUpdateDto appTransactionUpdateDto);

    ResponseEntity<Double> previewTransaction(AppTransactionPreviewDto appTransactionPreviewDto);

    ResponseEntity<List<AppTransaction>> getTransactionsByStartStationWithLimitAndOffset(UUID startStationId, Integer limit, Integer offset);

    ResponseEntity<List<AppTransaction>> getTransactionsByFinishStationWithLimitAndOffset(UUID finishStationId, Integer limit, Integer offset);

    void solveOverdueTransactions();

    void onScheduleCallSolveOverdueTransactions();

    void onStartupCallSolveOverdueTransactions();

}
