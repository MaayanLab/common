package edu.mssm.pharm.maayanlab.common.database.hibernateObjects;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import edu.mssm.pharm.maayanlab.common.database.Gene;

@Entity
@DynamicInsert
@DynamicUpdate
//@Cache(usage=CacheConcurrencyStrategy.READ_ONLY)
@Table(name = "genes", catalog = "enrichr")
public class DbGene extends Gene implements Serializable {

	private static final long serialVersionUID = -8357672983491020270L;
	private int geneId;
	private String name;

	public DbGene() {
	}

	public DbGene(String name) {
		this.name = name;
	}

	public DbGene(int geneId, String name) {
		this.geneId = geneId;
		this.name = name;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "geneId", unique = true, nullable = false)
	public int getGeneId() {
		return geneId;
	}

	public void setGeneId(int geneId) {
		this.geneId = geneId;
	}

	@Override
	@Column(name = "name", nullable = false, unique = true, length = 100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	@Transient
	public Double getWeight() {
		return 1.0;
	}

	@Override
	public String toString() {
		return getName();
	}
	
}
