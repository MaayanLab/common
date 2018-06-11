package edu.mssm.pharm.maayanlab.common.database.hibernateObjects;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "category", catalog = "flyenrichr")
public class DbLibraryCategory implements Serializable {

	private static final long serialVersionUID = -2742503383047868840L;

	private int categoryId;
	@Expose
	private String name;
	@Expose
	@SerializedName("libraries")
	private List<DbGeneSetLibrary> dbGeneSetLibraries;
	private int displayOrder;
	
	public DbLibraryCategory(){}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "categoryId", unique = true, nullable = false)
	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	@Column(name = "name", nullable = false, unique = true, length = 100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "dbLibraryCategory")
	@Cascade({ CascadeType.ALL })
	public List<DbGeneSetLibrary> getDbGeneSetLibraries() {
		return dbGeneSetLibraries;
	}

	public void setDbGeneSetLibraries(List<DbGeneSetLibrary> dbGeneSetLibraries) {
		this.dbGeneSetLibraries = dbGeneSetLibraries;
	}
	
	@Transient
	public void addLibrary(DbGeneSetLibrary library){
		dbGeneSetLibraries.add(library);
	}
	
	@Transient
	public void removeLibrary(DbGeneSetLibrary library){
		dbGeneSetLibraries.remove(library);
	}
	
	@Transient
	public void removeAllLibraries(Collection<DbGeneSetLibrary> libraries){
		dbGeneSetLibraries.removeAll(libraries);
	}

	@OrderColumn
	@Column(name = "displayOrder")
	public int getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}
	
}
