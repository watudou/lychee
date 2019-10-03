package org.lychee;

import org.lychee.config.LycheeConfig;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author lizhixiao
 * @Date: 2019/9/30
 * @Description: lychee自动配置
 */
@Configuration
@EnableConfigurationProperties(LycheeConfig.class)
@ComponentScan(value = "org.lychee.service")
public class LycheeAutoConfiguration {

}