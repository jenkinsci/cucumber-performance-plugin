package com.castlemon.jenkins.performance.domain.reporting;

import org.agileware.test.PropertiesTester;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class ProjectSummaryTest {

	@Test
	public void testProperties() throws Exception {
		PropertiesTester tester = new PropertiesTester();
		tester.testAll(ProjectSummary.class);
	}

	@Test
	public void testGetFeatureSummaryList() {
		Summary summary1 = new Summary();
		Summary summary2 = new Summary();
		Map<String, Summary> featureSummaries = new HashMap<String, Summary>();
		featureSummaries.put("summary1", summary1);
		featureSummaries.put("summary2", summary2);
		ProjectSummary projectSummary = new ProjectSummary();
		projectSummary.setFeatureSummaries(featureSummaries);
		Collection<Summary> features = projectSummary.getFeatureSummaryList();
		Assert.assertEquals(2, features.size());
		Assert.assertTrue(features.contains(summary1));
		Assert.assertTrue(features.contains(summary2));
	}

	@Test
	public void testGetScenarioSummaryList() {
		Summary summary1 = new Summary();
		Summary summary2 = new Summary();
		Map<String, Summary> scenarioSummaries = new HashMap<String, Summary>();
		scenarioSummaries.put("summary1", summary1);
		scenarioSummaries.put("summary2", summary2);
		ProjectSummary projectSummary = new ProjectSummary();
		projectSummary.setScenarioSummaries(scenarioSummaries);
		Collection<Summary> scenarios = projectSummary.getScenarioSummaryList();
		Assert.assertEquals(2, scenarios.size());
		Assert.assertTrue(scenarios.contains(summary1));
		Assert.assertTrue(scenarios.contains(summary2));
	}

	@Test
	public void testGetStepSummaryList() {
		Summary summary1 = new Summary();
		Summary summary2 = new Summary();
		Map<String, Summary> stepSummaries = new HashMap<String, Summary>();
		stepSummaries.put("summary1", summary1);
		stepSummaries.put("summary2", summary2);
		ProjectSummary projectSummary = new ProjectSummary();
		projectSummary.setStepSummaries(stepSummaries);
		Collection<Summary> steps = projectSummary.getStepSummaryList();
		Assert.assertEquals(2, steps.size());
		Assert.assertTrue(steps.contains(summary1));
		Assert.assertTrue(steps.contains(summary2));
	}

	@Test
	public void testAssembleAllSummaries() {
		Summary summary1 = new Summary();
		Summary summary2 = new Summary();
		Map<String, Summary> featureSummaries1 = new HashMap<String, Summary>();
		featureSummaries1.put("summary1", summary1);
		featureSummaries1.put("summary2", summary2);
		ProjectSummary projectSummary = new ProjectSummary();
		projectSummary.setFeatureSummaries(featureSummaries1);
		Map<String, Summary> scenarioSummaries1 = new HashMap<String, Summary>();
		scenarioSummaries1.put("summary3", summary1);
		scenarioSummaries1.put("summary4", summary2);
		projectSummary.setScenarioSummaries(scenarioSummaries1);
		Map<String, Summary> stepSummaries1 = new HashMap<String, Summary>();
		stepSummaries1.put("summary5", summary1);
		stepSummaries1.put("summary6", summary2);
		projectSummary.setStepSummaries(stepSummaries1);
		Map<String, Summary> allSummaries = projectSummary
				.assembleAllSummaries();
		Assert.assertEquals(6, allSummaries.size());
	}

	@Test
	public void testGetSortedFeatureSummaryList() {
        Summary summary1 = new Summary();
        List<PerformanceEntry> entries = new ArrayList<PerformanceEntry>();
        PerformanceEntry entry1 = new PerformanceEntry();
        entry1.setPassed(true);
        entry1.setElapsedTime(1000000000l);
        entries.add(entry1);
        PerformanceEntry entry2 = new PerformanceEntry();
        entry2.setPassed(true);
        entry2.setElapsedTime(5000000000l);
        entries.add(entry2);
        summary1.setEntries(entries);
        Assert.assertEquals("3 secs", summary1.getFormattedAverageDuration());
        Summary summary2 = new Summary();
        List<PerformanceEntry> entries2 = new ArrayList<PerformanceEntry>();
        PerformanceEntry entry12 = new PerformanceEntry();
        entry12.setPassed(true);
        entry12.setElapsedTime(1000000000l);
        entries2.add(entry12);
        PerformanceEntry entry22 = new PerformanceEntry();
        entry22.setPassed(true);
        entry22.setElapsedTime(6000000000l);
        entries2.add(entry22);
        summary2.setEntries(entries2);
        Assert.assertEquals("3 secs 500 ms",
                summary2.getFormattedAverageDuration());
		Map<String, Summary> featureSummaries = new HashMap<String, Summary>();
		featureSummaries.put("summary1", summary1);
		featureSummaries.put("summary2", summary2);
		ProjectSummary projectSummary = new ProjectSummary();
        projectSummary.setNumberOfSummariesToDisplay(5);
		projectSummary.setFeatureSummaries(featureSummaries);
		Collection<Summary> features = projectSummary
				.getSortedFeatureSummaryList();
		Assert.assertEquals(2, features.size());
		Assert.assertTrue(features.contains(summary1));
		Assert.assertTrue(features.contains(summary2));
	}

    @Test
    public void testGetSortedScenarioSummaryList() {
        Summary summary1 = new Summary();
        List<PerformanceEntry> entries = new ArrayList<PerformanceEntry>();
        PerformanceEntry entry1 = new PerformanceEntry();
        entry1.setPassed(true);
        entry1.setElapsedTime(1000000000l);
        entries.add(entry1);
        PerformanceEntry entry2 = new PerformanceEntry();
        entry2.setPassed(true);
        entry2.setElapsedTime(5000000000l);
        entries.add(entry2);
        summary1.setEntries(entries);
        Assert.assertEquals("3 secs", summary1.getFormattedAverageDuration());
        Summary summary2 = new Summary();
        List<PerformanceEntry> entries2 = new ArrayList<PerformanceEntry>();
        PerformanceEntry entry12 = new PerformanceEntry();
        entry12.setPassed(true);
        entry12.setElapsedTime(1000000000l);
        entries2.add(entry12);
        PerformanceEntry entry22 = new PerformanceEntry();
        entry22.setPassed(true);
        entry22.setElapsedTime(6000000000l);
        entries2.add(entry22);
        summary2.setEntries(entries2);
        Assert.assertEquals("3 secs 500 ms",
                summary2.getFormattedAverageDuration());
        Map<String, Summary> scenarioSummaries = new HashMap<String, Summary>();
        scenarioSummaries.put("summary1", summary1);
        scenarioSummaries.put("summary2", summary2);
        ProjectSummary projectSummary = new ProjectSummary();
        projectSummary.setNumberOfSummariesToDisplay(1);
        projectSummary.setScenarioSummaries(scenarioSummaries);
        Collection<Summary> scenarios = projectSummary
                .getSortedScenarioSummaryList();
        Assert.assertEquals(1, scenarios.size());
        Assert.assertTrue(scenarios.contains(summary2));
    }

    @Test
    public void testGetSortedStepSummaryList() {
        Summary summary1 = new Summary();
        List<PerformanceEntry> entries = new ArrayList<PerformanceEntry>();
        PerformanceEntry entry1 = new PerformanceEntry();
        entry1.setPassed(true);
        entry1.setElapsedTime(1000000000l);
        entries.add(entry1);
        PerformanceEntry entry2 = new PerformanceEntry();
        entry2.setPassed(true);
        entry2.setElapsedTime(5000000000l);
        entries.add(entry2);
        summary1.setEntries(entries);
        Assert.assertEquals("3 secs", summary1.getFormattedAverageDuration());
        Summary summary2 = new Summary();
        List<PerformanceEntry> entries2 = new ArrayList<PerformanceEntry>();
        PerformanceEntry entry12 = new PerformanceEntry();
        entry12.setPassed(true);
        entry12.setElapsedTime(1000000000l);
        entries2.add(entry12);
        PerformanceEntry entry22 = new PerformanceEntry();
        entry22.setPassed(true);
        entry22.setElapsedTime(6000000000l);
        entries2.add(entry22);
        summary2.setEntries(entries2);
        Assert.assertEquals("3 secs 500 ms",
                summary2.getFormattedAverageDuration());
        Map<String, Summary> stepSummaries = new HashMap<String, Summary>();
        stepSummaries.put("summary1", summary1);
        stepSummaries.put("summary2", summary2);
        ProjectSummary projectSummary = new ProjectSummary();
        projectSummary.setNumberOfSummariesToDisplay(5);
        projectSummary.setStepSummaries(stepSummaries);
        Collection<Summary> steps = projectSummary
                .getSortedStepSummaryList();
        Assert.assertEquals(2, steps.size());
        Assert.assertTrue(steps.contains(summary1));
        Assert.assertTrue(steps.contains(summary2));
    }

}
