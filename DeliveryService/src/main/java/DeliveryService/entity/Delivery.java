package DeliveryService.entity;

import DeliveryService.enums.DeliveryStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(indexes = {@Index(name = "idx_orderId", columnList = "orderId")})
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public Long orderId;
    public String productName;
    public Long productCount;
    public String address;
    public Long referenceCode;
    public DeliveryStatus status;

}
