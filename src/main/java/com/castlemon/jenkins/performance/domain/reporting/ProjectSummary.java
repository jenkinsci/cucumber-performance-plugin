package com.castlemon.jenkins.performance.domain.reporting;

import java.util.List;

public class ProjectSummary {

	private String projectName;

	private int totalBuilds;

	private int passedBuilds;

	private int failedBuilds;

	private int reportedBuilds;

	private int passedSteps;

	private int failedSteps;

	private int skippedSteps;

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

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public int getPassedSteps() {
		return passedSteps;
	}

	public void setPassedSteps(int passedSteps) {
		this.passedSteps = passedSteps;
	}

	public int getFailedSteps() {
		return failedSteps;
	}

	public void setFailedSteps(int failedSteps) {
		this.failedSteps = failedSteps;
	}

	public int getSkippedSteps() {
		return skippedSteps;
	}

	public void setSkippedSteps(int skippedSteps) {
		this.skippedSteps = skippedSteps;
	}

}
