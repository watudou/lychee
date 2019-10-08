package org.lychee;

import org.lychee.config.LycheeConfig;
import org.lychee.config.WebMvcConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author lizhixiao
 * @Date: 2019/9/30
 * @Description: lychee自动配置
 */
@Configuration
@EnableConfigurationProperties(LycheeConfig.class)
@ComponentScan({"org.lychee.service", "org.lychee.config"})
public class LycheeAutoConfiguration {

    @Bean
    public WebMvcConfig webMvcConfig() {
        return new WebMvcConfig();
    }
}