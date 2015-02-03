

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
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import javax.persistence.SecondaryTables;
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
@Table(name = "silentlySavedListGenes", catalog = "enrichr")
@SecondaryTables({
    @SecondaryTable(name="silentlySavedGeneWeights", pkJoinColumns={
        @PrimaryKeyJoinColumn(name="listGeneId", referencedColumnName="listGeneId") })
})
public class DbSilentlySavedListGenes implements Serializable, Gene {

	private static final long serialVersionUID = 8287127875319016100L;
	private int listGeneId;
	private DbGene dbGene;
	private DbSilentlySavedList dbSilentlySavedList;
	private Double weight;

	public DbSilentlySavedListGenes() {
	}

	public DbSilentlySavedListGenes(DbGene dbGene, DbSilentlySavedList savedList) {
		this.dbGene = dbGene;
		this.dbSilentlySavedList = savedList;
	}
	
	public DbSilentlySavedListGenes(DbGene dbGene, DbSilentlySavedList savedList, double weight) {
		this.dbGene = dbGene;
		this.dbSilentlySavedList = savedList;
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
	public DbSilentlySavedList getSilentlySavedList() {
		return this.dbSilentlySavedList;
	}

	public void setSilentlySavedList(DbSilentlySavedList dbSilentlySavedList) {
		this.dbSilentlySavedList = dbSilentlySavedList;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "geneId", nullable = false)
	@Cascade({CascadeType.SAVE_UPDATE})
	public DbGene getGene() {
		return dbGene;
	}

	public void setGene(DbGene dbGene) {
		this.dbGene = dbGene;
	}

	@Column(name = "weight", table = "silentlySavedGeneWeights")
	public Double getWeight() {
		if(weight == null)
			return 1.0;
		else
			return weight;
	}

	public void setWeight(Double weight) {
		this.weight = weight;
	}

	@Override
	public boolean equals(Object obj) {
		return (obj instanceof DbSilentlySavedListGenes)&&((DbSilentlySavedListGenes) obj).getGene().equals(this.getGene())&&
				((DbSilentlySavedListGenes) obj).getSilentlySavedList().equals(this.getSilentlySavedList());
	}

	@Override
	@Transient
	public String getName() {
		return dbGene.getName();
	}
}
