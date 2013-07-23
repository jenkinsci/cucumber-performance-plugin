package com.castlemon.jenkins.performance.reporting;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
		Summary jobOutput = performanceReporter.getPerformanceData(runs, "112");
		Assert.assertEquals(1, jobOutput.getEntries().size());
		Assert.assertEquals(date, jobOutput.getEntries().get(0).getRunDate());
		Assert.assertEquals(expectedDuration, jobOutput.getEntries().get(0)
				.getElapsedTime());
		Assert.assertTrue(jobOutput.getEntries().get(0).isPassed());
		Assert.assertEquals(1, jobOutput.getPassedBuilds());
		Assert.assertEquals(0, jobOutput.getFailedBuilds());
		Assert.assertEquals(192349832481l, jobOutput.getLongestDuration());
		Assert.assertEquals(0, jobOutput.getShortestDuration());
		Assert.assertEquals(55, jobOutput.getPassedSteps());
		Assert.assertEquals(0, jobOutput.getFailedSteps());
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
		Summary jobOutput = performanceReporter.getPerformanceData(runs, "113");
		Assert.assertEquals(date, jobOutput.getEntries().get(0).getRunDate());
		Assert.assertEquals(expectedDuration, jobOutput.getEntries().get(0)
				.getElapsedTime());
		Assert.assertFalse(jobOutput.getEntries().get(0).isPassed());
		Assert.assertEquals(0, jobOutput.getPassedBuilds());
		Assert.assertEquals(1, jobOutput.getFailedBuilds());
		Assert.assertEquals(204151315589l, jobOutput.getLongestDuration());
		Assert.assertEquals(0, jobOutput.getShortestDuration());
		Assert.assertEquals(52, jobOutput.getPassedSteps());
		Assert.assertEquals(2, jobOutput.getFailedSteps());
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
		Summary jobOutput = performanceReporter.getPerformanceData(runs, "114");
		Assert.assertEquals(date, jobOutput.getEntries().get(0).getRunDate());
		Assert.assertFalse(jobOutput.getEntries().get(0).isPassed());
	}

	@Test
	public void testProcessStep() throws IOException {
		String jsonString = testUtils.loadJsonFile("/cucumber-success.json");
		Assert.assertNotNull(jsonString);
		List<Feature> features = CucumberPerfUtils.getData(jsonString);
		Assert.assertFalse(features.isEmpty());
		PerformanceEntry stepEntry = performanceReporter.processStep(features
				.get(0).getElements().get(0).getSteps().get(0));
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
		PerformanceEntry stepEntry = performanceReporter
				.processScenario(features.get(0).getElements().get(0));
		Assert.assertEquals(17383328936l, stepEntry.getElapsedTime());
		Assert.assertEquals(5, stepEntry.getPassedSteps());
		Assert.assertEquals(0, stepEntry.getFailedSteps());
		Assert.assertEquals(0, stepEntry.getSkippedSteps());
		Assert.assertTrue(stepEntry.isPassed());
	}

	@Test
	public void testProcessFeature() throws IOException {
		String jsonString = testUtils.loadJsonFile("/cucumber-success.json");
		Assert.assertNotNull(jsonString);
		List<Feature> features = CucumberPerfUtils.getData(jsonString);
		Assert.assertFalse(features.isEmpty());
		PerformanceEntry stepEntry = performanceReporter
				.processFeature(features.get(0));
		Assert.assertEquals(191183691567l, stepEntry.getElapsedTime());
		Assert.assertEquals(54, stepEntry.getPassedSteps());
		Assert.assertEquals(0, stepEntry.getFailedSteps());
		Assert.assertEquals(0, stepEntry.getSkippedSteps());
		Assert.assertTrue(stepEntry.isPassed());
	}

	@Test
	public void testProcessRun() throws IOException {
		String jsonString = testUtils.loadJsonFile("/cucumber-success.json");
		Assert.assertNotNull(jsonString);
		List<Feature> features = CucumberPerfUtils.getData(jsonString);
		Assert.assertFalse(features.isEmpty());
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
