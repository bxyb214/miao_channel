package com.songzi.channel.service.dto;

//    {
//        "code": 0,
//        "data": {
//        "ip": "210.75.225.254",
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
        private String ip;
        private String country;
        private String area;
        private String region;
        private String city;
        private String county;
        private String isp;

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
    }
}
