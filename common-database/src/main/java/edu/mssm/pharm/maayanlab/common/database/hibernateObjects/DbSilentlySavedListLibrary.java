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

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "silentlySavedListLibrary", catalog = "enrichr")
public class DbSilentlySavedListLibrary implements Serializable {

	private static final long serialVersionUID = -8305956910782832376L;
	private int listLibraryId;
	private DbSilentlySavedList dbSilentlySavedList;
	private DbGeneSetLibrary dbGeneSetLibrary;
	
	public DbSilentlySavedListLibrary() {
	}

	public DbSilentlySavedListLibrary(DbSilentlySavedList dbSilentlySavedList, DbGeneSetLibrary dbGeneSetLibrary) {
		this.dbSilentlySavedList = dbSilentlySavedList;
		this.dbGeneSetLibrary = dbGeneSetLibrary;
	}
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "listLibraryId", unique = true, nullable = false)
	public int getListLibraryGeneId() {
		return listLibraryId;
	}

	public void setListLibraryGeneId(int listGeneId) {
		this.listLibraryId = listGeneId;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "listId", nullable = false)
	public DbSilentlySavedList getSilentlySavedList() {
		return dbSilentlySavedList;
	}

	public void setSilentlySavedList(DbSilentlySavedList dbSilentlySavedList) {
		this.dbSilentlySavedList = dbSilentlySavedList;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "libraryId", nullable = false)
	public DbGeneSetLibrary getGeneSetLibrary() {
		return dbGeneSetLibrary;
	}

	public void setGeneSetLibrary(DbGeneSetLibrary dbGeneSetLibrary) {
		this.dbGeneSetLibrary = dbGeneSetLibrary;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof DbSilentlySavedListLibrary){
			DbSilentlySavedListLibrary other = (DbSilentlySavedListLibrary) obj;
			return other.getSilentlySavedList().equals(this.getSilentlySavedList()) && 
					other.getGeneSetLibrary().equals(this.getGeneSetLibrary());
		}
		return false;
	}

}
