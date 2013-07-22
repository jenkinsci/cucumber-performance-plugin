package com.castlemon.jenkins.performance.reporting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.castlemon.jenkins.performance.domain.Elements;
import com.castlemon.jenkins.performance.domain.Feature;
import com.castlemon.jenkins.performance.domain.Step;
import com.castlemon.jenkins.performance.domain.reporting.PerformanceEntry;
import com.castlemon.jenkins.performance.domain.reporting.ProjectRun;
import com.castlemon.jenkins.performance.domain.reporting.Summary;

public class ProjectReporter {

	private static final String STEP_FAILED = "failed";
	private static final String STEP_SKIPPED = "skipped";

	Map<String, Summary> featureEntries;
	Map<String, Summary> scenarioEntries;
	Map<String, Summary> stepEntries;

	public PerformanceEntry generateProjectPerformanceData(ProjectRun projectRun) {
		PerformanceEntry output = new PerformanceEntry();
		output.setRunDate(projectRun.getRunDate());
		output.setBuildNumber(projectRun.getBuildNumber());
		long projectDuration = 0;
		int projectPassedSteps = 0;
		int projectFailedSteps = 0;
		int projectSkippedSteps = 0;
		for (Feature feature : projectRun.getFeatures()) {
			Summary featureSummary = getRelevantFeatureSummary(feature);
			PerformanceEntry featurePerformanceEntry = createFeaturePerformanceEntry(
					feature.getId(), projectRun.getBuildNumber());
			long featureDuration = 0;
			int featurePassedSteps = 0;
			int featureFailedSteps = 0;
			int featureSkippedSteps = 0;
			for (Elements scenario : feature.getElements()) {
				Summary scenarioSummary = getRelevantScenarioSummary(scenario);
				PerformanceEntry scenarioPerformanceEntry = createScenarioPerformanceEntry(projectRun
						.getBuildNumber());
				long scenarioDuration = 0;
				int scenarioPassedSteps = 0;
				int scenarioFailedSteps = 0;
				int scenarioSkippedSteps = 0;
				for (Step step : scenario.getSteps()) {
					Summary stepSummary = getRelevantStepSummary(step);
					PerformanceEntry stepPerformanceEntry = createStepPerformanceEntry(
							stepSummary.getId(), projectRun.getBuildNumber());
					long duration;
					if (step.getResult().getStatus().equals(STEP_SKIPPED)) {
						duration = 0;
						featureSkippedSteps++;
						stepPerformanceEntry.setPassed(false);
						stepSummary.addToSkippedSteps(1);

					} else if (step.getResult().getStatus().equals(STEP_FAILED)) {
						duration = step.getResult().getDuration();
						featureFailedSteps++;
						stepPerformanceEntry.setPassed(false);
						stepSummary.addToFailedSteps(1);
					} else {
						duration = step.getResult().getDuration();
						featurePassedSteps++;
						stepPerformanceEntry.setPassed(true);
						stepSummary.addToPassedSteps(1);
					}
					scenarioDuration += duration;
					featureDuration += duration;
					stepPerformanceEntry.setElapsedTime(duration);
					updateStepData(stepSummary, stepPerformanceEntry);
				}
				scenarioSummary.addToPassedSteps(featurePassedSteps);
				scenarioSummary.addToFailedSteps(featureFailedSteps);
				scenarioSummary.addToSkippedSteps(featureSkippedSteps);
				if (scenarioFailedSteps == 0 && scenarioSkippedSteps == 0) {
					scenarioSummary.incrementPassedBuilds();
				} else {
					scenarioSummary.incrementFailedBuilds();
				}
				//updateScenarioData(scenarioSummary, scenarioPerformanceEntry);
			}
			featurePerformanceEntry.setPassedSteps(featurePassedSteps);
			featurePerformanceEntry.setFailedSteps(featureFailedSteps);
			featurePerformanceEntry.setSkippedSteps(featureSkippedSteps);
			featurePerformanceEntry.setElapsedTime(featureDuration);
			featureSummary.addToPassedSteps(featurePassedSteps);
			featureSummary.addToFailedSteps(featureFailedSteps);
			featureSummary.addToSkippedSteps(featureSkippedSteps);
			if (featureFailedSteps == 0 && featureSkippedSteps == 0) {
				featureSummary.incrementPassedBuilds();
			} else {
				featureSummary.incrementFailedBuilds();
			}
			updateFeatureData(featureSummary, featurePerformanceEntry);
			projectDuration += featureDuration;
			projectPassedSteps += featurePassedSteps;
			projectFailedSteps += featureFailedSteps;
			projectSkippedSteps += featureSkippedSteps;
		}
		output.setElapsedTime(projectDuration);
		output.setFailedSteps(projectFailedSteps);
		output.setPassedSteps(projectPassedSteps);
		output.setSkippedSteps(projectSkippedSteps);
		if (projectFailedSteps == 0 && projectSkippedSteps == 0) {
			output.setPassed(true);
		} else {
			output.setPassed(false);
		}
		return output;
	}

	public void initialiseFeatureEntries() {
		featureEntries = new HashMap<String, Summary>();
	}

	public Map<String, Summary> getFeatureEntries() {
		return featureEntries;
	}

	private void updateFeatureData(Summary summary, PerformanceEntry entry) {
		// check the duration fields
		if (entry.getElapsedTime() < summary.getShortestDuration()) {
			summary.setShortestDuration(entry.getElapsedTime());
		}
		if (entry.getElapsedTime() > summary.getLongestDuration()) {
			summary.setLongestDuration(entry.getElapsedTime());
		}
		// add the entry to the list
		summary.getEntries().add(entry);
	}

	private PerformanceEntry createFeaturePerformanceEntry(String featureId,
			int buildNumber) {
		PerformanceEntry featurePerformanceEntry = new PerformanceEntry();
		featurePerformanceEntry.setBuildNumber(buildNumber);
		return featurePerformanceEntry;
	}

	private PerformanceEntry createScenarioPerformanceEntry(int buildNumber) {
		PerformanceEntry featurePerformanceEntry = new PerformanceEntry();
		featurePerformanceEntry.setBuildNumber(buildNumber);
		return featurePerformanceEntry;
	}

	private Summary getRelevantFeatureSummary(Feature feature) {
		// find the right feature summary to use
		Summary featureSummary = null;
		if (featureEntries.containsKey(feature.getId())) {
			// exists - use it
			featureSummary = featureEntries.get(feature.getId());
		} else {
			// doesn't exist - create it
			featureSummary = new Summary();
			featureSummary.setId(feature.getId());
			featureSummary.setName(feature.getName());
			List<PerformanceEntry> entries = new ArrayList<PerformanceEntry>();
			featureSummary.setEntries(entries);
			// List<Summary> stepSummaries = new ArrayList<Summary>();
			// featureSummary.setStepSummaries(stepSummaries);
			featureEntries.put(feature.getId(), featureSummary);
		}
		// add the new entry to the list
		return featureSummary;
	}

	private Summary getRelevantScenarioSummary(Elements scenario) {
		// find the right feature summary to use
		Summary scenarioSummary = null;
		if (scenarioEntries.containsKey(scenario.getId())) {
			// exists - use it
			scenarioSummary = scenarioEntries.get(scenario.getId());
		} else {
			// doesn't exist - create it
			scenarioSummary = new Summary();
			scenarioSummary.setId(scenario.getId());
			scenarioSummary.setName(scenario.getName());
			List<PerformanceEntry> entries = new ArrayList<PerformanceEntry>();
			scenarioSummary.setEntries(entries);
			// add the new entry to the list
			scenarioEntries.put(scenario.getId(), scenarioSummary);
		}
		return scenarioSummary;
	}

	public void initialiseStepEntries() {
		stepEntries = new HashMap<String, Summary>();
	}

	public Map<String, Summary> getStepEntries() {
		return stepEntries;
	}

	private PerformanceEntry createStepPerformanceEntry(String stepId,
			int buildNumber) {
		PerformanceEntry stepPerformanceEntry = new PerformanceEntry();
		// stepPerformanceEntry.setStepId(stepId);
		stepPerformanceEntry.setBuildNumber(buildNumber);
		return stepPerformanceEntry;
	}

	private Summary getRelevantStepSummary(Step step) {
		// find the right step summary to use
		Summary stepSummary = null;
		String currentStepId = step.getName().replaceAll("\\s+", "_");
		if (featureEntries.containsKey(currentStepId)) {
			// exists - use it
			stepSummary = stepEntries.get(currentStepId);
		} else {
			// doesn't exist - create it
			stepSummary = new Summary();
			stepSummary.setId(currentStepId);
			stepSummary.setName(step.getName());
			List<PerformanceEntry> entries = new ArrayList<PerformanceEntry>();
			stepSummary.setEntries(entries);
			stepEntries.put(currentStepId, stepSummary);
		}
		// add the new entry to the list
		return stepSummary;
	}

	private void updateStepData(Summary summary, PerformanceEntry entry) {
		// check the duration fields
		if (entry.getElapsedTime() < summary.getShortestDuration()) {
			summary.setShortestDuration(entry.getElapsedTime());
		}
		if (entry.getElapsedTime() > summary.getLongestDuration()) {
			summary.setLongestDuration(entry.getElapsedTime());
		}
		// add the entry to the list
		summary.getEntries().add(entry);
	}

	/*
	 * private void addStepDataToFeature(Summary featureSummary, Summary
	 * stepSummary) {
	 * 
	 * for (StepSummary existingSummary : featureSummary.getStepSummaries()) {
	 * if (existingSummary.getId().equals(stepSummary.getId())) { // update the
	 * existing version
	 * existingSummary.getEntries().addAll(stepSummary.getEntries()); if
	 * (stepSummary.getShortestDuration() < existingSummary
	 * .getShortestDuration()) { existingSummary.setShortestDuration(stepSummary
	 * .getShortestDuration()); } if (stepSummary.getLongestDuration() >
	 * existingSummary .getLongestDuration()) {
	 * existingSummary.setLongestDuration(stepSummary .getLongestDuration()); }
	 * existingSummary.addToPassedSteps(stepSummary.getPassedSteps());
	 * existingSummary.addToFailedSteps(stepSummary.getFailedSteps());
	 * existingSummary.addToFailedSteps(stepSummary.getFailedSteps()); return; }
	 * } // no existing version, so add to the list
	 * featureSummary.getStepSummaries().add(stepSummary); }
	 */
}
