package cn.cnnic.porterrecord.aop;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ApplicationAspect {
	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationAspect.class);
	private static final String LOG_FORMATTER="%s.%s - %s";
	@Pointcut("execution(* cn.cnnic.porterrecord.service.*.add**(..))")
	public void add() {}
	
	@Before("add()")
	public void addLog(JoinPoint join) {
		String className=join.getTarget().getClass().getName();
		String methodName=join.getSignature().getName();
		Object []params=join.getArgs();
		LOGGER.info(String.format(LOG_FORMATTER, className,methodName,Arrays.toString(params)));
	}
}
