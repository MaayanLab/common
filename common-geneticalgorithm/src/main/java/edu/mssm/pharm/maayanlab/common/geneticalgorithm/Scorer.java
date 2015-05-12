package edu.mssm.pharm.maayanlab.common.geneticalgorithm;

import java.util.Collection;

public interface Scorer {

	public double score(String identifier, Collection<String> outputList);
	
}
