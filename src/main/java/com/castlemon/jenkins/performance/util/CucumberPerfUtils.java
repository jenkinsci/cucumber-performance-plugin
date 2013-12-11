package com.castlemon.jenkins.performance.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.tools.ant.DirectoryScanner;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import com.castlemon.jenkins.performance.domain.Feature;
import com.castlemon.jenkins.performance.domain.reporting.PerformanceEntry;
import com.castlemon.jenkins.performance.domain.reporting.Summary;
import com.castlemon.jenkins.performance.domain.reporting.comparator.SummaryOrderComparator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CucumberPerfUtils {

	private static int nanosInAMilli = 1000000;

	public static List<Summary> getRelevantSummaries(
			Map<String, Summary> summaries, String seniorId) {
		List<Summary> summaryList = new ArrayList<Summary>();
		for (Map.Entry<String, Summary> entry : summaries.entrySet()) {
			Summary summary = entry.getValue();
			if (summary.getSeniorId().equals(seniorId)) {
				summaryList.add(summary);
			}
		}
		sortSummaryList(summaryList);
		return summaryList;
	}

	public static void sortSummaryList(List<Summary> summaries) {
		Collections.sort(summaries, new SummaryOrderComparator());
	}

	public static String buildGraphData(Summary summary) {
		StringBuilder output = new StringBuilder();
		output.append("[");
		int i = 1;
		for (PerformanceEntry run : summary.getEntries()) {
			if (run.isPassed()) {
				output.append("["
						+ run.getBuildNumber()
						+ ", "
						+ getDurationInSeconds(run.getElapsedTime()
								/ nanosInAMilli) + "]");
				if (i < summary.getEntries().size()) {
					output.append(",");
				}
			}
			i++;
		}
		output.append("]");
		return output.toString();
	}

	public static String buildAverageData(Summary summary) {
		long totalDuration = 0l;
		long executionCount = 0l;
		StringBuilder output = new StringBuilder();
		for (PerformanceEntry run : summary.getEntries()) {
			if (run.isPassed()) {
				totalDuration += run.getElapsedTime();
				executionCount++;
			}
		}
		long average = 0l;
		if (executionCount > 0) {
			average = totalDuration / executionCount;
		}
		output.append("[");
		int i = 1;
		for (PerformanceEntry run : summary.getEntries()) {
			if (run.isPassed()) {
				output.append("[" + run.getBuildNumber() + ", "
						+ getDurationInSeconds(average / nanosInAMilli) + "]");
				if (i < summary.getEntries().size()) {
					output.append(",");
				}
			}
			i++;
		}
		output.append("]");
		return output.toString();
	}

	public static long getDurationInSeconds(Long duration) {
		Duration minutes = new Duration(duration);
		return minutes.getStandardSeconds();
	}

	public static long getDurationInMinutes(Long duration) {
		Duration minutes = new Duration(duration);
		return minutes.getStandardMinutes();
	}

	public static String[] findJsonFiles(File targetDirectory, String filename) {
		DirectoryScanner scanner = new DirectoryScanner();
		String requiredFiles = "**/*" + filename;
		scanner.setIncludes(new String[] { requiredFiles });
		scanner.setBasedir(targetDirectory);
		scanner.scan();
		return scanner.getIncludedFiles();
	}

	public static List<Feature> getData(String[] jsonReportFiles,
			File targetBuildDirectory) {
		List<Feature> overallFeatures = new ArrayList<Feature>();
		for (String fileName : jsonReportFiles) {
			String jsonInput = loadJsonFile(targetBuildDirectory, fileName);
			if (StringUtils.isNotEmpty(jsonInput)) {
				overallFeatures.addAll(getData(jsonInput));
			}
		}
		return overallFeatures;
	}

	public static List<Feature> getData(String jsonInput) {
		ObjectMapper mapper = new ObjectMapper();
		List<Feature> features = new ArrayList<Feature>();
		try {
			JavaType type = mapper.getTypeFactory().constructCollectionType(
					List.class, Feature.class);
			features = mapper.readValue(jsonInput, type);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return features;
	}

	public static String formatDuration(Long durationInNanos) {
		PeriodFormatter formatter = new PeriodFormatterBuilder()
				.printZeroRarelyLast().appendDays()
				.appendSuffix(" day", " days").appendSeparator(" ")
				.appendHours().appendSuffix(" hour", " hours")
				.appendSeparator(" and ").appendMinutes()
				.appendSuffix(" min", " mins").appendSeparator(" ")
				.appendSeconds().appendSuffix(" sec", " secs")
				.appendSeparator(" ").appendMillis().appendSuffix(" ms", " ms")
				.toFormatter();
		return formatter.print(new Period(0, durationInNanos / 1000000));
	}

	public static String generateJsonSummary(List<Summary> summaries) {
		StringBuilder output = new StringBuilder();
		output.append("[");
		int i = 1;
		for (Summary summary : summaries) {
			output.append("[" + '"' + summary.getName() + '"' + ", "
					+ summary.getNumberOfSubItems() + ", " + '"'
					+ summary.getFormattedShortestDuration() + '"' + ", " + '"'
					+ summary.getFormattedLongestDuration() + '"' + ", " + '"'
					+ summary.getFormattedAverageDuration() + '"' + ", "
					+ summary.getShortestDuration() + ", "
					+ summary.getLongestDuration() + ", "
					+ summary.calculateAverageDuration() + "]");
			if (i < summaries.size()) {
				output.append(",");
			}
			i++;
		}
		output.append("]");
		return output.toString();
	}

	private static String loadJsonFile(File targetBuildDirectory,
			String fileName) {
		String content = null;

		File file = new File(targetBuildDirectory, fileName);
		try {
			FileReader reader = new FileReader(file);
			char[] chars = new char[(int) file.length()];
			reader.read(chars);
			content = new String(chars);
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content;
	}

}
