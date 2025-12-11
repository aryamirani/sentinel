package com.sentinel.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeviceFingerprint {

    private String fingerprintHash;
    private String userAgent;
    private String os;
    private String browser;
    private String screenResolution;
    private String timezone;
    private boolean vpnDetected;
    private boolean emulatorDetected;
}
