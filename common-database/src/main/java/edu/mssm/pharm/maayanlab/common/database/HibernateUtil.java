package edu.mssm.pharm.maayanlab.common.database;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

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
		getCurrentSession().getTransaction().rollback();
	}
	
	public static void saveOrUpdate(Object object) {
		getCurrentSession().saveOrUpdate(object);
	}
	
	public static void update(Object object){
		getCurrentSession().update(object);
	}
}
