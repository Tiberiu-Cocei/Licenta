package com.thesis.webapi.services;

import com.thesis.webapi.entities.Discount;
import com.thesis.webapi.repositories.DiscountRepository;
import com.thesis.webapi.services.impl.DiscountServiceImpl;
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
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class DiscountServiceTest {

    @Mock
    DiscountRepository discountRepository;

    @InjectMocks
    DiscountServiceImpl discountService;

    private UUID discountId;

    private ArrayList<Discount> discountList;

    private Date date;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        discountId = UUID.randomUUID();
        Discount firstDiscount = new Discount(discountId);
        Discount secondDiscount = new Discount(discountId);
        discountList = new ArrayList<>();
        discountList.add(firstDiscount);
        discountList.add(secondDiscount);
        date = new Date();
    }

    @Test
    public void whenGetDiscountsByStationAndTimeIsCalled_WithExistingIdAndCorrectDate_ThenReturnCorrectList() {
        //Arrange
        Mockito.when(discountRepository.getDiscountsByStationAndTime(discountId, date)).thenReturn(discountList);

        //Act
        ResponseEntity<List<Discount>> discounts = discountService.getDiscountsByStationAndTime(discountId);

        //Assert
        Assertions.assertThat(discounts).isNotNull();
        Assertions.assertThat(discounts.getBody().size()).isEqualTo(2);
        Assertions.assertThat(discounts.getBody().get(0)).isEqualToComparingFieldByFieldRecursively(discountList.get(0));
        Assertions.assertThat(discounts.getBody().get(1)).isEqualToComparingFieldByFieldRecursively(discountList.get(1));
        Assertions.assertThat(discounts.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    }

    @Test
    public void whenGetDiscountsByStationAndTimeIsCalled_WithNonexistentIdAndCorrectDate_ThenReturnEmptyList() {
        //Arrange
        Mockito.when(discountRepository.getDiscountsByStationAndTime(discountId, date)).thenReturn(discountList);

        //Act
        ResponseEntity<List<Discount>> discounts = discountService.getDiscountsByStationAndTime(UUID.randomUUID());

        //Assert
        Assertions.assertThat(discounts).isNotNull();
        Assertions.assertThat(discounts.getBody().size()).isEqualTo(0);
        Assertions.assertThat(discounts.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    }

    @Test
    public void whenGetDiscountsByStationAndTimeIsCalled_WithNonexistentIdAndIncorrectDate_ThenReturnEmptyList() {
        //Arrange
        Mockito.when(discountRepository.getDiscountsByStationAndTime(discountId, null)).thenReturn(discountList);

        //Act
        ResponseEntity<List<Discount>> discounts = discountService.getDiscountsByStationAndTime(discountId);

        //Assert
        Assertions.assertThat(discounts).isNotNull();
        Assertions.assertThat(discounts.getBody().size()).isEqualTo(0);
        Assertions.assertThat(discounts.getStatusCode()).isEqualByComparingTo(HttpStatus.OK);
    }

    @After
    public void tearDown() {
        discountId = null;
        discountList = null;
    }

}
