package com.castlemon.jenkins.performance.domain.reporting.comparator;

import java.util.Comparator;

import com.castlemon.jenkins.performance.domain.reporting.Summary;

public class SummaryLongestDurationComparator implements Comparator<Summary> {

	public int compare(Summary summary1, Summary summary2) {

		long order1 = summary1.getLongestDuration();
		long order2 = summary2.getLongestDuration();

		// ascending order
		return (int) (order1 - order2);

	}

};
