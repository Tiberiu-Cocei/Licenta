package com.thesis.webapi.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.thesis.webapi.dtos.PaymentMethodCreateDto;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "payment_method")
public class PaymentMethod {

    @Id
    @Column(name="id")
    private UUID id;

    @Column(name="card_number")
    private String cardNumber;

    @Column(name="expiry_date")
    private Date expiryDate;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "paymentMethod")
    @JsonBackReference
    private AppUser appUser;

    public PaymentMethod() {}

    public PaymentMethod(UUID id, PaymentMethodCreateDto paymentMethodCreateDto) {
        this.id = id;
        this.cardNumber = paymentMethodCreateDto.getCardNumber();
        this.expiryDate = paymentMethodCreateDto.getExpiryDate();
        this.firstName = paymentMethodCreateDto.getFirstName();
        this.lastName = paymentMethodCreateDto.getLastName();
    }

    public UUID getId() {
        return id;
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

    public AppUser getAppUser() {
        return appUser;
    }
}
