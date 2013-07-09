package com.castlemon.jenkins.performance;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.castlemon.jenkins.performance.domain.Elements;
import com.castlemon.jenkins.performance.domain.Result;
import com.castlemon.jenkins.performance.domain.Scenario;
import com.castlemon.jenkins.performance.domain.Step;
import com.castlemon.jenkins.performance.domain.reporting.ProjectRun;

public class TestUtils {

	public String loadJsonFile(String fileName) throws IOException {
		String content = null;
		URL fileURL = this.getClass().getResource(fileName);
		File file = new File(fileURL.getFile());
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

	public ProjectRun generateRun(String outcome) {
		ProjectRun run = new ProjectRun();

		List<Step> steps = new ArrayList<Step>();
		steps.add(generateStep(outcome));
		Elements element = new Elements();
		element.setSteps(steps);
		List<Elements> elementsList = new ArrayList<Elements>();
		elementsList.add(element);
		Scenario scenario = new Scenario();
		scenario.setElements(elementsList);
		List<Scenario> scenarios = new ArrayList<Scenario>();
		scenarios.add(scenario);
		run.setScenarios(scenarios);

		return run;
	}

	private Step generateStep(String outcome) {
		Result result = new Result();
		result.setStatus(outcome);
		result.setDuration(3456l);
		Step step = new Step();
		step.setResult(result);
		return step;
	}

}
