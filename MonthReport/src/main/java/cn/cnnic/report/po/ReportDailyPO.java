package cn.cnnic.report.po;


public class ReportDailyPO {
	private int id;
	private String serviceName;
	private String reportType;
	private String reportDate;
	private long docCount;
	
	public static class Builder{
		private int id;
		private String serviceName;
		private String reportType;
		private String reportDate;
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
		public Builder docCount(long docCount) {
			this.docCount=docCount;
			return this;
		}
		public ReportDailyPO build() {
			return new ReportDailyPO(this);
		}
	}
	private ReportDailyPO(Builder builder) {
		this.id=builder.id;
		this.serviceName=builder.serviceName;
		this.reportDate=builder.reportDate;
		this.reportType=builder.reportType;
		this.docCount=builder.docCount;
	}

	public ReportDailyPO() {
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

	public long getDocCount() {
		return docCount;
	}

	@Override
	public String toString() {
		return "ReportDailyPO [id=" + id + ", serviceName=" + serviceName + ", reportType=" + reportType
				+ ", reportDate=" + reportDate + ", docCount=" + docCount + "]";
	}
	
}
