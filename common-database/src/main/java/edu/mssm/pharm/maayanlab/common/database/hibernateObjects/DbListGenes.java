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
import edu.mssm.pharm.maayanlab.common.math.NumberUtils;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "listgenes", catalog = "enrichr")
public class DbListGenes extends Gene implements Serializable {

	private static final long serialVersionUID = 8287127875319016100L;
	private int listGeneId;
	private DbGene dbGene;
	private DbList dbList;
	private Double weight = 1.0;

	public DbListGenes() {
	}

	public DbListGenes(DbGene dbGene) {
		this.dbGene = dbGene;
	}

	public DbListGenes(DbGene dbGene, double weight) {
		this.dbGene = dbGene;
		setWeight(weight);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "listGeneId", unique = true, nullable = false)
	public int getListGeneId() {
		return this.listGeneId;
	}

	public void setListGeneId(int listGeneId) {
		this.listGeneId = listGeneId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "listId", nullable = false)
	public DbList getDbList() {
		return dbList;
	}

	public void setDbList(DbList dbList) {
		this.dbList = dbList;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "geneId", nullable = false)
	@Cascade({ CascadeType.SAVE_UPDATE })
	public DbGene getDbGene() {
		return dbGene;
	}

	public void setDbGene(DbGene dbGene) {
		this.dbGene = dbGene;
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
		return dbGene.getName();
	}

	@Override
	public String toString() {
		return dbGene.toString() + " " + NumberUtils.round(getWeight(), 3);
	}

}
