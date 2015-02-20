package edu.mssm.pharm.maayanlab.common.database;

public interface Gene {
	public String getName();
	public Double getWeight();
	public boolean equals(Gene other);
}
