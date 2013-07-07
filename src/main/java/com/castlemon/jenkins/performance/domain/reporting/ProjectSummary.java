package com.castlemon.jenkins.performance.domain.reporting;

import java.util.List;

public class ProjectSummary {

	private int totalBuilds;

	private int passedBuilds;

	private int failedBuilds;

	private int reportedBuilds;

	private List<ProjectPerformanceEntry> performanceEntries;

	public int getTotalBuilds() {
		return totalBuilds;
	}

	public void setTotalBuilds(int totalBuilds) {
		this.totalBuilds = totalBuilds;
	}

	public int getPassedBuilds() {
		return passedBuilds;
	}

	public void setPassedBuilds(int passedBuilds) {
		this.passedBuilds = passedBuilds;
	}

	public int getFailedBuilds() {
		return failedBuilds;
	}

	public void setFailedBuilds(int failedBuilds) {
		this.failedBuilds = failedBuilds;
	}

	public int getReportedBuilds() {
		return reportedBuilds;
	}

	public void setReportedBuilds(int reportedBuilds) {
		this.reportedBuilds = reportedBuilds;
	}

	public List<ProjectPerformanceEntry> getPerformanceEntries() {
		return performanceEntries;
	}

	public void setPerformanceEntries(
			List<ProjectPerformanceEntry> performanceEntries) {
		this.performanceEntries = performanceEntries;
	}

}
