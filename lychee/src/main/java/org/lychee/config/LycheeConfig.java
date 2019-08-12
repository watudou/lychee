package org.lychee.config;

import org.lychee.framework.SpringContextHolder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LycheeConfig {

	@Bean
	public SpringContextHolder springContextHolder() {
		return new SpringContextHolder();
	}
}
