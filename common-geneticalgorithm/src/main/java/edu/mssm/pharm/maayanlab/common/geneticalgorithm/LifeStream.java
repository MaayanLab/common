package edu.mssm.pharm.maayanlab.common.geneticalgorithm;

import java.util.Collection;
import java.util.concurrent.Callable;

import cern.colt.bitvector.BitVector;

public abstract class LifeStream implements Callable<Collection<String>> {
	
		protected BitVector geneticCode;
		protected Collection<String> input;

		public LifeStream(BitVector geneticCode, Collection<String> input) {
			this.geneticCode = geneticCode;
			this.input = input;
		}
		
		@Override
		public abstract Collection<String> call() throws Exception;
	
}
