package com.castlemon.jenkins.performance.reporting;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.castlemon.jenkins.performance.TestUtils;
import com.castlemon.jenkins.performance.domain.Scenario;
import com.castlemon.jenkins.performance.domain.reporting.ProjectPerformanceEntry;
import com.castlemon.jenkins.performance.domain.reporting.ProjectRun;
import com.castlemon.jenkins.performance.domain.reporting.ScenarioPerformanceEntry;
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
		List<Scenario> scenarios = CucumberPerfUtils.getData(jsonString);
		Assert.assertFalse(scenarios.isEmpty());
		ProjectRun run = new ProjectRun();
		run.setScenarios(scenarios);
		Date date = new Date();
		run.setRunDate(date);
		run.setBuildNumber(112);
		jobReporter.initialiseScenarioEntries();
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
		List<Scenario> scenarios = CucumberPerfUtils.getData(jsonString);
		Assert.assertFalse(scenarios.isEmpty());
		ProjectRun run = new ProjectRun();
		run.setScenarios(scenarios);
		Date date = new Date();
		run.setRunDate(date);
		run.setBuildNumber(113);
		jobReporter.initialiseScenarioEntries();
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
		jobReporter.initialiseScenarioEntries();
		ProjectPerformanceEntry jobOutput = jobReporter
				.generateBasicProjectPerformanceData(run);
		Assert.assertEquals(date, jobOutput.getRunDate());
		Assert.assertFalse(jobOutput.isPassedBuild());
	}

	@Test
	public void testScenarioPerformanceEntry() throws IOException {
		long expectedDuration = 192349832481l;
		String jsonString = testUtils.loadJsonFile("/cucumber-success.json");
		Assert.assertNotNull(jsonString);
		List<Scenario> scenarios = CucumberPerfUtils.getData(jsonString);
		Assert.assertFalse(scenarios.isEmpty());
		ProjectRun run = new ProjectRun();
		run.setScenarios(scenarios);
		Date date = new Date();
		run.setRunDate(date);
		run.setBuildNumber(112);
		jobReporter.initialiseScenarioEntries();
		ProjectPerformanceEntry jobOutput = jobReporter
				.generateBasicProjectPerformanceData(run);
		Assert.assertEquals(date, jobOutput.getRunDate());
		Assert.assertEquals(expectedDuration, jobOutput.getElapsedTime());
		Assert.assertTrue(jobOutput.isPassedBuild());
		Map<String, List<ScenarioPerformanceEntry>> scenarioEntries = jobReporter
				.getScenarioEntries();
		Assert.assertEquals(2, scenarioEntries.size());
		for (Scenario scenario : scenarios) {
			Assert.assertEquals("error on count of scenarios called "
					+ scenario.getId(), 1, scenarioEntries
					.get(scenario.getId()).size());
		}
	}

}
