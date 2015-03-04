
package edu.mssm.pharm.maayanlab.common.database.hibernateObjects;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import edu.mssm.pharm.maayanlab.common.bio.InputGenes;
import edu.mssm.pharm.maayanlab.common.database.DAOs.GeneralDAO;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "userLists", catalog = "enrichr")
public class DbUserList implements Serializable {


	private static final long serialVersionUID = -1064570202349856526L;
	private int userListid;
	private DbUser dbUser;
	private DbList dbList;
	private String description;
	private String inputMethod;
	private int ipAddress;
	private Date timestamp;
	private boolean isFuzzy;
	private boolean isSaved;
	private String shortId;
	private Set<DbListGenes> dbListGenes;
	private Set<DbListLibrary> dbListLibrary = new HashSet<DbListLibrary>();

	public DbUserList() {
		this.dbListLibrary = new HashSet<DbListLibrary>();
	}

	public DbUserList(int userListid) {
		this.userListid = userListid;
		this.dbListLibrary = new HashSet<DbListLibrary>();
	}

	public DbUserList(int userListid, String description) {
		this.userListid = userListid;
		this.dbListLibrary = new HashSet<DbListLibrary>();
		if (!description.trim().isEmpty()) {
			this.description = description;
		}
	}

	public DbUserList(int userListid, String description, String inputMethod, int ipAddress) {
		this.userListid = userListid;
		this.dbListLibrary = new HashSet<DbListLibrary>();
		if (!description.trim().isEmpty()) {
			this.description = description;
		}
		this.inputMethod = inputMethod;
		this.ipAddress = ipAddress;
	}
	
	public DbUserList(Collection<String> geneList, boolean validate) throws ParseException {
		this.setIsFuzzy(InputGenes.isFuzzy(geneList));
		if (validate) // Check if input list is valid
			if (isFuzzy)
				InputGenes.validateFuzzyInputGenes(geneList);
			else
				InputGenes.validateInputGenes(geneList);
		if (isFuzzy) {
			HashSet<DbListGenes> listGenes = parseFuzzyGeneList(geneList);
			DbList list = GeneralDAO.getDbList(listGenes);
			setDbList(list);
			list.normalizeFuzzyList();
			list.addUserList(this);
		} else {
			HashSet<DbListGenes> listGenes = new HashSet<DbListGenes>();
			for (String geneName : geneList){
				DbGene gene = GeneralDAO.getGene(geneName);
				if(gene==null)
					gene = new DbGene(geneName.trim());
				
				listGenes.add(new DbListGenes(gene));
			}
			DbList list = GeneralDAO.getDbList(listGenes);
			setDbList(list);
			list.addUserList(this);
		}
	}

	private HashSet<DbListGenes> parseFuzzyGeneList(Collection<String> geneList) {
		String[] split;
		HashSet<DbListGenes> listGenes = new HashSet<DbListGenes>();
		for (String geneName : geneList) {
			split = geneName.split(",");
			try {
				DbGene gene = GeneralDAO.getGene(split[0]);
				if(gene==null)
					gene = new DbGene(split[0].trim());
				listGenes.add(new DbListGenes(gene, Double.parseDouble(split[1])));
			} catch (NumberFormatException nfe) {
				listGenes.add(new DbListGenes(new DbGene(split[0].trim())));
			}
		}
		return listGenes;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "userListId", unique = true, nullable = false)
	public int getUserListid() {
		return userListid;
	}

	public void setUserListid(int userListid) {
		this.userListid = userListid;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "userId", nullable = false)
	public DbUser getDbUser() {
		return dbUser;
	}

	public void setDbUser(DbUser dbUser) {
		this.dbUser = dbUser;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "listId", nullable = false)
	@Cascade({ CascadeType.ALL })
	public DbList getDbList() {
		return dbList;
	}

	public void setDbList(DbList dbList) {
		this.dbList = dbList;
	}

	@Column(name = "description", length = 100)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "inputMethod", length = 20)
	public String getInputMethod() {
		return inputMethod;
	}

	public void setInputMethod(String inputMethod){
		this.inputMethod = inputMethod;
	}

	@Column(name = "ipAddress")
	public int getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(int ipAddress) {
		this.ipAddress = ipAddress;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "datetime", nullable = false)
	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
	@Column(name = "isFuzzy")
	public boolean getIsFuzzy() {
		return isFuzzy;
	}

	public void setIsFuzzy(boolean isFuzzy) {
		this.isFuzzy = isFuzzy;
	}

	@Column(name = "isSaved")
	public boolean getIsSaved() {
		return isSaved;
	}

	public void setIsSaved(boolean isSaved) {
		this.isSaved = isSaved;
	}
	
	@Column(name = "shortId")
	public String getShortId() {
		return shortId;
	}

	public void setShortId(String shortId) {
		this.shortId = shortId;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "dbUserList")
	@BatchSize(size = 10)
	@Cascade({ CascadeType.ALL })
	public Set<DbListGenes> getDbListGenes() {
		return dbListGenes;
	}

	public void setDbListGenes(Set<DbListGenes> listGenes) {
		this.dbListGenes = listGenes;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "dbUserList")
	@Cascade({ CascadeType.ALL })
	public Set<DbListLibrary> getDbListLibrary() {
		return dbListLibrary;
	}

	public void setDbListLibrary(Set<DbListLibrary> silentLibraries) {
		this.dbListLibrary = silentLibraries;
	}
	
	public void addListLibrary(DbListLibrary listLibrary){
		dbListLibrary.add(listLibrary);
	}
	
	@Transient
	public Collection<DbGene> getDbGenes(){
		ArrayList<DbGene> genes = new ArrayList<DbGene>();
		for(DbListGenes listGenes:dbList.getDbListGenes())
			genes.add(listGenes.getDbGene());
		return genes;
	}

	@Override
	public boolean equals(Object obj) {
		return (obj instanceof DbUserList)&&((DbUserList)obj).userListid==this.userListid;
	}
	
	
}
