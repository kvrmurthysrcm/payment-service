package com.twopc.paymentservice;

import com.twopc.paymentservice.entity.Payment;
import com.twopc.paymentservice.model.TransactionData;
import com.twopc.paymentservice.repository.PaymentRepository;
import com.twopc.paymentservice.util.PaymentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentController {

    @Autowired
    private PaymentRepository paymentRepository;

    @PostMapping("/prepare_payment")
    public ResponseEntity<String> prepareOrder(@RequestBody TransactionData transactionData){

        try{
            Payment payment = new Payment(); // table entity
            payment.setOrderNumber(transactionData.getOrderNumber());
            payment.setItem(transactionData.getItem());
            payment.setPreparationStatus(PaymentStatus.PENDING.name());

            payment.setPrice(transactionData.getPrice());
            payment.setPaymentMode(transactionData.getPaymentMode());

            paymentRepository.save(payment); // saving to DB

            if(failToPrepare()){
               throw new RuntimeException("Prepare failed for payment: " + transactionData.getOrderNumber());
            }
        }catch(Exception e){
            System.out.println("Exception occurred: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error during Payment preparation");
        }
        return ResponseEntity.ok("Payment prepared for order number: " + transactionData.getOrderNumber());
    }

    private boolean failToPrepare(){
        return false;
    }

    @PostMapping("/commit_payment")
    public ResponseEntity<String> commitOrder(@RequestBody TransactionData transactionData){
       Payment payment = paymentRepository.findByItem(transactionData.getItem());
        if(payment != null &&
            payment.getPreparationStatus().equalsIgnoreCase(PaymentStatus.PENDING.name())){
            payment.setPreparationStatus(PaymentStatus.APPROVED.name());
            paymentRepository.save(payment);
            return ResponseEntity.ok("Payment committed successfully");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Payment couldn't be committed for order id: " + transactionData.getOrderNumber());
    }

    @PostMapping("/rollback_payment")
    public ResponseEntity<String> rollbackOrder(@RequestBody TransactionData transactionData){
        Payment payment = paymentRepository.findByItem(transactionData.getItem());
        if(payment != null ){
            payment.setPreparationStatus(PaymentStatus.ROLLBACK.name());
            paymentRepository.save(payment);
            return ResponseEntity.ok("Payment Rollback successfully");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error during Rollback Payment for order id: " + transactionData.getOrderNumber());
    }

}
