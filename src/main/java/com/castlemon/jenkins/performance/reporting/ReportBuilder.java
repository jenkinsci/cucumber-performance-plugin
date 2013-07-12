package com.castlemon.jenkins.performance.reporting;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import com.castlemon.jenkins.performance.domain.reporting.ProjectPerformanceEntry;
import com.castlemon.jenkins.performance.domain.reporting.ProjectRun;
import com.castlemon.jenkins.performance.domain.reporting.ProjectSummary;
import com.castlemon.jenkins.performance.util.CucumberPerfUtils;

public class ReportBuilder {

	ProjectReporter reporter = new ProjectReporter();

	public boolean generateProjectReports(List<ProjectRun> projectRuns,
			File reportDirectory, String buildProject, String buildNumber,
			String pluginUrlPath) {
		copyCSSFile(reportDirectory);
		reporter.initialiseScenarioEntries();
		ProjectSummary projectSummary = getPerformanceData(projectRuns,
				buildNumber);
		projectSummary.setProjectName(buildProject);
		VelocityEngine velocityEngine = new VelocityEngine();
		velocityEngine.setProperty("resource.loader", "class");
		velocityEngine
				.setProperty("class.resource.loader.class",
						"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		velocityEngine.init();
		Template template = velocityEngine.getTemplate("/templates/project.vm");
		VelocityContext context = new VelocityContext();
		context.put("genDate", new Date());
		context.put("projectSummary", projectSummary);
		context.put("perfData",
				CucumberPerfUtils.buildProjectGraphData(projectSummary));
		context.put("scenarioData", reporter.getScenarioEntries());
		context.put("build_project", buildProject);
		context.put("build_number", buildNumber);
		context.put("jenkins_base", getPluginUrlPath(pluginUrlPath));
		return (generateReport("projectview.html", reportDirectory, template,
				context));
	}

	private ProjectSummary getPerformanceData(List<ProjectRun> runs,
			String buildNumber) {
		ProjectSummary projectSummary = new ProjectSummary();
		List<ProjectPerformanceEntry> entries = new ArrayList<ProjectPerformanceEntry>();
		int passedSteps = 0;
		int failedSteps = 0;
		int skippedSteps = 0;
		int passedBuilds = 0;
		int failedBuilds = 0;

		for (ProjectRun run : runs) {
			ProjectPerformanceEntry performanceEntry = reporter
					.generateBasicProjectPerformanceData(run);
			passedSteps += performanceEntry.getPassedSteps();
			failedSteps += performanceEntry.getFailedSteps();
			skippedSteps += performanceEntry.getSkippedSteps();
			if (performanceEntry.isPassedBuild()) {
				passedBuilds++;
			} else {
				failedBuilds++;
			}
			entries.add(performanceEntry);
		}
		projectSummary.setPassedBuilds(passedBuilds);
		projectSummary.setFailedBuilds(failedBuilds);
		projectSummary.setTotalBuilds(passedBuilds + failedBuilds);
		projectSummary.setPassedSteps(passedSteps);
		projectSummary.setFailedSteps(failedSteps);
		projectSummary.setSkippedSteps(skippedSteps);
		projectSummary.setPerformanceEntries(entries);
		return projectSummary;
	}

	private boolean generateReport(String fileName, File reportDirectory,
			Template featureResult, VelocityContext context) {
		try {
			File file = new File(reportDirectory, fileName);
			FileOutputStream fileStream = new FileOutputStream(file);
			OutputStreamWriter writer = new OutputStreamWriter(fileStream,
					"UTF-8");
			featureResult.merge(context, writer);
			writer.flush();
			writer.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private String getPluginUrlPath(String path) {
		if (path == null || path.isEmpty()) {
			return "/";
		}
		return path;
	}

	private void copyCSSFile(File reportDirectory) {
		InputStream resourceArchiveInputStream = null;
		FileOutputStream cssOutStream = null;
		try {
			resourceArchiveInputStream = ReportBuilder.class
					.getResourceAsStream("css/main.css");
			if (resourceArchiveInputStream == null) {
				resourceArchiveInputStream = ReportBuilder.class
						.getResourceAsStream("/css/main.css");
			}
			File file = new File(reportDirectory, "main.css");
			cssOutStream = new FileOutputStream(file);

			IOUtils.copy(resourceArchiveInputStream, cssOutStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtils.closeQuietly(resourceArchiveInputStream);
			IOUtils.closeQuietly(cssOutStream);
		}

	}
}
