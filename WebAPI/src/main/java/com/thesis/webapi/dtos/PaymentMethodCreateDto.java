package com.thesis.webapi.dtos;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class PaymentMethodCreateDto {

    @NotNull(message = "Card number cannot be null.")
    private String cardNumber;

    @NotNull(message = "Expiry date cannot be null.")
    private Date expiryDate;

    @NotNull(message = "First name cannot be null.")
    private String firstName;

    @NotNull(message = "Last name cannot be null.")
    private String lastName;

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
