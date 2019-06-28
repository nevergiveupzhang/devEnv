package cn.cnnic.report.config.model;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
@Component
@ConfigurationProperties(prefix="report")
public class ReportApplicationConfigModel {
	private List<IndexConfigModel> indexes;

	public List<IndexConfigModel> getIndexes() {
		return indexes;
	}

	public void setIndexes(List<IndexConfigModel> indexes) {
		this.indexes = indexes;
	}
}
