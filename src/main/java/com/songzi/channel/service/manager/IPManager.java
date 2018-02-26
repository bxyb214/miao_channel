package com.songzi.channel.service.manager;

import com.songzi.channel.service.dto.IPInfoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class IPManager {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 省市区转换使用 http://ysj5125094.iteye.com/blog/2227874
     * http://ip.taobao.com/service/getIpInfo.php?ip=[ip地址字串]
     */
    public String getCity(String ip){

        String url = "http://ip.taobao.com/service/getIpInfo.php?ip=" + ip ;
        IPInfoDTO ipInfo = restTemplate.getForObject(url, IPInfoDTO.class);
        return ipInfo.getData().getCity();
    }
}
