package com.castlemon.jenkins.performance;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.Action;
import hudson.model.BuildListener;
import hudson.model.Computer;
import hudson.model.Run;
import hudson.model.RunMap;
import hudson.slaves.SlaveComputer;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Publisher;
import hudson.tasks.Recorder;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;
import com.castlemon.jenkins.performance.domain.reporting.ProjectRun;
import com.castlemon.jenkins.performance.reporting.ReportBuilder;
import com.castlemon.jenkins.performance.util.CucumberPerfUtils;

public class CucumberPerfRecorder extends Recorder {

	public final String jsonReportDirectory;
	public final String jsonReportFileName;
	public final int countOfSortedSummaries;
	private ReportBuilder reportBuilder;
	private File targetBuildDirectory;

	// Fields in config.jelly must match the parameter names in the
	// "DataBoundConstructor"
	@DataBoundConstructor
	public CucumberPerfRecorder(String jsonReportDirectory,
			String jsonReportFileName, int countOfSortedSummaries) {
		this.jsonReportDirectory = jsonReportDirectory;
		if (StringUtils.isNotBlank(jsonReportFileName)) {
			this.jsonReportFileName = jsonReportFileName;
		} else {
			this.jsonReportFileName = "cucumber.json";
		}
		if (countOfSortedSummaries == 0) {
			this.countOfSortedSummaries = 20;
		} else {
			this.countOfSortedSummaries = countOfSortedSummaries;
		}
	}

	@Override
	public boolean perform(AbstractBuild<?, ?> build, Launcher launcher,
			BuildListener listener) throws IOException, InterruptedException {
		listener.getLogger()
				.println(
						"[CucumberPerfRecorder] Starting Cucumber Performance Report generation...");
		reportBuilder = new ReportBuilder();
		targetBuildDirectory = new File(build.getRootDir(),
				"cucumber-perf-reports");
		if (!targetBuildDirectory.exists()) {
			boolean success = targetBuildDirectory.mkdirs();
			if(!success) {
				listener.getLogger().println("[CucumberPerfRecorder] FAILED to create " + targetBuildDirectory.getAbsolutePath());
			}
		}
		String buildProjectName = build.getProject().getName();
		listener.getLogger().println(
				"[CucumberPerfRecorder] Reporting on performance for "
						+ buildProjectName + " #"
						+ Integer.toString(build.getNumber()));
		gatherJsonResultFiles(build, listener, targetBuildDirectory);
		return generateProjectReport(build, listener, targetBuildDirectory,
				buildProjectName);
	}

	private boolean generateProjectReport(AbstractBuild<?, ?> build,
			BuildListener listener, File targetBuildDirectory,
			String buildProjectName) throws IOException, InterruptedException {
		List<ProjectRun> projectRuns = new ArrayList<ProjectRun>();
		RunMap<?> runMap = build.getProject()._getRuns();
		for (Run<?, ?> run : runMap) {
			ProjectRun projectRun = new ProjectRun();
			projectRun.setRunDate(run.getTime());
			projectRun.setBuildNumber(run.getNumber());
			File workspaceJsonReportDirectory = run.getArtifactsDir()
					.getParentFile();
			projectRun.setFeatures(CucumberPerfUtils.getData(CucumberPerfUtils
					.findJsonFiles(workspaceJsonReportDirectory,
							"**/cucumber-perf*.json"),
					workspaceJsonReportDirectory));
			listener.getLogger().println("found files");
			// only report on runs that have been analysed
			if (!projectRun.getFeatures().isEmpty()) {
				projectRuns.add(projectRun);
			}
		}
		listener.getLogger().println(
				"[CucumberPerfRecorder] running project reports on "
						+ projectRuns.size() + " builds");
		boolean success = reportBuilder.generateProjectReports(projectRuns,
				targetBuildDirectory, buildProjectName);
		listener.getLogger().println(
				"[CucumberPerfRecorder] project report generation complete");
		return success;
	}

	private void gatherJsonResultFiles(AbstractBuild<?, ?> build,
			BuildListener listener, File targetBuildDirectory)
			throws IOException, InterruptedException {
		File workspaceJsonReportDirectory = new File(getWorkspacePath(build));
		if (StringUtils.isNotBlank(jsonReportDirectory)) {
			workspaceJsonReportDirectory = new File(getWorkspacePath(build), jsonReportDirectory);
		}
		// if we are on a slave
		if (Computer.currentComputer() instanceof SlaveComputer) {
			listener.getLogger().println(
					"[CucumberPerfRecorder] detected slave build ");
			FilePath projectWorkspaceOnSlave = getSlaveWorkSpace(build, listener);
			FilePath masterJsonReportDirectory = new FilePath(
					targetBuildDirectory);
			projectWorkspaceOnSlave.copyRecursiveTo("**/" + jsonReportFileName,
					"", masterJsonReportDirectory);
		} else {
			// if we are on the master
			listener.getLogger().println(
					"[CucumberPerfRecorder] detected master build ");
			listener.getLogger().println(
					"looking in "
							+ workspaceJsonReportDirectory.getAbsolutePath());
			String[] files = CucumberPerfUtils.findJsonFiles(
					workspaceJsonReportDirectory, jsonReportFileName);

			if (files.length != 0) {
				for (String file : files) {
					FileUtils.copyFile(
							new File(workspaceJsonReportDirectory.getPath()
									+ "/" + file), new File(
									targetBuildDirectory, file));
				}
			} else {
				listener.getLogger().println(
						"[CucumberPerfRecorder] there were no json results found in: "
								+ workspaceJsonReportDirectory);
			}
		}
		// rename the json file in the performance report directory
		String[] oldJsonReportFiles = CucumberPerfUtils.findJsonFiles(
				targetBuildDirectory, "*.json");
		int i = 0;
		for (String fileName : oldJsonReportFiles) {
			File file = new File(targetBuildDirectory, fileName);
			String newFileName = "cucumber-perf" + i + ".json";
			File newFile = new File(targetBuildDirectory, newFileName);
			try {
				FileUtils.moveFile(file, newFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
			i++;
		}
	}

	private FilePath getSlaveWorkSpace(AbstractBuild<?, ?> build, BuildListener listener) {
		AbstractProject<?, ?> project = build.getProject();
		FilePath filePath = null;
		if(project != null){
			filePath = project.getSomeWorkspace();
			if(filePath == null){
				listener.error("Cannot get workspace on slave");
			}
		}
		return filePath;
	}

	private String getWorkspacePath(AbstractBuild<?, ?> build) throws IOException, InterruptedException {
		if(build == null){
			return "";
		}
		FilePath workspace = build.getWorkspace();
		if(workspace == null){
			return "";
		}
		return workspace.toURI().getPath();
	}

	public BuildStepMonitor getRequiredMonitorService() {
		return BuildStepMonitor.NONE;
	}

	@Override
	public Action getProjectAction(AbstractProject<?, ?> project) {
		return new CucumberProjectAction(project, countOfSortedSummaries);
	}

	@Extension
	public static final class DescriptorImpl extends
			BuildStepDescriptor<Publisher> {

		public boolean isApplicable(Class<? extends AbstractProject> aClass) {
			return true;
		}

		/**
		 * This human readable name is used in the configuration screen.
		 */
		public String getDisplayName() {
			return "Generate Cucumber performance reports";
		}

	}

}
