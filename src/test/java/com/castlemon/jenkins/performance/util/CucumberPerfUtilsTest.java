package com.castlemon.jenkins.performance.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.castlemon.jenkins.performance.TestUtils;
import com.castlemon.jenkins.performance.domain.Feature;
import com.castlemon.jenkins.performance.domain.reporting.ProjectPerformanceEntry;
import com.castlemon.jenkins.performance.domain.reporting.ProjectSummary;

public class CucumberPerfUtilsTest {

	private TestUtils testUtils = new TestUtils();

	@Rule
	public TemporaryFolder testFolder = new TemporaryFolder();

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

	@Test
	public void testGetDataSingle() throws IOException {
		String jsonString = testUtils.loadJsonFile("/cucumber-success.json");
		List<Feature> features = CucumberPerfUtils.getData(jsonString);
		Assert.assertEquals(2, features.size());
	}

	@Test
	public void testGetDataSingleJsonParseException() throws IOException {
		String jsonString = "fred";
		List<Feature> features = CucumberPerfUtils.getData(jsonString);
		Assert.assertEquals(0, features.size());
	}

	@Test
	public void testGetDataSingleJsonMappingException() throws IOException {
		String jsonString = "[1,4{}";
		List<Feature> features = CucumberPerfUtils.getData(jsonString);
		Assert.assertEquals(0, features.size());
	}

	@Test
	public void testFindJsonFiles() throws IOException {
		File f = FileUtils.toFile(this.getClass().getResource(
				"/cucumber-success.json"));
		String[] results = CucumberPerfUtils.findJsonFiles(f.getParentFile(),
				".json");
		Assert.assertEquals(2, results.length);
		Assert.assertEquals("cucumber-failure.json", results[0]);
		Assert.assertEquals("cucumber-success.json", results[1]);
	}

	@Test
	public void testGetDataMultiple() throws IOException {
		String[] jsonReportFiles = { "cucumber-success.json",
				"cucumber-failure.json" };
		File f = FileUtils.toFile(this.getClass().getResource(
				"/cucumber-success.json"));
		List<Feature> features = CucumberPerfUtils.getData(jsonReportFiles,
				f.getParentFile());
		Assert.assertEquals(4, features.size());
	}
	
	@Test
	public void testGetDataMultipleIOException() throws IOException {
		String[] jsonReportFiles = { "cucumber-success.json",
				"nonexist.json" };
		File f = FileUtils.toFile(this.getClass().getResource(
				"/cucumber-success.json"));
		List<Feature> features = CucumberPerfUtils.getData(jsonReportFiles,
				f.getParentFile());
		Assert.assertEquals(2, features.size());
	}
}
