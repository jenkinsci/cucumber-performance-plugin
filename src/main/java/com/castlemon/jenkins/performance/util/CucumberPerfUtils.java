package com.castlemon.jenkins.performance.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.tools.ant.DirectoryScanner;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import com.castlemon.jenkins.performance.domain.Feature;
import com.castlemon.jenkins.performance.domain.reporting.ProjectSummary;
import com.castlemon.jenkins.performance.domain.reporting.Summary;
import com.castlemon.jenkins.performance.domain.reporting.comparator.SummaryOrderComparator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.xstream.XStream;

public class CucumberPerfUtils {

	private static int nanosInAMilli = 1000000;

	public static boolean writeSummaryToDisk(ProjectSummary projectSummary,
			File outputDirectory) {
		try {
			XStream xstream = prepareXStream();
			File outputFile = new File(outputDirectory, "cukeperf.xml");
			Writer writer = new PrintWriter(outputFile);
			xstream.toXML(projectSummary, writer);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	public static ProjectSummary readSummaryFromDisk(File outputDirectory) {
		XStream xstream = prepareXStream();
		String str;
		try {
			File inputFile = new File(outputDirectory, "cukeperf.xml");
			str = FileUtils.readFileToString(inputFile);
			return ((ProjectSummary) xstream.fromXML(str));
		} catch (Exception e) {
			// returning null indicates that this file does not exist - either
			// the job hasn't run or is currently running
			return null;
		}

	}

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

	private static XStream prepareXStream() {
		XStream xstream = new XStream();
		xstream.processAnnotations(com.castlemon.jenkins.performance.domain.reporting.ProjectSummary.class);
		xstream.processAnnotations(com.castlemon.jenkins.performance.domain.reporting.Summary.class);
		xstream.processAnnotations(com.castlemon.jenkins.performance.domain.reporting.PerformanceEntry.class);
		xstream.setClassLoader(com.castlemon.jenkins.performance.domain.reporting.ProjectSummary.class
				.getClassLoader());
		return xstream;
	}

	public static void sortSummaryList(List<Summary> summaries) {
		Collections.sort(summaries, new SummaryOrderComparator());
	}

	public static long getDurationInSeconds(Long duration) {
		Duration minutes = new Duration(duration);
		return minutes.getStandardSeconds();
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
		if (durationInNanos < nanosInAMilli) {
			return (durationInNanos + " ns");
		}
		PeriodFormatter formatter = new PeriodFormatterBuilder()
				.printZeroRarelyLast().appendDays()
				.appendSuffix(" day", " days").appendSeparator(" ")
				.appendHours().appendSuffix(" hour", " hours")
				.appendSeparator(" and ").appendMinutes()
				.appendSuffix(" min", " mins").appendSeparator(" ")
				.appendSeconds().appendSuffix(" sec", " secs")
				.appendSeparator(" ").appendMillis().appendSuffix(" ms", " ms")
				.toFormatter();
		return formatter.print(new Period(0, durationInNanos / nanosInAMilli));
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
