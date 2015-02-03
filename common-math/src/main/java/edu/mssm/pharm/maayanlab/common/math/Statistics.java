package edu.mssm.pharm.maayanlab.common.math;

import java.util.Collection;

public class Statistics {
	
	public static double findMean(int[] values) {
		double mean = 0;
		
		for (int value : values)
			mean += value;
		mean /= values.length;
		
		return mean;
	}
	
	public static double findMean(double[] values) {
		double mean = 0;
		
		for (double value : values)
			mean += value;
		mean /= values.length;
		
		return mean;
	}
	
	public static double findMean(Collection<Integer> values) {
		double mean = 0;
		
		for (int value : values)
			mean += value;
		mean /= values.size();
		
		return mean;
	}
	
	public static double findStandardDeviation(int[] values, double mean) {
		double sd = 0;
		
		for (int value : values)
			sd += Math.pow(value-mean, 2);
		sd /= values.length;
		sd = Math.pow(sd, 0.5);
		
		return sd;
	}
	
	public static double findStandardDeviation(double[] values, double mean) {
		double sd = 0;
		
		for (double value : values)
			sd += Math.pow(value-mean, 2);
		sd /= values.length;
		sd = Math.pow(sd, 0.5);
		
		return sd;
	}	
	
	public static double findStandardDeviation(Collection<Integer> values, double mean) {
		double sd = 0;
		
		for (int value : values)
			sd += Math.pow(value-mean, 2);
		
		sd /= values.size();
		sd = Math.pow(sd, 0.5);
		
		return sd;
	}
	
}
