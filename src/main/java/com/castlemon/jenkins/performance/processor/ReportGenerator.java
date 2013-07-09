package com.castlemon.jenkins.performance.processor;

import hudson.model.BuildListener;

import java.io.File;
import java.util.List;

import com.castlemon.jenkins.performance.domain.reporting.ProjectRun;
import com.castlemon.jenkins.performance.reporting.ReportBuilder;

public class ReportGenerator {

	private ReportBuilder reportBuilder = new ReportBuilder();

	public void generateProjectReports(List<ProjectRun> projectRuns,
			BuildListener listener, File projectDirectory, String buildProject,
			String buildNumber, String pluginUrlPath) {
		listener.getLogger().println(
				"[CucumberPerfRecorder] running project reports");

		boolean success = reportBuilder.generateProjectReports(projectRuns,
				projectDirectory, buildProject, buildNumber, pluginUrlPath);
		listener.getLogger().println(
				"[CucumberPerfRecorder] project report generation complete - "
						+ success);
	}

}
