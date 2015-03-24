package edu.mssm.pharm.maayanlab.common.database.hibernateObjects;

import java.io.Serializable;
import java.sql.Date;
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

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import edu.mssm.pharm.maayanlab.common.database.DAOs.GeneralDAO;

@NamedQueries({
		@NamedQuery(name = "getActiveLibraries", query = "FROM DbGeneSetLibrary WHERE isActive = 1"),
		@NamedQuery(name = "genePoolSize", query = "SELECT count(distinct genes) " + "FROM DbTerm term " + "join term.dbTermGenes as termGenes " + "join termGenes.dbGene as genes "
				+ "where term.dbGeneSetLibrary=:library")
})
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "geneSetLibrary", catalog = "enrichr")
public class DbGeneSetLibrary implements Serializable {

	private static final long serialVersionUID = -5707352435128205417L;
	private Integer libraryId;
	@Expose
	@SerializedName("name")
	private String libraryName;
	@Expose
	private Boolean isFuzzy;
	@Expose
	private Boolean hasGrid;
	private Boolean isRanked;
	@Expose
	private String format;
	private Boolean isActive;
	
	private String description;
	private String publication;
	private String source;
	private String author;
	private Date creationDate;
	
	private int numTerms = 0;
	private int geneCoverage = 0;
	private float genesPerTerm = 0;
	private String link = "";
	
	private DbLibraryCategory dbLibraryCategory;
	private String canvas;
	
	private List<DbTerm> dbTerms;
	@Transient
	private int genePoolSize = -1;
	private int displayOrder = 1000000;

	public DbGeneSetLibrary() {
	}

	public DbGeneSetLibrary(String libraryName) {
		this.libraryName = libraryName;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "libraryId", unique = true, nullable = false)
	public Integer getLibraryId() {
		return libraryId;
	}

	public void setLibraryId(Integer id) {
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

	/**
	 * @return the description
	 */
	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the publication
	 */
	@Column(name = "publication")
	public String getPublication() {
		return publication;
	}

	/**
	 * @param publication the publication to set
	 */
	public void setPublication(String publication) {
		this.publication = publication;
	}

	/**
	 * @return the source
	 */
	@Column(name = "source")
	public String getSource() {
		return source;
	}

	/**
	 * @param source the source to set
	 */
	public void setSource(String source) {
		this.source = source;
	}

	/**
	 * @return the author
	 */
	@Column(name = "author")
	public String getAuthor() {
		return author;
	}

	/**
	 * @param author the author to set
	 */
	public void setAuthor(String author) {
		this.author = author;
	}

	/**
	 * @return the creationDate
	 */
	@Column(name = "creationDate")
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate the creationDate to set
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	@OrderColumn
	@Column(name = "displayOrder")
	public int getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}
	
	@Column(name = "numTerms", nullable = false)
	public int getNumTerms() {
		return numTerms;
	}

	public void setNumTerms(int numTerms) {
		this.numTerms = numTerms;
	}

	@Column(name = "geneCoverage", nullable = false)
	public int getGeneCoverage() {
		return geneCoverage;
	}

	public void setGeneCoverage(int geneCoverage) {
		this.geneCoverage = geneCoverage;
	}

	@Column(name = "genesPerTerm", nullable = false)
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "categoryId", nullable = false)
	public DbLibraryCategory getDbLibraryCategory() {
		return dbLibraryCategory;
	}

	public void setDbLibraryCategory(DbLibraryCategory category) {
		this.dbLibraryCategory = category;
	}
	
	@Column(name = "canvas")
	public String getCanvas() {
		return canvas;
	}
	public void setCanvas(String canvas) {
		this.canvas = canvas;
	}

	@LazyCollection(LazyCollectionOption.EXTRA)
	@OneToMany(mappedBy = "dbGeneSetLibrary")
	@Cascade({ CascadeType.ALL })
	@BatchSize(size = 10)
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
	public int getGenePoolSize() {
		if (genePoolSize == -1) {
			genePoolSize = (int) GeneralDAO.getGenePoolSize(this);
		}

		return genePoolSize;
	}
}
