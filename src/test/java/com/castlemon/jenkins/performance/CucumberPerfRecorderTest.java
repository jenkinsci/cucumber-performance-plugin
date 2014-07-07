package com.castlemon.jenkins.performance;

import hudson.model.Action;
import hudson.model.BuildListener;
import hudson.model.AbstractProject;
import hudson.tasks.BuildStepMonitor;

import java.io.IOException;

import junit.framework.Assert;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mockito;

//@RunWith(PowerMockRunner.class)
//@PrepareForTest({ AbstractBuild.class, Computer.class, RunMap.class,
//		AbstractProject.class })
@SuppressWarnings("rawtypes")
public class CucumberPerfRecorderTest {

	@Rule
	public TemporaryFolder testFolder = new TemporaryFolder();

	String jsonReportDirectory = "";

	int countOfSummaries = 10;

	CucumberPerfRecorder cucumberPerfRecorder = new CucumberPerfRecorder(
			jsonReportDirectory, countOfSummaries);

	AbstractProject project = Mockito.mock(AbstractProject.class);

	BuildListener listener = Mockito.mock(BuildListener.class);

	// Run run;

	@Before
	public void setup() throws IOException {
		testFolder.newFolder("cucumber-perf-reports");
		FileUtils.copyFile(
				FileUtils.toFile(this.getClass().getResource("/cukeperf.xml")),
				testFolder.newFile("cucumber-perf-reports/cukeperf.xml"));
		// run = Mockito.mock(Run.class);
		// Mockito.when(run.getRootDir()).thenReturn(testFolder.getRoot());
		// Mockito.when(project.getLastCompletedBuild()).thenReturn(run);
		Mockito.when(project.getName()).thenReturn("TestP");
		Mockito.when(listener.getLogger()).thenReturn(System.out);
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
				"fred", 0);
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

	/*
	 * @Test public void testPerformOnlyOldFiles() throws IOException,
	 * InterruptedException { AbstractBuild build =
	 * PowerMockito.mock(AbstractBuild.class); AbstractProject powerProject =
	 * PowerMockito.mock(AbstractProject.class);
	 * PowerMockito.when(powerProject.getLastCompletedBuild()).thenReturn(run);
	 * PowerMockito.when(powerProject.getName()).thenReturn("TestP");
	 * PowerMockito.mockStatic(Computer.class);
	 * PowerMockito.when(Computer.currentComputer()).thenReturn(null); //not a
	 * slave PowerMockito.when(build.getProject()).thenReturn(powerProject);
	 * PowerMockito.when(build.getWorkspace()).thenReturn(new
	 * FilePath(testFolder.getRoot())); RunMap<?> runMap =
	 * Mockito.mock(RunMap.class); Iterator iterator =
	 * Mockito.mock(Iterator.class);
	 * Mockito.when(runMap.iterator()).thenReturn(iterator); //return true the
	 * first time, false the second
	 * Mockito.when(iterator.hasNext()).thenReturn(true).thenReturn(false);
	 * Mockito.when(iterator.next()).thenReturn(run);
	 * PowerMockito.when(powerProject._getRuns()).thenReturn(runMap);
	 * Mockito.when(run.getArtifactsDir()).thenReturn(testFolder.getRoot());
	 * FileUtils.copyFile(
	 * FileUtils.toFile(this.getClass().getResource("/cucumber-success.json")),
	 * testFolder.newFile("cucumber-perf-reports/cucumber-perf001.json"));
	 * Assert.assertTrue(cucumberPerfRecorder.perform(build, null, listener)); }
	 */

	/*
	 * @Test public void testPerformSlaveBuild() throws IOException,
	 * InterruptedException { AbstractBuild build =
	 * PowerMockito.mock(AbstractBuild.class); AbstractProject powerProject =
	 * PowerMockito.mock(AbstractProject.class);
	 * PowerMockito.when(powerProject.getLastCompletedBuild()).thenReturn(run);
	 * PowerMockito.when(powerProject.getName()).thenReturn("TestP");
	 * PowerMockito.mockStatic(Computer.class); SlaveComputer slaveComputer =
	 * Mockito.mock(SlaveComputer.class);
	 * PowerMockito.when(Computer.currentComputer()).thenReturn(slaveComputer);
	 * PowerMockito.when(build.getProject()).thenReturn(powerProject);
	 * PowerMockito.when(powerProject.getSomeWorkspace()).thenReturn(new
	 * FilePath(testFolder.getRoot()));
	 * PowerMockito.when(build.getWorkspace()).thenReturn(new
	 * FilePath(testFolder.getRoot())); RunMap<?> runMap =
	 * Mockito.mock(RunMap.class); Iterator iterator =
	 * Mockito.mock(Iterator.class);
	 * Mockito.when(runMap.iterator()).thenReturn(iterator); //return true the
	 * first time, false the second
	 * Mockito.when(iterator.hasNext()).thenReturn(true).thenReturn(false);
	 * Mockito.when(iterator.next()).thenReturn(run);
	 * PowerMockito.when(powerProject._getRuns()).thenReturn(runMap);
	 * Mockito.when(run.getArtifactsDir()).thenReturn(testFolder.getRoot());
	 * FileUtils.copyFile(
	 * FileUtils.toFile(this.getClass().getResource("/cucumber-success.json")),
	 * testFolder.newFile("cucumber-perf-reports/cucumber-perf001.json"));
	 * Assert.assertTrue(cucumberPerfRecorder.perform(build, null, listener)); }
	 */

	/*
	 * @Test public void testPerformOldAndNewFiles() throws IOException,
	 * InterruptedException { AbstractBuild build =
	 * PowerMockito.mock(AbstractBuild.class); AbstractProject powerProject =
	 * PowerMockito.mock(AbstractProject.class);
	 * PowerMockito.when(powerProject.getLastCompletedBuild()).thenReturn(run);
	 * PowerMockito.when(powerProject.getName()).thenReturn("TestP");
	 * PowerMockito.mockStatic(Computer.class);
	 * PowerMockito.when(Computer.currentComputer()).thenReturn(null); //not a
	 * slave PowerMockito.when(build.getProject()).thenReturn(powerProject);
	 * PowerMockito.when(build.getWorkspace()).thenReturn(new
	 * FilePath(testFolder.getRoot())); RunMap<?> runMap =
	 * Mockito.mock(RunMap.class); Iterator iterator =
	 * Mockito.mock(Iterator.class);
	 * Mockito.when(runMap.iterator()).thenReturn(iterator); //return true the
	 * first time, false the second
	 * Mockito.when(iterator.hasNext()).thenReturn(true).thenReturn(false);
	 * Mockito.when(iterator.next()).thenReturn(run);
	 * PowerMockito.when(powerProject._getRuns()).thenReturn(runMap);
	 * Mockito.when(run.getArtifactsDir()).thenReturn(testFolder.getRoot());
	 * FileUtils.copyFile(
	 * FileUtils.toFile(this.getClass().getResource("/cucumber-success.json")),
	 * testFolder.newFile("cucumber-perf-reports/cucumber-perf001.json"));
	 * FileUtils.copyFile(
	 * FileUtils.toFile(this.getClass().getResource("/cucumber-success.json")),
	 * testFolder.newFile("cucumber-perf-reports/cucumber.json"));
	 * Assert.assertTrue(cucumberPerfRecorder.perform(build, null, listener)); }
	 */

}
