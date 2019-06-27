package cn.cnnic.domainstat.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import cn.cnnic.domainstat.service.DomainStatService;
@Configuration
public class DomainStatSchedule {
	@Autowired
	private DomainStatService domainStatService;
	
	@Scheduled(cron="${cron.domainstat}")
	private void domainStatsTask() throws Exception {
		domainStatService.fetchData();
	}
}
