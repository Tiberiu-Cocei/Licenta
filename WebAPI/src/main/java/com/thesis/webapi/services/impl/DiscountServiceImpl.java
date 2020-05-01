package com.thesis.webapi.services.impl;

import com.thesis.webapi.entities.Discount;
import com.thesis.webapi.repositories.DiscountRepository;
import com.thesis.webapi.services.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class DiscountServiceImpl implements DiscountService {

    private final DiscountRepository discountRepository;

    @Autowired
    public DiscountServiceImpl(DiscountRepository discountRepository) {
        this.discountRepository = discountRepository;
    }

    @Override
    public ResponseEntity<List<Discount>> getDiscountsByStationAndTime(UUID stationId) {
        Date date = new Date();
        return new ResponseEntity<>(discountRepository.getDiscountsByStationAndTime(stationId, date), HttpStatus.OK);
    }

}
