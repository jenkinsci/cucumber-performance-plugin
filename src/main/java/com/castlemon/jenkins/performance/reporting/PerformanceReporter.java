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

public class PerformanceReporter {

	private static final String STEP_FAILED = "failed";
	private static final String STEP_SKIPPED = "skipped";

	Map<String, Summary> featureSummaries;
	Map<String, Summary> scenarioSummaries;
	Map<String, Summary> stepSummaries;

	public PerformanceEntry generateProjectPerformanceData(ProjectRun projectRun) {
		PerformanceEntry output = new PerformanceEntry();
		output.setRunDate(projectRun.getRunDate());
		output.setBuildNumber(projectRun.getBuildNumber());
		long projectDuration = 0;
		int projectPassedSteps = 0;
		int projectFailedSteps = 0;
		int projectSkippedSteps = 0;
		for (Feature feature : projectRun.getFeatures()) {
			Summary featureSummary = getRelevantSummary(feature.getId(),
					feature.getName(), featureSummaries);
			for (Elements scenario : feature.getElements()) {
				Summary scenarioSummary = getRelevantSummary(scenario.getId(),
						scenario.getName(), scenarioSummaries);
				for (Step step : scenario.getSteps()) {
					PerformanceEntry stepEntry = processStep(step);
					projectDuration += stepEntry.getElapsedTime();
					updateSummaryDataFromEntry(scenarioSummary, stepEntry);
				}
				scenarioSummary.incrementTotalBuilds();
				if (scenarioSummary.getFailedSteps() == 0
						&& scenarioSummary.getSkippedSteps() == 0) {
					scenarioSummary.incrementPassedBuilds();
				} else {
					scenarioSummary.incrementFailedBuilds();
				}
				updateSummaryDataFromLowerSummary(featureSummary,
						scenarioSummary);
				projectPassedSteps += featureSummary.getPassedSteps();
				projectFailedSteps += featureSummary.getFailedSteps();
				projectSkippedSteps += featureSummary.getSkippedSteps();
			}
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

	public void initialiseEntryMaps() {
		featureSummaries = new HashMap<String, Summary>();
		scenarioSummaries = new HashMap<String, Summary>();
		stepSummaries = new HashMap<String, Summary>();
	}

	public Map<String, Summary> getFeatureSummaries() {
		return featureSummaries;
	}

	public Map<String, Summary> getScenarioSummaries() {
		return featureSummaries;
	}

	public Map<String, Summary> getStepSummaries() {
		return featureSummaries;
	}

	private PerformanceEntry processStep(Step step) {
		PerformanceEntry stepEntry = new PerformanceEntry();
		if (step.getResult().getStatus().equals(STEP_SKIPPED)) {
			stepEntry.setElapsedTime(0);
			stepEntry.setSkippedSteps(1);
		} else if (step.getResult().getStatus().equals(STEP_FAILED)) {
			stepEntry.setElapsedTime(step.getResult().getDuration());
			stepEntry.setFailedSteps(1);
		} else {
			stepEntry.setElapsedTime(step.getResult().getDuration());
			stepEntry.setPassedSteps(1);
		}
		return stepEntry;
	}

	private void updateSummaryDataFromLowerSummary(Summary upperSummary,
			Summary lowerSummary) {
		// update the count fields
		upperSummary.addToFailedSteps(lowerSummary.getFailedSteps());
		upperSummary.addToPassedSteps(lowerSummary.getPassedSteps());
		upperSummary.addToSkippedSteps(lowerSummary.getSkippedSteps());
		// check the duration fields
		if (lowerSummary.getShortestDuration() < upperSummary
				.getShortestDuration()) {
			upperSummary
					.setShortestDuration(lowerSummary.getShortestDuration());
		}
		if (lowerSummary.getLongestDuration() > upperSummary
				.getLongestDuration()) {
			upperSummary.setLongestDuration(lowerSummary.getLongestDuration());
		}
		// add the entry to the list
		upperSummary.getEntries().addAll(lowerSummary.getEntries());
	}

	private void updateSummaryDataFromEntry(Summary summary,
			PerformanceEntry entry) {
		// update the count fields
		summary.addToFailedSteps(entry.getFailedSteps());
		summary.addToPassedSteps(entry.getPassedSteps());
		summary.addToSkippedSteps(entry.getSkippedSteps());
		// check the duration fields
		if (entry.getElapsedTime() > 0
				&& entry.getElapsedTime() < summary.getShortestDuration()) {
			summary.setShortestDuration(entry.getElapsedTime());
		}
		if (entry.getElapsedTime() > summary.getLongestDuration()) {
			summary.setLongestDuration(entry.getElapsedTime());
		}
		// add the entry to the list
		summary.getEntries().add(entry);
	}

	private Summary getRelevantSummary(String id, String name,
			Map<String, Summary> summaries) {
		// find the right step summary to use
		Summary summary = null;
		if (summaries.containsKey(id)) {
			// exists - use it
			summary = summaries.get(id);
		} else {
			// doesn't exist - create it
			summary = new Summary();
			summary.setId(id);
			summary.setName(name);
			List<PerformanceEntry> entries = new ArrayList<PerformanceEntry>();
			summary.setEntries(entries);
			summaries.put(id, summary);
		}
		// add the new entry to the list
		return summary;
	}

}
