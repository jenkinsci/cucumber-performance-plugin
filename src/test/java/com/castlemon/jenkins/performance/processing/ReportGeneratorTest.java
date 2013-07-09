package com.castlemon.jenkins.performance.processing;

import hudson.model.BuildListener;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.castlemon.jenkins.performance.TestUtils;
import com.castlemon.jenkins.performance.domain.reporting.ProjectRun;
import com.castlemon.jenkins.performance.processor.ReportGenerator;

public class ReportGeneratorTest {

	@Mock
	private BuildListener listener = Mockito.mock(BuildListener.class);

	@Mock
	private PrintStream stream = Mockito.mock(PrintStream.class);

	private ReportGenerator generator = new ReportGenerator();

	@Rule
	public TemporaryFolder testFolder = new TemporaryFolder();

	private TestUtils testUtils = new TestUtils();

	@Test
	public void testGenerateProjectReports() {
		Mockito.when(listener.getLogger()).thenReturn(stream);
		List<ProjectRun> projectRuns = new ArrayList<ProjectRun>();
		projectRuns.add(testUtils.generateRun("passed"));
		generator.generateProjectReports(projectRuns, listener,
				testFolder.getRoot(), "test build 1", "1", null);
		Assert.assertEquals(2, testFolder.getRoot().listFiles().length);
	}

}
