package com.example.rbacdemo.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * 安全路径白名单配置类
 *
 * @author djhaa
 */
@ConfigurationProperties(prefix = "secure.ignored")
@Configuration
public class IgnoreUrlsConfig {
    private List<String> urls = new ArrayList<>();

    public String[] getUrls() {
        int n = this.urls.size();
        String[] res = new String[n];
        for(int i = 0; i < n; i++) {
            res[i] = urls.get(i);
        }
        return res;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }
}
