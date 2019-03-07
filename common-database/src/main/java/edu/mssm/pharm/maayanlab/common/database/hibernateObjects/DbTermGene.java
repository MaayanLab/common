package edu.mssm.pharm.maayanlab.common.database.hibernateObjects;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import edu.mssm.pharm.maayanlab.common.database.Gene;

@Entity
@DynamicInsert
@DynamicUpdate
//@Cache(usage=CacheConcurrencyStrategy.READ_ONLY)
@Table(name = "termgenes", catalog = "enrichr")
public class DbTermGene extends Gene implements Serializable {

	private static final long serialVersionUID = 4207016932083532101L;

	private int termGeneId;
	private DbTerm dbTerm;
	private String name;
	private Double weight = 1.0;
	private DbGene dbGene;

	public DbTermGene() {
	}

	public DbTermGene(String name, double weight) {
		this.name = name;
		this.weight = weight;
	}

	public DbTermGene(DbGene gene, double weight, DbTerm term) {
		this.dbGene = gene;
		this.weight = weight;
		this.dbTerm = term;
	}

	public DbTermGene(DbGene gene, DbTerm term) {
		this.dbGene = gene;
		this.dbTerm = term;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "termGeneId", unique = true, nullable = false)
	public int getTermGeneId() {
		return termGeneId;
	}

	public void setTermGeneId(int termGeneId) {
		this.termGeneId = termGeneId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "termId", nullable = false)
	public DbTerm getDbTerm() {
		return dbTerm;
	}

	public void setDbTerm(DbTerm dbTerm) {
		this.dbTerm = dbTerm;
	}

	@Column(name = "weight")
	public Double getWeight() {
		return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	@Override
	@Transient
	public String getName() {
		if (dbGene != null)
			return dbGene.getName();
		else
			return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "geneId", nullable = false)
	@Cascade({ CascadeType.ALL })
	public DbGene getDbGene() {
		return dbGene;
	}

	public void setDbGene(DbGene gene) {
		this.dbGene = gene;
	}

}
