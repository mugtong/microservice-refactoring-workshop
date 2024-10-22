package app.anne.device.infrastructure;

import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

import java.time.Instant;

@DynamoDbBean
@Setter
@NoArgsConstructor
public class DeviceLogRecord {

    public static String getPartitionKeyValue(String deviceId) {
        return String.format("DEVICE#%s", deviceId);
    }

    private String pk;
    private Instant sk;
    private String deviceId;
    private DeviceRecord device;
    private Instant createdAt;

    @DynamoDbAttribute("PK")
    @DynamoDbPartitionKey
    public String getPk() {
        return pk;
    }

    @DynamoDbAttribute("SK")
    @DynamoDbSortKey
    public Instant getSk() {
        return sk;
    }

    @DynamoDbAttribute("deviceId")
    public String getDeviceId() {
        return deviceId;
    }

    @DynamoDbFlatten
    public DeviceRecord getDevice() {
        return device;
    }

    @DynamoDbAttribute("ct")
    public Instant getCreatedAt() {
        return createdAt;
    }
}
