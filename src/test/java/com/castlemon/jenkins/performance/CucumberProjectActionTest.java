package com.castlemon.jenkins.performance;

import hudson.model.AbstractProject;
import hudson.model.Run;

import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.castlemon.jenkins.performance.domain.reporting.ProjectSummary;
import com.castlemon.jenkins.performance.domain.reporting.Summary;

@SuppressWarnings("rawtypes")
public class CucumberProjectActionTest {

	@Mock
	private AbstractProject project = Mockito.mock(AbstractProject.class);

	@Mock
	Run run = Mockito.mock(Run.class);

	@Rule
	public TemporaryFolder testFolder = new TemporaryFolder();

	@InjectMocks
	private CucumberProjectAction cucumberProjectAction;

	@Before
	public void setup() throws IOException {
		testFolder.newFolder("cucumber-perf-reports");
		FileUtils.copyFile(
				FileUtils.toFile(this.getClass().getResource("/cukeperf.xml")),
				testFolder.newFile("cucumber-perf-reports/cukeperf.xml"));
		Run run = Mockito.mock(Run.class);
		Mockito.when(run.getRootDir()).thenReturn(testFolder.getRoot());
		Mockito.when(project.getLastCompletedBuild()).thenReturn(run);
		cucumberProjectAction = new CucumberProjectAction(project, 20);
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
	public void testGetProjectSummary() throws IOException {
		ProjectSummary summary = cucumberProjectAction.getProjectSummary();
		Assert.assertEquals(1, summary.getOverallSummary().getPassedBuilds());
		Assert.assertEquals(3, summary.getFeatureSummaries().size());
		Assert.assertEquals(1, summary.getOverallSummary().getEntries().size());
	}

	@Test
	public void testGetProjectSummaryFromProject() throws IOException {
        Mockito.when(project.getLastCompletedBuild()).thenReturn(null);
        Mockito.when(project.getRootDir()).thenReturn(testFolder.getRoot());
        CucumberProjectAction tempCucumberProjectAction = new CucumberProjectAction(project, 20);
		ProjectSummary summary = tempCucumberProjectAction.getProjectSummary();
		Assert.assertEquals(1, summary.getOverallSummary().getPassedBuilds());
		Assert.assertEquals(3, summary.getFeatureSummaries().size());
		Assert.assertEquals(1, summary.getOverallSummary().getEntries().size());
	}

	/*@Test
	public void testGetFeature() throws IOException {
		Map<String, Summary> featureCollection = cucumberProjectAction
				.getFeature();
		Assert.assertEquals(3, featureCollection.size());
	}

	@Test
	public void testGetScenario() throws IOException {
		Map<String, Summary> scenarioCollection = cucumberProjectAction
				.getScenario();
		Assert.assertEquals(17, scenarioCollection.size());
	}

	@Test
	public void testGetStep() throws IOException {
		Map<String, Summary> stepCollection = cucumberProjectAction.getStep();
		Assert.assertEquals(313, stepCollection.size());
	}*/

	@Test
	public void testGetProject() throws IOException {
		Assert.assertEquals(project, cucumberProjectAction.getProject());
	}

}
