package edu.mssm.pharm.maayanlab.common.database.hibernateObjects;

import java.io.Serializable;
import java.util.List;

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
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import edu.mssm.pharm.maayanlab.common.database.HibernateUtil;

@NamedQueries({
		@NamedQuery(name = "getActiveLibraries", query = "FROM DbGeneSetLibrary WHERE isActive = 1"),
		@NamedQuery(name = "genePoolSize", query = "SELECT count(distinct genes) " + "FROM DbTerm term " + "join term.dbTermGenes as termGenes " + "join termGenes.dbGene as genes "
				+ "where term.dbGeneSetLibrary.libraryName=:libraryName"), })
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "geneSetLibrary", catalog = "enrichr")
public class DbGeneSetLibrary implements Serializable {

	private static final long serialVersionUID = -5707352435128205417L;
	private int libraryId;
	@Expose
	@SerializedName("name")
	private String libraryName;
	@Expose
	private boolean isFuzzy;
	@Expose
	private boolean hasGrid;
	private boolean isRanked;
	@Expose
	private String format;
	private boolean isActive;
	private DbLibraryCategory dbLibraryCategory;
	private List<DbTerm> dbTerms;
	@Transient
	private int genePoolSize = -1;
	private int displayOrder;

	public DbGeneSetLibrary() {
	}

	public DbGeneSetLibrary(String libraryName) {
		this.libraryName = libraryName;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "libraryId")
	public int getLibraryId() {
		return libraryId;
	}

	public void setLibraryId(int id) {
		this.libraryId = id;
	}

	@Column(name = "libraryName", nullable = false, unique = true, length = 100)
	public String getLibraryName() {
		return libraryName;
	}

	public void setLibraryName(String libraryName) {
		this.libraryName = libraryName;
	}

	@Column(name = "isFuzzy")
	public boolean getIsFuzzy() {
		return isFuzzy;
	}

	public void setIsFuzzy(boolean isFuzzy) {
		this.isFuzzy = isFuzzy;
	}

	@Column(name = "hasGrid")
	public boolean getHasGrid() {
		return hasGrid;
	}

	public void setHasGrid(boolean hasGrid) {
		this.hasGrid = hasGrid;
	}

	@Column(name = "isRanked")
	public boolean getIsRanked() {
		return isRanked;
	}

	public void setIsRanked(boolean isRanked) {
		this.isRanked = isRanked;
	}

	@Column(name = "format", length = 300)
	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	@Column(name = "isActive")
	public boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	@OrderColumn
	@Column(name = "displayOrder")
	public int getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "categoryId", nullable = false)
	public DbLibraryCategory getDbLibraryCategory() {
		return dbLibraryCategory;
	}

	public void setDbLibraryCategory(DbLibraryCategory category) {
		this.dbLibraryCategory = category;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "dbGeneSetLibrary")
	@Cascade({ CascadeType.ALL })
	@BatchSize(size = 300)
	public List<DbTerm> getDbTerms() {
		return dbTerms;
	}

	public void setDbTerms(List<DbTerm> dbTerms) {
		this.dbTerms = dbTerms;
	}

	@Override
	public boolean equals(Object obj) {
		return (obj instanceof DbGeneSetLibrary) && ((DbGeneSetLibrary) obj).getLibraryName().equals(this.getLibraryName());
	}

	@Transient
	public int getGenePoolSize(Session hbSession) {
		if (genePoolSize == -1) {
			long poolSize = 0;
			poolSize = (Long) hbSession.getNamedQuery("genePoolSize").setString("libraryName", libraryName).uniqueResult();
			genePoolSize = (int) poolSize;
		}

		return genePoolSize;
	}
}
