package com.sentinel.model;


public class DeviceFingerprint {

    private String fingerprintHash;
    private String userAgent;
    private String os;
    private String browser;
    private String screenResolution;
    private String timezone;
    private boolean vpnDetected;
    private boolean emulatorDetected;

    public String getFingerprintHash() { return fingerprintHash; }
    public void setFingerprintHash(String fingerprintHash) { this.fingerprintHash = fingerprintHash; }
    public String getUserAgent() { return userAgent; }
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }
    public String getOs() { return os; }
    public void setOs(String os) { this.os = os; }
    public String getBrowser() { return browser; }
    public void setBrowser(String browser) { this.browser = browser; }
    public String getScreenResolution() { return screenResolution; }
    public void setScreenResolution(String screenResolution) { this.screenResolution = screenResolution; }
    public String getTimezone() { return timezone; }
    public void setTimezone(String timezone) { this.timezone = timezone; }
    public boolean getVpnDetected() { return vpnDetected; }
    public void setVpnDetected(boolean vpnDetected) { this.vpnDetected = vpnDetected; }
    public boolean getEmulatorDetected() { return emulatorDetected; }
    public void setEmulatorDetected(boolean emulatorDetected) { this.emulatorDetected = emulatorDetected; }
    public DeviceFingerprint() {}

    public static DeviceFingerprintBuilder builder() { return new DeviceFingerprintBuilder(); }
    public static class DeviceFingerprintBuilder {
        private String fingerprintHash;
        public DeviceFingerprintBuilder fingerprintHash(String fingerprintHash) { this.fingerprintHash = fingerprintHash; return this; }
        private String userAgent;
        public DeviceFingerprintBuilder userAgent(String userAgent) { this.userAgent = userAgent; return this; }
        private String os;
        public DeviceFingerprintBuilder os(String os) { this.os = os; return this; }
        private String browser;
        public DeviceFingerprintBuilder browser(String browser) { this.browser = browser; return this; }
        private String screenResolution;
        public DeviceFingerprintBuilder screenResolution(String screenResolution) { this.screenResolution = screenResolution; return this; }
        private String timezone;
        public DeviceFingerprintBuilder timezone(String timezone) { this.timezone = timezone; return this; }
        private boolean vpnDetected;
        public DeviceFingerprintBuilder vpnDetected(boolean vpnDetected) { this.vpnDetected = vpnDetected; return this; }
        private boolean emulatorDetected;
        public DeviceFingerprintBuilder emulatorDetected(boolean emulatorDetected) { this.emulatorDetected = emulatorDetected; return this; }
        public DeviceFingerprint build() {
            DeviceFingerprint instance = new DeviceFingerprint();
        instance.setFingerprintHash(this.fingerprintHash);
        instance.setUserAgent(this.userAgent);
        instance.setOs(this.os);
        instance.setBrowser(this.browser);
        instance.setScreenResolution(this.screenResolution);
        instance.setTimezone(this.timezone);
        instance.setVpnDetected(this.vpnDetected);
        instance.setEmulatorDetected(this.emulatorDetected);
            return instance;
        }
    }

}
