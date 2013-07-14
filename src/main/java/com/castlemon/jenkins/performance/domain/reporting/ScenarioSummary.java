package com.castlemon.jenkins.performance.domain.reporting;

import java.util.List;

import com.castlemon.jenkins.performance.util.CucumberPerfUtils;

public class ScenarioSummary {

	private String scenarioId;

	private String scenarioName;

	private List<ScenarioPerformanceEntry> entries;

	private long shortestDuration;

	private long longestDuration;

	public String getFormattedAverageDuration() {
		long totalDuration = 0;
		for (ScenarioPerformanceEntry entry : entries) {
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

	public String getScenarioId() {
		return scenarioId;
	}

	public void setScenarioId(String scenarioId) {
		this.scenarioId = scenarioId;
	}

	public String getScenarioName() {
		return scenarioName;
	}

	public void setScenarioName(String scenarioName) {
		this.scenarioName = scenarioName;
	}

	public List<ScenarioPerformanceEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<ScenarioPerformanceEntry> entries) {
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

}
