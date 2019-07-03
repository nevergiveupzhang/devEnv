package cn.cnnic.domainstat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import net.cnnic.borlan.utils.lookup.ChinesePostalCodeUtil;
import net.cnnic.borlan.utils.lookup.PostalCodeUtil;

@Configuration
public class BeanConfig {
	@Bean
	public PostalCodeUtil pcu() {
		return new ChinesePostalCodeUtil();
	}
}
