package com.castlemon.jenkins.performance.domain.reporting;

import junit.framework.Assert;

import org.agileware.test.PropertiesTester;
import org.junit.Test;

public class PerformanceEntryTest {

	@Test
	public void test() throws Exception {
		PropertiesTester tester = new PropertiesTester();
		tester.testAll(PerformanceEntry.class);
	}

	@Test
	public void testAddToPassedSteps() {
		PerformanceEntry entry = new PerformanceEntry();
		entry.setPassedSteps(4);
		entry.addToPassedSteps(5);
		Assert.assertEquals(9, entry.getPassedSteps());
	}

	@Test
	public void testAddToSkippedSteps() {
		PerformanceEntry entry = new PerformanceEntry();
		entry.setSkippedSteps(3);
		entry.addToSkippedSteps(7);
		Assert.assertEquals(10, entry.getSkippedSteps());
	}

	@Test
	public void testAddToElapsedTime() {
		PerformanceEntry entry = new PerformanceEntry();
		entry.setElapsedTime(1l);
		entry.addToElapsedTime(12l);
		Assert.assertEquals(13l, entry.getElapsedTime());
	}

	@Test
	public void testAddToFailedSteps() {
		PerformanceEntry entry = new PerformanceEntry();
		entry.setFailedSteps(2);
		entry.addToFailedSteps(6);
		Assert.assertEquals(8, entry.getFailedSteps());
	}

}
