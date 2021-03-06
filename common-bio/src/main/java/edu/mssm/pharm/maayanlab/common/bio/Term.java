package edu.mssm.pharm.maayanlab.common.bio;

import java.util.Map;
import java.util.Set;

import pal.statistics.FisherExact;

import edu.mssm.pharm.maayanlab.common.math.FuzzyEnrichment;
import edu.mssm.pharm.maayanlab.common.math.SetOps;

public class Term {

	private String name;
	protected GeneSetLibrary geneSetLibrary;
	
	private Set<String> geneSet;
	protected int numOfTermGenes;
	
	private double mean;
	private double standardDeviation;
	
	public Term(String name, GeneSetLibrary geneSetLibrary) {
		this.name = name;
		this.geneSetLibrary = geneSetLibrary;
	}
	
	public Term(String name, GeneSetLibrary geneSetLibrary, Set<String> geneSet) {
		this(name, geneSetLibrary);
		setGeneSet(geneSet);
	}
	
	public String getName() {
		return this.name;
	}
	
	public boolean hasAssociation(String gene) {
		return geneSet.contains(gene);
	}
	
	public Set<String> getGeneSet() {
		return this.geneSet;
	}
	
	public void setGeneSet(Set<String> geneSet) {
		this.geneSet = geneSet;
		this.numOfTermGenes = geneSet.size();
	}
	
	public int getNumOfTermGenes() {
		return this.numOfTermGenes;
	}
	
	public double getMean() {
		return mean;
	}

	public void setMean(double mean) {
		this.mean = mean;
	}

	public double getStandardDeviation() {
		return standardDeviation;
	}

	public void setStandardDeviation(double standardDeviation) {
		this.standardDeviation = standardDeviation;
	}
	
	protected Set<String> getGenePool() {
		return geneSetLibrary.getGenePool();
	}
	
	public EnrichedTerm getEnrichedTerm(Set<String> inputGenes) {
		EnrichedTerm enrichedTerm = null;
		
		// Intersection of term's gene set and input genes
		Set<String> overlap = SetOps.intersection(this.getGeneSet(), inputGenes); 
		
		int numOfTermGenes = this.getNumOfTermGenes();
		int numOfBgGenes = geneSetLibrary.getGenePoolSize();
		int numOfInputGenes = inputGenes.size();
		int numOfOverlappingGenes = overlap.size();
		
		// Don't bother if there are no target input genes
		if (numOfOverlappingGenes > 0) {
			FisherExact fisherTest = new FisherExact(numOfInputGenes + numOfBgGenes);				
			double pValue = fisherTest.getRightTailedP(numOfOverlappingGenes, numOfInputGenes - numOfOverlappingGenes, numOfTermGenes, numOfBgGenes - numOfTermGenes);
							
			return new EnrichedTerm(this, overlap, pValue);
		}
				
		return enrichedTerm;
	}
	
	public EnrichedTerm getEnrichedTerm(Map<String, Double> inputGenes) {
		EnrichedTerm enrichedTerm = null;
		
		// Intersection of term's gene set and input genes
		Set<String> overlap = SetOps.intersection(this.getGeneSet(), inputGenes.keySet());
		
		if (overlap.size() > 0) {
			return new EnrichedTerm(this, overlap, FuzzyEnrichment.getPValue(inputGenes, this.getGeneSet(), getGenePool()));
		}
		
		return enrichedTerm;
	}
}
