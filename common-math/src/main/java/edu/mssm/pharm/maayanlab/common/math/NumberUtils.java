package edu.mssm.pharm.maayanlab.common.math;

public class NumberUtils {

	public static int findMin(int[] values) {
		int min = values[0];
		
		for (int value : values) {
			min = Math.min(min, value);
		}
		
		return min;
	}
	
	public static double findMin(double[] values) {
		double min = values[0];
		
		for (double value : values) {
			min = Math.min(min, value);
		}
		
		return min;
	}
	
	public static int findMax(int[] values) {
		int max = values[0];
		
		for (int value : values) {
			max = Math.max(max, value);
		}
		
		return max;
	}
	
	public static double findMax(double[] values) {
		double max = values[0];
		
		for (double value : values) {
			max = Math.max(max, value);
		}
		
		return max;
	}
	
	public static int roundUpDivision(int number, int divisor) {
		return (number + divisor - 1) / divisor;
	}
	
}
