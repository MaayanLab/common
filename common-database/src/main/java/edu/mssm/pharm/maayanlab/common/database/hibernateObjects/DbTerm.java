package edu.mssm.pharm.maayanlab.common.database.hibernateObjects;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@NamedQueries({
	@NamedQuery(
	name = "getOverlappingTerms",
	query = "SELECT term, termGenes FROM DbTerm term " 
			+ "join term.dbTermGenes as termGenes "
			+ "where termGenes.dbGene in (:searchList) "
			+ "and term.dbGeneSetLibrary=:library")
})
@Entity
@DynamicInsert
@DynamicUpdate
//@Cache(usage=CacheConcurrencyStrategy.READ_ONLY)
@Table(name = "term", catalog = "enrichr")
public class DbTerm implements Serializable {

	private static final long serialVersionUID = 4059953668344530610L;
	private int termId;
	private String name;
	private Double mean;
	private Double std;
	
	private DbGeneSetLibrary dbGeneSetLibrary;
	private Set<DbTermGene> dbTermGenes;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "termId", unique = true, nullable = false)
	public int getTermId() {
		return termId;
	}

	public void setTermId(int termId) {
		this.termId = termId;
	}

	@Column(name = "name", nullable = false, length = 300)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "mean", nullable = true)
	public Double getMean() {
		return mean;
	}

	public void setMean(Double mean) {
		this.mean = mean;
	}

	@Column(name = "std", nullable = true)
	public Double getStd() {
		return std;
	}

	public void setStd(Double std) {
		this.std = std;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "libraryId", nullable = false)
	public DbGeneSetLibrary getDbGeneSetLibrary() {
		return dbGeneSetLibrary;
	}

	public void setDbGeneSetLibrary(DbGeneSetLibrary dbGeneSetLibrary) {
		this.dbGeneSetLibrary = dbGeneSetLibrary;
	}

	@OneToMany(mappedBy = "dbTerm")
	@LazyCollection(LazyCollectionOption.EXTRA)
	@Cascade({ CascadeType.ALL })
	@BatchSize(size=10)
	public Set<DbTermGene> getDbTermGenes() {
		return dbTermGenes;
	}

	public void setDbTermGenes(Set<DbTermGene> dbTermGenes) {
		this.dbTermGenes = dbTermGenes;
	}
	
	@Transient
	public int getGeneSetSize(){
		return getDbTermGenes().size();

	}
	
}
