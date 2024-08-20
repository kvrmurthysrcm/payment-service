package com.twopc.paymentservice.repository;

import com.twopc.paymentservice.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Payment findByItem(String item);

}
