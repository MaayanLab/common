package edu.mssm.pharm.maayanlab.common.math;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SetOps {

	public static Set<String> union(Collection<String> setA, Collection<String> setB) {		
		HashSet<String> resultSet = new HashSet<String>(setA);
		resultSet.addAll(setB);
		return resultSet;
	}
	
	public static Set<String> intersection(Collection<String> setA, Collection<String> setB) {
		HashSet<String> resultSet = new HashSet<String>(setA);
		resultSet.retainAll(setB);
		return resultSet;
	}
	
	public static Set<String> difference(Collection<String> setA, Collection<String> setB) {
		HashSet<String> resultSet = new HashSet<String>(setA);
		resultSet.removeAll(intersection(setA, setB));
		return resultSet;
	}
	
	public static Set<String> symmetricDifference(Collection<String> setA, Collection<String> setB) {
		Set<String> resultSet = union(setA, setB);
		resultSet.removeAll(intersection(setA, setB));
		return resultSet;
	}
	
}
