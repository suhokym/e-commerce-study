package com.example.PaymentService.entity;

import com.example.PaymentService.enums.PaymentMethodType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@Table(indexes = {@Index(name = "idx_userId", columnList = "userId")})
public class PaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public Long userId;
    public PaymentMethodType paymentMethodType;
    public String creditCardNumber;


}
