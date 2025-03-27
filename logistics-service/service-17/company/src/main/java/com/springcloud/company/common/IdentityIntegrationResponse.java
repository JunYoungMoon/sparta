package com.springcloud.company.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.apache.kafka.common.serialization.Serializer;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class IdentityIntegrationResponse implements Serializer {

    private static ObjectMapper objectMapper = new ObjectMapper();
    private UUID userId;
    private UUID hubId;
    private UUID companyId;
    private List<UUID> orderIdList;
    private UUID deliveryId;

    @Override
    public byte[] serialize(String s, Object data) {
        try {
            if (data == null) {
                return null;
            }
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing OrderCreateEvent", e);
        }
    }
//
//    public List<UUID> getOrderIdList() {
//        return this.orderId;
//    }
}
