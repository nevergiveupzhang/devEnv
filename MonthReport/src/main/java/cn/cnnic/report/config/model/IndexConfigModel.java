package cn.cnnic.report.config.model;

import java.util.List;

public class IndexConfigModel {
		private String name;
		private String conditionField;
		private List<ReportChannelConfigModel> channels;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getConditionField() {
			return conditionField;
		}
		public void setConditionField(String conditionField) {
			this.conditionField = conditionField;
		}
		public List<ReportChannelConfigModel> getChannels() {
			return channels;
		}
		public void setChannels(List<ReportChannelConfigModel> channels) {
			this.channels = channels;
		}
		
}
