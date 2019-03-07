/**
 * Model for storing users
 * 
 * @author		Matthew Jones
 * @since		12/13/2012 
 */

package edu.mssm.pharm.maayanlab.common.database.hibernateObjects;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import edu.mssm.pharm.maayanlab.common.math.HashFunctions;

@Entity
@DynamicInsert
@DynamicUpdate

@Table(name = "users", catalog = "flyenrichr", uniqueConstraints = @UniqueConstraint(columnNames = "email"))

public class DbUser implements Serializable {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userid == null) ? 0 : userid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DbUser other = (DbUser) obj;
		if (userid == null) {
			if (other.userid != null)
				return false;
		} else if (!userid.equals(other.userid))
			return false;
		return true;
	}

	private static final long serialVersionUID = 1893085998342363733L;
	
	private Integer userid;
	private String email;
	private String salt;
	private String password;
	private String first;
	private String last;
	private String institute;
	private Date accessed;
	private Set<DbOldList> dbOldLists;
	private Set<DbUserList> dbUserLists;
	
	public DbUser() {
	}

	public DbUser(String email, String password) {
		this(email, password, null, null, null, new HashSet<DbOldList>(0));
	}
	
	public DbUser(String email, String password, String first, String last, String institute) {
		this(email, password, first, last, institute, new HashSet<DbOldList>(0));
	}

	public DbUser(String email, String password, String first, String last, String institute, Set<DbOldList> dbOldLists) {
		updateUser(email, password, first, last, institute);
		this.dbOldLists = dbOldLists;
	}
	
	public boolean updateUser(String email, String password, String first, String last, String institute) {
		boolean changed = false;
		
		if (!email.equals(this.email) && !email.trim().isEmpty()) {	// Do not update user if field is same or empty
			this.email = email;
			changed |= true;
		}
		if (!password.trim().isEmpty()) {
			updatePassword(password);
			changed |= true;
		}
		if (!first.equals(this.first) && !first.trim().isEmpty()) {
			this.first = first;
			changed |= true;
		}
		if (!last.equals(this.last) && !last.trim().isEmpty()) {
			this.last = last;
			changed |= true;
		}
		if (!institute.equals(this.institute) && !institute.trim().isEmpty()) {
			this.institute = institute;
			changed |= true;
		}
		
		return changed;
	}
	
	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "userid", unique = true, nullable = false)
	public Integer getUserid() {
		return this.userid;
	}

	// Shouldn't be used because auto-incremented by db
	public void setUserid(Integer userid) {
		this.userid = userid;
	}	
	
	@Column(name = "email", unique = true, nullable = false, length = 100)
	public String getEmail() {		
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	@Column(name = "salt", nullable = false, length = 16)
	public String getSalt() {
		return this.salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}
	
	private String generateSalt() {
		Random r = new SecureRandom();
		byte[] saltBytes = new byte[8];
		r.nextBytes(saltBytes);
		return new BigInteger(1, saltBytes).toString(16);
	}

	@Column(name = "password", nullable = false, length = 32)
	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public boolean checkPassword(String password) {
		return this.password.equals(HashFunctions.md5(this.salt + password)); 
	}
	
	public void updatePassword(String password) {
		this.setSalt(generateSalt());
		this.setPassword(HashFunctions.md5(this.salt + password));
	}
	
	@Column(name = "first", length = 50)
	public String getFirst() {
		return this.first;
	}

	public void setFirst(String first) {		
		this.first = first;
	}

	@Column(name = "last", length = 200)
	public String getLast() {
		return this.last;
	}

	public void setLast(String last) {
		this.last = last;
	}

	@Column(name = "institute", length = 200)
	public String getInstitute() {
		return this.institute;
	}

	public void setInstitute(String institute) {
		this.institute = institute;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "accessed", nullable = false, length = 19)
	public Date getAccessed() {
		return this.accessed;
	}

	// Shouldn't be used because it uses default timestamp by db
	public void setAccessed(Date accessed) {
		this.accessed = accessed;
	}
	
	// Use this to update timestamp
	public void updateAccessed() {
		this.accessed = null;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "dbUser", orphanRemoval = true)	// Lists can be removed and propagated here
	@Cascade({CascadeType.ALL})
	public Set<DbOldList> getDbOldLists() {
		return this.dbOldLists;
	}

	public void setDbOldLists(Set<DbOldList> dbOldLists) {
		this.dbOldLists = dbOldLists;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "dbUser", orphanRemoval = true)	// Lists can be removed and propagated here
	@Cascade({CascadeType.ALL})
	public Set<DbUserList> getDbUserLists() {
		return dbUserLists;
	}
	
	@Transient
	public Set<DbUserList> getSavedDbUserLists(){
		Set<DbUserList> savedLists = new HashSet<DbUserList>();
		for(DbUserList userList:getDbUserLists()){
			if(userList.getIsSaved())
				savedLists.add(userList);
		}
		return savedLists;
	}

	public void setDbUserLists(Set<DbUserList> dbUserLists) {
		this.dbUserLists = dbUserLists;
	}

	@Override
	public String toString() {	// For testing purposes
		StringBuilder output = new StringBuilder();
		output.append("userid: ").append(userid).append(", ")
			  .append("email: ").append(email).append(", ")
			  .append("salt: ").append(salt).append(", ")
			  .append("password: ").append(password);
			  
		if (first != null)
			output.append(",").append("first: ").append(first);
		if (last != null)
			output.append(", ").append("last: ").append(last);
		if (institute != null)
			output.append(", ").append("institute: ").append(institute);
		
		output.append(", ").append("accessed: ").append(accessed);
		
		return output.toString();
	}
}
