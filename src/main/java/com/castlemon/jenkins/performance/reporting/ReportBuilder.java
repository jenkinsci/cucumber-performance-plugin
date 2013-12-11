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
		copyResourceFiles(reportDirectory);
		reporter.initialiseEntryMaps();
		// VelocityEngine velocityEngine = new VelocityEngine();
		// velocityEngine.setProperty("resource.loader", "class");
		// velocityEngine
		// .setProperty("class.resource.loader.class",
		// "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		// velocityEngine.init();
		// Template template =
		// velocityEngine.getTemplate("/templates/basic.vm");
		// String fullPluginPath = getPluginUrlPath(pluginUrlPath);
		// generateFeatureReports(reporter.getFeatureSummaries(),
		// velocityEngine,
		// fullPluginPath, reportDirectory);
		// VelocityContext context = new VelocityContext();
		// context.put("genDate", new Date());
		// context.put("features", features);
		// context.put("build_project", buildProject);
		// context.put("build_number", buildNumber);
		// context.put("jenkins_base", fullPluginPath);
		// return (writeReport("basicview.html", reportDirectory, template,
		// context));
		return true;
	}

	public boolean generateProjectReports(List<ProjectRun> projectRuns,
			File reportDirectory, String buildProject, String buildNumber,
			String pluginUrlPath) {
		copyResourceFiles(reportDirectory);
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
		// feature reports
		generateReports(reporter.getFeatureSummaries(), velocityEngine,
				fullPluginPath, reportDirectory, "feature", "scenario");
		// scenario reports
		generateReports(reporter.getScenarioSummaries(), velocityEngine,
				fullPluginPath, reportDirectory, "scenario", "step");
		// sorted reports
		generateSortedReports(reporter.getFeatureSummaries(),
				reporter.getScenarioSummaries(), reporter.getStepSummaries(),
				velocityEngine, fullPluginPath, reportDirectory, "feature");
		VelocityContext context = new VelocityContext();
		context.put("genDate", new Date());
		context.put("projectSummary", projectSummary);
		context.put("perfData",
				CucumberPerfUtils.buildGraphData(projectSummary));
		context.put("averageData",
				CucumberPerfUtils.buildAverageData(projectSummary));
		List<Summary> summaryList = new ArrayList<Summary>(reporter
				.getFeatureSummaries().values());
		CucumberPerfUtils.sortSummaryList(summaryList);
		context.put("featureData", summaryList);
		context.put("build_project", buildProject);
		context.put("build_number", buildNumber);
		context.put("jenkins_base", fullPluginPath);
		return (writeReport("projectview.html", reportDirectory, template,
				context));
	}

	private void generateReports(Map<String, Summary> summaries,
			VelocityEngine velocityEngine, String pluginPath,
			File reportDirectory, String type, String lowerType) {
		Template template = velocityEngine.getTemplate("/templates/" + type
				+ ".vm");
		VelocityContext context = new VelocityContext();
		context.put("genDate", new Date());
		context.put("jenkins_base", pluginPath);
		for (Summary summary : summaries.values()) {
			context.put(type + "Summary", summary);
			context.put(lowerType + "Data", CucumberPerfUtils
					.getRelevantSummaries(getSubSummaries(lowerType),
							summary.getId()));
			context.put("perfData", CucumberPerfUtils.buildGraphData(summary));
			context.put("averageData",
					CucumberPerfUtils.buildAverageData(summary));
			writeReport(summary.getPageLink(), reportDirectory, template,
					context);
		}
	}

	private void generateSortedReports(Map<String, Summary> featureSummaries,
			Map<String, Summary> scenarioSummaries,
			Map<String, Summary> stepSummaries, VelocityEngine velocityEngine,
			String pluginPath, File reportDirectory, String type) {
		Template template = velocityEngine
				.getTemplate("/templates/sortedlists.vm");
		VelocityContext context = new VelocityContext();
		context.put("genDate", new Date());
		context.put("jenkins_base", pluginPath);
		// feature summary
		List<Summary> featureSummaryList = new ArrayList<Summary>(
				featureSummaries.values());
		context.put("sortedFeatureData",
				CucumberPerfUtils.generateJsonSummary(featureSummaryList));
		// scenario summary
		List<Summary> scenarioSummaryList = new ArrayList<Summary>(
				scenarioSummaries.values());
		context.put("sortedScenarioData",
				CucumberPerfUtils.generateJsonSummary(scenarioSummaryList));
		// step summary
		List<Summary> stepSummaryList = new ArrayList<Summary>(
				stepSummaries.values());
		context.put("sortedStepData",
				CucumberPerfUtils.generateJsonSummary(stepSummaryList));
		writeReport("featuresreport.html", reportDirectory, template, context);
	}

	private Map<String, Summary> getSubSummaries(String lowerType) {
		if (lowerType.equals("step")) {
			return reporter.getStepSummaries();
		}
		// assume scenario summaries required
		return reporter.getScenarioSummaries();

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

	private void copyResourceFiles(File reportDirectory) {
		// copy css
		File targetCssDirectory = new File(reportDirectory.getAbsolutePath()
				+ "/css");
		targetCssDirectory.mkdir();
		copyResource(targetCssDirectory, "css/main.css", "main.css");
		copyResource(targetCssDirectory, "css/jquery.dataTables.css",
				"jquery.dataTables.css");
		// copy javascript
		File targetJsDirectory = new File(reportDirectory.getAbsolutePath()
				+ "/js");
		targetJsDirectory.mkdir();
		copyResource(targetJsDirectory,
				"javascript/jquery/jquery-1.8.2.min.js", "jquery-1.8.2.min.js");
		copyResource(targetJsDirectory,
				"javascript/jquery/jquery.dataTables.min.js",
				"jquery.dataTables.min.js");
		copyResource(targetJsDirectory,
				"javascript/highcharts-3.0.2/highcharts.js", "highcharts.js");
		// copy images
		File targetImagesDirectory = new File(reportDirectory.getAbsolutePath()
				+ "/images");
		targetImagesDirectory.mkdir();
		copyResource(targetImagesDirectory, "images/sort_asc.png",
				"sort_asc.png");
		copyResource(targetImagesDirectory, "images/sort_both.png",
				"sort_both.png");
		copyResource(targetImagesDirectory, "images/sort_desc.png",
				"sort_desc.png");
	}

	private void copyResource(File reportDirectory, String resourceName,
			String targetName) {
		InputStream resourceArchiveInputStream = null;
		FileOutputStream cssOutStream = null;
		try {
			resourceArchiveInputStream = ReportBuilder.class
					.getResourceAsStream(resourceName);
			if (resourceArchiveInputStream == null) {
				resourceArchiveInputStream = ReportBuilder.class
						.getResourceAsStream("/" + resourceName);
			}
			File file = new File(reportDirectory, targetName);
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
