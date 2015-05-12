/**
 * Handles the math to calculate fuzzy enrichment
 * 
 * @author		Edward Y. Chen
 * @since		05/20/2013 
 */

package edu.mssm.pharm.maayanlab.common.math;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import pal.statistics.FisherExact;

public class FuzzyEnrichment {
	
	// Get p-value for fuzzy enrichment for crisp input vs fuzzy gene set
	public static double getPValue(Set<String> input, Map<String, Double> fuzzyGeneSet, Set<String> genePool) {
		double actualScore = calculateOverlap(input, normalizeFuzzySet(fuzzyGeneSet));
		
		int numOfTermGenes = fuzzyGeneSet.size();
		int numOfBgGenes = genePool.size();
		int numOfInputGenes = input.size();
		int numOfOverlappingGenes = (int) Math.round(actualScore);
		
		FisherExact fisherTest = new FisherExact(numOfInputGenes + numOfBgGenes);				
		double pValue = fisherTest.getRightTailedP(numOfOverlappingGenes, numOfInputGenes - numOfOverlappingGenes, numOfTermGenes, numOfBgGenes - numOfTermGenes);
		
		return pValue;
	}
	
	// Get p-value for fuzzy enrichment for fuzzy input vs crisp gene set
	public static double getPValue(Map<String, Double> fuzzyInput, Set<String> geneSet, Set<String> genePool) {
		double actualScore = calculateOverlap(geneSet, normalizeFuzzySet(fuzzyInput));
		
		int numOfTermGenes = geneSet.size();
		int numOfBgGenes = genePool.size();
		int numOfInputGenes = fuzzyInput.size();
		int numOfOverlappingGenes = (int) Math.round(actualScore);
		
		FisherExact fisherTest = new FisherExact(numOfInputGenes + numOfBgGenes);				
		double pValue = fisherTest.getRightTailedP(numOfOverlappingGenes, numOfInputGenes - numOfOverlappingGenes, numOfTermGenes, numOfBgGenes - numOfTermGenes);
		
		return pValue;
	}
	
	// Get p-value for fuzzy enrichment for fuzzy input vs fuzzy gene set
	public static double getPValue(Map<String, Double> fuzzyInput, Map<String, Double> fuzzyGeneSet, Set<String> genePool) {		
		double actualScore = calculateOverlap(normalizeFuzzySet(fuzzyInput), normalizeFuzzySet(fuzzyGeneSet));
		
		int numOfTermGenes = fuzzyGeneSet.size();
		int numOfBgGenes = genePool.size();
		int numOfInputGenes = fuzzyInput.size();
		int numOfOverlappingGenes = (int) Math.round(actualScore);
		
		FisherExact fisherTest = new FisherExact(numOfInputGenes + numOfBgGenes);				
		double pValue = fisherTest.getRightTailedP(numOfOverlappingGenes, numOfInputGenes - numOfOverlappingGenes, numOfTermGenes, numOfBgGenes - numOfTermGenes);
		
		return pValue;
	}
	
	// Calculate overlap for a crisp list vs fuzzy list
	private static double calculateOverlap(Set<String> unweightedList, Map<String, Double> weightedList) {
		double overlapScore = 0;
		for (String name : unweightedList) {
			if (weightedList.containsKey(name)) {
				overlapScore += Math.min(weightedList.get(name), 1);
			}
		}
		
		return overlapScore;
	}
	
	// Calculate overlap for a fuzzy list vs fuzzy list
	private static double calculateOverlap(Map<String, Double> weightedList1, Map<String, Double> weightedList2) {
		double overlapScore = 0;
		for (String name : weightedList1.keySet()) {
			if (weightedList2.containsKey(name)) {
				overlapScore += Math.min(weightedList1.get(name), weightedList2.get(name));
			}
		}
		
		return overlapScore;
	}
	
	// Normalize degree of membership so you can use Fisher Exact Test on it
	private static Map<String, Double> normalizeFuzzySet(Map<String, Double> fuzzySet) {
		HashMap<String, Double> normalizedSet = new HashMap<String, Double>(NumberUtils.roundUpDivision(fuzzySet.size()*4, 3));
		
		double total = 0.0;
		for (Double value : fuzzySet.values()) {
			total += value;
		}
		
		double scale = fuzzySet.size() / total;
		for (String key : fuzzySet.keySet()) {
			normalizedSet.put(key, fuzzySet.get(key) * scale);
		}
		
		return normalizedSet;
	}
}
