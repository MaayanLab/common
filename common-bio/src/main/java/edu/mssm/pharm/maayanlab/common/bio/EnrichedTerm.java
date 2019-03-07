package edu.mssm.pharm.maayanlab.common.bio;

import java.text.DecimalFormat;
import java.util.Set;

public class EnrichedTerm implements Comparable<EnrichedTerm> {

	private Term associatedTerm;

	private Set<String> overlap;
	private int numOfOverlappingGenes;
	
	private double pValue;
	private double adjustedPValue;
	private double zScore;
	private double combinedScore;
	
	public EnrichedTerm(Term associatedTerm, Set<String> overlap, double pValue) {
		this.associatedTerm = associatedTerm;
		this.overlap = overlap;
		this.numOfOverlappingGenes = overlap.size();
		this.pValue = (pValue == 0) ? Double.MIN_VALUE : pValue;
	}
	
	public String getName() {
		return associatedTerm.getName();
	}
	
	public double getPValue() {
		return this.pValue;
	}
	
	public double getAdjustedPValue() {
		return this.adjustedPValue;
	}
	
	public double getZScore() {
		return this.zScore;
	}
	
	public double getCombinedScore() {
		return this.combinedScore;
	}
	
	public boolean hasAssociation(String gene) {
		return associatedTerm.hasAssociation(gene);
	}
	
	public Set<String> getGeneSet() {
		return associatedTerm.getGeneSet();
	}
	
	public Set<String> getOverlap() {
		return this.overlap;
	}
	
	public void setAdjustedPValue(double adjustedPValue) {
		this.adjustedPValue = adjustedPValue;
	}
	
	public void computeZScore(int rank) {
		double mean = associatedTerm.getMean();
		double standardDeviation = associatedTerm.getStandardDeviation();
		
		if (mean == 0 && standardDeviation == 0)
			zScore = 0;
		else
			zScore = (rank - mean)/standardDeviation;
	}
	
	public void computeScore() {
		combinedScore = zScore*Math.log(adjustedPValue);
	}
	
	public void computeUnadjustedScore() {
		combinedScore = zScore*Math.log(pValue);
	}
	
	@Override
	public String toString() {
		DecimalFormat df = new DecimalFormat("#.############");
		StringBuilder outputString = new StringBuilder();
		outputString.append(associatedTerm.getName()).append("\t");
		outputString.append(numOfOverlappingGenes).append("/").append(associatedTerm.getNumOfTermGenes()).append("\t");
		outputString.append(df.format(pValue)).append("\t");
		outputString.append(df.format(adjustedPValue)).append("\t");
		outputString.append(df.format(zScore)).append("\t");
		outputString.append(df.format(combinedScore)).append("\t");
		
		boolean firstTarget = true;
		for (String target : overlap) {
			if (firstTarget) {
				outputString.append(target);
				firstTarget = false;
			}
			else
				outputString.append(";").append(target);
		}
		
		return outputString.toString();
	}
	
	//@Override
	public int compareTo(EnrichedTerm o) {
		if (this.pValue > o.pValue)
			return 1;
		else if (this.pValue < o.pValue)
			return -1;
		else
			return 0;
	}
	
}
