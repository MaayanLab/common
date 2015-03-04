package edu.mssm.pharm.maayanlab.common.database.hibernateObjects;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "lists", catalog = "enrichr")
public class DbList implements Serializable {

	private static final long serialVersionUID = -5336418115229427957L;

	private long listId;
	private int hash;

	private Set<DbUserList> dbUserLists;
	private Set<DbListGenes> dbListGenes;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "listId", unique = true, nullable = false)
	public long getListId() {
		return listId;
	}

	public void setListId(long listId) {
		this.listId = listId;
	}

	@Column(name = "hash")
	public int getHash() {
		return hash;
	}

	public void setHash(int hash) {
		this.hash = hash;
	}

	@OneToMany(mappedBy = "dbList")
	@BatchSize(size = 10)
	@Cascade({ CascadeType.ALL })
	public Set<DbUserList> getDbUserLists() {
		return dbUserLists;
	}

	public void setDbUserLists(Set<DbUserList> dbUserLists) {
		this.dbUserLists = dbUserLists;
	}

	@OneToMany(mappedBy = "dbList")
	@BatchSize(size = 10)
	@Cascade({ CascadeType.ALL })
	public Set<DbListGenes> getDbListGenes() {
		return dbListGenes;
	}

	public void setDbListGenes(Set<DbListGenes> listGenes) {
		this.dbListGenes = listGenes;
	}

	@Override
	public String toString() {
		return stringify(this.getDbListGenes());
	}

	public void addUserList(DbUserList userList) {
		if (dbUserLists == null)
			dbUserLists = new HashSet<DbUserList>();
		dbUserLists.add(userList);
	}

	public void createAndSetHash() {
		setHash(createHash());
	}

	public int createHash() {
		return toString().hashCode();
	}

	public static int createHash(Collection<DbListGenes> tempListGenes) {
		return stringify(tempListGenes).hashCode();
	}

	public static String stringify(Collection<DbListGenes> tempListGenes) {
		ArrayList<String> genes = new ArrayList<String>();
		for (DbListGenes listGene : tempListGenes)
			genes.add(listGene.toString());
		Collections.sort(genes);
		return String.join("\t", genes);
	}

	// Normalize degree of membership so you can use Fisher Exact Test on it
	public void normalizeFuzzyList() {

		double total = 0.0;
		for (DbListGenes listGene : getDbListGenes()) {
			total += listGene.getWeight();
		}

		double scale = getDbListGenes().size() / total;
		for (DbListGenes listGene : getDbListGenes()) {
			listGene.setWeight(listGene.getWeight() * scale);
		}
	}

}
