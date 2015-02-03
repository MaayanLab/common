package edu.mssm.pharm.maayanlab.common.database.hibernateObjects;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.SecondaryTable;
import javax.persistence.SecondaryTables;
import javax.persistence.Table;

import org.hibernate.annotations.Immutable;

import com.google.gson.annotations.Expose;

@NamedQueries({
	@NamedQuery(name = "getDatasetStatistics", query = "FROM DbLibraryStatistics WHERE numTerms > 0 AND isActive = 1")
})

@Entity
@Immutable
@Table(name = "libraryStatistics", catalog = "enrichr")
@SecondaryTables({ @SecondaryTable(name = "geneSetLibrary", pkJoinColumns = { @PrimaryKeyJoinColumn(name = "libraryId", referencedColumnName = "libraryId") }) })
public class DbLibraryStatistics implements Serializable {
	
	private static final long serialVersionUID = -6934279514210968083L;
	@Expose
	private String libraryName;
	private int libraryId;
	@Expose
	private int numTerms;
	@Expose
	private int geneCoverage;
	@Expose
	private float genesPerTerm;
	@Expose
	private String link;
	private boolean isActive;

	@Id
	@Column(name = "libraryId", unique = true, nullable = false)
	public int getLibraryId() {
		return libraryId;
	}

	public void setLibraryId(int libraryId) {
		this.libraryId = libraryId;
	}

	@Column(name = "numTerms")
	public int getNumTerms() {
		return numTerms;
	}

	public void setNumTerms(int numTerms) {
		this.numTerms = numTerms;
	}

	@Column(name = "geneCoverage")
	public int getGeneCoverage() {
		return geneCoverage;
	}

	public void setGeneCoverage(int geneCoverage) {
		this.geneCoverage = geneCoverage;
	}

	@Column(name = "genesPerTerm")
	public float getGenesPerTerm() {
		return genesPerTerm;
	}

	public void setGenesPerTerm(float genesPerTerm) {
		this.genesPerTerm = genesPerTerm;
	}

	@Column(name = "link")
	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Column(name = "libraryName", table = "geneSetLibrary")
	public String getLibraryName() {
		return libraryName;
	}

	public void setLibraryName(String libraryName) {
		this.libraryName = libraryName;
	}
	
	@Column(name = "isActive", table = "geneSetLibrary")
	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

}
