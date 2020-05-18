package com.android.android.dtos;

import java.util.Date;

public class PaymentMethodSaveDto {

    private String cardNumber;

    private Date expiryDate;

    private String firstName;

    private String lastName;

    public PaymentMethodSaveDto(String cardNumber, Date expiryDate, String firstName, String lastName) {
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
