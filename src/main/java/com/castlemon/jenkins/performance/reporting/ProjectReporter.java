package com.castlemon.jenkins.performance.reporting;

import com.castlemon.jenkins.performance.domain.Elements;
import com.castlemon.jenkins.performance.domain.Scenario;
import com.castlemon.jenkins.performance.domain.Step;
import com.castlemon.jenkins.performance.domain.reporting.ProjectPerformanceEntry;
import com.castlemon.jenkins.performance.domain.reporting.ProjectRun;

public class ProjectReporter {

	/*
	 * accepts a list of Scenarios from 1 run of a project, and assembles a
	 * PerformanceEntry object
	 */

	private static final String STEP_PASSED = "passed";
	private static final String STEP_FAILED = "failed";
	private static final String STEP_SKIPPED = "skipped";

	public ProjectPerformanceEntry generateBasicProjectPerformanceData(
			ProjectRun projectRun) {
		ProjectPerformanceEntry output = new ProjectPerformanceEntry();
		output.setRunDate(projectRun.getRunDate());
		output.setBuildNumber(projectRun.getBuildNumber());
		long durationSum = 0;
		int passedSteps = 0;
		int failedSteps = 0;
		int skippedSteps = 0;
		for (Scenario scenario : projectRun.getScenarios()) {
			for (Elements element : scenario.getElements()) {
				for (Step step : element.getSteps()) {
					long duration;
					if (step.getResult().getStatus().equals(STEP_SKIPPED)) {
						duration = 0;
						skippedSteps++;
					} else if (step.getResult().getStatus().equals(STEP_FAILED)) {
						duration = step.getResult().getDuration();
						failedSteps++;
					} else {
						duration = step.getResult().getDuration();
						passedSteps++;
					}
					durationSum = durationSum + duration;
				}
			}
		}
		output.setElapsedTime(durationSum);
		output.setFailedSteps(failedSteps);
		output.setPassedSteps(passedSteps);
		output.setSkippedSteps(skippedSteps);
		if (failedSteps == 0 && skippedSteps == 0) {
			output.setPassedBuild(true);
		} else {
			output.setPassedBuild(false);
		}
		return output;
	}
}
