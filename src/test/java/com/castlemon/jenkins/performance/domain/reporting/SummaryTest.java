package com.castlemon.jenkins.performance.domain.reporting;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.agileware.test.PropertiesTester;
import org.junit.Test;

public class SummaryTest {

	Summary projectSummary = new Summary();

	@Test
	public void testProperties() throws Exception {
		PropertiesTester tester = new PropertiesTester();
		tester.setNameExclusions("pageLink");
		tester.testAll(Summary.class);
	}

	@Test
	public void testGetFormattedAverageDurationSingle() {
		Summary summary = new Summary();
		PerformanceEntry entry = new PerformanceEntry();
		entry.setPassed(true);
		entry.setElapsedTime(1000000000l);
		List<PerformanceEntry> entries = new ArrayList<PerformanceEntry>();
		entries.add(entry);
		summary.setEntries(entries);
		Assert.assertEquals("1 sec", summary.getFormattedAverageDuration());
	}

	@Test
	public void testGetFormattedAverageDurationMultiple() {
		Summary summary = new Summary();
		List<PerformanceEntry> entries = new ArrayList<PerformanceEntry>();
		PerformanceEntry entry1 = new PerformanceEntry();
		entry1.setPassed(true);
		entry1.setElapsedTime(1000000000l);
		entries.add(entry1);
		PerformanceEntry entry2 = new PerformanceEntry();
		entry2.setPassed(true);
		entry2.setElapsedTime(5000000000l);
		entries.add(entry2);
		summary.setEntries(entries);
		Assert.assertEquals("3 secs", summary.getFormattedAverageDuration());
	}

	@Test
	public void testGetFormattedAverageDurationRounding() {
		Summary summary = new Summary();
		List<PerformanceEntry> entries = new ArrayList<PerformanceEntry>();
		PerformanceEntry entry1 = new PerformanceEntry();
		entry1.setPassed(true);
		entry1.setElapsedTime(1000000000l);
		entries.add(entry1);
		PerformanceEntry entry2 = new PerformanceEntry();
		entry2.setPassed(true);
		entry2.setElapsedTime(6000000000l);
		entries.add(entry2);
		summary.setEntries(entries);
		Assert.assertEquals("3 secs 500 ms",
				summary.getFormattedAverageDuration());
	}

	@Test
	public void testGetPageLink() {
		Summary summary = new Summary();
		String pageLink = summary.getPageLink();
		Assert.assertEquals(10, pageLink.length());
	}

	@Test
	public void testHasRows() {
		List<List<String>> temp = new ArrayList<List<String>>();
		Summary summary = new Summary();
		Assert.assertFalse(summary.hasRows());
		summary.setRows(temp);
		Assert.assertTrue(summary.hasRows());
	}

	@Test
	public void testGetFormattedShortestDurationZero() {
		Summary summary = new Summary();
		Assert.assertEquals("0 ns", summary.getFormattedShortestDuration());
	}

	@Test
	public void testGetFormattedShortestDurationNonZero() {
		Summary summary = new Summary();
		summary.setShortestDuration(192349832481l);
		Assert.assertEquals("3 mins 12 secs 349 ms",
				summary.getFormattedShortestDuration());
	}

	@Test
	public void testGetFormattedLongestDurationZero() {
		Summary summary = new Summary();
		Assert.assertEquals("0 ns", summary.getFormattedLongestDuration());
	}

	@Test
	public void testGetFormattedLongestDurationNonZero() {
		Summary summary = new Summary();
		summary.setLongestDuration(192349832481l);
		Assert.assertEquals("3 mins 12 secs 349 ms",
				summary.getFormattedLongestDuration());
	}

	@Test
	public void testAddToPassedSteps() {
		Summary summary = new Summary();
		Assert.assertEquals(0, summary.getPassedSteps());
		summary.addToPassedSteps(3);
		Assert.assertEquals(3, summary.getPassedSteps());
	}

	@Test
	public void testAddToFailedSteps() {
		Summary summary = new Summary();
		Assert.assertEquals(0, summary.getFailedSteps());
		summary.addToFailedSteps(3);
		Assert.assertEquals(3, summary.getFailedSteps());
	}

	@Test
	public void testAddToSkippedSteps() {
		Summary summary = new Summary();
		Assert.assertEquals(0, summary.getSkippedSteps());
		summary.addToSkippedSteps(3);
		Assert.assertEquals(3, summary.getSkippedSteps());
	}

	@Test
	public void testIncrementTotalBuilds() {
		Summary summary = new Summary();
		Assert.assertEquals(0, summary.getTotalBuilds());
		summary.incrementTotalBuilds();
		Assert.assertEquals(1, summary.getTotalBuilds());
	}

	@Test
	public void testIncrementPassedBuilds() {
		Summary summary = new Summary();
		Assert.assertEquals(0, summary.getPassedBuilds());
		summary.incrementPassedBuilds();
		Assert.assertEquals(1, summary.getPassedBuilds());
	}

	@Test
	public void testIncrementFailedBuilds() {
		Summary summary = new Summary();
		Assert.assertEquals(0, summary.getFailedBuilds());
		summary.incrementFailedBuilds();
		Assert.assertEquals(1, summary.getFailedBuilds());
	}

	@Test
	public void testBuildProjectGraphDataAllSuccess() {
		PerformanceEntry entry1 = new PerformanceEntry();
		entry1.setBuildNumber(1);
		entry1.setElapsedTime(5000000000l);
		entry1.setPassed(true);
		PerformanceEntry entry2 = new PerformanceEntry();
		entry2.setBuildNumber(2);
		entry2.setElapsedTime(6000000000l);
		entry2.setPassed(true);
		List<PerformanceEntry> runs = new ArrayList<PerformanceEntry>();
		runs.add(entry1);
		runs.add(entry2);
		Summary projectSummary = new Summary();
		projectSummary.setEntries(runs);
		String expectedReturn = "[[1,5],[2,6]]";
		org.junit.Assert.assertEquals(expectedReturn,
				projectSummary.getGraphData());
	}

	@Test
	public void testBuildProjectGraphDataOneFail() {
		PerformanceEntry entry1 = new PerformanceEntry();
		entry1.setBuildNumber(1);
		entry1.setElapsedTime(5000000000l);
		entry1.setPassed(false);
		PerformanceEntry entry2 = new PerformanceEntry();
		entry2.setBuildNumber(2);
		entry2.setElapsedTime(6000000000l);
		entry2.setPassed(true);
		List<PerformanceEntry> runs = new ArrayList<PerformanceEntry>();
		runs.add(entry1);
		runs.add(entry2);
		Summary projectSummary = new Summary();
		projectSummary.setEntries(runs);
		String expectedReturn = "[[2,6]]";
		org.junit.Assert.assertEquals(expectedReturn,
				projectSummary.getGraphData());
	}

	@Test
	public void testBuildAverageDataAllSuccess() {
		PerformanceEntry entry1 = new PerformanceEntry();
		entry1.setBuildNumber(1);
		entry1.setElapsedTime(4000000000l);
		entry1.setPassed(true);
		PerformanceEntry entry2 = new PerformanceEntry();
		entry2.setBuildNumber(2);
		entry2.setElapsedTime(6000000000l);
		entry2.setPassed(true);
		List<PerformanceEntry> runs = new ArrayList<PerformanceEntry>();
		runs.add(entry1);
		runs.add(entry2);
		Summary projectSummary = new Summary();
		projectSummary.setEntries(runs);
		String expectedReturn = "[[1,5],[2,5]]";
		org.junit.Assert.assertEquals(expectedReturn,
				projectSummary.getAverageData());
	}

}
