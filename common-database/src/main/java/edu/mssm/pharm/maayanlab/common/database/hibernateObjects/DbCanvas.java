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

@NamedQueries({
	@NamedQuery(name = "getCanvas", query = "FROM DbCanvas WHERE libraryName = :libraryName")
})

@Entity
@Immutable
@Table(name = "libraryCanvases", catalog = "enrichr")
@SecondaryTables({ @SecondaryTable(name = "geneSetLibrary", pkJoinColumns = { @PrimaryKeyJoinColumn(name = "libraryId", referencedColumnName = "libraryId") }) })
public class DbCanvas implements Serializable {

	private static final long serialVersionUID = 311384489532818945L;

	private int libraryId;
	private String libraryName;
	private String canvas;
	
	@Id
	@Column(name = "libraryId")
	public int getLibraryId() {
		return libraryId;
	}
	public void setLibraryId(int libraryId) {
		this.libraryId = libraryId;
	}
	
	@Column(name = "libraryName", table = "geneSetLibrary")
	public String getLibraryName() {
		return libraryName;
	}
	public void setLibraryName(String libraryName) {
		this.libraryName = libraryName;
	}
	
	@Column(name = "canvas")
	public String getCanvas() {
		return canvas;
	}
	public void setCanvas(String canvas) {
		this.canvas = canvas;
	}
	
}
