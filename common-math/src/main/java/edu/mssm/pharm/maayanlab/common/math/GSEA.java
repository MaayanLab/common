package edu.mssm.pharm.maayanlab.common.math;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;


public class GSEA {

	public static double computeGSEA(HashSet<String> geneSet, ArrayList<String> geneList) {
		int enrichmentScore = runningSum(geneSet, geneList);
		int[] nullDistribution = generateNullDistribution(geneSet, geneList);
		int exceedCount = 0;
		
		for (int randomDeviation : nullDistribution) {
			if (randomDeviation > enrichmentScore)
				exceedCount++;
		}
		
		return ((double) exceedCount) / nullDistribution.length;
	}
	
	public static int runningSum(HashSet<String> geneSet, ArrayList<String> geneList) {
		int difference = geneList.size() - geneSet.size();
		assert difference > 0;
		int correlationCount = 0;
		
		boolean[] indicator = new boolean[geneList.size()];
		for (int i = 0; i < indicator.length; i++) {
			if (geneSet.contains(geneList.get(i))) {
				indicator[i] = true;
				correlationCount++;
			}
			else {
				indicator[i] = false;
			}
		}
		
		int positiveDelta = difference;
		int negativeDelta = correlationCount;
		
		int ES = 0;
		int maxDeviation = 0;
		
		for (boolean position : indicator) {
			if (position)
				ES += positiveDelta;
			else
				ES -= negativeDelta;
			
			maxDeviation = Math.max(maxDeviation, Math.abs(ES));
		}
		
		return maxDeviation;
	}
	
	public static int[] generateNullDistribution(HashSet<String> geneSet, ArrayList<String> geneList) {
		final int maxRuns = 1000;
		int[] randomSums = new int[maxRuns];
		
		ArrayList<String> shuffledGenes = new ArrayList<String>(geneList);
		
		for (int run = 0; run < maxRuns; run++) {
			Collections.shuffle(shuffledGenes);
			randomSums[run] = runningSum(geneSet, shuffledGenes);
		}
		
		return randomSums;
	}
	
}
