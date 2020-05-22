package com.thesis.webapi.controllers;

import com.thesis.webapi.dtos.AppTransactionCreateDto;
import com.thesis.webapi.dtos.AppTransactionHistoryDto;
import com.thesis.webapi.dtos.AppTransactionPreviewDto;
import com.thesis.webapi.dtos.AppTransactionUpdateDto;
import com.thesis.webapi.services.AppTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/secure/transactions")
public class AppTransactionController {

    private final AppTransactionService appTransactionService;

    @Autowired
    public AppTransactionController(AppTransactionService appTransactionService) {
        this.appTransactionService = appTransactionService;
    }

    @GetMapping(value = "/get-all-transactions/{id}")
    public ResponseEntity<List<AppTransactionHistoryDto>> getTransactionsByUserId(@PathVariable("id") UUID userId) {
        return appTransactionService.getAppTransactionsByUserId(userId);
    }

    @GetMapping(value = "/get-active-transaction/{id}")
    public ResponseEntity<List<AppTransactionHistoryDto>> getActiveTransactionByUserId(@PathVariable("id") UUID userId) {
        return appTransactionService.getActiveAppTransactionByUserId(userId);
    }

    @PostMapping(value = "/create")
    public ResponseEntity<String> createTransaction(@Valid @RequestBody AppTransactionCreateDto appTransactionCreateDto) {
        return appTransactionService.createTransaction(appTransactionCreateDto);
    }

    @PutMapping(value = "/finalize")
    public ResponseEntity<String> finalizeTransaction(@Valid @RequestBody AppTransactionUpdateDto appTransactionUpdateDto) {
        return appTransactionService.finalizeTransaction(appTransactionUpdateDto);
    }

    @PostMapping(value = "/preview")
    public ResponseEntity<Double> previewTransaction(@Valid @RequestBody AppTransactionPreviewDto appTransactionPreviewDto) {
        return appTransactionService.previewTransaction(appTransactionPreviewDto);
    }

}
