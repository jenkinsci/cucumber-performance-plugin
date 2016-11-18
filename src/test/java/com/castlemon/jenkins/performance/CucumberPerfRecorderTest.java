package com.castlemon.jenkins.performance;

import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mockito;

import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.BuildListener;
import hudson.tasks.BuildStepMonitor;
import junit.framework.Assert;

@SuppressWarnings("rawtypes")
public class CucumberPerfRecorderTest {

	@Rule
	public TemporaryFolder testFolder = new TemporaryFolder();

	String jsonReportDirectory = "";

	String jsonReportFileName = "";

	int countOfSummaries = 10;

	CucumberPerfRecorder cucumberPerfRecorder = new CucumberPerfRecorder(
			jsonReportDirectory, jsonReportFileName, countOfSummaries);

	AbstractProject project = Mockito.mock(AbstractProject.class);

	BuildListener listener = Mockito.mock(BuildListener.class);

	@Before
	public void setup() throws IOException {
		testFolder.newFolder("cucumber-perf-reports");
		FileUtils.copyFile(
				FileUtils.toFile(this.getClass().getResource("/cukeperf.xml")),
				testFolder.newFile("cucumber-perf-reports/cukeperf.xml"));
		Mockito.when(project.getName()).thenReturn("TestP");
		Mockito.when(listener.getLogger()).thenReturn(System.out);
	}

	@After
	public void tearDown(){
		testFolder.delete();
	}

	@Test
	public void testConstructor() {
		Assert.assertEquals(10, cucumberPerfRecorder.countOfSortedSummaries);
		Assert.assertEquals(jsonReportDirectory,
				cucumberPerfRecorder.jsonReportDirectory);
	}

	@Test
	public void testConstructorZeroSortedSummaries() {
		CucumberPerfRecorder newCucumberPerfRecorder = new CucumberPerfRecorder(
				"fred", "bill", 0);
		Assert.assertEquals(20, newCucumberPerfRecorder.countOfSortedSummaries);
		Assert.assertEquals("fred", newCucumberPerfRecorder.jsonReportDirectory);
	}

	@Test
	public void testGetRequiredMonitorService() {
		Assert.assertEquals(BuildStepMonitor.NONE,
				cucumberPerfRecorder.getRequiredMonitorService());
	}

	@Test
	public void testGetProjectAction() {
		Assert.assertTrue(cucumberPerfRecorder.getProjectAction(project) instanceof Action);
	}

}
