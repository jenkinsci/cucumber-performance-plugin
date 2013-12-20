package com.castlemon.jenkins.performance.reporting;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.castlemon.jenkins.performance.domain.Elements;
import com.castlemon.jenkins.performance.domain.Feature;
import com.castlemon.jenkins.performance.domain.Result;
import com.castlemon.jenkins.performance.domain.Step;
import com.castlemon.jenkins.performance.domain.reporting.PerformanceEntry;
import com.castlemon.jenkins.performance.domain.reporting.ProjectRun;
import com.castlemon.jenkins.performance.domain.reporting.Summary;

public class PerformanceReporter {

	public static final String STEP_FAILED = "failed";
	public static final String STEP_SKIPPED = "skipped";
	public static final String STEP_PASSED = "passed";

	private static final String ROWS = "rows";

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
			PerformanceEntry runPerformanceEntry = processRun(run);
			passedSteps += runPerformanceEntry.getPassedSteps();
			failedSteps += runPerformanceEntry.getFailedSteps();
			skippedSteps += runPerformanceEntry.getSkippedSteps();
			if (runPerformanceEntry.isPassed()) {
				passedBuilds++;
				if (runPerformanceEntry.getElapsedTime() < projectSummary
						.getShortestDuration()) {
					projectSummary.setShortestDuration(runPerformanceEntry
							.getElapsedTime());
				}
				if (runPerformanceEntry.getElapsedTime() > projectSummary
						.getLongestDuration()) {
					projectSummary.setLongestDuration(runPerformanceEntry
							.getElapsedTime());
				}
			} else {
				failedBuilds++;
			}
			entries.add(runPerformanceEntry);
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
		int orderParam = 0;
		for (Feature feature : projectRun.getFeatures()) {
			PerformanceEntry featureEntry = processFeature(feature,
					projectRun.getRunDate(), projectRun.getBuildNumber(),
					orderParam);
			passedSteps += featureEntry.getPassedSteps();
			failedSteps += featureEntry.getFailedSteps();
			skippedSteps += featureEntry.getSkippedSteps();
			elapsedTime += featureEntry.getElapsedTime();
			orderParam++;
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
			int buildNumber, int higherOrderParam) {
		Summary featureSummary = getRelevantSummary(feature.getId(),
				feature.getId(), feature.getName(), featureSummaries,
				higherOrderParam, feature.getElements().size(),
				feature.getName());
		PerformanceEntry featureEntry = new PerformanceEntry();
		featureEntry.setRunDate(runDate);
		featureEntry.setBuildNumber(buildNumber);
		int passedSteps = 0;
		int failedSteps = 0;
		int skippedSteps = 0;
		long elapsedTime = 0l;
		int orderParam = 0;
		for (Elements scenario : feature.getElements()) {
			PerformanceEntry scenarioEntry = processScenario(scenario, runDate,
					buildNumber, feature.getId(), orderParam, feature.getName());
			passedSteps += scenarioEntry.getPassedSteps();
			failedSteps += scenarioEntry.getFailedSteps();
			skippedSteps += scenarioEntry.getSkippedSteps();
			elapsedTime += scenarioEntry.getElapsedTime();
			updateSummaryDataFromEntry(featureSummary, scenarioEntry);
			orderParam++;
		}
		featureEntry.setElapsedTime(elapsedTime);
		featureEntry.setPassedSteps(passedSteps);
		featureEntry.setFailedSteps(failedSteps);
		featureEntry.setSkippedSteps(skippedSteps);
		if (failedSteps == 0 && skippedSteps == 0) {
			featureEntry.setPassed(true);
			featureSummary.incrementPassedBuilds();
			// check the duration fields
			if (featureEntry.getElapsedTime() > 0
					&& featureEntry.getElapsedTime() < featureSummary
							.getShortestDuration()) {
				featureSummary.setShortestDuration(featureEntry
						.getElapsedTime());
			}
			if (featureEntry.getElapsedTime() > featureSummary
					.getLongestDuration()) {
				featureSummary
						.setLongestDuration(featureEntry.getElapsedTime());
			}
		} else {
			featureSummary.incrementFailedBuilds();
		}
		featureSummary.setTotalBuilds(featureSummary.getPassedBuilds()
				+ featureSummary.getFailedBuilds());
		featureSummary.getEntries().add(featureEntry);
		return featureEntry;
	}

	protected PerformanceEntry processScenario(Elements scenario, Date runDate,
			int buildNumber, String featureId, int higherOrderParam,
			String featureName) {
		Summary scenarioSummary = getRelevantSummary(scenario.getId(),
				featureId, scenario.getName(), scenarioSummaries,
				higherOrderParam, scenario.getSteps().size(), featureName);
		PerformanceEntry scenarioEntry = new PerformanceEntry();
		scenarioEntry.setRunDate(runDate);
		scenarioEntry.setBuildNumber(buildNumber);
		int passedSteps = 0;
		int failedSteps = 0;
		int skippedSteps = 0;
		long elapsedTime = 0l;
		int orderParam = 0;
		for (Step step : scenario.getSteps()) {
			PerformanceEntry stepEntry = processStep(step, runDate,
					buildNumber, scenario.getId(), orderParam,
					scenario.getName());
			passedSteps += stepEntry.getPassedSteps();
			failedSteps += stepEntry.getFailedSteps();
			skippedSteps += stepEntry.getSkippedSteps();
			elapsedTime += stepEntry.getElapsedTime();
			updateSummaryDataFromEntry(scenarioSummary, stepEntry);
			orderParam++;
		}
		scenarioEntry.setElapsedTime(elapsedTime);
		scenarioEntry.setPassedSteps(passedSteps);
		scenarioEntry.setFailedSteps(failedSteps);
		scenarioEntry.setSkippedSteps(skippedSteps);
		if (failedSteps == 0 && skippedSteps == 0) {
			scenarioEntry.setPassed(true);
			scenarioSummary.incrementPassedBuilds();
			// check the duration fields
			if (scenarioEntry.getElapsedTime() > 0
					&& scenarioEntry.getElapsedTime() < scenarioSummary
							.getShortestDuration()) {
				scenarioSummary.setShortestDuration(scenarioEntry
						.getElapsedTime());
			}
			if (scenarioEntry.getElapsedTime() > scenarioSummary
					.getLongestDuration()) {
				scenarioSummary.setLongestDuration(scenarioEntry
						.getElapsedTime());
			}
		} else {
			scenarioSummary.incrementFailedBuilds();
		}
		scenarioSummary.getEntries().add(scenarioEntry);
		return scenarioEntry;
	}

	@SuppressWarnings("unchecked")
	protected PerformanceEntry processStep(Step step, Date runDate,
			int buildNumber, String scenarioId, int orderParam,
			String scenarioName) {
		Summary stepSummary = getRelevantSummary(step.getName(), scenarioId,
				step.getName(), stepSummaries, orderParam, 1, scenarioName);
		if (StringUtils.isEmpty(stepSummary.getKeyword())) {
			stepSummary.setKeyword(step.getKeyword());
		}
		if (step.getAdditionalProperties().size() > 0
				&& stepSummary.getRows() == null) {
			List<List<String>> rows = new ArrayList<List<String>>();
			List<Map<String, List<String>>> rawRows = (List<Map<String, List<String>>>) step
					.getAdditionalProperties().get(ROWS);
			if (rawRows != null) {
				for (Map<String, List<String>> row : rawRows) {
					List<String> cells = row.get("cells");
					rows.add(cells);
				}
			}
			stepSummary.setRows(rows);

		}
		PerformanceEntry stepEntry = new PerformanceEntry();
		stepEntry.setRunDate(runDate);
		stepEntry.setBuildNumber(buildNumber);
		Result result = step.getResult();
		if (result == null) {
			System.err.println("No result for step: " + step);
		} else {
			String status = result.getStatus();
			if (STEP_SKIPPED.equals(status)) {
				stepEntry.setElapsedTime(0);
				stepEntry.setSkippedSteps(1);
			} else if (STEP_FAILED.equals(status)) {
				stepEntry.setElapsedTime(result.getDuration());
				stepEntry.setFailedSteps(1);
				stepSummary.incrementFailedBuilds();
			} else if (STEP_PASSED.equals(status)) {
				stepEntry.setElapsedTime(result.getDuration());
				stepEntry.setPassedSteps(1);
				stepEntry.setPassed(true);
				stepSummary.incrementFailedBuilds();
			} else {
				System.err.println("Unexpected build result: '" + status + "'");
			}
		}
		// check the duration fields
		if (stepEntry.isPassed()) {
			if (stepEntry.getElapsedTime() > 0
					&& stepEntry.getElapsedTime() < stepSummary
							.getShortestDuration()) {
				stepSummary.setShortestDuration(stepEntry.getElapsedTime());
			}
			if (stepEntry.getElapsedTime() > stepSummary.getLongestDuration()) {
				stepSummary.setLongestDuration(stepEntry.getElapsedTime());
			}
		}
		stepSummary.getEntries().add(stepEntry);
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
		return scenarioSummaries;
	}

	public Map<String, Summary> getStepSummaries() {
		return stepSummaries;
	}

	private void updateSummaryDataFromEntry(Summary summary,
			PerformanceEntry entry) {
		// update the count fields
		summary.addToFailedSteps(entry.getFailedSteps());
		summary.addToPassedSteps(entry.getPassedSteps());
		summary.addToSkippedSteps(entry.getSkippedSteps());
		// summary.addToTotalDuration(entry.getElapsedTime());
	}

	private Summary getRelevantSummary(String id, String seniorId, String name,
			Map<String, Summary> summaries, int orderParam, int subItemCount,
			String seniorName) {
		// generate the key for the map
		String complexKey = seniorId + id;
		// find the right step summary to use
		Summary summary = null;
		if (summaries.containsKey(complexKey)) {
			// exists - use it
			summary = summaries.get(complexKey);
		} else {
			// doesn't exist - create it
			summary = new Summary();
			summary.setId(id);
			summary.setSeniorId(seniorId);
			summary.setSeniorName(seniorName);
			summary.setName(name);
			summary.setOrder(orderParam);
			summary.setNumberOfSubItems(subItemCount);
			List<PerformanceEntry> entries = new ArrayList<PerformanceEntry>();
			summary.setEntries(entries);
			summaries.put(complexKey, summary);
		}
		// add the new entry to the list
		return summary;
	}

}
