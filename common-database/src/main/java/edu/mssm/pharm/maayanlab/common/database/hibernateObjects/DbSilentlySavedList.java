
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import edu.mssm.pharm.maayanlab.common.bio.InputGenes;
import edu.mssm.pharm.maayanlab.common.database.DAOs.GeneralDAO;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "silentlySavedLists", catalog = "enrichr")
public class DbSilentlySavedList implements Serializable {


	private static final long serialVersionUID = -1064570202349856526L;
	private int listid;
	private String description;
	private String inputMethod;
	private int ipAddress;
	private Date timestamp;
	private boolean isFuzzy;
	private Set<DbSilentlySavedListGenes> silentGenes;
	private Set<DbSilentlySavedListLibrary> dbSilentlySavedListLibrary = new HashSet<DbSilentlySavedListLibrary>();

	public DbSilentlySavedList() {
		this.dbSilentlySavedListLibrary = new HashSet<DbSilentlySavedListLibrary>();
	}

	public DbSilentlySavedList(int listid) {
		this.listid = listid;
		this.dbSilentlySavedListLibrary = new HashSet<DbSilentlySavedListLibrary>();
	}

	public DbSilentlySavedList(int listid, String description) {
		this.listid = listid;
		this.dbSilentlySavedListLibrary = new HashSet<DbSilentlySavedListLibrary>();
		if (!description.trim().isEmpty()) {
			this.description = description;
		}
	}

	public DbSilentlySavedList(int listid, String description, String inputMethod, int ipAddress) {
		this.listid = listid;
		this.dbSilentlySavedListLibrary = new HashSet<DbSilentlySavedListLibrary>();
		if (!description.trim().isEmpty()) {
			this.description = description;
		}
		this.inputMethod = inputMethod;
		this.ipAddress = ipAddress;
	}
	
	public DbSilentlySavedList(Collection<String> geneList, boolean validate) throws ParseException {
		this.setIsFuzzy(InputGenes.isFuzzy(geneList));
		if (validate) // Check if input list is valid
			if (isFuzzy)
				InputGenes.validateFuzzyInputGenes(geneList);
			else
				InputGenes.validateInputGenes(geneList);
		if (isFuzzy) {
			HashSet<DbSilentlySavedListGenes> listGenes = parseFuzzyGeneList(geneList);
			setSilentGenes(listGenes);
			normalizeFuzzyList();
		} else {
			HashSet<DbSilentlySavedListGenes> listGenes = new HashSet<DbSilentlySavedListGenes>();
			for (String geneName : geneList){
				DbGene gene = GeneralDAO.getGene(geneName);
				if(gene!=null)
					listGenes.add(new DbSilentlySavedListGenes(gene, this));
			}
			setSilentGenes(listGenes);
		}
	}

	private HashSet<DbSilentlySavedListGenes> parseFuzzyGeneList(Collection<String> geneList) {
		String[] split;
		HashSet<DbSilentlySavedListGenes> listGenes = new HashSet<DbSilentlySavedListGenes>();
		for (String geneName : geneList) {
			split = geneName.split(",");
			try {
				DbGene gene = GeneralDAO.getGene(split[0]);
				if(gene==null)
					gene = new DbGene(split[0].trim());
				listGenes.add(new DbSilentlySavedListGenes(gene, this, Double.parseDouble(split[1])));
			} catch (NumberFormatException nfe) {
				listGenes.add(new DbSilentlySavedListGenes(new DbGene(split[0].trim()), this));
			}
		}
		return listGenes;
	}

	// Normalize degree of membership so you can use Fisher Exact Test on it
	private void normalizeFuzzyList() {

		double total = 0.0;
		for (DbSilentlySavedListGenes listGene : getSilentGenes()) {
			total += listGene.getWeight();
		}

		double scale = getSilentGenes().size() / total;
		for (DbSilentlySavedListGenes listGene : getSilentGenes()) {
			listGene.setWeight(listGene.getWeight() * scale);
		}
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "listId", unique = true, nullable = false)
	public int getListid() {
		return listid;
	}

	public void setListid(int listid) {
		this.listid = listid;
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

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "silentlySavedList")
	@Cascade({ CascadeType.ALL })
	public Set<DbSilentlySavedListGenes> getSilentGenes() {
		return silentGenes;
	}

	public void setSilentGenes(Set<DbSilentlySavedListGenes> silentGenes) {
		this.silentGenes = silentGenes;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "silentlySavedList")
	@Cascade({ CascadeType.ALL })
	public Set<DbSilentlySavedListLibrary> getSilentlySavedListLibrary() {
		return dbSilentlySavedListLibrary;
	}

	public void setSilentlySavedListLibrary(Set<DbSilentlySavedListLibrary> silentLibraries) {
		this.dbSilentlySavedListLibrary = silentLibraries;
	}
	
	public void addListLibrary(DbSilentlySavedListLibrary listLibrary){
		dbSilentlySavedListLibrary.add(listLibrary);
	}
	
	@Transient
	public Collection<DbGene> getDbGenes(){
		ArrayList<DbGene> genes = new ArrayList<DbGene>();
		for(DbSilentlySavedListGenes listGenes:silentGenes)
			genes.add(listGenes.getGene());
		return genes;
	}

	@Override
	public boolean equals(Object obj) {
		return (obj instanceof DbSilentlySavedList)&&((DbSilentlySavedList)obj).listid==this.listid;
	}
	
	
}
