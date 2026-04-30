package DeliveryService.service;

import DeliveryService.dg.DeliveryAdapter;
import DeliveryService.entity.Delivery;
import DeliveryService.entity.UserAddress;
import DeliveryService.enums.DeliveryStatus;
import DeliveryService.repository.DeliveryRepository;
import DeliveryService.repository.UserAddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeliveryService {
    @Autowired
    UserAddressRepository userAddressRepository;
    
    @Autowired
    DeliveryRepository deliveryRepository;

    @Autowired
    DeliveryAdapter deliveryAdapter;

    public UserAddress addUserAddress(Long userId, String address, String alias) {
        var userAddress = UserAddress.builder().userId(userId).address(address).alias(alias).build();

        return userAddressRepository.save(userAddress);
    }

    public Delivery processDelivery(
            Long orderId,
            String productName,
            Long productCount,
            String address
    ) {
        var refCode = deliveryAdapter.processDelivery(productName, productCount, address);
        var delivery = new Delivery(orderId, productName, productCount, address, refCode, DeliveryStatus.REQUESTED);

        return deliveryRepository.save(delivery);
    }

    public Delivery getDelivery(Long deliveryId) {
        return deliveryRepository.findById(deliveryId).orElseThrow();
    }

    public UserAddress getAddress(Long addressId) {
        return userAddressRepository.findById(addressId).orElseThrow();
    }

    public UserAddress getUserAddress(Long userId) {
        return userAddressRepository.findByUserId(userId).stream().findFirst().orElseThrow();
    }
}
