package cn.cnnic.domainstat;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import cn.cnnic.domainstat.config.ApplicationConfigModel;
import cn.cnnic.domainstat.service.DomainStatService;


@SpringBootApplication
@ComponentScan(basePackages="cn.cnnic.domainstat")
@MapperScan(basePackages="cn.cnnic.domainstat.mapper")
public class MainApplication 
{
	@Autowired
	private DomainStatService domainStatService;
	@Autowired
	private ApplicationConfigModel config;
	
	public ApplicationConfigModel getConfig() {
		return config;
	}

	public DomainStatService getDomainStatService() {
		return domainStatService;
	}

	public static void main( String[] args ) throws Exception
    {
    	ConfigurableApplicationContext applicationContext =SpringApplication.run(MainApplication.class, args);
    	MainApplication application=applicationContext.getBean(MainApplication.class);
    	if(StringUtils.isBlank(application.getConfig().getCron())) {
    		application.getDomainStatService().fetchData();
    	}else {
    		startCron(application);
    	}
    }

	private static void startCron(MainApplication application) {
		ThreadPoolTaskScheduler scheduler=new ThreadPoolTaskScheduler();
		scheduler.initialize();
		scheduler.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					application.getDomainStatService().fetchData();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, new Trigger(){
		
		    @Override
		    public Date nextExecutionTime(TriggerContext triggerContext){
		        return new CronTrigger(application.getConfig().getCron()).nextExecutionTime(triggerContext);
		    }
		});		
	}
}
