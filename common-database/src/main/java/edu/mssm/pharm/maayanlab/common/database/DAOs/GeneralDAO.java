package edu.mssm.pharm.maayanlab.common.database.DAOs;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

import edu.mssm.pharm.maayanlab.common.bio.InputGenes;
import edu.mssm.pharm.maayanlab.common.database.Gene;
import edu.mssm.pharm.maayanlab.common.database.HibernateUtil;
import edu.mssm.pharm.maayanlab.common.database.hibernateObjects.DbGene;
import edu.mssm.pharm.maayanlab.common.database.hibernateObjects.DbGeneSetLibrary;
import edu.mssm.pharm.maayanlab.common.database.hibernateObjects.DbLibraryCategory;
import edu.mssm.pharm.maayanlab.common.database.hibernateObjects.DbList;
import edu.mssm.pharm.maayanlab.common.database.hibernateObjects.DbListGenes;
import edu.mssm.pharm.maayanlab.common.database.hibernateObjects.DbListLibrary;
import edu.mssm.pharm.maayanlab.common.database.hibernateObjects.DbOldList;
import edu.mssm.pharm.maayanlab.common.database.hibernateObjects.DbSharedList;
import edu.mssm.pharm.maayanlab.common.database.hibernateObjects.DbTerm;
import edu.mssm.pharm.maayanlab.common.database.hibernateObjects.DbUser;
import edu.mssm.pharm.maayanlab.common.database.hibernateObjects.DbUserList;


/**
 * All of these methods must be run inside a hibernate session and a transaction (see {@link HibernateUtil}). No method in this class creates, closes, begins or commits a session or transaction.
 * All objects returned by methods in this class are backed by the database.
 * Typical use case is as follows:
 * <pre><code>
 * try {
 *		HibernateUtil.beginTransaction();
 *		categories = GeneralDAO.getActiveCategories();
 *		HibernateUtil.commitTransaction();
 *	}catch(Exception e){
 *		HibernateUtil.rollbackTransaction();
 *		throw new ServletException("Failed to get active categories.", e);
 *	}finally {
 *		HibernateUtil.close();
 *	}
 * </code></pre>
 * @author Matthew Jones
 * @version 2.3
 * @see HibernateUtil
 */
public class GeneralDAO {

	
	/**
	 * Gets the Gene set library with name matching libraryName. If no name matches, return value is null.
	 * @param libraryName The name of the library you want
	 * @return A DbGeneSetLibrary object or null if no names matched.
	 */
	public static DbGeneSetLibrary getGeneSetLibrary(String libraryName) {
		DbGeneSetLibrary geneSetLibrary = (DbGeneSetLibrary) HibernateUtil.getCurrentSession().createCriteria(DbGeneSetLibrary.class).add(Restrictions.eq("libraryName", libraryName))
				.uniqueResult();
		return geneSetLibrary;
	}

	/**
	 * Finds the terms in a gene set library with overlapping genes and the genes that are overlapping.
	 * @param list The list to find matches against.
	 * @param geneSetLibrary The library to search for terms with overlaps.
	 * @return A map where terms with overlaps are the keys and each value is a list of the genes that are overlapping for that term.
	 */
	@SuppressWarnings("unchecked")
	public static Map<DbTerm, List<Gene>> getOverlappingTermsAndGenes(DbList list, DbGeneSetLibrary geneSetLibrary) {
		HashMap<DbTerm, List<Gene>> termOverlaps = new HashMap<DbTerm, List<Gene>>();

		if(list.getDbGenes().size() == 0)
			return termOverlaps;
		
		List<Object[]> queryResults = HibernateUtil.getCurrentSession().getNamedQuery("getOverlappingTerms").setParameterList("searchList", list.getDbGenes())
				.setParameter("library", geneSetLibrary).list();

		for (Object[] row : queryResults) {
			if (!termOverlaps.containsKey((DbTerm) row[0]))
				termOverlaps.put((DbTerm) row[0], new ArrayList<Gene>());
			termOverlaps.get((DbTerm) row[0]).add((Gene) row[1]);
		}

		return termOverlaps;
	}

	/**
	 * Gets a DbGene object backed by the database. If no gene exists, one is created.
	 * @param geneName The name of the gene.
	 * @return A DbGene object.
	 */
	public static DbGene getGene(String geneName) {
		geneName = geneName.trim();
		if(geneName.length()>100)
			geneName = geneName.substring(0, 100);
		DbGene gene = (DbGene) HibernateUtil.getCurrentSession().createCriteria(DbGene.class).add(Restrictions.eq("name", geneName)).uniqueResult();
		if(gene==null)
			gene = new DbGene(geneName);
		return gene;
	}

	/**
	 * Gets the currently active categories.
	 * @return A list of all active categories.
	 */
	@SuppressWarnings("unchecked")
	public static List<DbLibraryCategory> getActiveCategories() {
		List<DbLibraryCategory> categories = null;
		categories = HibernateUtil.getCurrentSession().createQuery("from DbLibraryCategory").list();

		for (DbLibraryCategory category : categories) {

			Collections.sort(category.getDbGeneSetLibraries(), new LibraryComparator());
			ArrayList<DbGeneSetLibrary> removeLibs = new ArrayList<DbGeneSetLibrary>();
			for (DbGeneSetLibrary library : category.getDbGeneSetLibraries()) {
				if (!library.getIsActive())
					removeLibs.add(library);
			}
			category.removeAllLibraries(removeLibs);
		}

		return categories;
	}

	private static class LibraryComparator implements Comparator<DbGeneSetLibrary> {
		@Override
		public int compare(DbGeneSetLibrary arg0, DbGeneSetLibrary arg1) {
			return arg0.getDisplayOrder() - arg1.getDisplayOrder();
		}

	}

	/**
	 * Get a user by their email.
	 * @param email
	 * @return The DbUser with the given email or null if no email matched.
	 */
	public static DbUser getUser(String email) {
		Criteria criteria = HibernateUtil.getCurrentSession().createCriteria(DbUser.class).add(Restrictions.eq("email", email).ignoreCase());
		DbUser dbUser = (DbUser) criteria.uniqueResult();
		return dbUser;
	}

	/**
	 * Legacy method. Gets the DbOldList with the given Id.
	 * @param listId
	 * @return The list or null if no list matched the Id.
	 */
	public static DbOldList getOldList(int listId) {
		return (DbOldList) HibernateUtil.getCurrentSession().get(DbOldList.class, listId);
	}

	/**
	 * Legacy method. Gets the DbSharedList with the given Id.
	 * @param sharedListId
	 * @return The list or null if no list matched the Id.
	 */
	public static DbSharedList getSharedList(int sharedListId) {
		return (DbSharedList) HibernateUtil.getCurrentSession().get(DbSharedList.class, sharedListId);
	}

	/**
	 * Gets the value of a counter.
	 * @param counterName The name of the counter.
	 * @return The value of the counter.
	 */
	public static int getCounter(String counterName) {
		Query query = HibernateUtil.getCurrentSession().createSQLQuery("SELECT count FROM wormenrichr.counters where name = :counter").setParameter("counter", counterName);
		List<?> data = query.list();

		Integer counterValue = (Integer) data.get(0);

		return counterValue.intValue();
	}

	/**
	 * Sorry for this dirty solution the maintainer from the future
	 * @return
	 */
	public static int getCounterLibs() {

		Query query = HibernateUtil.getCurrentSession().createSQLQuery("SELECT count(*) FROM genesetlibrary WHERE isActive=1;");
		List<?> data = query.list();

		BigInteger counterValue = (BigInteger) data.get(0);

		return counterValue.intValue();
	}	
	
	/**
	 * And for this one too
	 * @return
	 */
	public static int getCounterTerms() {

		Query query = HibernateUtil.getCurrentSession().createSQLQuery("SELECT count(*) FROM term JOIN genesetlibrary ON term.libraryId = genesetlibrary.libraryId WHERE isActive = 1;");
		List<?> data = query.list();
		BigInteger counterValue = (BigInteger) data.get(0);

		return counterValue.intValue();
	}		
	
	/**
	 * Increments a counter.
	 * @param counterName The name of the counter.
	 * @return The value after incrementing.
	 */
	public static int incrementCounter(String counterName) {
		Query query = HibernateUtil.getCurrentSession().createSQLQuery("CALL wormenrichr.IncrementCounter(:counter)").setParameter("counter", counterName);
		List<?> data = query.list();

		BigInteger counterValue = (BigInteger) data.get(0);

		return counterValue.intValue();
	}

	/**
	 * Gets all terms containing a gene and organises them by library.
	 * @param gene The name of the gene you want to look for.
	 * @return A map where the keys are library names and the values are lists containing the term names that contain the gene.
	 */
	public static Map<String, List<String>> getlibrariesAndTermsContaining(String gene) {
		Criteria criteria = HibernateUtil.getCurrentSession().createCriteria(DbTerm.class);
		criteria.createAlias("dbTermGenes", "termGenes");
		criteria.createAlias("termGenes.dbGene", "genes");
		criteria.add(Restrictions.eq("genes.name", gene));
		List<DbTerm> terms = criteria.list();

		Map<String, List<String>> libraries = new HashMap<String, List<String>>();
		for (DbTerm t : terms) {
			if (!libraries.containsKey(t.getDbGeneSetLibrary().getLibraryName()))
				libraries.put(t.getDbGeneSetLibrary().getLibraryName(), new ArrayList<String>());
			libraries.get(t.getDbGeneSetLibrary().getLibraryName()).add(t.getName());
		}

		return libraries;
	}

	/**
	 * Gets the term with the given name in the given library.
	 * @param libraryName
	 * @param termName
	 * @return
	 */
	public static DbTerm getTerm(String libraryName, String termName) {
		DbTerm term = (DbTerm) HibernateUtil.getCurrentSession().createCriteria(DbTerm.class).createAlias("dbGeneSetLibrary", "library").add(Restrictions.eq("library.libraryName", libraryName))
				.add(Restrictions.eq("name", termName)).uniqueResult();
		return term;
	}
	
	/**
	 * Gets the term with the given name in the given library.
	 * @param library
	 * @param termName
	 * @return
	 */
	public static DbTerm getTerm(DbGeneSetLibrary library, String termName) {
		DbTerm term = (DbTerm) HibernateUtil.getCurrentSession().createCriteria(DbTerm.class).add(Restrictions.eq("dbGeneSetLibrary", library))
				.add(Restrictions.eq("name", termName)).uniqueResult();
		return term;
	}

	/**
	 * Gets the DbLibraryCategory object with the given name. If no entry exists in the database, null is returned.
	 * @param categoryName
	 * @return The DbLibraryCategory or null if no category matched.
	 */
	public static DbLibraryCategory getCategory(String categoryName) {
		DbLibraryCategory category = (DbLibraryCategory) HibernateUtil.getCurrentSession().createCriteria(DbLibraryCategory.class).add(Restrictions.eq("name", categoryName)).uniqueResult();
		return category;
	}

	/**
	 * Gets the DbList containing the genes and gene weights in listGenes. If no list matches, one is created.
	 * @param listGenes
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static DbList getDbListFromListGenes(Collection<DbListGenes> listGenes) {
		int hash = DbList.createHash(listGenes);
		String stringified = DbList.stringify(listGenes);
		List<DbList> lists = (List<DbList>) HibernateUtil.getCurrentSession().createCriteria(DbList.class).add(Restrictions.eq("hash", hash)).list();

		
		for (DbList list : lists) {
			if (stringified.equals(list.toString())) {
				return list;
			}
		}

		DbList returnList = new DbList();
		returnList.setDbListGenes(new HashSet<DbListGenes>(listGenes));
		for(DbListGenes listGene:listGenes)
			listGene.setDbList(returnList);
		returnList.setHash(hash);
		return returnList;
	}
	
	/**
	 * Gets the DbList containing the genes genes. All weights are assumed to be 1. If no list matches, one is created.
	 * @param genes
	 * @return
	 */
	public static DbList getDbListFromGenes(Collection<DbGene> genes){
		HashSet<DbListGenes> listGenes = new HashSet<DbListGenes>();
		for (DbGene gene : genes){
			listGenes.add(new DbListGenes(gene));
		}
		return getDbListFromListGenes(listGenes);
	}
	
	/**
	 * Gets the DbList containing the genes in genes. Each string in genes can either be a gene name or a gene name followed by a comma followed by a weight.
	 * @param genes
	 * @return
	 */
	public static DbList getDbListFromStrings(Collection<String> genes, boolean normalize){
		DbList list;
		if (InputGenes.isFuzzy(genes)) {
			HashSet<DbListGenes> listGenes = parseFuzzyGeneList(genes, normalize);
			list = getDbListFromListGenes(listGenes);
		} else {
			HashSet<DbListGenes> listGenes = new HashSet<DbListGenes>();
			for (String geneName : genes){
				DbGene gene = getGene(geneName);
				listGenes.add(new DbListGenes(gene));
			}
			list = getDbListFromListGenes(listGenes);
		}
		return list;
	}
	
	private static HashSet<DbListGenes> parseFuzzyGeneList(Collection<String> geneList, boolean normalize) {
		String[] split;
		HashSet<DbListGenes> listGenes = new HashSet<DbListGenes>();
		for (String geneName : geneList) {
			split = geneName.split(",");
			DbGene gene = getGene(split[0]);
			try {
				listGenes.add(new DbListGenes(gene, Double.parseDouble(split[1])));
			} catch (NumberFormatException nfe) {
				listGenes.add(new DbListGenes(gene));
			}
		}
		if(normalize)
			normalizeListGenes(listGenes);
		return listGenes;
	}
	
	private static void normalizeListGenes(HashSet<DbListGenes> listGenes) {
		double total = 0.0;
		double max = 0.0;
		double scale = 1.0;
		for (DbListGenes listGene : listGenes) {
			total += Math.abs(listGene.getWeight());
			// Map gene weights to [0;1] range
			if(Math.abs(listGene.getWeight()) >= max){
					max = Math.abs(listGene.getWeight());
				}
			}
		
		scale = 1 / max;
		for (DbListGenes listGene : listGenes) {
			listGene.setWeight(listGene.getWeight() * scale);
		}
	}

	/**
	 * Adds an interaction between a userList and a gene set library. Note does not call HibernateUtil.saveOrUpdate().
	 * @param userList
	 * @param libraryName
	 * @throws IOException
	 */
	public static void addListLibrary(DbUserList userList, String libraryName) {
		if (userList != null) {
			DbGeneSetLibrary library = getGeneSetLibrary(libraryName);

			DbListLibrary listLibrary = new DbListLibrary(userList, library);

			userList.addListLibrary(listLibrary);

			HibernateUtil.merge(userList);
		}
	}
	
	/**
	 * Gets the user list with the given shortId. Null if none exists.
	 * @param shortId The shortId of the desired list.
	 * @return The DbUserList or null.
	 */
	public static DbUserList getDbUserList(String shortId){
		DbUserList userList = (DbUserList) HibernateUtil.getCurrentSession().createCriteria(DbUserList.class).add(Restrictions.eq("shortId", shortId)).uniqueResult();
		return userList;
	}
	
	
	/**
	 * Gets the gene pool size of the library
	 * @param library The library.
	 * @return The pool size.
	 */
	public static long getGenePoolSize(DbGeneSetLibrary library){
		return (Long) HibernateUtil.getCurrentSession().getNamedQuery("genePoolSize").setParameter("library", library).uniqueResult();
	}

	/**
	 * Gets all the active libraries.
	 * @return A list of active libraries.
	 */
	public static List<DbGeneSetLibrary> getActiveLibraries() {
		return HibernateUtil.getCurrentSession().createCriteria(DbGeneSetLibrary.class).add(Restrictions.eq("isActive", true)).list();
	}
}
