package com.sentinel.model;

import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;

public class GeoLocation {

    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private double[] coordinates;
    private String city;
    private String country;
    private String region;
    private String postalCode;

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
    public GeoLocation() {}

    public static GeoLocationBuilder builder() { return new GeoLocationBuilder(); }
    public static class GeoLocationBuilder {
        private String city;
        public GeoLocationBuilder city(String city) { this.city = city; return this; }
        private String country;
        public GeoLocationBuilder country(String country) { this.country = country; return this; }
        private String region;
        public GeoLocationBuilder region(String region) { this.region = region; return this; }
        private String postalCode;
        public GeoLocationBuilder postalCode(String postalCode) { this.postalCode = postalCode; return this; }
        public GeoLocation build() {
            GeoLocation instance = new GeoLocation();
        instance.setCity(this.city);
        instance.setCountry(this.country);
        instance.setRegion(this.region);
        instance.setPostalCode(this.postalCode);
            return instance;
        }
    }

}
