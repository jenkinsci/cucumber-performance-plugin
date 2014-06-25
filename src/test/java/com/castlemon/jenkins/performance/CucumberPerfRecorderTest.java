package com.castlemon.jenkins.performance;

import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.Run;
import hudson.tasks.BuildStepMonitor;
import junit.framework.Assert;
import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mockito;

import java.io.IOException;

@SuppressWarnings("rawtypes")
public class CucumberPerfRecorderTest {

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();
    String jsonReportDirectory = "";
    int countOfSummaries = 10;
    CucumberPerfRecorder cucumberPerfRecorder = new CucumberPerfRecorder(
            jsonReportDirectory, countOfSummaries);
    AbstractProject project = Mockito.mock(AbstractProject.class);


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

    }

    @Test
    public void testConstructor() {
        Assert.assertEquals(10,cucumberPerfRecorder.countOfSortedSummaries);
        Assert.assertEquals(jsonReportDirectory, cucumberPerfRecorder.jsonReportDirectory);
    }

    @Test
    public void testConstructorZeroSortedSummaries() {
        CucumberPerfRecorder newCucumberPerfRecorder = new CucumberPerfRecorder(
                "fred", 0);
        Assert.assertEquals(20,newCucumberPerfRecorder.countOfSortedSummaries);
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
