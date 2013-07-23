package com.castlemon.jenkins.performance.domain.reporting;

import java.util.List;

import com.castlemon.jenkins.performance.util.CucumberPerfUtils;

public class Summary {

	private String id;

	private String name;

	private List<PerformanceEntry> entries;

	private long shortestDuration;

	private long longestDuration;

	private int totalBuilds;

	private int passedBuilds;

	private int failedBuilds;

	private int reportedBuilds;

	private int passedSteps;

	private int failedSteps;

	private int skippedSteps;
	
	private List<Summary> summaries;

	public String getPageLink() {
		return this.id + ".html";
	}

	public String getFormattedAverageDuration() {
		long totalDuration = 0;
		for (PerformanceEntry entry : entries) {
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
	
	public void incrementTotalBuilds() {
		this.totalBuilds++;
	}

	public void incrementPassedBuilds() {
		this.passedBuilds++;
	}

	public void incrementFailedBuilds() {
		this.failedBuilds++;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<PerformanceEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<PerformanceEntry> entries) {
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

	public List<Summary> getSummaries() {
		return summaries;
	}

	public void setSummaries(List<Summary> summaries) {
		this.summaries = summaries;
	}

}