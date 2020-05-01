package com.thesis.webapi.controllers;

import com.thesis.webapi.entities.Discount;
import com.thesis.webapi.services.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/secure/discounts")
public class DiscountController {

    private final DiscountService discountService;

    @Autowired
    public DiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }

    @GetMapping(value = "/station/{id}")
    public ResponseEntity<List<Discount>> getDiscountsByStationAndTime(@PathVariable("id") UUID stationId) {
        return discountService.getDiscountsByStationAndTime(stationId);
    }

}
