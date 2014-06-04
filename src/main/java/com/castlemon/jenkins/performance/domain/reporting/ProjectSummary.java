package com.castlemon.jenkins.performance.domain.reporting;

import java.util.List;
import java.util.Map;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("projectsummary")
public class ProjectSummary {

	@XStreamAlias("overallsummary")
	private Summary overallSummary;

	@XStreamImplicit
	@XStreamAlias("projectsummaries")
	private List<Summary> projectSummaries;

	@XStreamImplicit
	@XStreamAlias("featuresummaries")
	private Map<String, Summary> featureSummaries;

	@XStreamImplicit
	@XStreamAlias("scenariosummaries")
	private Map<String, Summary> scenarioSummaries;

	@XStreamImplicit
	@XStreamAlias("stepsummaries")
	private Map<String, Summary> stepSummaries;

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

	public Map<String, Summary> getStepSummaries() {
		return stepSummaries;
	}

	public void setStepSummaries(Map<String, Summary> stepSummaries) {
		this.stepSummaries = stepSummaries;
	}

}
