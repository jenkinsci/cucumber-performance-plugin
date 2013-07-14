package com.castlemon.jenkins.performance.reporting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.castlemon.jenkins.performance.domain.Elements;
import com.castlemon.jenkins.performance.domain.Scenario;
import com.castlemon.jenkins.performance.domain.Step;
import com.castlemon.jenkins.performance.domain.reporting.ProjectPerformanceEntry;
import com.castlemon.jenkins.performance.domain.reporting.ProjectRun;
import com.castlemon.jenkins.performance.domain.reporting.ScenarioPerformanceEntry;
import com.castlemon.jenkins.performance.domain.reporting.ScenarioSummary;

public class ProjectReporter {

	private static final String STEP_PASSED = "passed";
	private static final String STEP_FAILED = "failed";
	private static final String STEP_SKIPPED = "skipped";

	Map<String, ScenarioSummary> scenarioEntries;

	public ProjectPerformanceEntry generateBasicProjectPerformanceData(
			ProjectRun projectRun) {
		ProjectPerformanceEntry output = new ProjectPerformanceEntry();
		output.setRunDate(projectRun.getRunDate());
		output.setBuildNumber(projectRun.getBuildNumber());
		long projectDuration = 0;
		int projectPassedSteps = 0;
		int projectFailedSteps = 0;
		int projectSkippedSteps = 0;
		for (Scenario scenario : projectRun.getScenarios()) {
			ScenarioSummary scenarioSummary = getRelevantScenarioSummary(scenario);
			ScenarioPerformanceEntry scenarioPerformanceEntry = createScenarioPerformanceEntry(
					scenario.getId(), projectRun.getBuildNumber());
			long scenarioDuration = 0;
			int scenarioPassedSteps = 0;
			int scenarioFailedSteps = 0;
			int scenarioSkippedSteps = 0;
			for (Elements element : scenario.getElements()) {
				for (Step step : element.getSteps()) {
					long duration;
					if (step.getResult().getStatus().equals(STEP_SKIPPED)) {
						duration = 0;
						scenarioSkippedSteps++;
					} else if (step.getResult().getStatus().equals(STEP_FAILED)) {
						duration = step.getResult().getDuration();
						scenarioFailedSteps++;
					} else {
						duration = step.getResult().getDuration();
						scenarioPassedSteps++;
					}
					scenarioDuration += duration;
				}
			}
			scenarioPerformanceEntry.setPassedSteps(scenarioPassedSteps);
			scenarioPerformanceEntry.setFailedSteps(scenarioFailedSteps);
			scenarioPerformanceEntry.setSkippedSteps(scenarioSkippedSteps);
			scenarioPerformanceEntry.setElapsedTime(scenarioDuration);
			updateScenarioData(scenarioSummary, scenarioPerformanceEntry);
			projectDuration += scenarioDuration;
			projectPassedSteps += scenarioPassedSteps;
			projectFailedSteps += scenarioFailedSteps;
			projectSkippedSteps += scenarioSkippedSteps;
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

	public void initialiseScenarioEntries() {
		scenarioEntries = new HashMap<String, ScenarioSummary>();
	}

	public Map<String, ScenarioSummary> getScenarioEntries() {
		return scenarioEntries;
	}

	private void updateScenarioData(ScenarioSummary summary,
			ScenarioPerformanceEntry entry) {
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

	private ScenarioPerformanceEntry createScenarioPerformanceEntry(
			String scenarioId, int buildNumber) {
		ScenarioPerformanceEntry scenarioPerformanceEntry = new ScenarioPerformanceEntry();
		scenarioPerformanceEntry.setScenarioId(scenarioId);
		scenarioPerformanceEntry.setBuildNumber(buildNumber);
		return scenarioPerformanceEntry;
	}

	private ScenarioSummary getRelevantScenarioSummary(Scenario scenario) {
		// find the right scenario summary to use
		ScenarioSummary scenarioSummary = null;
		if (scenarioEntries.containsKey(scenario.getId())) {
			// exists - use it
			scenarioSummary = scenarioEntries.get(scenario.getId());
		} else {
			// doesn't exist - create it
			scenarioSummary = new ScenarioSummary();
			scenarioSummary.setScenarioId(scenario.getId());
			scenarioSummary.setScenarioName(scenario.getName());
			List<ScenarioPerformanceEntry> entries = new ArrayList<ScenarioPerformanceEntry>();
			scenarioSummary.setEntries(entries);
			scenarioEntries.put(scenario.getId(), scenarioSummary);
		}
		// add the new entry to the list
		return scenarioSummary;
	}
}
