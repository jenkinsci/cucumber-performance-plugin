package com.castlemon.jenkins.performance.reporting;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.castlemon.jenkins.performance.domain.Scenario;
import com.castlemon.jenkins.performance.domain.reporting.ProjectPerformanceEntry;
import com.castlemon.jenkins.performance.domain.reporting.ProjectRun;
import com.castlemon.jenkins.performance.util.CucumberPerfUtils;

public class ProjectReporterTest {

	ProjectReporter jobReporter = new ProjectReporter();

	@Test
	public void testGenerateBasicTextDataSuccess() throws IOException {
		long expectedDuration = 192349832481l;
		String jsonString = loadJsonFile("/cucumber-success.json");
		Assert.assertNotNull(jsonString);
		List<Scenario> scenarios = CucumberPerfUtils.getData(jsonString);
		Assert.assertFalse(scenarios.isEmpty());
		ProjectRun run = new ProjectRun();
		String projectName = "Dummy Project 1";
		run.setProjectName(projectName);
		run.setScenarios(scenarios);
		Date date = new Date();
		run.setRunDate(date);
		ProjectPerformanceEntry jobOutput = jobReporter
				.generateBasicProjectPerformanceData(run);
		Assert.assertEquals(date, jobOutput.getRunDate());
		Assert.assertEquals(expectedDuration, jobOutput.getElapsedTime());
	}

	@Test
	public void testGenerateBasicTextDataFailure() throws IOException {
		long expectedDuration = 204151315589l;
		String jsonString = loadJsonFile("/cucumber-failure.json");
		Assert.assertNotNull(jsonString);
		List<Scenario> scenarios = CucumberPerfUtils.getData(jsonString);
		Assert.assertFalse(scenarios.isEmpty());
		ProjectRun run = new ProjectRun();
		String projectName = "Dummy Project 1";
		run.setProjectName(projectName);
		run.setScenarios(scenarios);
		Date date = new Date();
		run.setRunDate(date);
		ProjectPerformanceEntry jobOutput = jobReporter
				.generateBasicProjectPerformanceData(run);
		Assert.assertEquals(date, jobOutput.getRunDate());
		Assert.assertEquals(expectedDuration, jobOutput.getElapsedTime());
	}

	private String loadJsonFile(String fileName) throws IOException {
		String content = null;
		URL fileURL = this.getClass().getResource(fileName);
		File file = new File(fileURL.getFile());
		try {
			FileReader reader = new FileReader(file);
			char[] chars = new char[(int) file.length()];
			reader.read(chars);
			content = new String(chars);
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}

}
