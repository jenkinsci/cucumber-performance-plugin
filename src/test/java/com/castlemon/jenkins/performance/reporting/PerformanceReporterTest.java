package com.castlemon.jenkins.performance.reporting;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.castlemon.jenkins.performance.TestUtils;
import com.castlemon.jenkins.performance.domain.Feature;
import com.castlemon.jenkins.performance.domain.reporting.PerformanceEntry;
import com.castlemon.jenkins.performance.domain.reporting.ProjectRun;
import com.castlemon.jenkins.performance.domain.reporting.Summary;
import com.castlemon.jenkins.performance.util.CucumberPerfUtils;

public class PerformanceReporterTest {

	private PerformanceReporter performanceReporter = new PerformanceReporter();

	private TestUtils testUtils = new TestUtils();

	@Test
	public void testGetPerformanceDataSuccess() throws IOException {
		long expectedDuration = 192349832481l;
		String jsonString = testUtils.loadJsonFile("/cucumber-success.json");
		Assert.assertNotNull(jsonString);
		List<Feature> features = CucumberPerfUtils.getData(jsonString);
		Assert.assertFalse(features.isEmpty());
		List<ProjectRun> runs = new ArrayList<ProjectRun>();
		ProjectRun run = new ProjectRun();
		run.setFeatures(features);
		Date date = new Date();
		run.setRunDate(date);
		run.setBuildNumber(112);
		runs.add(run);
		performanceReporter.initialiseEntryMaps();
		Summary jobOutput = performanceReporter.getPerformanceData(runs);
		Assert.assertEquals(1, jobOutput.getEntries().size());
		Assert.assertEquals(date, jobOutput.getEntries().get(0).getRunDate());
		Assert.assertEquals(expectedDuration, jobOutput.getEntries().get(0)
				.getElapsedTime());
		Assert.assertTrue(jobOutput.getEntries().get(0).isPassed());
		Assert.assertEquals(1, jobOutput.getPassedBuilds());
		Assert.assertEquals(0, jobOutput.getFailedBuilds());
		Assert.assertEquals(192349832481l, jobOutput.getLongestDuration());
		Assert.assertEquals(192349832481l, jobOutput.getShortestDuration());
		Assert.assertEquals(192349832481l, jobOutput.calculateAverageDuration());
		Assert.assertEquals("3 mins 12 secs 349 ms",
				jobOutput.getFormattedLongestDuration());
		Assert.assertEquals("3 mins 12 secs 349 ms",
				jobOutput.getFormattedShortestDuration());
		Assert.assertEquals("3 mins 12 secs 349 ms",
				jobOutput.getFormattedAverageDuration());
		Assert.assertEquals(55, jobOutput.getPassedSteps());
		Assert.assertEquals(0, jobOutput.getFailedSteps());
		Map<String, Summary> featureSummaries = performanceReporter
				.getFeatureSummaries();
		Assert.assertEquals(2, featureSummaries.size());
		String complexKey = features.get(0).getId() + features.get(0).getId();
		Summary mainSummary = featureSummaries.get(complexKey);
		Assert.assertEquals(1, mainSummary.getPassedBuilds());
		Assert.assertEquals(0, mainSummary.getFailedBuilds());
		Assert.assertEquals(8, mainSummary.getNumberOfSubItems());
		Assert.assertEquals(1, mainSummary.getEntries().size());
		Map<String, Summary> scenarioSummaries = performanceReporter
				.getScenarioSummaries();
		Assert.assertEquals(9, scenarioSummaries.size());
		// test first scenario summary
		String complexId = features.get(0).getId()
				+ features.get(0).getElements().get(0).getId();
		Summary firstScenarioSummary = scenarioSummaries.get(complexId);
		Assert.assertEquals(17383328936l,
				firstScenarioSummary.getShortestDuration());
		Assert.assertEquals(17383328936l,
				firstScenarioSummary.getLongestDuration());
		Assert.assertEquals(17383328936l,
				firstScenarioSummary.calculateAverageDuration());
	}

	@Test
	public void testGetPerformanceDataFailure() throws IOException {
		long expectedDuration = 204151315589l;
		String jsonString = testUtils.loadJsonFile("/cucumber-failure.json");
		Assert.assertNotNull(jsonString);
		List<Feature> features = CucumberPerfUtils.getData(jsonString);
		Assert.assertFalse(features.isEmpty());
		List<ProjectRun> runs = new ArrayList<ProjectRun>();
		ProjectRun run = new ProjectRun();
		run.setFeatures(features);
		Date date = new Date();
		run.setRunDate(date);
		run.setBuildNumber(113);
		runs.add(run);
		performanceReporter.initialiseEntryMaps();
		Summary jobOutput = performanceReporter.getPerformanceData(runs);
		Assert.assertEquals(date, jobOutput.getEntries().get(0).getRunDate());
		Assert.assertEquals(expectedDuration, jobOutput.getEntries().get(0)
				.getElapsedTime());
		Assert.assertFalse(jobOutput.getEntries().get(0).isPassed());
		Assert.assertEquals(0, jobOutput.getPassedBuilds());
		Assert.assertEquals(1, jobOutput.getFailedBuilds());
		Assert.assertEquals(204151315589l, jobOutput.getLongestDuration());
		Assert.assertEquals(204151315589l, jobOutput.getShortestDuration());
		Assert.assertEquals(52, jobOutput.getPassedSteps());
		Assert.assertEquals(2, jobOutput.getFailedSteps());
		Map<String, Summary> featureSummaries = performanceReporter
				.getFeatureSummaries();
		Assert.assertEquals(2, featureSummaries.size());
	}

	@Test
	public void testGenerateBasicProjectPerformanceDataSkippedSteps()
			throws IOException {
		List<ProjectRun> runs = new ArrayList<ProjectRun>();
		ProjectRun run = testUtils.generateRun("skipped");
		Date date = new Date();
		run.setRunDate(date);
		run.setBuildNumber(114);
		runs.add(run);
		performanceReporter.initialiseEntryMaps();
		Summary jobOutput = performanceReporter.getPerformanceData(runs);
		Assert.assertEquals(date, jobOutput.getEntries().get(0).getRunDate());
		Assert.assertFalse(jobOutput.getEntries().get(0).isPassed());
	}

	@Test
	public void testProcessStep() throws IOException {
		String jsonString = testUtils.loadJsonFile("/cucumber-success.json");
		Assert.assertNotNull(jsonString);
		List<Feature> features = CucumberPerfUtils.getData(jsonString);
		Assert.assertFalse(features.isEmpty());
		Date date = new Date();
		performanceReporter.initialiseEntryMaps();
		PerformanceEntry stepEntry = performanceReporter.processStep(features
				.get(0).getElements().get(0).getSteps().get(0), date, 115);
		Assert.assertEquals(10957080635l, stepEntry.getElapsedTime());
		Assert.assertEquals(1, stepEntry.getPassedSteps());
		Assert.assertEquals(0, stepEntry.getFailedSteps());
		Assert.assertEquals(0, stepEntry.getSkippedSteps());
		Assert.assertTrue(stepEntry.isPassed());
	}

	@Test
	public void testProcessScenario() throws IOException {
		String jsonString = testUtils.loadJsonFile("/cucumber-success.json");
		Assert.assertNotNull(jsonString);
		List<Feature> features = CucumberPerfUtils.getData(jsonString);
		Assert.assertFalse(features.isEmpty());
		Date date = new Date();
		performanceReporter.initialiseEntryMaps();
		PerformanceEntry scenarioEntry = performanceReporter.processScenario(
				features.get(0).getElements().get(0), date, 116, features
						.get(0).getId(), 0);
		Assert.assertEquals(17383328936l, scenarioEntry.getElapsedTime());
		Assert.assertEquals(5, scenarioEntry.getPassedSteps());
		Assert.assertEquals(0, scenarioEntry.getFailedSteps());
		Assert.assertEquals(0, scenarioEntry.getSkippedSteps());
		Assert.assertTrue(scenarioEntry.isPassed());
	}

	@Test
	public void testProcessFeature() throws IOException {
		String jsonString = testUtils.loadJsonFile("/cucumber-success.json");
		Assert.assertNotNull(jsonString);
		List<Feature> features = CucumberPerfUtils.getData(jsonString);
		Assert.assertFalse(features.isEmpty());
		Date date = new Date();
		performanceReporter.initialiseEntryMaps();
		PerformanceEntry featureEntry = performanceReporter.processFeature(
				features.get(0), date, 117, 0);
		Assert.assertEquals(191183691567l, featureEntry.getElapsedTime());
		Assert.assertEquals(54, featureEntry.getPassedSteps());
		Assert.assertEquals(0, featureEntry.getFailedSteps());
		Assert.assertEquals(0, featureEntry.getSkippedSteps());
		Assert.assertTrue(featureEntry.isPassed());
		Map<String, Summary> featureSummaries = performanceReporter
				.getFeatureSummaries();
		Assert.assertEquals(1, featureSummaries.size());
		Map<String, Summary> scenarioSummaries = performanceReporter
				.getScenarioSummaries();
		Assert.assertEquals(8, scenarioSummaries.size());
	}

	@Test
	public void testProcessRun() throws IOException {
		String jsonString = testUtils.loadJsonFile("/cucumber-success.json");
		Assert.assertNotNull(jsonString);
		List<Feature> features = CucumberPerfUtils.getData(jsonString);
		Assert.assertFalse(features.isEmpty());
		performanceReporter.initialiseEntryMaps();
		ProjectRun run = new ProjectRun();
		run.setFeatures(features);
		PerformanceEntry stepEntry = performanceReporter.processRun(run);
		Assert.assertEquals(192349832481l, stepEntry.getElapsedTime());
		Assert.assertEquals(55, stepEntry.getPassedSteps());
		Assert.assertEquals(0, stepEntry.getFailedSteps());
		Assert.assertEquals(0, stepEntry.getSkippedSteps());
		Assert.assertTrue(stepEntry.isPassed());
	}

}
