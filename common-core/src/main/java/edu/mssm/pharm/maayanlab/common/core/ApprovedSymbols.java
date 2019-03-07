package edu.mssm.pharm.maayanlab.common.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class ApprovedSymbols {

	private ArrayList<String> geneList;
	private HashSet<String> genes;
	private Random rng = new Random();
	
	public static void main(String[] args) {
		ApprovedSymbols approvedSymbols = ApprovedSymbols.getInstance();
		approvedSymbols.setRandomSeed(0);		
		for (String name : approvedSymbols.randomSample(10)) {
			System.out.println(name);
		}
	}
	
	private ApprovedSymbols() {
		geneList = FileUtils.readResource("approved_symbols.txt");
		genes = new HashSet<String>(geneList);
	}
	
	private static class ApprovedSymbolsHolder {
		public static final ApprovedSymbols INSTANCE = new ApprovedSymbols();
	}
	
	public static ApprovedSymbols getInstance() {
		return ApprovedSymbolsHolder.INSTANCE;
	}
	
	public void setRandomSeed(long seed) {
		rng = new Random(seed);
	}
	
	public boolean contains(String gene) {
		return genes.contains(gene);
	}
	
	public HashSet<String> randomSample(int size) {
		HashSet<String> sampleList = new HashSet<String>();
		while (sampleList.size() < size) {
			sampleList.add(geneList.get(rng.nextInt(geneList.size())));
		}
		return sampleList;
	}
}
