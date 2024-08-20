package com.twopc.paymentservice.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
// @Table(name="order_details")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String orderNumber;
    private String item;
    private String preparationStatus;

    private String paymentMode;
    private String price;

}
