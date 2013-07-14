package com.castlemon.jenkins.performance.util;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.tools.ant.DirectoryScanner;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import com.castlemon.jenkins.performance.domain.Scenario;
import com.castlemon.jenkins.performance.domain.reporting.ProjectPerformanceEntry;
import com.castlemon.jenkins.performance.domain.reporting.ProjectSummary;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CucumberPerfUtils {

	private static int nanosInAMilli = 1000000;

	public static String buildProjectGraphData(ProjectSummary projectSummary) {
		StringBuilder output = new StringBuilder();
		output.append("[");
		int i = 1;
		for (ProjectPerformanceEntry run : projectSummary
				.getPerformanceEntries()) {
			output.append("["
					+ run.getBuildNumber()
					+ ", "
					+ getDurationInSeconds(run.getElapsedTime() / nanosInAMilli)
					+ "]");
			if (i < projectSummary.getPerformanceEntries().size()) {
				output.append(",");
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

	public static List<Scenario> getData(String[] jsonReportFiles,
			File targetBuildDirectory) {
		List<Scenario> overallScenarios = new ArrayList<Scenario>();
		for (String fileName : jsonReportFiles) {
			String jsonInput = loadJsonFile(targetBuildDirectory, fileName);
			if (StringUtils.isNotEmpty(jsonInput)) {
				overallScenarios.addAll(getData(jsonInput));
			}
		}
		return overallScenarios;
	}

	public static List<Scenario> getData(String jsonInput) {
		ObjectMapper mapper = new ObjectMapper();
		List<Scenario> scenarios = new ArrayList<Scenario>();
		try {
			JavaType type = mapper.getTypeFactory().constructCollectionType(
					List.class, Scenario.class);
			scenarios = mapper.readValue(jsonInput, type);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return scenarios;
	}
	
	public static String formatDuration(Long duration) {
        PeriodFormatter formatter = new PeriodFormatterBuilder()
                .appendDays()
                .appendSuffix(" day", " days")
                .appendSeparator(" and ")
                .appendHours()
                .appendSuffix(" hour", " hours")
                .appendSeparator(" and ")
                .appendMinutes()
                .appendSuffix(" min", " mins")
                .appendSeparator(" and ")
                .appendSeconds()
                .appendSuffix(" sec", " secs")
                .appendSeparator(" and ")
                .appendMillis()
                .appendSuffix(" ms", " ms")
                .toFormatter();
        return formatter.print(new Period(0, duration / 1000000));
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
