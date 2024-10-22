package app.anne.device.controller;

import app.anne.device.controller.dto.CreateDeviceLogRequest;
import app.anne.device.infrastructure.DeviceRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeviceController {

    private final DeviceRepository deviceRepository;

    public DeviceController(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @PostMapping("/device/log")
    public ResponseEntity<Void> createDeviceLog(@RequestBody @Valid CreateDeviceLogRequest request) {
        deviceRepository.save(request);
        return ResponseEntity.ok().build();
    }

}
