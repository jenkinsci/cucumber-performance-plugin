package com.castlemon.jenkins.performance.domain.reporting;

import java.util.List;

import com.castlemon.jenkins.performance.util.CucumberPerfUtils;

public class StepSummary {

	private String stepId;

	private String stepName;

	private List<StepPerformanceEntry> entries;

	private long shortestDuration;

	private long longestDuration;

	private int passedSteps;

	private int failedSteps;

	private int skippedSteps;

	public String getPageLink() {
		return this.stepId + ".html";
	}

	public String getFormattedAverageDuration() {
		long totalDuration = 0;
		for (StepPerformanceEntry entry : entries) {
			totalDuration += entry.getElapsedTime();
		}
		long count = Long.valueOf(entries.size());
		long average = totalDuration / count;
		return CucumberPerfUtils.formatDuration(average);
	}

	public String getFormattedShortestDuration() {
		return CucumberPerfUtils.formatDuration(shortestDuration);
	}

	public String getFormattedLongestDuration() {
		return CucumberPerfUtils.formatDuration(longestDuration);
	}

	public void addToPassedSteps(int passedSteps) {
		this.passedSteps += passedSteps;
	}

	public void addToSkippedSteps(int skippedSteps) {
		this.skippedSteps += skippedSteps;
	}

	public void addToFailedSteps(int failedSteps) {
		this.failedSteps += failedSteps;
	}

	public String getStepId() {
		return stepId;
	}

	public void setStepId(String stepId) {
		this.stepId = stepId;
	}

	public String getStepName() {
		return stepName;
	}

	public void setStepName(String stepName) {
		this.stepName = stepName;
	}

	public List<StepPerformanceEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<StepPerformanceEntry> entries) {
		this.entries = entries;
	}

	public long getShortestDuration() {
		return shortestDuration;
	}

	public void setShortestDuration(long shortestDuration) {
		this.shortestDuration = shortestDuration;
	}

	public long getLongestDuration() {
		return longestDuration;
	}

	public void setLongestDuration(long longestDuration) {
		this.longestDuration = longestDuration;
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
