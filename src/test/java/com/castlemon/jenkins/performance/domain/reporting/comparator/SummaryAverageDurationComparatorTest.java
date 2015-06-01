package com.castlemon.jenkins.performance.domain.reporting.comparator;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.castlemon.jenkins.performance.domain.reporting.PerformanceEntry;
import com.castlemon.jenkins.performance.domain.reporting.Summary;

public class SummaryAverageDurationComparatorTest {

	Summary summary1;
	Summary summary2;

	@Before
	public void setup() {
		summary1 = new Summary();
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

		summary2 = new Summary();
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
	}

	@Test
	public void summary1Bigger() {
		SummaryAverageDurationDescComparator summaryAverageDurationDescComparator = new SummaryAverageDurationDescComparator();
		Assert.assertTrue(summaryAverageDurationDescComparator.compare(
				summary1, summary2) > 0);
	}

	@Test
	public void summary2Bigger() {
		SummaryAverageDurationDescComparator summaryAverageDurationDescComparator = new SummaryAverageDurationDescComparator();
		Assert.assertTrue(summaryAverageDurationDescComparator.compare(
				summary2, summary1) < 0);
	}

	@Test
	public void bothEqual() {
		SummaryAverageDurationDescComparator summaryAverageDurationDescComparator = new SummaryAverageDurationDescComparator();
		Assert.assertTrue(summaryAverageDurationDescComparator.compare(
				summary1, summary1) == 0);
	}

}
