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

public class ProjectReporter {

	private static final String STEP_PASSED = "passed";
	private static final String STEP_FAILED = "failed";
	private static final String STEP_SKIPPED = "skipped";

	Map<String, List<ScenarioPerformanceEntry>> scenarioEntries;

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
			ScenarioPerformanceEntry scenarioPerformanceEntry = createScenarioPerformanceEntry(
					scenario, projectRun.getBuildNumber());
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
			addScenarioPerformanceDataToList(scenarioPerformanceEntry);
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
		scenarioEntries = new HashMap<String, List<ScenarioPerformanceEntry>>();
	}

	public Map<String, List<ScenarioPerformanceEntry>> getScenarioEntries() {
		return scenarioEntries;
	}

	private ScenarioPerformanceEntry createScenarioPerformanceEntry(
			Scenario scenario, int buildNumber) {
		ScenarioPerformanceEntry scenarioPerformanceEntry = new ScenarioPerformanceEntry();
		scenarioPerformanceEntry.setScenarioId(scenario.getId());
		scenarioPerformanceEntry.setBuildNumber(buildNumber);
		return scenarioPerformanceEntry;
	}

	private void addScenarioPerformanceDataToList(
			ScenarioPerformanceEntry scenarioPerformanceEntry) {
		// firstly, find the right scenario list to update
		List<ScenarioPerformanceEntry> entries = null;
		if (scenarioEntries.containsKey(scenarioPerformanceEntry
				.getScenarioId())) {
			// exists - use it
			entries = scenarioEntries.get(scenarioPerformanceEntry
					.getScenarioId());
		} else {
			// doesn't exist - create it
			entries = new ArrayList<ScenarioPerformanceEntry>();
			scenarioEntries.put(scenarioPerformanceEntry.getScenarioId(),
					entries);
		}
		// add the new entry to the list
		entries.add(scenarioPerformanceEntry);
	}
}
