/**
 * Model for storing genes from crowdsourced lists
 * 
 * @author		Edward Y. Chen
 * @since		04/17/2013 
 */

package edu.mssm.pharm.maayanlab.common.database.hibernateObjects;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "shared_genes", catalog = "enrichr")
public class DbSharedGene implements Serializable {

	private static final long serialVersionUID = -738468100827509312L;

	private Integer geneid;
	private DbSharedList dbSharedList;
	private String genename;

	public DbSharedGene() {
	}

	public DbSharedGene(DbSharedList dbSharedList, String genename) {
		this.dbSharedList = dbSharedList;
		this.genename = genename;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "geneid", unique = true, nullable = false)
	public Integer getGeneid() {
		return this.geneid;
	}

	public void setGeneid(Integer geneid) {
		this.geneid = geneid;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "listid", nullable = false)
	public DbSharedList getSharedList() {
		return this.dbSharedList;
	}

	public void setSharedList(DbSharedList dbSharedList) {
		this.dbSharedList = dbSharedList;
	}

	@Column(name = "genename", nullable = false, length = 20)
	public String getGenename() {
		return this.genename;
	}

	public void setGenename(String genename) {
		this.genename = genename;
	}
}
