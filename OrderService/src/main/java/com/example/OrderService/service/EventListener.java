package com.example.OrderService.service;

import blackfriday.protobuf.EdaMessage;
import com.example.OrderService.dto.DecreaseStockCountDto;
import com.example.OrderService.entity.ProductOrder;
import com.example.OrderService.enums.OrderStatus;
import com.example.OrderService.feign.CatalogClient;
import com.example.OrderService.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EventListener {

    @Autowired
    OrderRepository orderRepository;

    private KafkaTemplate<String, byte[]> kafkaTemplate;
    @Autowired
    private CatalogClient catalogClient;

    @KafkaListener(topics = "payment_result")
    public void consumePaymentResult(byte[] message) throws Exception {
        var object = EdaMessage.PaymentResultV1.parseFrom(message);
        log.info("[delivery_request] consumed: {}", object);

        ProductOrder order = orderRepository.findById(object.getOrderId()).orElseThrow();
        order.paymentId = object.getPaymentId();
        order.orderStatus = OrderStatus.DELIVERY_REQUESTED;
        orderRepository.save(order);

        var product = catalogClient.getProduct(order.productId);

       var deliveryRequest = EdaMessage.DeliveryRequestV1.newBuilder()
                .setOrderId(object.getOrderId())
                .setProductName(product.get("name").toString())
                .setProductCount(order.count)
                .setAddress(order.deliveryAddress)
                .build();

        kafkaTemplate.send("delivery_request", deliveryRequest.toByteArray());
    }

    @KafkaListener(topics = "delivery_status_update")
    public void consumeDeliveryStatusUpdate(byte[] message) throws Exception {
        var object = EdaMessage.DeliveryStatusUpdateV1.parseFrom(message);
        log.info("[delivery_status_update] consumed: {}", object);

        if(object.getDeliveryStatus().equals("RQUESTED")){
            //상품 재고 감소
            var order = orderRepository.findById(object.getOrderId()).orElseThrow();
            var decreaseStockCountDto = new DecreaseStockCountDto();
            decreaseStockCountDto.decreaseCount = order.count;
            catalogClient.decreaseStockCount(order.productId, decreaseStockCountDto);
        }

    }



}
