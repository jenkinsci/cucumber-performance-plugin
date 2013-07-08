package com.castlemon.jenkins.performance.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.castlemon.jenkins.performance.domain.reporting.ProjectPerformanceEntry;
import com.castlemon.jenkins.performance.domain.reporting.ProjectSummary;

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
		ProjectSummary projectSummary = new ProjectSummary();
		projectSummary.setPerformanceEntries(runs);
		String expectedReturn = "[[1, 5],[2, 6]]";
		Assert.assertEquals(expectedReturn,
				CucumberPerfUtils.buildProjectGraphData(projectSummary));
	}

	@Test
	public void testGetDurationInMinutes() {
		Assert.assertEquals(3, CucumberPerfUtils.getDurationInMinutes(180000l));
	}

	/*@Test
	public void testFindJsonFiles() {
		File rootDir = newMockFile("root", true);
		File blahTxt = newMockFile("blah.txt");
		File fooTxt = newMockFile("foo.txt");
		File pigJava = newMockFile("pig.java");
		Mockito.when(rootDir.listFiles()).thenReturn(
				new File[] { blahTxt, fooTxt, pigJava });
		String[] results = CucumberPerfUtils.findJsonFiles(rootDir, ".txt");
		Assert.assertEquals(1, results.length);
	}

	private File newMockFile(String name) {
		return newMockFile(name, false);
	}

	private File newMockFile(String name, boolean isDirectory) {
		File result = Mockito.mock(File.class);
		Mockito.when(result.getName()).thenReturn(name);
		Mockito.when(result.isDirectory()).thenReturn(isDirectory);
		return result;
	}*/

}
