package com.thesis.webapi.controllers;

import com.thesis.webapi.dtos.AppTransactionCreateDto;
import com.thesis.webapi.dtos.AppTransactionHistoryDto;
import com.thesis.webapi.dtos.AppTransactionPreviewDto;
import com.thesis.webapi.dtos.AppTransactionUpdateDto;
import com.thesis.webapi.entities.AppTransaction;
import com.thesis.webapi.services.AppTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api")
public class AppTransactionController {

    private final AppTransactionService appTransactionService;

    @Autowired
    public AppTransactionController(AppTransactionService appTransactionService) {
        this.appTransactionService = appTransactionService;
    }

    @GetMapping(value = "/secure/transactions/get-all-transactions/{id}")
    public ResponseEntity<List<AppTransactionHistoryDto>> getTransactionsByUserId(@PathVariable("id") UUID userId) {
        return appTransactionService.getAppTransactionsByUserId(userId);
    }

    @GetMapping(value = "/secure/transactions/get-active-transaction/{id}")
    public ResponseEntity<List<AppTransactionHistoryDto>> getActiveTransactionByUserId(@PathVariable("id") UUID userId) {
        return appTransactionService.getActiveAppTransactionByUserId(userId);
    }

    @PostMapping(value = "/secure/transactions/create")
    public ResponseEntity<String> createTransaction(@Valid @RequestBody AppTransactionCreateDto appTransactionCreateDto) {
        return appTransactionService.createTransaction(appTransactionCreateDto);
    }

    @PutMapping(value = "/secure/transactions/finalize")
    public ResponseEntity<String> finalizeTransaction(@Valid @RequestBody AppTransactionUpdateDto appTransactionUpdateDto) {
        return appTransactionService.finalizeTransaction(appTransactionUpdateDto);
    }

    @PostMapping(value = "/secure/transactions/preview")
    public ResponseEntity<Double> previewTransaction(@Valid @RequestBody AppTransactionPreviewDto appTransactionPreviewDto) {
        return appTransactionService.previewTransaction(appTransactionPreviewDto);
    }

    @GetMapping(value = "/admin/transactions/with-start-station")
    public ResponseEntity<List<AppTransaction>> getTransactionsByStartStationWithLimitAndOffset(
            @RequestParam UUID startStationId, @RequestParam Integer limit, @RequestParam Integer offset) {
        return appTransactionService.getTransactionsByStartStationWithLimitAndOffset(startStationId, limit, offset);
    }

    @GetMapping(value = "/admin/transactions/with-finish-station")
    public ResponseEntity<List<AppTransaction>> getTransactionsByFinishStationWithLimitAndOffset(
            @RequestParam UUID finishStationId, @RequestParam Integer limit, @RequestParam Integer offset) {
        return appTransactionService.getTransactionsByFinishStationWithLimitAndOffset(finishStationId, limit, offset);
    }

}
