package com.castlemon.jenkins.performance.domain.reporting;

import java.util.Date;

public class StepPerformanceEntry {
	
	private String stepId;

	private String stepName;

	private int buildNumber;

	private long elapsedTime;

	private Date runDate;

	private boolean passed;

	public String getStepName() {
		return stepName;
	}

	public void setStepName(String scenarioId) {
		this.stepName = scenarioId;
	}

	public int getBuildNumber() {
		return buildNumber;
	}

	public void setBuildNumber(int buildNumber) {
		this.buildNumber = buildNumber;
	}

	public long getElapsedTime() {
		return elapsedTime;
	}

	public void setElapsedTime(long elapsedTime) {
		this.elapsedTime = elapsedTime;
	}

	public Date getRunDate() {
		return runDate;
	}

	public void setRunDate(Date runDate) {
		this.runDate = runDate;
	}

	public boolean isPassed() {
		return passed;
	}

	public void setPassed(boolean passed) {
		this.passed = passed;
	}

	public String getStepId() {
		return stepId;
	}

	public void setStepId(String stepId) {
		this.stepId = stepId;
	}

}
