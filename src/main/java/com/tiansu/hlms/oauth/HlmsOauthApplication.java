package com.tiansu.hlms.oauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

//@EnableDiscoveryClient
//@RestController
@SpringBootApplication
@EnableAutoConfiguration
@EnableScheduling
@ComponentScan(basePackages = {"com.tiansu.hlms"})
@PropertySource(value = {"classpath:/oauthservice.properties", "file:./config/oauthservice.properties"},
        ignoreResourceNotFound = true)
public class HlmsOauthApplication {

//    @RequestMapping("/oauth")
//    public String home() {
//        return "hlms-oauth正在保持通信---";
//    }

    public static void main(String[] args) {
        SpringApplication.run(HlmsOauthApplication.class, args);
    }
}
