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
		for (Feature feature : projectRun.getFeatures()) {
			for (Elements scenario : feature.getElements()) {
				Summary scenarioSummary = getRelevantSummary(scenario.getId(),
						scenario.getName(), scenarioSummaries);
				// PerformanceEntry scenarioPerformanceEntry =
				// createPerformanceEntry(projectRun
				// .getBuildNumber());
				for (Step step : scenario.getSteps()) {
					Summary stepSummary = processStep(step,
							projectRun.getBuildNumber());
					scenarioSummary.getSummaries().add(stepSummary);
				}
				// process the step summaries to get the scenario summaries
				updateScenarioSummary(scenarioSummary);
			}
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

	private void updateScenarioSummary(Summary scenarioSummary) {
		// update count
		scenarioSummary.incrementTotalBuilds();
		// go through the list of step summaries to assemble counts
		for (Summary stepSummary : scenarioSummary.getSummaries()) {
			scenarioSummary.addToPassedSteps(stepSummary.getPassedSteps());
			scenarioSummary.addToFailedSteps(stepSummary.getFailedSteps());
			scenarioSummary.addToSkippedSteps(stepSummary.getSkippedSteps());
		}
	}

	private Summary processStep(Step step, int buildNumber) {
		String currentStepId = step.getName().replaceAll("\\s+", "_");
		Summary stepSummary = getRelevantSummary(currentStepId, step.getName(),
				stepSummaries);
		PerformanceEntry stepPerformanceEntry = createPerformanceEntry(buildNumber);
		long duration;
		if (step.getResult().getStatus().equals(STEP_SKIPPED)) {
			duration = 0;
			stepPerformanceEntry.setPassed(false);
			stepSummary.addToSkippedSteps(1);

		} else if (step.getResult().getStatus().equals(STEP_FAILED)) {
			duration = step.getResult().getDuration();
			stepPerformanceEntry.setPassed(false);
			stepSummary.addToFailedSteps(1);
		} else {
			duration = step.getResult().getDuration();
			stepPerformanceEntry.setPassed(true);
			stepSummary.addToPassedSteps(1);
		}
		stepPerformanceEntry.setElapsedTime(duration);
		updateSummaryData(stepSummary, stepPerformanceEntry);
		return stepSummary;
	}

	private void updateSummaryData(Summary summary, PerformanceEntry entry) {
		// update the count fields

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

	private PerformanceEntry createPerformanceEntry(int buildNumber) {
		PerformanceEntry performanceEntry = new PerformanceEntry();
		performanceEntry.setBuildNumber(buildNumber);
		return performanceEntry;
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
