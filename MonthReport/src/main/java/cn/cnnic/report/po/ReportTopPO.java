package cn.cnnic.report.po;

public class ReportTopPO {
	private int id;
	private String serviceName;
	private String reportType;
	private String reportDate;
	private String fieldName;
	private String fieldValue;
	private long docCount;
	
	public static class Builder{
		private int id;
		private String serviceName;
		private String reportType;
		private String reportDate;
		private String fieldName;
		private String fieldValue;
		private long docCount;
		
		public Builder id(int id) {
			this.id=id;
			return this;
		}
		public Builder serviceName(String serviceName) {
			this.serviceName=serviceName;
			return this;
		}
		public Builder reportType(String reportType) {
			this.reportType=reportType;
			return this;
		}
		public Builder reportDate(String reportDate) {
			this.reportDate=reportDate;
			return this;
		}
		public Builder fieldName(String fieldName) {
			this.fieldName=fieldName;
			return this;
		}
		public Builder fieldValue(String fieldValue) {
			this.fieldValue=fieldValue;
			return this;
		}
		public Builder docCount(long docCount) {
			this.docCount=docCount;
			return this;
		}
		public ReportTopPO build() {
			return new ReportTopPO(this);
		}
	}
	private ReportTopPO(Builder builder) {
		this.id=builder.id;
		this.serviceName=builder.serviceName;
		this.reportDate=builder.reportDate;
		this.reportType=builder.reportType;
		this.fieldName=builder.fieldName;
		this.fieldValue=builder.fieldValue;
		this.docCount=builder.docCount;
	}
	public ReportTopPO() {
	}
	public int getId() {
		return id;
	}
	public String getServiceName() {
		return serviceName;
	}
	public String getReportType() {
		return reportType;
	}
	public String getReportDate() {
		return reportDate;
	}
	public String getFieldName() {
		return fieldName;
	}
	public String getFieldValue() {
		return fieldValue;
	}
	public long getDocCount() {
		return docCount;
	}
	@Override
	public String toString() {
		return "ReportTopPO [id=" + id + ", serviceName=" + serviceName + ", reportType=" + reportType + ", reportDate="
				+ reportDate + ", fieldName=" + fieldName + ", fieldValue=" + fieldValue + ", docCount=" + docCount
				+ "]";
	}
	
}
