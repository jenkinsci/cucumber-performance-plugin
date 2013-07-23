package com.castlemon.jenkins.performance.reporting;

import java.util.ArrayList;
import java.util.Date;
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

	public Summary getPerformanceData(List<ProjectRun> runs) {
		Summary projectSummary = new Summary();
		List<PerformanceEntry> entries = new ArrayList<PerformanceEntry>();
		int passedSteps = 0;
		int failedSteps = 0;
		int skippedSteps = 0;
		int passedBuilds = 0;
		int failedBuilds = 0;
		for (ProjectRun run : runs) {
			PerformanceEntry performanceEntry = processRun(run);
			passedSteps += performanceEntry.getPassedSteps();
			failedSteps += performanceEntry.getFailedSteps();
			skippedSteps += performanceEntry.getSkippedSteps();
			if (performanceEntry.isPassed()) {
				passedBuilds++;
			} else {
				failedBuilds++;
			}
			if (performanceEntry.getElapsedTime() < projectSummary
					.getShortestDuration()) {
				projectSummary.setShortestDuration(performanceEntry
						.getElapsedTime());
			} else if (performanceEntry.getElapsedTime() > projectSummary
					.getLongestDuration()) {
				projectSummary.setLongestDuration(performanceEntry
						.getElapsedTime());
			}
			entries.add(performanceEntry);
		}
		projectSummary.setPassedBuilds(passedBuilds);
		projectSummary.setFailedBuilds(failedBuilds);
		projectSummary.setTotalBuilds(passedBuilds + failedBuilds);
		projectSummary.setPassedSteps(passedSteps);
		projectSummary.setFailedSteps(failedSteps);
		projectSummary.setSkippedSteps(skippedSteps);
		projectSummary.setEntries(entries);
		return projectSummary;
	}

	protected PerformanceEntry processRun(ProjectRun projectRun) {
		PerformanceEntry runEntry = new PerformanceEntry();
		runEntry.setRunDate(projectRun.getRunDate());
		runEntry.setBuildNumber(projectRun.getBuildNumber());
		int passedSteps = 0;
		int failedSteps = 0;
		int skippedSteps = 0;
		long elapsedTime = 0l;
		for (Feature feature : projectRun.getFeatures()) {
			PerformanceEntry featureEntry = processFeature(feature,
					projectRun.getRunDate(), projectRun.getBuildNumber());
			passedSteps += featureEntry.getPassedSteps();
			failedSteps += featureEntry.getFailedSteps();
			skippedSteps += featureEntry.getSkippedSteps();
			elapsedTime += featureEntry.getElapsedTime();
		}
		runEntry.setElapsedTime(elapsedTime);
		runEntry.setPassedSteps(passedSteps);
		runEntry.setFailedSteps(failedSteps);
		runEntry.setSkippedSteps(skippedSteps);
		if (failedSteps == 0 && skippedSteps == 0) {
			runEntry.setPassed(true);
		}
		return runEntry;
	}

	protected PerformanceEntry processFeature(Feature feature, Date runDate,
			int buildNumber) {
		Summary featureSummary = getRelevantSummary(feature.getId(),
				feature.getName(), featureSummaries);
		PerformanceEntry featureEntry = new PerformanceEntry();
		featureEntry.setRunDate(runDate);
		featureEntry.setBuildNumber(buildNumber);
		int passedSteps = 0;
		int failedSteps = 0;
		int skippedSteps = 0;
		long elapsedTime = 0l;
		for (Elements scenario : feature.getElements()) {
			PerformanceEntry scenarioEntry = processScenario(scenario, runDate,
					buildNumber);
			passedSteps += scenarioEntry.getPassedSteps();
			failedSteps += scenarioEntry.getFailedSteps();
			skippedSteps += scenarioEntry.getSkippedSteps();
			elapsedTime += scenarioEntry.getElapsedTime();
			updateSummaryDataFromEntry(featureSummary, scenarioEntry);
		}
		featureEntry.setElapsedTime(elapsedTime);
		featureEntry.setPassedSteps(passedSteps);
		featureEntry.setFailedSteps(failedSteps);
		featureEntry.setSkippedSteps(skippedSteps);
		if (failedSteps == 0 && skippedSteps == 0) {
			featureEntry.setPassed(true);
			featureSummary.incrementPassedBuilds();
		} else {
			featureSummary.incrementFailedBuilds();
		}
		return featureEntry;
	}

	protected PerformanceEntry processScenario(Elements scenario, Date runDate,
			int buildNumber) {
		Summary scenarioSummary = getRelevantSummary(scenario.getId(),
				scenario.getName(), scenarioSummaries);
		PerformanceEntry scenarioEntry = new PerformanceEntry();
		scenarioEntry.setRunDate(runDate);
		scenarioEntry.setBuildNumber(buildNumber);
		// List<PerformanceEntry> entries = new ArrayList<PerformanceEntry>();
		int passedSteps = 0;
		int failedSteps = 0;
		int skippedSteps = 0;
		long elapsedTime = 0l;
		for (Step step : scenario.getSteps()) {
			PerformanceEntry stepEntry = processStep(step, runDate, buildNumber);
			passedSteps += stepEntry.getPassedSteps();
			failedSteps += stepEntry.getFailedSteps();
			skippedSteps += stepEntry.getSkippedSteps();
			elapsedTime += stepEntry.getElapsedTime();
			updateSummaryDataFromEntry(scenarioSummary, stepEntry);
		}
		scenarioEntry.setElapsedTime(elapsedTime);
		scenarioEntry.setPassedSteps(passedSteps);
		scenarioEntry.setFailedSteps(failedSteps);
		scenarioEntry.setSkippedSteps(skippedSteps);
		if (failedSteps == 0 && skippedSteps == 0) {
			scenarioEntry.setPassed(true);
		}
		return scenarioEntry;
	}

	protected PerformanceEntry processStep(Step step, Date runDate,
			int buildNumber) {
		PerformanceEntry stepEntry = new PerformanceEntry();
		stepEntry.setRunDate(runDate);
		stepEntry.setBuildNumber(buildNumber);
		if (step.getResult().getStatus().equals(STEP_SKIPPED)) {
			stepEntry.setElapsedTime(0);
			stepEntry.setSkippedSteps(1);
		} else if (step.getResult().getStatus().equals(STEP_FAILED)) {
			stepEntry.setElapsedTime(step.getResult().getDuration());
			stepEntry.setFailedSteps(1);
		} else {
			stepEntry.setElapsedTime(step.getResult().getDuration());
			stepEntry.setPassedSteps(1);
			stepEntry.setPassed(true);
		}
		return stepEntry;
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
