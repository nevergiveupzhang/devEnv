package cn.cnnic.plan;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages="cn.cnnic.plan")
@MapperScan(basePackages="cn.cnnic.plan.mapper")
public class MainApplication  extends SpringBootServletInitializer{
	public static void main(String[] args) {
		SpringApplication.run(MainApplication.class, args);
	}
	protected SpringApplicationBuilder config(SpringApplicationBuilder applicationBuilder){
        return applicationBuilder.sources(MainApplication.class);
	}
}
