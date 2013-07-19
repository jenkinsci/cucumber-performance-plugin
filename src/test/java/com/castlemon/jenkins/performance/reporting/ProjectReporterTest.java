package com.castlemon.jenkins.performance.reporting;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.castlemon.jenkins.performance.TestUtils;
import com.castlemon.jenkins.performance.domain.Feature;
import com.castlemon.jenkins.performance.domain.reporting.ProjectPerformanceEntry;
import com.castlemon.jenkins.performance.domain.reporting.ProjectRun;
import com.castlemon.jenkins.performance.domain.reporting.FeatureSummary;
import com.castlemon.jenkins.performance.util.CucumberPerfUtils;

public class ProjectReporterTest {

	private ProjectReporter jobReporter = new ProjectReporter();

	private TestUtils testUtils = new TestUtils();

	@Test
	public void testGenerateBasicProjectPerformanceDataSuccess()
			throws IOException {
		long expectedDuration = 192349832481l;
		String jsonString = testUtils.loadJsonFile("/cucumber-success.json");
		Assert.assertNotNull(jsonString);
		List<Feature> features = CucumberPerfUtils.getData(jsonString);
		Assert.assertFalse(features.isEmpty());
		ProjectRun run = new ProjectRun();
		run.setFeatures(features);
		Date date = new Date();
		run.setRunDate(date);
		run.setBuildNumber(112);
		jobReporter.initialiseFeatureEntries();
		jobReporter.initialiseStepEntries();
		ProjectPerformanceEntry jobOutput = jobReporter
				.generateBasicProjectPerformanceData(run);
		Assert.assertEquals(date, jobOutput.getRunDate());
		Assert.assertEquals(expectedDuration, jobOutput.getElapsedTime());
		Assert.assertTrue(jobOutput.isPassedBuild());
	}

	@Test
	public void testGenerateBasicProjectPerformanceDataFailure()
			throws IOException {
		long expectedDuration = 204151315589l;
		String jsonString = testUtils.loadJsonFile("/cucumber-failure.json");
		Assert.assertNotNull(jsonString);
		List<Feature> features = CucumberPerfUtils.getData(jsonString);
		Assert.assertFalse(features.isEmpty());
		ProjectRun run = new ProjectRun();
		run.setFeatures(features);
		Date date = new Date();
		run.setRunDate(date);
		run.setBuildNumber(113);
		jobReporter.initialiseFeatureEntries();
		jobReporter.initialiseStepEntries();
		ProjectPerformanceEntry jobOutput = jobReporter
				.generateBasicProjectPerformanceData(run);
		Assert.assertEquals(date, jobOutput.getRunDate());
		Assert.assertEquals(expectedDuration, jobOutput.getElapsedTime());
		Assert.assertFalse(jobOutput.isPassedBuild());
	}

	@Test
	public void testGenerateBasicProjectPerformanceDataSkippedSteps()
			throws IOException {
		ProjectRun run = testUtils.generateRun("skipped");
		Date date = new Date();
		run.setRunDate(date);
		run.setBuildNumber(114);
		jobReporter.initialiseFeatureEntries();
		jobReporter.initialiseStepEntries();
		ProjectPerformanceEntry jobOutput = jobReporter
				.generateBasicProjectPerformanceData(run);
		Assert.assertEquals(date, jobOutput.getRunDate());
		Assert.assertFalse(jobOutput.isPassedBuild());
	}

	@Test
	public void testFeaturePerformanceEntry() throws IOException {
		long expectedDuration = 192349832481l;
		String jsonString = testUtils.loadJsonFile("/cucumber-success.json");
		Assert.assertNotNull(jsonString);
		List<Feature> features = CucumberPerfUtils.getData(jsonString);
		Assert.assertFalse(features.isEmpty());
		ProjectRun run = new ProjectRun();
		run.setFeatures(features);
		Date date = new Date();
		run.setRunDate(date);
		run.setBuildNumber(112);
		jobReporter.initialiseFeatureEntries();
		jobReporter.initialiseStepEntries();
		ProjectPerformanceEntry jobOutput = jobReporter
				.generateBasicProjectPerformanceData(run);
		Assert.assertEquals(date, jobOutput.getRunDate());
		Assert.assertEquals(expectedDuration, jobOutput.getElapsedTime());
		Assert.assertTrue(jobOutput.isPassedBuild());
		Map<String, FeatureSummary> featureEntries = jobReporter
				.getFeatureEntries();
		Assert.assertEquals(2, featureEntries.size());
		for (Feature feature : features) {
			Assert.assertEquals(
					"error on count of features called " + feature.getId(), 1,
					featureEntries.get(feature.getId()).getEntries().size());
		}
	}

}
