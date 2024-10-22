package app.anne.device.infrastructure;

import app.anne.device.controller.dto.CreateDeviceLogRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Repository
public class DeviceRepository {

    private final DynamoDbTable<DeviceLogRecord> deviceLogTable;

    public DeviceRepository(DynamoDbEnhancedClient client,
                            @Value("${aws.dynamodb.table}") String tableName) {
        this.deviceLogTable = client.table(tableName, TableSchema.fromBean(DeviceLogRecord.class));
    }

    public void save(CreateDeviceLogRequest request) {
        DeviceLogRecord item = DeviceRecordMapper.convertDtoToDeviceLogRecord(request);
        deviceLogTable.putItem(item);
    }
}
