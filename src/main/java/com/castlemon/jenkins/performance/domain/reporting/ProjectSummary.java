package com.castlemon.jenkins.performance.domain.reporting;

import java.util.Collection;
import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@XStreamAlias("projectsummary")
public class ProjectSummary {

	@XStreamAlias("overallsummary")
	private Summary overallSummary;

	@XStreamAlias("featuresummaries")
	private Map<String, Summary> featureSummaries;

	@XStreamAlias("scenariosummaries")
	private Map<String, Summary> scenarioSummaries;

	@XStreamAlias("stepsummaries")
	private Map<String, Summary> stepSummaries;

	public Summary getOverallSummary() {
		return overallSummary;
	}

	public void setOverallSummary(Summary overallSummary) {
		this.overallSummary = overallSummary;
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

	public Map<String, Summary> getStepSummaries() {
		return stepSummaries;
	}

	public void setStepSummaries(Map<String, Summary> stepSummaries) {
		this.stepSummaries = stepSummaries;
	}

	public Collection<Summary> getFeatureSummaryList() {
		return this.featureSummaries.values();
	}

}
