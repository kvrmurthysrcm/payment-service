package com.twopc.paymentservice.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionData {
    private String orderNumber;
    private String item;
    private String price;
    private String paymentMode;

}