package com.castlemon.jenkins.performance.domain.reporting;

import java.util.List;
import java.util.Map;

public class ProjectSummary {

	private Summary overallSummary;

	private List<Summary> projectSummaries;

	private Map<String, Summary> featureSummaries;

	private Map<String, Summary> scenarioSummaries;

	public Summary getOverallSummary() {
		return overallSummary;
	}

	public void setOverallSummary(Summary overallSummary) {
		this.overallSummary = overallSummary;
	}

	public List<Summary> getProjectSummaries() {
		return projectSummaries;
	}

	public void setProjectSummaries(List<Summary> projectSummaries) {
		this.projectSummaries = projectSummaries;
	}

	public Map<String, Summary> getFeatureSummaries() {
		return featureSummaries;
	}

	public void setFeatureSummaries(Map<String, Summary> featureSummaries) {
		this.featureSummaries = featureSummaries;
	}

	public Map<String, Summary> getScenarioSummaries() {
		return scenarioSummaries;
	}

	public void setScenarioSummaries(Map<String, Summary> scenarioSummaries) {
		this.scenarioSummaries = scenarioSummaries;
	}

}
