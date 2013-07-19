package com.castlemon.jenkins.performance.reporting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.castlemon.jenkins.performance.domain.Elements;
import com.castlemon.jenkins.performance.domain.Feature;
import com.castlemon.jenkins.performance.domain.Step;
import com.castlemon.jenkins.performance.domain.reporting.ProjectPerformanceEntry;
import com.castlemon.jenkins.performance.domain.reporting.ProjectRun;
import com.castlemon.jenkins.performance.domain.reporting.FeaturePerformanceEntry;
import com.castlemon.jenkins.performance.domain.reporting.FeatureSummary;
import com.castlemon.jenkins.performance.domain.reporting.StepPerformanceEntry;
import com.castlemon.jenkins.performance.domain.reporting.StepSummary;

public class ProjectReporter {

	private static final String STEP_FAILED = "failed";
	private static final String STEP_SKIPPED = "skipped";

	Map<String, FeatureSummary> featureEntries;
	Map<String, StepSummary> stepEntries;

	public ProjectPerformanceEntry generateBasicProjectPerformanceData(
			ProjectRun projectRun) {
		ProjectPerformanceEntry output = new ProjectPerformanceEntry();
		output.setRunDate(projectRun.getRunDate());
		output.setBuildNumber(projectRun.getBuildNumber());
		long projectDuration = 0;
		int projectPassedSteps = 0;
		int projectFailedSteps = 0;
		int projectSkippedSteps = 0;
		for (Feature feature : projectRun.getFeatures()) {
			FeatureSummary featureSummary = getRelevantFeatureSummary(feature);
			FeaturePerformanceEntry featurePerformanceEntry = createFeaturePerformanceEntry(
					feature.getId(), projectRun.getBuildNumber());
			long featureDuration = 0;
			int featurePassedSteps = 0;
			int featureFailedSteps = 0;
			int featureSkippedSteps = 0;
			for (Elements element : feature.getElements()) {
				for (Step step : element.getSteps()) {
					StepSummary stepSummary = getRelevantStepSummary(step);
					StepPerformanceEntry stepPerformanceEntry = createStepPerformanceEntry(
							stepSummary.getStepId(),
							projectRun.getBuildNumber());
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
					featureDuration += duration;
					stepPerformanceEntry.setElapsedTime(duration);
					updateStepData(stepSummary, stepPerformanceEntry);
					addStepDataToFeature(featureSummary, stepSummary);
				}
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
			output.setPassedBuild(true);
		} else {
			output.setPassedBuild(false);
		}
		return output;
	}

	public void initialiseFeatureEntries() {
		featureEntries = new HashMap<String, FeatureSummary>();
	}

	public Map<String, FeatureSummary> getFeatureEntries() {
		return featureEntries;
	}

	private void updateFeatureData(FeatureSummary summary,
			FeaturePerformanceEntry entry) {
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

	private FeaturePerformanceEntry createFeaturePerformanceEntry(
			String featureId, int buildNumber) {
		FeaturePerformanceEntry featurePerformanceEntry = new FeaturePerformanceEntry();
		featurePerformanceEntry.setFeatureId(featureId);
		featurePerformanceEntry.setBuildNumber(buildNumber);
		return featurePerformanceEntry;
	}

	private FeatureSummary getRelevantFeatureSummary(Feature feature) {
		// find the right feature summary to use
		FeatureSummary featureSummary = null;
		if (featureEntries.containsKey(feature.getId())) {
			// exists - use it
			featureSummary = featureEntries.get(feature.getId());
		} else {
			// doesn't exist - create it
			featureSummary = new FeatureSummary();
			featureSummary.setFeatureId(feature.getId());
			featureSummary.setFeatureName(feature.getName());
			List<FeaturePerformanceEntry> entries = new ArrayList<FeaturePerformanceEntry>();
			featureSummary.setEntries(entries);
			List<StepSummary> stepSummaries = new ArrayList<StepSummary>();
			featureSummary.setStepSummaries(stepSummaries);
			featureEntries.put(feature.getId(), featureSummary);
		}
		// add the new entry to the list
		return featureSummary;
	}

	public void initialiseStepEntries() {
		stepEntries = new HashMap<String, StepSummary>();
	}

	public Map<String, StepSummary> getStepEntries() {
		return stepEntries;
	}

	private StepPerformanceEntry createStepPerformanceEntry(String stepId,
			int buildNumber) {
		StepPerformanceEntry stepPerformanceEntry = new StepPerformanceEntry();
		stepPerformanceEntry.setStepId(stepId);
		stepPerformanceEntry.setBuildNumber(buildNumber);
		return stepPerformanceEntry;
	}

	private StepSummary getRelevantStepSummary(Step step) {
		// find the right step summary to use
		StepSummary stepSummary = null;
		String currentStepId = step.getName().replaceAll("\\s+", "_");
		if (featureEntries.containsKey(currentStepId)) {
			// exists - use it
			stepSummary = stepEntries.get(currentStepId);
		} else {
			// doesn't exist - create it
			stepSummary = new StepSummary();
			stepSummary.setStepId(currentStepId);
			stepSummary.setStepName(step.getName());
			List<StepPerformanceEntry> entries = new ArrayList<StepPerformanceEntry>();
			stepSummary.setEntries(entries);
			stepEntries.put(currentStepId, stepSummary);
		}
		// add the new entry to the list
		return stepSummary;
	}

	private void updateStepData(StepSummary summary, StepPerformanceEntry entry) {
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

	private void addStepDataToFeature(FeatureSummary featureSummary,
			StepSummary stepSummary) {

		for (StepSummary existingSummary : featureSummary.getStepSummaries()) {
			if (existingSummary.getStepId().equals(stepSummary.getStepId())) {
				// update the existing version
				existingSummary.getEntries().addAll(stepSummary.getEntries());
				if (stepSummary.getShortestDuration() < existingSummary
						.getShortestDuration()) {
					existingSummary.setShortestDuration(stepSummary
							.getShortestDuration());
				}
				if (stepSummary.getLongestDuration() > existingSummary
						.getLongestDuration()) {
					existingSummary.setLongestDuration(stepSummary
							.getLongestDuration());
				}
				existingSummary.addToPassedSteps(stepSummary.getPassedSteps());
				existingSummary.addToFailedSteps(stepSummary.getFailedSteps());
				existingSummary.addToFailedSteps(stepSummary.getFailedSteps());
				return;
			}
		}
		// no existing version, so add to the list
		featureSummary.getStepSummaries().add(stepSummary);
	}
}
