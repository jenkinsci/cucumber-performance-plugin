package com.castlemon.jenkins.performance.processor;

import hudson.model.BuildListener;

import java.io.File;
import java.util.List;

import com.castlemon.jenkins.performance.domain.Scenario;
import com.castlemon.jenkins.performance.domain.reporting.ProjectRun;
import com.castlemon.jenkins.performance.reporting.ReportBuilder;
import com.castlemon.jenkins.performance.util.CucumberPerfUtils;

public class ReportGenerator {

	private BuildListener listener;

	private ReportBuilder reportBuilder = new ReportBuilder();

	public void generateBuildReports(BuildListener listener,
			String[] jsonReportFiles, File targetBuildDirectory,
			String buildProject, String buildNumber, String pluginUrlPath,
			File projectDirectory) {
		this.listener = listener;
		generateBuildReport(jsonReportFiles, targetBuildDirectory,
				buildProject, buildNumber, pluginUrlPath);
	}

	public void generateProjectReports(List<ProjectRun> projectRuns,
			BuildListener listener, File projectDirectory, String buildProject,
			String buildNumber, String pluginUrlPath) {
		this.listener = listener;
		listener.getLogger().println(
				"[CucumberPerfRecorder] running project reports");

		boolean success = reportBuilder.generateProjectReports(projectRuns,
				projectDirectory, buildProject, buildNumber, pluginUrlPath);
		listener.getLogger().println(
				"[CucumberPerfRecorder] project report generation complete - "
						+ success);
	}

	private void generateBuildReport(String[] jsonReportFiles,
			File targetBuildDirectory, String buildProject, String buildNumber,
			String pluginUrlPath) {
		List<Scenario> scenarios = CucumberPerfUtils.getData(jsonReportFiles,
				targetBuildDirectory);
		listener.getLogger().println(
				"[CucumberPerfRecorder] retrieved " + scenarios.size()
						+ " scenarios");
		listener.getLogger().println(
				"[CucumberPerfRecorder] generating basic report in "
						+ targetBuildDirectory.getName());
		boolean success = reportBuilder.generateBuildReports(scenarios,
				targetBuildDirectory, buildProject, buildNumber, pluginUrlPath);
		listener.getLogger().println(
				"[CucumberPerfRecorder] basic report generation complete - "
						+ success);
	}


}
