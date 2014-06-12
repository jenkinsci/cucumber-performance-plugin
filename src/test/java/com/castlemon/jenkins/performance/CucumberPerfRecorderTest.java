package com.castlemon.jenkins.performance;

import hudson.FilePath;
import hudson.Launcher;
import hudson.model.*;
import hudson.tasks.BuildStepMonitor;
import junit.framework.Assert;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

@SuppressWarnings("rawtypes")
public class CucumberPerfRecorderTest {

	String jsonReportDirectory = "";
	int countOfSummaries = 10;

	CucumberPerfRecorder cucumberPerfRecorder = new CucumberPerfRecorder(
			jsonReportDirectory, countOfSummaries);

	@Rule
	public TemporaryFolder testFolder = new TemporaryFolder();

	AbstractProject project = Mockito.mock(AbstractProject.class);

    AbstractBuild build = Mockito.mock(AbstractBuild.class);

    Launcher launcher = Mockito.mock(Launcher.class);

    BuildListener listener = Mockito.mock(BuildListener.class);

	@Before
	public void setup() throws IOException {
		testFolder.newFolder("cucumber-perf-reports");
		FileUtils.copyFile(
				FileUtils.toFile(this.getClass().getResource("/cukeperf.xml")),
				testFolder.newFile("cucumber-perf-reports/cukeperf.xml"));
		Run run = Mockito.mock(Run.class);
		Mockito.when(run.getRootDir()).thenReturn(testFolder.getRoot());
		Mockito.when(project.getLastCompletedBuild()).thenReturn(run);
        Mockito.when(project.getName()).thenReturn("TestP");
        PrintStream outContent = new PrintStream(new ByteArrayOutputStream());
        Mockito.when(listener.getLogger()).thenReturn(outContent);
        Mockito.when(build.getWorkspace()).thenReturn(new FilePath(testFolder.getRoot()));
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

    @Test
    public void testPerform() throws IOException, InterruptedException {
        Assert.assertTrue(cucumberPerfRecorder.perform(build,launcher,listener));
    }

}
