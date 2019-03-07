/**
 * Data structure to represent a gene-set library.
 * 
 * @author		Edward Y. Chen
 * @since		5/14/2013 
 */

package edu.mssm.pharm.maayanlab.common.bio;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class GeneSetLibrary {

	protected final HashMap<String, Term> terms = new HashMap<String, Term>();
	protected final HashSet<String> genePool = new HashSet<String>();	
	protected int genePoolSize;
	protected boolean isRanked = false;
	
	public GeneSetLibrary(Collection<String> libraryLines) {
		constructTerms(libraryLines);
		genePoolSize = genePool.size();
	}
	
	public GeneSetLibrary(Collection<String> libraryLines, Collection<String> rankLines) {
		this(libraryLines);
		constructRanks(rankLines);
	}
	
	protected void constructTerms(Collection<String> libraryLines) {
		for (String line : libraryLines) {
			// In gmt file, 1st column is key, 2nd column is irrelevant, and the rest are the values
			String[] splitLine = line.split("\\t");
			String termName = splitLine[0];
				
			Set<String> geneSet;
			if (terms.containsKey(termName))	// If term exists, append to existing gene set
				geneSet = terms.get(termName).getGeneSet();
			else
				geneSet = new HashSet<String>();
				
			for (int i = 2; i < splitLine.length; i++) {
				String gene = splitLine[i].toUpperCase();
				genePool.add(gene);
				geneSet.add(gene);
			}
			
			if (terms.containsKey(termName)) {	// If term exists, reassign gene set
				terms.get(termName).setGeneSet(geneSet);
			}
			else {
				Term term = new Term(termName, this, geneSet);
				terms.put(termName, term);
			}
		}
	}
	
	protected void constructRanks(Collection<String> rankLines) {
		for (String line : rankLines) {
			String[] splitLine = line.split("\\t");	// Splits into 3 columns: name, average, std
			Term term = terms.get(splitLine[0]);
			term.setMean(Double.parseDouble(splitLine[1]));
			term.setStandardDeviation(Double.parseDouble(splitLine[2]));
		}
		this.isRanked = true;
	}
	
	public Term getTerm(String termName) {
		return terms.get(termName);
	}

	public Collection<Term> getTerms() {
		return terms.values();
	}
	
	public Set<String> getGenePool() {
		return genePool;
	}
	
	public int getGenePoolSize() {
		return genePoolSize;
	}
	
	public boolean contains(String gene) {
		return genePool.contains(gene);
	}

	public boolean isRanked() {
		return isRanked;
	}
}
