/**
 * Model for storing lists
 * 
 * @author		Edward Y. Chen
 * @since		02/08/2013 
 */

package edu.mssm.pharm.maayanlab.common.database.hibernateObjects;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicInsert
// Don't need to include all fields in insert, don't know why not on by default
@DynamicUpdate
// Don't need to update all fields in update, don't know why not on by default

@Table(name = "oldLists", catalog = "flyenrichr")

public class DbOldList implements Serializable {

	private static final long serialVersionUID = -1387864947273228907L;

	private int listid;
	private DbUser dbUser;
	private String description;
	private String passkey;
	private Date created;

	public DbOldList() {
	}

	public DbOldList(int listid) {
		this.listid = listid;
	}

	public DbOldList(int listid, DbUser dbUser, String description) {
		this(listid, dbUser, description, null);
	}

	public DbOldList(int listid, DbUser dbUser, String description, String passkey) {
		this.listid = listid;
		this.dbUser = dbUser;
		this.description = description;
		this.passkey = passkey;
	}

	@Id
	@Column(name = "listid", unique = true, nullable = false)
	public int getListid() {
		return this.listid;
	}

	public void setListid(int listid) {
		this.listid = listid;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ownerid")
	public DbUser getDbUser() {
		return this.dbUser;
	}

	public void setDbUser(DbUser dbUser) {
		this.dbUser = dbUser;
	}

	@Column(name = "description", length = 200)
	public String getDescription() {
		if(this.description!=null)
			return this.description;
		else
			return "";
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "passkey", length = 16)
	public String getPasskey() {
		return this.passkey;
	}

	public void setPasskey(String passkey) {
		this.passkey = passkey;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created", nullable = false, length = 19)
	public Date getCreated() {
		return this.created;
	}

	// Shouldn't be used because it uses default timestamp by db
	public void setCreated(Date created) {
		this.created = created;
	}
}
