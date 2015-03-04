package edu.mssm.pharm.maayanlab.common.database;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

/**
 * @author Matthew Jones
 * @version %I%, %G%
 */
public class HibernateUtil {
	private static final SessionFactory sessionFactory;
	
	static {
		try {
			Configuration configuration = new Configuration().configure();
			ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
			sessionFactory = configuration.buildSessionFactory(serviceRegistry);
		} catch (Throwable ex) {
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	public static void shutdown() {
		sessionFactory.close();
	}
	
	public static Session getCurrentSession(){
		return sessionFactory.getCurrentSession();
	}

	public static void close() {
		getCurrentSession().close();
	}
	
	public static void merge(Object object){
		getCurrentSession().merge(object);
	}
	
	public static void beginTransaction(){
		getCurrentSession().beginTransaction();
	}
	
	public static void commitTransaction(){
		getCurrentSession().getTransaction().commit();
	}
	
	public static void rollbackTransaction(){
		if(getCurrentSession().getTransaction().isActive())
			getCurrentSession().getTransaction().rollback();
	}
	
	public static void saveOrUpdate(Object object) {
		getCurrentSession().saveOrUpdate(object);
	}
	
	public static void update(Object object){
		getCurrentSession().update(object);
	}
	
	public static Object load(Class theClass, Serializable id){
		return getCurrentSession().load(theClass, id);
	}
	
	public static List getAll(Class theClass){
		return getCurrentSession().createCriteria(theClass).list();
	}

	public static void flush() {
		getCurrentSession().flush();		
	}

	public static void clear() {
		getCurrentSession().clear();
	}

	public static void evict(Object o) {
		getCurrentSession().evict(o);
	}
}
