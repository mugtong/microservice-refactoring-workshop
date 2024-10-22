package app.anne.device.infrastructure;

import app.anne.device.controller.dto.CreateDeviceLogRequest;

import java.time.Instant;

public class DeviceRecordMapper {

    public static DeviceLogRecord convertDtoToDeviceLogRecord(CreateDeviceLogRequest dto) {
        final Instant createdAt = Instant.now();
        final DeviceRecord deviceDetail = convertDtoToDeviceRecord(dto);
        DeviceLogRecord record = new DeviceLogRecord();
        record.setPk(DeviceLogRecord.getPartitionKeyValue(dto.getDeviceId()));
        record.setSk(createdAt);
        record.setDeviceId(dto.getDeviceId());
        record.setDevice(deviceDetail);
        record.setCreatedAt(createdAt);
        return record;
    }

    private static DeviceRecord convertDtoToDeviceRecord(CreateDeviceLogRequest dto) {
        DeviceRecord record = new DeviceRecord();
        record.setBrand(dto.getBrand());
        record.setDesignName(dto.getDesignName());
        record.setDeviceName(dto.getDeviceName());
        record.setDeviceType(dto.getDeviceType());
        record.setDeviceYearClass(dto.getDeviceYearClass());
        record.setDevice(dto.isDevice());
        record.setManufacturer(dto.getManufacturer());
        record.setModelId(dto.getModelId());
        record.setModelName(dto.getModelName());
        record.setOsBuildFingerprint(dto.getOsBuildFingerprint());
        record.setOsBuildId(dto.getOsBuildId());
        record.setOsInternalBuildId(dto.getOsInternalBuildId());
        record.setOsName(dto.getOsName());
        record.setOsVersion(dto.getOsVersion());
        record.setPlatformApiLevel(dto.getPlatformApiLevel());
        record.setProductName(dto.getProductName());
        record.setSupportedCpuArchitectures(dto.getSupportedCpuArchitectures());
        record.setTotalMemory(dto.getTotalMemory());
        return record;
    }
}
