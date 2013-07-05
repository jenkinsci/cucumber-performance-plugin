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

import com.castlemon.jenkins.performance.domain.Scenario;
import com.castlemon.jenkins.performance.domain.reporting.ProjectPerformanceEntry;
import com.castlemon.jenkins.performance.domain.reporting.ProjectRun;
import com.castlemon.jenkins.performance.util.CucumberPerfUtils;

public class ReportBuilder {

	ProjectReporter reporter = new ProjectReporter();

	public boolean generateProjectReports(List<ProjectRun> projectRuns,
			File reportDirectory, String buildProject, String buildNumber,
			String pluginUrlPath) {
		copyCSSFile(reportDirectory);
		List<ProjectPerformanceEntry> perfEntries = getPerformanceData(projectRuns);
		VelocityEngine velocityEngine = new VelocityEngine();
		velocityEngine.setProperty("resource.loader", "class");
		velocityEngine
				.setProperty("class.resource.loader.class",
						"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		velocityEngine.init();
		Template template = velocityEngine.getTemplate("/templates/project.vm");
		VelocityContext context = new VelocityContext();
		context.put("genDate", new Date());
		context.put("perfEntries", perfEntries);
		context.put("perfData",
				CucumberPerfUtils.buildProjectGraphData(perfEntries));
		context.put("build_project", buildProject);
		context.put("build_number", buildNumber);
		context.put("jenkins_base", getPluginUrlPath(pluginUrlPath));
		return (generateReport("projectview.html", reportDirectory, template,
				context));
	}

	public boolean generateBuildReports(List<Scenario> scenarios,
			File reportDirectory, String buildProject, String buildNumber,
			String pluginUrlPath) {
		VelocityEngine velocityEngine = new VelocityEngine();
		velocityEngine.setProperty("resource.loader", "class");
		velocityEngine
				.setProperty("class.resource.loader.class",
						"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		velocityEngine.init();
		Template template = velocityEngine.getTemplate("/templates/basic.vm");
		VelocityContext context = new VelocityContext();
		context.put("scenarios", scenarios);
		context.put("build_project", buildProject);
		context.put("build_number", buildNumber);
		context.put("jenkins_base", getPluginUrlPath(pluginUrlPath));
		return (generateReport("basicview.html", reportDirectory, template,
				context));
	}

	private List<ProjectPerformanceEntry> getPerformanceData(
			List<ProjectRun> runs) {
		List<ProjectPerformanceEntry> entries = new ArrayList<ProjectPerformanceEntry>();
		for (ProjectRun run : runs) {
			entries.add(reporter.generateBasicProjectPerformanceData(run));
		}
		return entries;
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
