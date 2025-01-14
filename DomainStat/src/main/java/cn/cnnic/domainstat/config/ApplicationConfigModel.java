package cn.cnnic.domainstat.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="domain.stat")
public class ApplicationConfigModel {
	private String cron;
	private String result;
	private String threshold;
	private List<CnDateRangeModel> ranges;
	
	public String getCron() {
		return cron;
	}
	public void setCron(String cron) {
		this.cron = cron;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getThreshold() {
		return threshold;
	}
	public void setThreshold(String threshold) {
		this.threshold = threshold;
	}
	public List<CnDateRangeModel> getRanges() {
		return ranges;
	}
	public void setRanges(List<CnDateRangeModel> ranges) {
		this.ranges = ranges;
	}
}
