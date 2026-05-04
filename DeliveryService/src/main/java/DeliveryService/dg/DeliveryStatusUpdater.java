package DeliveryService.dg;

import DeliveryService.entity.Delivery;
import DeliveryService.enums.DeliveryStatus;
import DeliveryService.repository.DeliveryRepository;
import blackfriday.protobuf.EdaMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DeliveryStatusUpdater {

    @Autowired
    DeliveryRepository deliveryRepository;

    private KafkaTemplate<String, byte[]> kafkaTemplate;

    @Scheduled(fixedDelay = 10000)
    public void deliveryStatusUpdate() {
        System.out.println("----------- delivery status update ------------");

        var inDeliveryList = deliveryRepository.findAllByStatus(DeliveryStatus.IN_DELIVERY);
        inDeliveryList.forEach(delivery -> {
            delivery.status = DeliveryStatus.COMPLETED;
            deliveryRepository.save(delivery);

            publishStatusUpdate(delivery);
        });

        var requestedList = deliveryRepository.findAllByStatus(DeliveryStatus.REQUESTED);
        requestedList.forEach(delivery -> {
            delivery.status = DeliveryStatus.IN_DELIVERY;
            deliveryRepository.save(delivery);

            publishStatusUpdate(delivery);
        });
    }

    private void publishStatusUpdate(Delivery delivery) {

        EdaMessage.DeliveryStatusUpdateV1 build = EdaMessage.DeliveryStatusUpdateV1.newBuilder()
                .setOrderId(delivery.getOrderId())
                .setDeliveryId(delivery.getId())
                .setDeliveryStatus(delivery.getStatus().toString())
                .build();

        kafkaTemplate.send("delivery_status_update", build.toByteArray());
    }
}
