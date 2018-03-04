package com.songzi.channel.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

//    {
//        "code": 0,
//        "data": {
//            "ip": "210.75.225.254",
//            "country": "中国",
//            "area": "华北",
//            "region": "北京市",
//            "city": "北京市",
//            "county": "",
//            "isp": "电信",
//            "country_id": "86",
//            "area_id": "100000",
//            "region_id": "110000",
//            "city_id": "110000",
//            "county_id": "-1",
//            "isp_id": "100017"
//    }
//    }
public class IPInfoDTO {

    private String code;

    private IPInfoData data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public IPInfoData getData() {
        return data;
    }

    public void setData(IPInfoData data) {
        this.data = data;
    }

    public class IPInfoData {

        @JsonProperty(value="ip")
        private String ip;
        @JsonProperty(value="country")
        private String country;
        @JsonProperty(value="area")
        private String area;
        @JsonProperty(value="region")
        private String region;
        @JsonProperty(value="city")
        private String city;
        @JsonProperty(value="county")
        private String county;
        @JsonProperty(value="isp")
        private String isp;
        @JsonProperty(value="country_id")
        private String countryId;
        @JsonProperty(value="area_id")
        private String areaId;
        @JsonProperty(value="region_id")
        private String regionId;
        @JsonProperty(value="city_id")
        private String cityId;
        @JsonProperty(value="county_id")
        private String countyId;
        @JsonProperty(value="isp_id")
        private String ispId;

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCounty() {
            return county;
        }

        public void setCounty(String county) {
            this.county = county;
        }

        public String getIsp() {
            return isp;
        }

        public void setIsp(String isp) {
            this.isp = isp;
        }

        public String getCountryId() {
            return countryId;
        }

        public void setCountryId(String countryId) {
            this.countryId = countryId;
        }

        public String getAreaId() {
            return areaId;
        }

        public void setAreaId(String areaId) {
            this.areaId = areaId;
        }

        public String getRegionId() {
            return regionId;
        }

        public void setRegionId(String regionId) {
            this.regionId = regionId;
        }

        public String getCityId() {
            return cityId;
        }

        public void setCityId(String cityId) {
            this.cityId = cityId;
        }

        public String getCountyId() {
            return countyId;
        }

        public void setCountyId(String countyId) {
            this.countyId = countyId;
        }

        public String getIspId() {
            return ispId;
        }

        public void setIspId(String ispId) {
            this.ispId = ispId;
        }
    }
}
