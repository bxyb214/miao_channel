package com.songzi.channel.service.manager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.songzi.channel.config.Constants;
import com.songzi.channel.service.dto.IPInfoDTO;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Service
public class IPManager {

    @Autowired
    private RestTemplate restTemplate;

    private final Logger log = LoggerFactory.getLogger(IPManager.class);


    /**
     * 省市区转换使用 http://ysj5125094.iteye.com/blog/2227874
     * http://ip.taobao.com/service/getIpInfo.php?ip=[ip地址字串]
     */
    public String getCity(String ip) {

        String city = Constants.UNKNOWN;
        String url = "http://ip.taobao.com/service/getIpInfo.php?ip=" + ip ;
        String json = restTemplate.getForObject(url, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            IPInfoDTO ipInfoDTO = objectMapper.readValue(json, IPInfoDTO.class);
            city = ipInfoDTO.getData().getCity();
        } catch (IOException e) {
            log.error("ip 解析失败，返回json = {}", json);
            e.printStackTrace();
        }
        return city;
    }

}
