package app.anne.device.controller.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

@Data
public class CreateDeviceLogRequest {
    @NotEmpty
    private String deviceId;
    private String brand;
    private String designName;
    private String deviceName;
    private String deviceType;
    private Integer deviceYearClass;
    private boolean device;
    private String manufacturer;
    private String modelId;
    private String modelName;
    private String osBuildFingerprint;
    private String osBuildId;
    private String osInternalBuildId;
    private String osName;
    private String osVersion;
    private Integer platformApiLevel;
    private String productName;
    private List<String> supportedCpuArchitectures;
    private String totalMemory;
}
