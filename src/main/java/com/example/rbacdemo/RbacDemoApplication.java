package com.example.rbacdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.servlet.ManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

/*
* ManagementWebSecurityAutoConfiguration类属于actuator包，会自动导入Spring Security相关配置类并监控
* */
//@SpringBootApplication(exclude = {SecurityAutoConfiguration.class, ManagementWebSecurityAutoConfiguration.class})
@SpringBootApplication
public class RbacDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(RbacDemoApplication.class, args);
    }

}
