package com.castlemon.jenkins.performance.reporting;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.castlemon.jenkins.performance.TestUtils;
import com.castlemon.jenkins.performance.domain.Elements;
import com.castlemon.jenkins.performance.domain.Feature;
import com.castlemon.jenkins.performance.domain.Step;
import com.castlemon.jenkins.performance.domain.reporting.ProjectRun;
import com.castlemon.jenkins.performance.domain.reporting.Summary;
import com.castlemon.jenkins.performance.util.CucumberPerfUtils;

public class CalculationsTest {

	/*
	 * This class contains tests to check the arithmetic and calculcations over
	 * different scenarios
	 */

	private TestUtils testUtils = new TestUtils();

	private PerformanceReporter performanceReporter = new PerformanceReporter();

	/*
	 * Used only to gather the duration information to import into Excel
	 */
	//@Test
	public void getFigures() throws IOException {
		String jsonString = testUtils.loadJsonFile("/cuc-error1.json");
		Assert.assertNotNull(jsonString);
		List<Feature> features = CucumberPerfUtils.getData(jsonString);
		for (Feature feature : features) {
			System.out.println("feature: " + feature.getId());
			for (Elements scenario : feature.getElements()) {
				System.out.println("    scenario: " + scenario.getId());
				for (Step step : scenario.getSteps()) {
					long duration = 0l;
					String status = null;
					if (step.getResult() != null) {
						status = step.getResult().getStatus();
						if (!status.equals(PerformanceReporter.STEP_SKIPPED)) {
							duration = step.getResult().getDuration();
						} else {
							duration = 0l;
						}
					}
					System.out.println("        step: " + scenario.getId()
							+ "    " + duration + "    " + status);
				}
				System.out.println("    **** scenario end");
			}
			System.out.println("**** feature end");
		}
	}

	@Test
	public void testCucLarge1Solo() throws IOException {
		String jsonString = testUtils.loadJsonFile("/cuc-large-1.json");
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
		Assert.assertEquals(378363603637l, jobOutput.getEntries().get(0)
				.getElapsedTime());
		Assert.assertTrue(jobOutput.getEntries().get(0).isPassed());
		Assert.assertEquals(1, jobOutput.getPassedBuilds());
		Assert.assertEquals(0, jobOutput.getFailedBuilds());
		Assert.assertEquals(378363603637l, jobOutput.getLongestDuration());
		Assert.assertEquals(378363603637l, jobOutput.getShortestDuration());
		Assert.assertEquals(378363603637l, jobOutput.calculateAverageDuration());
		Assert.assertEquals("6 mins 18 secs 363 ms",
				jobOutput.getFormattedLongestDuration());
		Assert.assertEquals("6 mins 18 secs 363 ms",
				jobOutput.getFormattedShortestDuration());
		Assert.assertEquals("6 mins 18 secs 363 ms",
				jobOutput.getFormattedAverageDuration());
		Assert.assertEquals(313, jobOutput.getPassedSteps());
		Assert.assertEquals(0, jobOutput.getFailedSteps());
		Map<String, Summary> featureSummaries = performanceReporter
				.getFeatureSummaries();
		Assert.assertEquals(3, featureSummaries.size());
		String complexKey = features.get(0).getId() + features.get(0).getId();
		Summary mainSummary = featureSummaries.get(complexKey);
		Assert.assertEquals(1, mainSummary.getPassedBuilds());
		Assert.assertEquals(0, mainSummary.getFailedBuilds());
		Assert.assertEquals(9, mainSummary.getNumberOfSubItems()); // scenarios
		Assert.assertEquals(1, mainSummary.getEntries().size());
		Map<String, Summary> scenarioSummaries = performanceReporter
				.getScenarioSummaries();
		Assert.assertEquals(17, scenarioSummaries.size());
		// test first scenario summary
		String complexId = features.get(0).getId()
				+ features.get(0).getElements().get(0).getId();
		Summary firstScenarioSummary = scenarioSummaries.get(complexId);
		Assert.assertEquals(23251577640l,
				firstScenarioSummary.getShortestDuration());
		Assert.assertEquals(23251577640l,
				firstScenarioSummary.getLongestDuration());
		Assert.assertEquals(23251577640l,
				firstScenarioSummary.calculateAverageDuration());
		// Test average calculations
		String buildData = CucumberPerfUtils.buildGraphData(jobOutput);
		String averageData = CucumberPerfUtils.buildAverageData(jobOutput);
		long durationInSeconds = CucumberPerfUtils
				.getDurationInSeconds(378363603637l / 1000000);
		Assert.assertEquals("[[112, " + durationInSeconds + "]]", buildData);
		Assert.assertEquals("[[112, " + durationInSeconds + "]]", averageData);
	}

	@Test
	public void testCucLarge2Solo() throws IOException {
		String jsonString = testUtils.loadJsonFile("/cuc-large-2.json");
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
		Assert.assertEquals(392534015855l, jobOutput.getEntries().get(0)
				.getElapsedTime());
		Assert.assertTrue(jobOutput.getEntries().get(0).isPassed());
		Assert.assertEquals(1, jobOutput.getPassedBuilds());
		Assert.assertEquals(0, jobOutput.getFailedBuilds());
		Assert.assertEquals(392534015855l, jobOutput.getLongestDuration());
		Assert.assertEquals(392534015855l, jobOutput.getShortestDuration());
		Assert.assertEquals(392534015855l, jobOutput.calculateAverageDuration());
		Assert.assertEquals("6 mins 32 secs 534 ms",
				jobOutput.getFormattedLongestDuration());
		Assert.assertEquals("6 mins 32 secs 534 ms",
				jobOutput.getFormattedShortestDuration());
		Assert.assertEquals("6 mins 32 secs 534 ms",
				jobOutput.getFormattedAverageDuration());
		Assert.assertEquals(313, jobOutput.getPassedSteps());
		Assert.assertEquals(0, jobOutput.getFailedSteps());
		Map<String, Summary> featureSummaries = performanceReporter
				.getFeatureSummaries();
		Assert.assertEquals(3, featureSummaries.size());
		String complexKey = features.get(0).getId() + features.get(0).getId();
		Summary mainSummary = featureSummaries.get(complexKey);
		Assert.assertEquals(1, mainSummary.getPassedBuilds());
		Assert.assertEquals(0, mainSummary.getFailedBuilds());
		Assert.assertEquals(9, mainSummary.getNumberOfSubItems()); // scenarios
		Assert.assertEquals(1, mainSummary.getEntries().size());
		Map<String, Summary> scenarioSummaries = performanceReporter
				.getScenarioSummaries();
		Assert.assertEquals(17, scenarioSummaries.size());
		// test first scenario summary
		String complexId = features.get(0).getId()
				+ features.get(0).getElements().get(0).getId();
		Summary firstScenarioSummary = scenarioSummaries.get(complexId);
		Assert.assertEquals(20518023874l,
				firstScenarioSummary.getShortestDuration());
		Assert.assertEquals(20518023874l,
				firstScenarioSummary.getLongestDuration());
		Assert.assertEquals(20518023874l,
				firstScenarioSummary.calculateAverageDuration());
		// Test average calculations
		String buildData = CucumberPerfUtils.buildGraphData(jobOutput);
		String averageData = CucumberPerfUtils.buildAverageData(jobOutput);
		long durationInSeconds = CucumberPerfUtils
				.getDurationInSeconds(392534015855l / 1000000);
		Assert.assertEquals("[[112, " + durationInSeconds + "]]", buildData);
		Assert.assertEquals("[[112, " + durationInSeconds + "]]", averageData);
	}

	@Test
	public void testCucLarge3Solo() throws IOException {
		String jsonString = testUtils.loadJsonFile("/cuc-large-3.json");
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
		Assert.assertEquals(113596081587l, jobOutput.getEntries().get(0)
				.getElapsedTime());
		Assert.assertTrue(jobOutput.getEntries().get(0).isPassed());
		Assert.assertEquals(1, jobOutput.getPassedBuilds());
		Assert.assertEquals(0, jobOutput.getFailedBuilds());
		Assert.assertEquals(113596081587l, jobOutput.getLongestDuration());
		Assert.assertEquals(113596081587l, jobOutput.getShortestDuration());
		Assert.assertEquals(113596081587l, jobOutput.calculateAverageDuration());
		Assert.assertEquals("1 min 53 secs 596 ms",
				jobOutput.getFormattedLongestDuration());
		Assert.assertEquals("1 min 53 secs 596 ms",
				jobOutput.getFormattedShortestDuration());
		Assert.assertEquals("1 min 53 secs 596 ms",
				jobOutput.getFormattedAverageDuration());
		Assert.assertEquals(313, jobOutput.getPassedSteps());
		Assert.assertEquals(0, jobOutput.getFailedSteps());
		Map<String, Summary> featureSummaries = performanceReporter
				.getFeatureSummaries();
		Assert.assertEquals(3, featureSummaries.size());
		String complexKey = features.get(0).getId() + features.get(0).getId();
		Summary mainSummary = featureSummaries.get(complexKey);
		Assert.assertEquals(1, mainSummary.getPassedBuilds());
		Assert.assertEquals(0, mainSummary.getFailedBuilds());
		Assert.assertEquals(9, mainSummary.getNumberOfSubItems()); // scenarios
		Assert.assertEquals(1, mainSummary.getEntries().size());
		Map<String, Summary> scenarioSummaries = performanceReporter
				.getScenarioSummaries();
		Assert.assertEquals(17, scenarioSummaries.size());
		// test first scenario summary
		String complexId = features.get(0).getId()
				+ features.get(0).getElements().get(0).getId();
		Summary firstScenarioSummary = scenarioSummaries.get(complexId);
		Assert.assertEquals(9704227551l,
				firstScenarioSummary.getShortestDuration());
		Assert.assertEquals(9704227551l,
				firstScenarioSummary.getLongestDuration());
		Assert.assertEquals(9704227551l,
				firstScenarioSummary.calculateAverageDuration());
		// Test average calculations
		String buildData = CucumberPerfUtils.buildGraphData(jobOutput);
		String averageData = CucumberPerfUtils.buildAverageData(jobOutput);
		long durationInSeconds = CucumberPerfUtils
				.getDurationInSeconds(113596081587l / 1000000);
		Assert.assertEquals("[[112, " + durationInSeconds + "]]", buildData);
		Assert.assertEquals("[[112, " + durationInSeconds + "]]", averageData);
	}

	@Test
	public void testCuc1and2Combined() throws IOException {
		String jsonString1 = testUtils.loadJsonFile("/cuc-large-1.json");
		Assert.assertNotNull(jsonString1);
		List<Feature> features1 = CucumberPerfUtils.getData(jsonString1);
		Assert.assertFalse(features1.isEmpty());
		String jsonString2 = testUtils.loadJsonFile("/cuc-large-2.json");
		Assert.assertNotNull(jsonString2);
		List<Feature> features2 = CucumberPerfUtils.getData(jsonString2);
		Assert.assertFalse(features2.isEmpty());
		List<ProjectRun> runs = new ArrayList<ProjectRun>();
		ProjectRun run1 = new ProjectRun();
		run1.setFeatures(features1);
		Date date = new Date();
		run1.setRunDate(date);
		run1.setBuildNumber(112);
		runs.add(run1);
		ProjectRun run2 = new ProjectRun();
		run2.setFeatures(features2);
		run2.setRunDate(date);
		run2.setBuildNumber(113);
		runs.add(run2);
		performanceReporter.initialiseEntryMaps();
		Summary jobOutput = performanceReporter.getPerformanceData(runs);
		Assert.assertEquals(2, jobOutput.getEntries().size());
		Assert.assertEquals(378363603637l, jobOutput.getEntries().get(0)
				.getElapsedTime());
		Assert.assertTrue(jobOutput.getEntries().get(0).isPassed());
		Assert.assertEquals(392534015855l, jobOutput.getEntries().get(1)
				.getElapsedTime());
		Assert.assertTrue(jobOutput.getEntries().get(1).isPassed());
		Assert.assertEquals(392534015855l, jobOutput.getLongestDuration());
		Assert.assertEquals(378363603637l, jobOutput.getShortestDuration());
		Assert.assertEquals(385448809746l, jobOutput.calculateAverageDuration());
		Assert.assertEquals(626, jobOutput.getPassedSteps());
		Assert.assertEquals(0, jobOutput.getFailedSteps());
		// feature testing
		Map<String, Summary> featureSummaries = performanceReporter
				.getFeatureSummaries();
		Assert.assertEquals(3, featureSummaries.size());
		// test first feature summary
		String firstComplexKey = features1.get(0).getId()
				+ features1.get(0).getId();
		Summary firstFeatureSummary = featureSummaries.get(firstComplexKey);
		Assert.assertEquals(2, firstFeatureSummary.getPassedBuilds());
		Assert.assertEquals(0, firstFeatureSummary.getFailedBuilds());
		Assert.assertEquals(2, firstFeatureSummary.getEntries().size());
		Assert.assertEquals(9, firstFeatureSummary.getNumberOfSubItems());
		Assert.assertEquals(213221533518l,
				firstFeatureSummary.getShortestDuration());
		Assert.assertEquals(228590612449l,
				firstFeatureSummary.getLongestDuration());
		Assert.assertEquals(220906072983l,
				firstFeatureSummary.calculateAverageDuration());
		// test second feature summary
		String secondComplexKey = features1.get(1).getId()
				+ features1.get(1).getId();
		Summary secondFeatureSummary = featureSummaries.get(secondComplexKey);
		Assert.assertEquals(2, secondFeatureSummary.getPassedBuilds());
		Assert.assertEquals(0, secondFeatureSummary.getFailedBuilds());
		Assert.assertEquals(2, secondFeatureSummary.getEntries().size());
		Assert.assertEquals(7, secondFeatureSummary.getNumberOfSubItems());
		Assert.assertEquals(161835571903l,
				secondFeatureSummary.getShortestDuration());
		Assert.assertEquals(163627555004l,
				secondFeatureSummary.getLongestDuration());
		Assert.assertEquals(162731563453l,
				secondFeatureSummary.calculateAverageDuration());
		// test third feature summary
		String thirdComplexKey = features1.get(2).getId()
				+ features1.get(2).getId();
		Summary thirdFeatureSummary = featureSummaries.get(thirdComplexKey);
		Assert.assertEquals(2, thirdFeatureSummary.getPassedBuilds());
		Assert.assertEquals(0, thirdFeatureSummary.getFailedBuilds());
		Assert.assertEquals(2, thirdFeatureSummary.getEntries().size());
		Assert.assertEquals(1, thirdFeatureSummary.getNumberOfSubItems());
		Assert.assertEquals(1514515115l,
				thirdFeatureSummary.getShortestDuration());
		Assert.assertEquals(2107831503l,
				thirdFeatureSummary.getLongestDuration());
		Assert.assertEquals(1811173309l,
				thirdFeatureSummary.calculateAverageDuration());
		// Test average calculations
		String buildData = CucumberPerfUtils.buildGraphData(jobOutput);
		String averageData = CucumberPerfUtils.buildAverageData(jobOutput);
		long durationOneInSeconds = CucumberPerfUtils
				.getDurationInSeconds(378363603637l / 1000000);
		long durationTwoInSeconds = CucumberPerfUtils
				.getDurationInSeconds(392534015855l / 1000000);
		long averageInSeconds = CucumberPerfUtils
				.getDurationInSeconds(385448809746l / 1000000);
		Assert.assertEquals("[[112, " + durationOneInSeconds + "],[113, "
				+ durationTwoInSeconds + "]]", buildData);
		Assert.assertEquals("[[112, " + averageInSeconds + "],[113, "
				+ averageInSeconds + "]]", averageData);
	}

	@Test
	public void testCuc1and2and3Combined() throws IOException {
		String jsonString1 = testUtils.loadJsonFile("/cuc-large-1.json");
		Assert.assertNotNull(jsonString1);
		List<Feature> features1 = CucumberPerfUtils.getData(jsonString1);
		Assert.assertFalse(features1.isEmpty());
		String jsonString2 = testUtils.loadJsonFile("/cuc-large-2.json");
		Assert.assertNotNull(jsonString2);
		List<Feature> features2 = CucumberPerfUtils.getData(jsonString2);
		Assert.assertFalse(features2.isEmpty());
		String jsonString3 = testUtils.loadJsonFile("/cuc-large-3.json");
		Assert.assertNotNull(jsonString3);
		List<Feature> features3 = CucumberPerfUtils.getData(jsonString3);
		Assert.assertFalse(features3.isEmpty());
		List<ProjectRun> runs = new ArrayList<ProjectRun>();
		ProjectRun run1 = new ProjectRun();
		run1.setFeatures(features1);
		Date date = new Date();
		run1.setRunDate(date);
		run1.setBuildNumber(112);
		runs.add(run1);
		ProjectRun run2 = new ProjectRun();
		run2.setFeatures(features2);
		run2.setRunDate(date);
		run2.setBuildNumber(113);
		runs.add(run2);
		ProjectRun run3 = new ProjectRun();
		run3.setFeatures(features3);
		run3.setRunDate(date);
		run3.setBuildNumber(114);
		runs.add(run3);
		performanceReporter.initialiseEntryMaps();
		Summary jobOutput = performanceReporter.getPerformanceData(runs);
		Assert.assertEquals(3, jobOutput.getEntries().size());
		Assert.assertEquals(378363603637l, jobOutput.getEntries().get(0)
				.getElapsedTime());
		Assert.assertTrue(jobOutput.getEntries().get(0).isPassed());
		Assert.assertEquals(392534015855l, jobOutput.getEntries().get(1)
				.getElapsedTime());
		Assert.assertTrue(jobOutput.getEntries().get(1).isPassed());
		Assert.assertEquals(113596081587l, jobOutput.getEntries().get(2)
				.getElapsedTime());
		Assert.assertTrue(jobOutput.getEntries().get(2).isPassed());

		Assert.assertEquals(392534015855l, jobOutput.getLongestDuration());
		Assert.assertEquals(113596081587l, jobOutput.getShortestDuration());
		Assert.assertEquals(294831233693l, jobOutput.calculateAverageDuration());
		Assert.assertEquals(939, jobOutput.getPassedSteps());
		Assert.assertEquals(0, jobOutput.getFailedSteps());
		// feature testing
		Map<String, Summary> featureSummaries = performanceReporter
				.getFeatureSummaries();
		Assert.assertEquals(3, featureSummaries.size());
		// test first feature summary
		String firstComplexKey = features1.get(0).getId()
				+ features1.get(0).getId();
		Summary firstFeatureSummary = featureSummaries.get(firstComplexKey);
		Assert.assertEquals(3, firstFeatureSummary.getPassedBuilds());
		Assert.assertEquals(0, firstFeatureSummary.getFailedBuilds());
		Assert.assertEquals(3, firstFeatureSummary.getEntries().size());
		Assert.assertEquals(9, firstFeatureSummary.getNumberOfSubItems());
		Assert.assertEquals(69743079496l,
				firstFeatureSummary.getShortestDuration());
		Assert.assertEquals(228590612449l,
				firstFeatureSummary.getLongestDuration());
		Assert.assertEquals(170518408487l,
				firstFeatureSummary.calculateAverageDuration());
		// test second feature summary
		String secondComplexKey = features1.get(1).getId()
				+ features1.get(1).getId();
		Summary secondFeatureSummary = featureSummaries.get(secondComplexKey);
		Assert.assertEquals(3, secondFeatureSummary.getPassedBuilds());
		Assert.assertEquals(0, secondFeatureSummary.getFailedBuilds());
		Assert.assertEquals(3, secondFeatureSummary.getEntries().size());
		Assert.assertEquals(7, secondFeatureSummary.getNumberOfSubItems());
		Assert.assertEquals(43841781869l,
				secondFeatureSummary.getShortestDuration());
		Assert.assertEquals(163627555004l,
				secondFeatureSummary.getLongestDuration());
		Assert.assertEquals(123101636258l,
				secondFeatureSummary.calculateAverageDuration());
		// test third feature summary
		String thirdComplexKey = features1.get(2).getId()
				+ features1.get(2).getId();
		Summary thirdFeatureSummary = featureSummaries.get(thirdComplexKey);
		Assert.assertEquals(3, thirdFeatureSummary.getPassedBuilds());
		Assert.assertEquals(0, thirdFeatureSummary.getFailedBuilds());
		Assert.assertEquals(3, thirdFeatureSummary.getEntries().size());
		Assert.assertEquals(1, thirdFeatureSummary.getNumberOfSubItems());
		Assert.assertEquals(11220222l,
				thirdFeatureSummary.getShortestDuration());
		Assert.assertEquals(2107831503l,
				thirdFeatureSummary.getLongestDuration());
		Assert.assertEquals(1211188946l,
				thirdFeatureSummary.calculateAverageDuration());
		// Test average calculations
		String buildData = CucumberPerfUtils.buildGraphData(jobOutput);
		String averageData = CucumberPerfUtils.buildAverageData(jobOutput);
		long durationOneInSeconds = CucumberPerfUtils
				.getDurationInSeconds(378363603637l / 1000000);
		long durationTwoInSeconds = CucumberPerfUtils
				.getDurationInSeconds(392534015855l / 1000000);
		long durationThreeInSeconds = CucumberPerfUtils
				.getDurationInSeconds(113596081587l / 1000000);
		long averageInSeconds = CucumberPerfUtils
				.getDurationInSeconds(294831233693l / 1000000);
		Assert.assertEquals("[[112, " + durationOneInSeconds + "],[113, "
				+ durationTwoInSeconds + "],[114, " + durationThreeInSeconds
				+ "]]", buildData);
		Assert.assertEquals("[[112, " + averageInSeconds + "],[113, "
				+ averageInSeconds + "],[114, " + averageInSeconds + "]]",
				averageData);
	}

	@Test
	public void testAllSkippedSolo() throws IOException {
		String jsonString = testUtils.loadJsonFile("/cuc-all-skipped.json");
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
		Assert.assertEquals(0l, jobOutput.getEntries().get(0).getElapsedTime());
		Assert.assertFalse(jobOutput.getEntries().get(0).isPassed());
		Assert.assertEquals(0, jobOutput.getPassedBuilds());
		Assert.assertEquals(1, jobOutput.getFailedBuilds());
		Assert.assertEquals(0l, jobOutput.getLongestDuration());
		Assert.assertEquals(Long.MAX_VALUE, jobOutput.getShortestDuration());
		Assert.assertEquals(0l, jobOutput.calculateAverageDuration());
		Assert.assertEquals("0 ns", jobOutput.getFormattedLongestDuration());
		Assert.assertEquals("0 ns", jobOutput.getFormattedShortestDuration());
		Assert.assertEquals("0 ns", jobOutput.getFormattedAverageDuration());
		Assert.assertEquals(0, jobOutput.getPassedSteps());
		Assert.assertEquals(0, jobOutput.getFailedSteps());
		Assert.assertEquals(313, jobOutput.getSkippedSteps());
		Map<String, Summary> featureSummaries = performanceReporter
				.getFeatureSummaries();
		Assert.assertEquals(3, featureSummaries.size());
		String complexKey = features.get(0).getId() + features.get(0).getId();
		Summary mainSummary = featureSummaries.get(complexKey);
		Assert.assertEquals(0, mainSummary.getPassedBuilds());
		Assert.assertEquals(1, mainSummary.getFailedBuilds());
		Assert.assertEquals(9, mainSummary.getNumberOfSubItems()); // scenarios
		Assert.assertEquals(1, mainSummary.getEntries().size());
		Map<String, Summary> scenarioSummaries = performanceReporter
				.getScenarioSummaries();
		Assert.assertEquals(17, scenarioSummaries.size());
		// test first scenario summary
		String complexId = features.get(0).getId()
				+ features.get(0).getElements().get(0).getId();
		Summary firstScenarioSummary = scenarioSummaries.get(complexId);
		Assert.assertEquals(Long.MAX_VALUE,
				firstScenarioSummary.getShortestDuration());
		Assert.assertEquals(0l, firstScenarioSummary.getLongestDuration());
		Assert.assertEquals(0l, firstScenarioSummary.calculateAverageDuration());
		// Test average calculations
		Assert.assertEquals("[]", CucumberPerfUtils.buildGraphData(jobOutput));
		Assert.assertEquals("[]", CucumberPerfUtils.buildAverageData(jobOutput));
	}

	@Test
	public void testCucLarge1AndSkipped() throws IOException {
		String jsonString = testUtils.loadJsonFile("/cuc-large-1.json");
		Assert.assertNotNull(jsonString);
		List<Feature> features1 = CucumberPerfUtils.getData(jsonString);
		Assert.assertFalse(features1.isEmpty());
		String jsonStringS = testUtils.loadJsonFile("/cuc-all-skipped.json");
		Assert.assertNotNull(jsonStringS);
		List<Feature> features2 = CucumberPerfUtils.getData(jsonStringS);
		List<ProjectRun> runs = new ArrayList<ProjectRun>();
		ProjectRun run = new ProjectRun();
		run.setFeatures(features1);
		Date date = new Date();
		run.setRunDate(date);
		run.setBuildNumber(112);
		runs.add(run);
		ProjectRun run2 = new ProjectRun();
		run2.setFeatures(features2);
		run2.setRunDate(date);
		run2.setBuildNumber(1123);
		runs.add(run2);
		performanceReporter.initialiseEntryMaps();
		Summary jobOutput = performanceReporter.getPerformanceData(runs);
		Assert.assertEquals(2, jobOutput.getEntries().size());
		Assert.assertEquals(378363603637l, jobOutput.getEntries().get(0)
				.getElapsedTime());
		Assert.assertTrue(jobOutput.getEntries().get(0).isPassed());
		Assert.assertEquals(0l, jobOutput.getEntries().get(1).getElapsedTime());
		Assert.assertFalse(jobOutput.getEntries().get(1).isPassed());
		Assert.assertEquals(1, jobOutput.getPassedBuilds());
		Assert.assertEquals(1, jobOutput.getFailedBuilds());
		Assert.assertEquals(378363603637l, jobOutput.getLongestDuration());
		Assert.assertEquals(378363603637l, jobOutput.getShortestDuration());
		Assert.assertEquals(378363603637l, jobOutput.calculateAverageDuration());
		Assert.assertEquals("6 mins 18 secs 363 ms",
				jobOutput.getFormattedLongestDuration());
		Assert.assertEquals("6 mins 18 secs 363 ms",
				jobOutput.getFormattedShortestDuration());
		Assert.assertEquals("6 mins 18 secs 363 ms",
				jobOutput.getFormattedAverageDuration());
		Assert.assertEquals(313, jobOutput.getPassedSteps());
		Assert.assertEquals(0, jobOutput.getFailedSteps());
		Map<String, Summary> featureSummaries = performanceReporter
				.getFeatureSummaries();
		Assert.assertEquals(3, featureSummaries.size());
		String complexKey = features1.get(0).getId() + features1.get(0).getId();
		Summary mainSummary = featureSummaries.get(complexKey);
		Assert.assertEquals(1, mainSummary.getPassedBuilds());
		Assert.assertEquals(1, mainSummary.getFailedBuilds());
		Assert.assertEquals(9, mainSummary.getNumberOfSubItems()); // scenarios
		Assert.assertEquals(2, mainSummary.getEntries().size());
		Map<String, Summary> scenarioSummaries = performanceReporter
				.getScenarioSummaries();
		Assert.assertEquals(17, scenarioSummaries.size());
		// test first scenario summary
		String complexId = features1.get(0).getId()
				+ features1.get(0).getElements().get(0).getId();
		Summary firstScenarioSummary = scenarioSummaries.get(complexId);
		Assert.assertEquals(23251577640l,
				firstScenarioSummary.getShortestDuration());
		Assert.assertEquals(23251577640l,
				firstScenarioSummary.getLongestDuration());
		Assert.assertEquals(23251577640l,
				firstScenarioSummary.calculateAverageDuration());
	}

	@Test
	public void testCucLarge1FailSolo() throws IOException {
		String jsonString = testUtils.loadJsonFile("/cuc-large-1-fail.json");
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
		Assert.assertEquals(378278303637l, jobOutput.getEntries().get(0)
				.getElapsedTime());
		Assert.assertFalse(jobOutput.getEntries().get(0).isPassed());
		Assert.assertEquals(0, jobOutput.getPassedBuilds());
		Assert.assertEquals(1, jobOutput.getFailedBuilds());
		Assert.assertEquals(0l, jobOutput.getLongestDuration());
		Assert.assertEquals(Long.MAX_VALUE, jobOutput.getShortestDuration());
		Assert.assertEquals(0l, jobOutput.calculateAverageDuration());
		Assert.assertEquals("0 ns", jobOutput.getFormattedLongestDuration());
		Assert.assertEquals("0 ns", jobOutput.getFormattedShortestDuration());
		Assert.assertEquals("0 ns", jobOutput.getFormattedAverageDuration());
		Assert.assertEquals(312, jobOutput.getPassedSteps());
		Assert.assertEquals(1, jobOutput.getFailedSteps());
		Assert.assertEquals(0, jobOutput.getSkippedSteps());
		Map<String, Summary> featureSummaries = performanceReporter
				.getFeatureSummaries();
		Assert.assertEquals(3, featureSummaries.size());
		String complexKey = features.get(0).getId() + features.get(0).getId();
		Summary mainSummary = featureSummaries.get(complexKey);
		Assert.assertEquals(0, mainSummary.getPassedBuilds());
		Assert.assertEquals(1, mainSummary.getFailedBuilds());
		Assert.assertEquals(9, mainSummary.getNumberOfSubItems()); // scenarios
		Assert.assertEquals(1, mainSummary.getEntries().size());
		Map<String, Summary> scenarioSummaries = performanceReporter
				.getScenarioSummaries();
		Assert.assertEquals(17, scenarioSummaries.size());
	}

	@Test
	public void testCuc1and1FailCombined() throws IOException {
		String jsonString1 = testUtils.loadJsonFile("/cuc-large-1.json");
		Assert.assertNotNull(jsonString1);
		List<Feature> features1 = CucumberPerfUtils.getData(jsonString1);
		Assert.assertFalse(features1.isEmpty());
		String jsonString2 = testUtils.loadJsonFile("/cuc-large-1-fail.json");
		Assert.assertNotNull(jsonString2);
		List<Feature> features2 = CucumberPerfUtils.getData(jsonString2);
		Assert.assertFalse(features2.isEmpty());
		List<ProjectRun> runs = new ArrayList<ProjectRun>();
		ProjectRun run1 = new ProjectRun();
		run1.setFeatures(features1);
		Date date = new Date();
		run1.setRunDate(date);
		run1.setBuildNumber(112);
		runs.add(run1);
		ProjectRun run2 = new ProjectRun();
		run2.setFeatures(features2);
		run2.setRunDate(date);
		run2.setBuildNumber(113);
		runs.add(run2);
		performanceReporter.initialiseEntryMaps();
		Summary jobOutput = performanceReporter.getPerformanceData(runs);
		Assert.assertEquals(2, jobOutput.getEntries().size());
		Assert.assertEquals(378363603637l, jobOutput.getEntries().get(0)
				.getElapsedTime());
		Assert.assertTrue(jobOutput.getEntries().get(0).isPassed());
		Assert.assertEquals(378278303637l, jobOutput.getEntries().get(1)
				.getElapsedTime());
		Assert.assertFalse(jobOutput.getEntries().get(1).isPassed());
		Assert.assertEquals(378363603637l, jobOutput.getLongestDuration());
		Assert.assertEquals(378363603637l, jobOutput.getShortestDuration());
		Assert.assertEquals(378363603637l, jobOutput.calculateAverageDuration());
		Assert.assertEquals(625, jobOutput.getPassedSteps());
		Assert.assertEquals(1, jobOutput.getFailedSteps());
		// feature testing
		Map<String, Summary> featureSummaries = performanceReporter
				.getFeatureSummaries();
		Assert.assertEquals(3, featureSummaries.size());
		// test first feature summary
		String firstComplexKey = features1.get(0).getId()
				+ features1.get(0).getId();
		Summary firstFeatureSummary = featureSummaries.get(firstComplexKey);
		Assert.assertEquals(1, firstFeatureSummary.getPassedBuilds());
		Assert.assertEquals(1, firstFeatureSummary.getFailedBuilds());
		Assert.assertEquals(2, firstFeatureSummary.getEntries().size());
		Assert.assertEquals(9, firstFeatureSummary.getNumberOfSubItems());
		Assert.assertEquals(213221533518l,
				firstFeatureSummary.getShortestDuration());
		Assert.assertEquals(213221533518l,
				firstFeatureSummary.getLongestDuration());
		Assert.assertEquals(213221533518l,
				firstFeatureSummary.calculateAverageDuration());
		// test second feature summary
		String secondComplexKey = features1.get(1).getId()
				+ features1.get(1).getId();
		Summary secondFeatureSummary = featureSummaries.get(secondComplexKey);
		Assert.assertEquals(2, secondFeatureSummary.getPassedBuilds());
		Assert.assertEquals(0, secondFeatureSummary.getFailedBuilds());
		Assert.assertEquals(2, secondFeatureSummary.getEntries().size());
		Assert.assertEquals(7, secondFeatureSummary.getNumberOfSubItems());
		Assert.assertEquals(163627555004l,
				secondFeatureSummary.getShortestDuration());
		Assert.assertEquals(163627555004l,
				secondFeatureSummary.getLongestDuration());
		Assert.assertEquals(163627555004l,
				secondFeatureSummary.calculateAverageDuration());
		// test third feature summary
		String thirdComplexKey = features1.get(2).getId()
				+ features1.get(2).getId();
		Summary thirdFeatureSummary = featureSummaries.get(thirdComplexKey);
		Assert.assertEquals(2, thirdFeatureSummary.getPassedBuilds());
		Assert.assertEquals(0, thirdFeatureSummary.getFailedBuilds());
		Assert.assertEquals(2, thirdFeatureSummary.getEntries().size());
		Assert.assertEquals(1, thirdFeatureSummary.getNumberOfSubItems());
		Assert.assertEquals(1514515115l,
				thirdFeatureSummary.getShortestDuration());
		Assert.assertEquals(1514515115l,
				thirdFeatureSummary.getLongestDuration());
		Assert.assertEquals(1514515115l,
				thirdFeatureSummary.calculateAverageDuration());
		// Test average calculations
		String buildData = CucumberPerfUtils.buildGraphData(jobOutput);
		String averageData = CucumberPerfUtils.buildAverageData(jobOutput);
		long durationInSeconds = CucumberPerfUtils
				.getDurationInSeconds(378363603637l / 1000000);
		Assert.assertEquals("[[112, " + durationInSeconds + "],]", buildData);
		Assert.assertEquals("[[112, " + durationInSeconds + "],]", averageData);
	}

}
