package com.castlemon.jenkins.performance.domain.reporting;

import java.util.List;

import com.castlemon.jenkins.performance.util.CucumberPerfUtils;

public class Summary {

	private String id;

	private String name;

	private List<PerformanceEntry> entries;

	private long shortestDuration = Long.MAX_VALUE;

	private long longestDuration;

	private long averageDuration;

	private long totalDuration;

	private int totalBuilds;

	private int passedBuilds;

	private int failedBuilds;

	private int reportedBuilds;

	private int passedSteps;

	private int failedSteps;

	private int skippedSteps;

	private String seniorId;

	private int order;

	private int numberOfSubItems;

	public String getPageLink() {
		return this.id + ".html";
	}

	public long calculateAverageDuration() {
		long count = Long.valueOf(entries.size());
		long duration = 0l;
		for (PerformanceEntry entry : entries) {
			duration += entry.getElapsedTime();
		}
		averageDuration = duration / count;
		return averageDuration;
	}

	public String getFormattedAverageDuration() {
		calculateAverageDuration();
		return CucumberPerfUtils.formatDuration(averageDuration);
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

	public void addToFailedSteps(long failedSteps) {
		this.failedSteps += failedSteps;
	}

	public void addToTotalDuration(long duration) {
		this.totalDuration += duration;
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

	public String getSeniorId() {
		return seniorId;
	}

	public void setSeniorId(String seniorId) {
		this.seniorId = seniorId;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public int getNumberOfSubItems() {
		return numberOfSubItems;
	}

	public void setNumberOfSubItems(int numberOfSubItems) {
		this.numberOfSubItems = numberOfSubItems;
	}

}
