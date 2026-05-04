package DeliveryService.service;

import DeliveryService.entity.Delivery;
import blackfriday.protobuf.EdaMessage;
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
    DeliveryService deliveryService;

    private KafkaTemplate<String, byte[]> kafkaTemplate;

    @KafkaListener(topics = "delivery_request")
    public void consumePaymentRequest(byte[] message) throws Exception {
        var object = EdaMessage.DeliveryRequestV1.parseFrom(message);
        log.info("[delivery_request] consumed: {}", object);

        Delivery delivery = deliveryService.processDelivery(
                object.getOrderId(),
                object.getProductName(),
                object.getProductCount(),
                object.getAddress()
        );

        EdaMessage.DeliveryStatusUpdateV1 build = EdaMessage.DeliveryStatusUpdateV1.newBuilder()
                .setOrderId(delivery.getOrderId())
                .setDeliveryId(delivery.getId())
                .setDeliveryStatus(delivery.getStatus().toString())
                .build();

        kafkaTemplate.send("delivery_status_update", build.toByteArray());
    }


}
