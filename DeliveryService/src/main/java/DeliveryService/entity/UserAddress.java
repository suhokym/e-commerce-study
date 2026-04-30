package DeliveryService.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(indexes = {@Index(name = "idx_userId", columnList = "userId")})
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public Long userId;
    public String address;
    public String alias;


}
