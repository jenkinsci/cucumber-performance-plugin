package com.castlemon.jenkins.performance;

import hudson.model.AbstractProject;

import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mockito;

import com.castlemon.jenkins.performance.domain.reporting.ProjectSummary;

@SuppressWarnings("rawtypes")
public class CucumberProjectActionTest {

	private AbstractProject project = Mockito.mock(AbstractProject.class);

	@Rule
	public TemporaryFolder testFolder = new TemporaryFolder();

	private CucumberProjectAction cucumberProjectAction;

	@Before
	public void setup() throws IOException {
		testFolder.newFolder("cucumber-perf-reports");
		FileUtils.copyFile(
				FileUtils.toFile(this.getClass().getResource("/cukeperf.xml")),
				testFolder.newFile("cucumber-perf-reports/cukeperf.xml"));
		cucumberProjectAction = new CucumberProjectAction(project, 20);
	}

	@Test
	public void checkConstructorNullSummary() throws IOException {
		FileUtils.forceDelete(testFolder.getRoot());
		cucumberProjectAction = new CucumberProjectAction(project, 20);
		Assert.assertNull(cucumberProjectAction.getProjectSummary());
	}

	@Test
	public void testGetDisplayName() {
		Assert.assertEquals("Cucumber Project Performance Report",
				cucumberProjectAction.getDisplayName());
	}

	@Test
	public void testGetIconFileName() {
		Assert.assertEquals("/plugin/cucumber-perf/performance.png",
				cucumberProjectAction.getIconFileName());
	}

	@Test
	public void testGetUrlName() {
		Assert.assertEquals("cucumber-perf-reports",
				cucumberProjectAction.getUrlName());
	}

	@Test
	public void testGetProjectSummaryFromProject() throws IOException {
		Mockito.when(project.getLastCompletedBuild()).thenReturn(null);
		Mockito.when(project.getRootDir()).thenReturn(testFolder.getRoot());
		CucumberProjectAction tempCucumberProjectAction = new CucumberProjectAction(
				project, 20);
		ProjectSummary summary = tempCucumberProjectAction.getProjectSummary();
		Assert.assertEquals(11, summary.getOverallSummary().getPassedBuilds());
		Assert.assertEquals(2, summary.getFeatureSummaries().size());
		Assert.assertEquals(11, summary.getOverallSummary().getEntries().size());
	}

	@Test
	public void testGetProject() throws IOException {
		Assert.assertEquals(project, cucumberProjectAction.getProject());
	}

}
