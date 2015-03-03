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
@Table(name = "listLibraries", catalog = "enrichr")
public class DbListLibrary implements Serializable {

	private static final long serialVersionUID = -8305956910782832376L;
	private int listLibraryId;
	private DbUserList dbUserList;
	private DbGeneSetLibrary dbGeneSetLibrary;
	
	public DbListLibrary() {
	}

	public DbListLibrary(DbUserList dbUserList, DbGeneSetLibrary dbGeneSetLibrary) {
		this.dbUserList = dbUserList;
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
	@JoinColumn(name = "userListId", nullable = false)
	public DbUserList getDbUserList() {
		return dbUserList;
	}

	public void setDbUserList(DbUserList dbUserList) {
		this.dbUserList = dbUserList;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "libraryId", nullable = false)
	public DbGeneSetLibrary getDbGeneSetLibrary() {
		return dbGeneSetLibrary;
	}

	public void setDbGeneSetLibrary(DbGeneSetLibrary dbGeneSetLibrary) {
		this.dbGeneSetLibrary = dbGeneSetLibrary;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof DbListLibrary){
			DbListLibrary other = (DbListLibrary) obj;
			return other.getDbUserList().equals(this.getDbUserList()) && 
					other.getDbGeneSetLibrary().equals(this.getDbGeneSetLibrary());
		}
		return false;
	}

}
