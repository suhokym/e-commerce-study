package com.example.PaymentService.service;

import blackfriday.protobuf.EdaMessage;
import com.example.PaymentService.entity.Payment;
import com.google.protobuf.InvalidProtocolBufferException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EventListener {

    @Autowired
    PaymentService paymentService;

    private KafkaTemplate<String, byte[]> kafkaTemplate;

    @KafkaListener(topics = "product_request")
    public void consumePaymentRequest(byte[] message) throws Exception {
        var object = EdaMessage.PaymentRequestV1.parseFrom(message);
        log.info("[payment_request] consumed: {}", object);

        Payment payment = paymentService.processPayment(
                object.getUserId(),
                object.getOrderId(),
                object.getAmountKRW(),
                object.getPaymentMethodId());

        EdaMessage.PaymentResultV1 build = EdaMessage.PaymentResultV1.newBuilder()
                .setOrderId(payment.getOrderId())
                .setPaymentId(payment.getId())
                .setPymentStatus(payment.getPaymentStatus().toString())
                .build();

        kafkaTemplate.send("payment_result", build.toByteArray());
    }


}
