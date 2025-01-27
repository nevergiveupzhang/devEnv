package cn.cnnic.report.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAdvice {
	private final static Logger LOGGER = LoggerFactory.getLogger(LogAdvice.class);
	
	@Pointcut("execution (* cn.cnnic.report.service.*.*(..))")
	public void servicePointcut() {}
	
	@Pointcut("execution (* cn.cnnic.report.mapper.*.*(..))")
	public void mapperPointcut() {}
	
	@Around("mapperPointcut()")
	public Object calcMapperExecTime(ProceedingJoinPoint joinPoint) throws Throwable {
		return printExecTime(joinPoint);
	}
	@Around("servicePointcut()")
	public Object calcServiceExecTime(ProceedingJoinPoint joinPoint) throws Throwable {
		return printExecTime(joinPoint);
	}
	private Object printExecTime(ProceedingJoinPoint joinPoint) throws Throwable {
		long startTime=System.currentTimeMillis();
		StringBuilder timeBuilder = new StringBuilder();
		Object result=joinPoint.proceed();
		timeBuilder.append("print execution time=>");
		timeBuilder.append("["
				+ joinPoint.getSignature().getDeclaringTypeName() + ".");
		timeBuilder.append(joinPoint.getSignature().getName()+"()");
		long endTime=System.currentTimeMillis();
		timeBuilder.append(" took "+(endTime-startTime)+"ms]");
		LOGGER.info(timeBuilder.toString());
		return result;
	}

	@Before("servicePointcut()")
	public void printServiceParams(JoinPoint joinPoint) {
		printParams(joinPoint);
	}
	@Before("mapperPointcut()")
	public void printMapperParams(JoinPoint joinPoint) {
		printParams(joinPoint);
	}
	private void printParams(JoinPoint joinPoint) {
		StringBuilder paramResult = new StringBuilder();
		paramResult.append("print method params=>");
		paramResult.append("["
				+ joinPoint.getSignature().getDeclaringTypeName() + ".");
		paramResult.append(joinPoint.getSignature().getName());
		paramResult.append("(");
		Object[] args = joinPoint.getArgs();
		for (Object obj : args) {
			if(null!=obj) {
				paramResult.append(obj.toString()+",");
			}else {
				paramResult.append("null,");
			}
		}
		if(args.length>0) {
			paramResult.deleteCharAt(paramResult.length()-1);
		}
		paramResult.deleteCharAt(paramResult.length()-1);
		paramResult.append(")]");
		LOGGER.info(paramResult.toString());
	}
}
