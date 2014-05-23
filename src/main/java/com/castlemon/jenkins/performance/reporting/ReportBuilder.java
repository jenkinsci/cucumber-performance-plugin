package com.castlemon.jenkins.performance.reporting;

import hudson.model.BuildListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;

import com.castlemon.jenkins.performance.domain.enums.SummaryType;
import com.castlemon.jenkins.performance.domain.reporting.ProjectRun;
import com.castlemon.jenkins.performance.domain.reporting.Summary;
import com.castlemon.jenkins.performance.util.CucumberPerfUtils;

public class ReportBuilder {

	private PerformanceReporter reporter = new PerformanceReporter();
	private VelocityEngine velocityEngine = setupVelocityEngine();
	private BuildListener listener;

	public ReportBuilder(BuildListener listener) {
		this.listener = listener;
	}

	public boolean generateProjectReports(List<ProjectRun> projectRuns,
			File reportDirectory, String buildProject, String buildNumber,
			String pluginUrlPath) {
		copyAllResourceFiles(reportDirectory);
		reporter.initialiseEntryMaps();
		Summary projectSummary = reporter.getPerformanceData(projectRuns);
		projectSummary.setName(buildProject);
		String fullPluginPath = getPluginUrlPath(pluginUrlPath);
		// set up velocity context
		VelocityContext context = new VelocityContext();
		context.put("genDate", new Date());
		context.put("build_project", buildProject);
		context.put("build_number", buildNumber);
		context.put("jenkins_base", fullPluginPath);
		// feature reports
		generateReports(reporter.getFeatureSummaries(), velocityEngine,
				reportDirectory, SummaryType.FEATURE.toString(),
				SummaryType.SCENARIO.toString(), context);
		// scenario reports
		generateReports(reporter.getScenarioSummaries(), velocityEngine,
				reportDirectory, SummaryType.SCENARIO.toString(),
				SummaryType.STEP.toString(), context);
		// sorted reports
		generateSortedReports(reporter.getFeatureSummaries(),
				reporter.getScenarioSummaries(), reporter.getStepSummaries(),
				velocityEngine, reportDirectory, context);
		// project reports
		List<Summary> summaryList = new ArrayList<Summary>(reporter
				.getFeatureSummaries().values());
		CucumberPerfUtils.sortSummaryList(summaryList);
		generateReport(projectSummary, velocityEngine, reportDirectory,
				SummaryType.PROJECT.toString(), SummaryType.FEATURE.toString(),
				context, "projectview.html", summaryList);
		return true;
	}

	private void generateReports(Map<String, Summary> summaries,
			VelocityEngine velocityEngine, File reportDirectory, String type,
			String subType, VelocityContext context) {
		for (Summary summary : summaries.values()) {
			List<Summary> subSummaries = CucumberPerfUtils
					.getRelevantSummaries(getSubSummaries(subType),
							summary.getId());
			generateReport(summary, velocityEngine, reportDirectory, type,
					subType, context, summary.getPageLink(), subSummaries);
		}
	}

	private void generateReport(Summary summary, VelocityEngine velocityEngine,
			File reportDirectory, String type, String subType,
			VelocityContext context, String pageName, List<Summary> subSummaries) {
		Template template = velocityEngine
				.getTemplate("/templates/combined.vm");
		context.put("summaryType", type);
		context.put("subType", subType);
		context.put("summary", summary);
		context.put("subData", subSummaries);
		context.put("perfData", CucumberPerfUtils.buildGraphData(summary));
		context.put("averageData", CucumberPerfUtils.buildAverageData(summary));
		writeReport(pageName, reportDirectory, template, context);
	}

	private void generateSortedReports(Map<String, Summary> featureSummaries,
			Map<String, Summary> scenarioSummaries,
			Map<String, Summary> stepSummaries, VelocityEngine velocityEngine,
			File reportDirectory, VelocityContext context) {
		Template template = velocityEngine
				.getTemplate("/templates/sortedlists.vm");
		// feature summary
		List<Summary> featureSummaryList = new ArrayList<Summary>(
				featureSummaries.values());
		context.put("sortedFeatureData", CucumberPerfUtils.generateJsonSummary(
				featureSummaryList, SummaryType.FEATURE));
		// scenario summary
		List<Summary> scenarioSummaryList = new ArrayList<Summary>(
				scenarioSummaries.values());
		context.put("sortedScenarioData", CucumberPerfUtils
				.generateJsonSummary(scenarioSummaryList, SummaryType.SCENARIO));
		// step summary
		List<Summary> stepSummaryList = new ArrayList<Summary>(
				stepSummaries.values());
		context.put("sortedStepData", CucumberPerfUtils.generateJsonSummary(
				stepSummaryList, SummaryType.STEP));
		writeReport("sortedreports.html", reportDirectory, template, context);
	}

	private Map<String, Summary> getSubSummaries(String lowerType) {
		if (lowerType.equals(SummaryType.FEATURE.toString())) {
			return reporter.getFeatureSummaries();
		} else if (lowerType.equals(SummaryType.SCENARIO.toString())) {
			return reporter.getScenarioSummaries();
		}
		// assume step summaries required
		return reporter.getStepSummaries();

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

	private void copyAllResourceFiles(File reportDirectory) {
		// copy css
		File sourceCssDirectory = new File(ReportBuilder.class.getResource(
				"/css").getPath());
		File targetCssDirectory = new File(reportDirectory.getAbsolutePath()
				+ "/css");

		targetCssDirectory.mkdir();
		try {
			FileUtils.copyDirectory(sourceCssDirectory, targetCssDirectory);
		} catch (IOException e) {
			listener.getLogger().println("unable to copy CSS files");
			e.printStackTrace(listener.getLogger());
		}
		// copy javascript
		File sourceJsDirectory = new File(ReportBuilder.class.getResource(
				"/javascript").getPath());
		File targetJsDirectory = new File(reportDirectory.getAbsolutePath()
				+ "/js");
		targetJsDirectory.mkdir();
		try {
			FileUtils.copyDirectory(sourceJsDirectory, targetJsDirectory);
		} catch (IOException e) {
			listener.getLogger().println("unable to copy Javascript files");
			e.printStackTrace(listener.getLogger());
		}
		// copy image files
		File sourceImageDirectory = new File(ReportBuilder.class.getResource(
				"/perfimages").getPath());
		File targetImageDirectory = new File(reportDirectory.getAbsolutePath()
				+ "/images");
		targetImageDirectory.mkdir();
		try {
			FileUtils.copyDirectory(sourceImageDirectory, targetImageDirectory);
		} catch (IOException e) {
			listener.getLogger().println("unable to copy image files");
			e.printStackTrace(listener.getLogger());
		}
	}

	private VelocityEngine setupVelocityEngine() {
		VelocityEngine velocityEngine = new VelocityEngine();
		velocityEngine.setProperty(RuntimeConstants.RESOURCE_LOADER, "class");
		velocityEngine
				.setProperty("class.resource.loader.class",
						"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		velocityEngine.init();
		return velocityEngine;
	}
}
