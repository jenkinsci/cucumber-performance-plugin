package com.castlemon.jenkins.performance.reporting;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import com.castlemon.jenkins.performance.domain.Feature;
import com.castlemon.jenkins.performance.domain.reporting.ProjectRun;
import com.castlemon.jenkins.performance.domain.reporting.Summary;
import com.castlemon.jenkins.performance.util.CucumberPerfUtils;

public class ReportBuilder {

	PerformanceReporter reporter = new PerformanceReporter();

	public boolean generateBuildReport(List<Feature> features,
			File reportDirectory, String buildProject, String buildNumber,
			String pluginUrlPath) {
		copyCSSFile(reportDirectory);
		reporter.initialiseEntryMaps();
		VelocityEngine velocityEngine = new VelocityEngine();
		velocityEngine.setProperty("resource.loader", "class");
		velocityEngine
				.setProperty("class.resource.loader.class",
						"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		velocityEngine.init();
		Template template = velocityEngine.getTemplate("/templates/basic.vm");
		String fullPluginPath = getPluginUrlPath(pluginUrlPath);
		generateFeatureReports(reporter.getFeatureSummaries(), velocityEngine,
				fullPluginPath, reportDirectory);
		VelocityContext context = new VelocityContext();
		context.put("genDate", new Date());
		context.put("features", features);
		context.put("build_project", buildProject);
		context.put("build_number", buildNumber);
		context.put("jenkins_base", fullPluginPath);
		//return (writeReport("basicview.html", reportDirectory, template,
		//		context));
		return true;
	}

	public boolean generateProjectReports(List<ProjectRun> projectRuns,
			File reportDirectory, String buildProject, String buildNumber,
			String pluginUrlPath) {
		copyCSSFile(reportDirectory);
		reporter.initialiseEntryMaps();
		Summary projectSummary = reporter.getPerformanceData(projectRuns);
		projectSummary.setName(buildProject);
		VelocityEngine velocityEngine = new VelocityEngine();
		velocityEngine.setProperty("resource.loader", "class");
		velocityEngine
				.setProperty("class.resource.loader.class",
						"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		velocityEngine.init();
		Template template = velocityEngine.getTemplate("/templates/project.vm");
		String fullPluginPath = getPluginUrlPath(pluginUrlPath);
		generateFeatureReports(reporter.getFeatureSummaries(), velocityEngine,
				fullPluginPath, reportDirectory);
		VelocityContext context = new VelocityContext();
		context.put("genDate", new Date());
		context.put("projectSummary", projectSummary);
		context.put("perfData",
				CucumberPerfUtils.buildGraphData(projectSummary));
		context.put("averageData",
				CucumberPerfUtils.buildAverageData(projectSummary));
		context.put("featureData", reporter.getFeatureSummaries());
		context.put("build_project", buildProject);
		context.put("build_number", buildNumber);
		context.put("jenkins_base", fullPluginPath);
		return (writeReport("projectview.html", reportDirectory, template,
				context));
	}

	private void generateFeatureReports(Map<String, Summary> featureSummaries,
			VelocityEngine velocityEngine, String pluginPath,
			File reportDirectory) {
		Template template = velocityEngine.getTemplate("/templates/feature.vm");
		VelocityContext context = new VelocityContext();
		context.put("genDate", new Date());
		context.put("jenkins_base", pluginPath);
		for (Summary featureSummary : featureSummaries.values()) {
			context.put("featureSummary", featureSummary);
			context.put(
					"scenarioData",
					CucumberPerfUtils.getRelevantSummaries(
							reporter.getScenarioSummaries(),
							featureSummary.getId()));
			context.put("perfData",
					CucumberPerfUtils.buildGraphData(featureSummary));
			context.put("averageData",
					CucumberPerfUtils.buildAverageData(featureSummary));
			writeReport(featureSummary.getPageLink(), reportDirectory,
					template, context);
		}

	}

	private boolean writeReport(String fileName, File reportDirectory,
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
