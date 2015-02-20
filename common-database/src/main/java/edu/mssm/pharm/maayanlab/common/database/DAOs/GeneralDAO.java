package edu.mssm.pharm.maayanlab.common.database.DAOs;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

import edu.mssm.pharm.maayanlab.common.database.Gene;
import edu.mssm.pharm.maayanlab.common.database.HibernateUtil;
import edu.mssm.pharm.maayanlab.common.database.hibernateObjects.DbCanvas;
import edu.mssm.pharm.maayanlab.common.database.hibernateObjects.DbGene;
import edu.mssm.pharm.maayanlab.common.database.hibernateObjects.DbGeneSetLibrary;
import edu.mssm.pharm.maayanlab.common.database.hibernateObjects.DbLibraryCategory;
import edu.mssm.pharm.maayanlab.common.database.hibernateObjects.DbLibraryStatistics;
import edu.mssm.pharm.maayanlab.common.database.hibernateObjects.DbList;
import edu.mssm.pharm.maayanlab.common.database.hibernateObjects.DbSharedList;
import edu.mssm.pharm.maayanlab.common.database.hibernateObjects.DbSilentlySavedList;
import edu.mssm.pharm.maayanlab.common.database.hibernateObjects.DbTerm;
import edu.mssm.pharm.maayanlab.common.database.hibernateObjects.DbUser;

public class GeneralDAO {

	public static DbGeneSetLibrary getGeneSetLibrary(String backgroundType) {
		DbGeneSetLibrary geneSetLibrary = (DbGeneSetLibrary) HibernateUtil.getCurrentSession().createCriteria(DbGeneSetLibrary.class).add(Restrictions.eq("libraryName", backgroundType))
				.uniqueResult();
		return geneSetLibrary;
	}

	@SuppressWarnings("unchecked")
	public static HashMap<DbTerm, ArrayList<Gene>> getOverlappingTermsAndGenes(DbSilentlySavedList userList, DbGeneSetLibrary geneSetLibrary) {
		HashMap<DbTerm, ArrayList<Gene>> termOverlaps = new HashMap<DbTerm, ArrayList<Gene>>();

		List<Object[]> queryResults = HibernateUtil.getCurrentSession().getNamedQuery("getOverlappingTerms").setParameterList("searchList", userList.getDbGenes())
				.setParameter("library", geneSetLibrary).list();

		for (Object[] row : queryResults) {
			if (!termOverlaps.containsKey((DbTerm) row[0]))
				termOverlaps.put((DbTerm) row[0], new ArrayList<Gene>());
			termOverlaps.get((DbTerm) row[0]).add((Gene) row[1]);
		}

		return termOverlaps;
	}

	public static DbGene getGene(String geneName) {
		return (DbGene) HibernateUtil.getCurrentSession().createCriteria(DbGene.class).add(Restrictions.eq("name", geneName.trim())).uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public static List<DbLibraryCategory> getActiveCategories() {
		List<DbLibraryCategory> categories = null;
		try {
			HibernateUtil.beginTransaction();
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
			
			HibernateUtil.commitTransaction();

		} catch (Exception e) {
			HibernateUtil.rollbackTransaction();
			e.printStackTrace();
		} finally {
			HibernateUtil.close();
		}
		return categories;
	}

	private static class LibraryComparator implements Comparator<DbGeneSetLibrary> {
		@Override
		public int compare(DbGeneSetLibrary arg0, DbGeneSetLibrary arg1) {
			return arg0.getDisplayOrder() - arg1.getDisplayOrder();
		}

	}

	public static DbUser getUser(String email) {
		Criteria criteria = HibernateUtil.getCurrentSession().createCriteria(DbUser.class).add(Restrictions.eq("email", email).ignoreCase());
		DbUser dbUser = (DbUser) criteria.uniqueResult();
		return dbUser;
	}

	public static DbList getList(int listId) {
		return (DbList) HibernateUtil.getCurrentSession().get(DbList.class, listId);
	}

	public static DbSharedList getSharedList(int sharedListId) {
		return (DbSharedList) HibernateUtil.getCurrentSession().get(DbSharedList.class, sharedListId);
	}

	public static DbCanvas getCanvas(String libraryName) {
		return (DbCanvas) HibernateUtil.getCurrentSession().getNamedQuery("getCanvas").setParameter("libraryName", libraryName).uniqueResult();
	}

	public static int getCounter(String counterName) {
		
		HibernateUtil.beginTransaction();
		Query query = HibernateUtil.getCurrentSession().createSQLQuery("SELECT count FROM enrichr.counters where name = :counter").setParameter("counter", counterName);
		List<?> data = query.list();

		Integer counterValue = (Integer) data.get(0);
		HibernateUtil.commitTransaction();
		HibernateUtil.close();

		return counterValue.intValue();
	}
	
	public static int incrementCounter(String counterName) {
		
		Query query = HibernateUtil.getCurrentSession().createSQLQuery("CALL enrichr.IncrementCounter(:counter)").setParameter("counter", counterName);
		List<?> data  = query.list();
		
		BigInteger counterValue = (BigInteger) data.get(0);
		
		return counterValue.intValue();
	}
	
	public static List<DbLibraryStatistics> getLibraryStatistics() {
		List<DbLibraryStatistics> stats = HibernateUtil.getCurrentSession().getNamedQuery("getDatasetStatistics").list();
		return stats;
	}
	
	public static HashMap<String, ArrayList<String>> getlibrariesAndTermsContaining(String gene){
		Criteria criteria = HibernateUtil.getCurrentSession().createCriteria(DbTerm.class);
		criteria.createAlias("dbTermGenes", "termGenes");
		criteria.createAlias("termGenes.dbGene", "genes");
		criteria.add(Restrictions.eq("genes.name", gene));
		List<DbTerm> terms = criteria.list();

		HashMap<String, ArrayList<String>> libraries = new HashMap<String, ArrayList<String>>();
		for (DbTerm t : terms) {
			if (!libraries.containsKey(t.getDbGeneSetLibrary().getLibraryName()))
				libraries.put(t.getDbGeneSetLibrary().getLibraryName(), new ArrayList<String>());
			libraries.get(t.getDbGeneSetLibrary().getLibraryName()).add(t.getName());
		}
		
		return libraries;
	}
	
	public static DbTerm getTerm(String libraryName, String termName) {
		DbTerm term = (DbTerm) HibernateUtil.getCurrentSession().createCriteria(DbTerm.class).createAlias("dbGeneSetLibrary", "library").add(Restrictions.eq("library.libraryName", libraryName))
				.add(Restrictions.eq("name", termName)).uniqueResult();
		return term;
	}
	
	public static DbLibraryCategory getCategory(String categoryName) {
		DbLibraryCategory category = (DbLibraryCategory) HibernateUtil.getCurrentSession().createCriteria(DbLibraryCategory.class).add(Restrictions.eq("name", categoryName)).uniqueResult();
		return category;
	}
}
