package com.castlemon.jenkins.performance.util;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.castlemon.jenkins.performance.domain.reporting.ProjectPerformanceEntry;

public class CucumberPerfUtilsTest {

	@Test
	public void testBuildProjectGraphData() {
		ProjectPerformanceEntry entry1 = new ProjectPerformanceEntry();
		entry1.setBuildNumber(1);
		entry1.setElapsedTime(5000000000l);
		ProjectPerformanceEntry entry2 = new ProjectPerformanceEntry();
		entry2.setBuildNumber(2);
		entry2.setElapsedTime(6000000000l);
		List<ProjectPerformanceEntry> runs = new ArrayList<ProjectPerformanceEntry>();
		runs.add(entry1);
		runs.add(entry2);
		String expectedReturn = "[[1, 5],[2, 6]]";
		Assert.assertEquals(expectedReturn,
				CucumberPerfUtils.buildProjectGraphData(runs));
	}

	@Test
	public void testGetDurationInMinutes() {
		Assert.assertEquals(3, CucumberPerfUtils.getDurationInMinutes(180000l));
	}

}
