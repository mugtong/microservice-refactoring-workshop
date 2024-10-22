package app.anne.device.infrastructure;

import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

import java.util.List;

@DynamoDbBean
@Setter
@NoArgsConstructor
public class DeviceRecord {
    private String brand;
    private String designName;
    private String deviceName;
    private String deviceType;
    private Integer deviceYearClass;
    private boolean isDevice;
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

    @DynamoDbAttribute("brd")
    public String getBrand() {
        return brand;
    }

    @DynamoDbAttribute("dsnNm")
    public String getDesignName() {
        return designName;
    }

    @DynamoDbAttribute("dvNm")
    public String getDeviceName() {
        return deviceName;
    }

    @DynamoDbAttribute("dvTyp")
    public String getDeviceType() {
        return deviceType;
    }

    @DynamoDbAttribute("dvYrCls")
    public Integer getDeviceYearClass() {
        return deviceYearClass;
    }

    @DynamoDbAttribute("isDv")
    public boolean isDevice() {
        return isDevice;
    }

    @DynamoDbAttribute("mnft")
    public String getManufacturer() {
        return manufacturer;
    }

    @DynamoDbAttribute("modId")
    public String getModelId() {
        return modelId;
    }

    @DynamoDbAttribute("modNm")
    public String getModelName() {
        return modelName;
    }

    @DynamoDbAttribute("osBldFgp")
    public String getOsBuildFingerprint() {
        return osBuildFingerprint;
    }

    @DynamoDbAttribute("osBldId")
    public String getOsBuildId() {
        return osBuildId;
    }

    @DynamoDbAttribute("osIntBldId")
    public String getOsInternalBuildId() {
        return osInternalBuildId;
    }

    @DynamoDbAttribute("osNm")
    public String getOsName() {
        return osName;
    }

    @DynamoDbAttribute("osVer")
    public String getOsVersion() {
        return osVersion;
    }

    @DynamoDbAttribute("pltApiLv")
    public Integer getPlatformApiLevel() {
        return platformApiLevel;
    }

    @DynamoDbAttribute("prdNm")
    public String getProductName() {
        return productName;
    }

    @DynamoDbAttribute("archs")
    public List<String> getSupportedCpuArchitectures() {
        return supportedCpuArchitectures;
    }

    @DynamoDbAttribute("totMem")
    public String getTotalMemory() {
        return totalMemory;
    }
}
