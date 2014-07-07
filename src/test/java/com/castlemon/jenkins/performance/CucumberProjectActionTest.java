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

//@RunWith(PowerMockRunner.class)
//@PrepareForTest(AbstractProject.class)
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
		// Run run = Mockito.mock(Run.class);
		// Mockito.when(run.getRootDir()).thenReturn(testFolder.getRoot());
		// Mockito.when(project.getLastCompletedBuild()).thenReturn(run);
		// PowerMockito.when(project.getUrl()).thenReturn("dummyUrl/");
		cucumberProjectAction = new CucumberProjectAction(project, 20);
	}

	@Test
	public void checkConstructorNullSummary() throws IOException {
		FileUtils.forceDelete(testFolder
				.newFile("cucumber-perf-reports/cukeperf.xml"));
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

	/*
	 * @Test public void testGetProjectSummary() throws IOException {
	 * ProjectSummary summary = cucumberProjectAction.getProjectSummary();
	 * Assert.assertEquals(11, summary.getOverallSummary().getPassedBuilds());
	 * Assert.assertEquals(2, summary.getFeatureSummaries().size());
	 * Assert.assertEquals(11, summary.getOverallSummary().getEntries().size());
	 * }
	 */

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

	/*
	 * @Test public void testGetFeature() throws IOException { List<Summary>
	 * summaryList = new
	 * ArrayList<Summary>(cucumberProjectAction.getProjectSummary
	 * ().getFeatureSummaries().values()); Summary feature =
	 * cucumberProjectAction.getFeature(summaryList.get(0).getPageLink());
	 * Assert.assertEquals(summaryList.get(0).getName(), feature.getName()); }
	 */

	/*
	 * @Test public void testGetScenario() throws IOException { List<Summary>
	 * summaryList = new
	 * ArrayList<Summary>(cucumberProjectAction.getProjectSummary
	 * ().getScenarioSummaries().values()); Summary scenario =
	 * cucumberProjectAction .getScenario(summaryList.get(0).getPageLink());
	 * Assert.assertEquals(summaryList.get(0).getName(), scenario.getName()); }
	 */

	/*
	 * @Test public void testGetStep() throws IOException { List<Summary>
	 * summaryList = new
	 * ArrayList<Summary>(cucumberProjectAction.getProjectSummary
	 * ().getStepSummaries().values()); Summary scenario = cucumberProjectAction
	 * .getStep(summaryList.get(0).getPageLink());
	 * Assert.assertEquals(summaryList.get(0).getName(), scenario.getName()); }
	 */

	// @Test
	// public void testGetPieChartData() throws IOException {
	// List<Summary> summaryList = new
	// ArrayList<Summary>(cucumberProjectAction.getProjectSummary().getStepSummaries().values());
	// Summary scenario = cucumberProjectAction
	// .getStep(summaryList.get(0).getPageLink());
	// String pieChartData =
	// cucumberProjectAction.getProjectSummary().getOverallSummary().getPieChartData();
	// Assert.assertEquals(pieChartData,
	// cucumberProjectAction.getPieChartData());
	// }

	@Test
	public void testGetProject() throws IOException {
		Assert.assertEquals(project, cucumberProjectAction.getProject());
	}

}
