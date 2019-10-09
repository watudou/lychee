package org.lychee.config;

import org.apache.commons.lang.StringUtils;
import org.lychee.web.interceptor.PermissionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 权限拦截
 *
 * @author Administrator
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    LycheeConfig lycheeConfig;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public PermissionInterceptor permissionInterceptor() {
        return new PermissionInterceptor();
    }

    /**
     * @param registry 拦截器注册类
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (!lycheeConfig.getUsePermission()) {
            return;
        }
        if (lycheeConfig.getUsePermission()) {
            if (StringUtils.isNotBlank(lycheeConfig.getPermissionExcludePath())) {
                registry.addInterceptor(permissionInterceptor()).addPathPatterns(lycheeConfig.getPermissionPath()).excludePathPatterns(lycheeConfig.getPermissionExcludePath());
            } else {
                registry.addInterceptor(permissionInterceptor()).addPathPatterns(lycheeConfig.getPermissionPath());
            }
        }
    }
}
