package edu.mssm.pharm.maayanlab.common.database;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

/**
 * This utility class acts as a layer between hibernate session operations and an application. Many of the methods here delegate to org.hibernate.Session.
 * See <a href="https://docs.jboss.org/hibernate/orm/3.5/javadocs/org/hibernate/Session.html">Session</a>
 * <p>
 * To configure the database you would like to use add a hibernate.properties file to your resources folder and define:
 * <ul>
 * <li>hibernate.connection.url = **The url of the database server, eg. jdbc:mysql://master/enrichr **</li>
 * <li>hibernate.connection.username = *The username of the user you want to use*</li>
 * <li>hibernate.connection.password = *The password for that user.*</li>
 * </ul>
 * To access database content you must always begin a transaction using {@link #beginTransaction() beginTransaction}. This will open a session if one was not already open and begin a transaction.
 * Access the data using a DAO (currently there is only the GeneralDAO). Then commit your transaction using {@link #commitTransaction() commitTransaction}.
 * This should be wrapped in a try-catch so that you can rollback the transaction if something goes wrong.
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
 * 
 * <p>
 * To persist anything in the database you must call {@link #saveOrUpdate(Object) saveOrUpdate} before committing. For batch jobs, if the jvm becomes huge and slows down it is because the session is
 * accumulating everything it sees in it's cache. To flush it first we flush the pending statements to the database using {@link #flush() flush}. We then evict the chosen objects from the cache using
 * {@link #evict(Object) evict}. If you do not call flush first you may lose the changes you made to the object you evicted.
 * <p>
 * @author Matthew Jones
 * @version 2.3
 */
public class HibernateUtil {
	private static final SessionFactory sessionFactory;
	
	private static String findEnv(String key) {
			String value;

			// Check OS Runtime Environment Variables
			value = System.getenv(key);
			if (!(value == null || value.equals("")))
					return value;

			// Check Java Runtime Properties
			value = System.getProperty(value);
			if (!(value == null || value.equals("")))
					return value;

			return null;
	}

	static {
		try {
			Configuration configuration = new Configuration().configure();

			// Override from properties or environment variable if present

			String url = HibernateUtil.findEnv("DB_URL");
			if (url != null)
				configuration.setProperty("hibernate.connection.url", url);

			String user = HibernateUtil.findEnv("DB_USER");
			if (user != null)
				configuration.setProperty("hibernate.connection.username", user);
			
			String pass = HibernateUtil.findEnv("DB_PASS");
			if (pass != null)
				configuration.setProperty("hibernate.connection.password", pass);

			ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
			sessionFactory = configuration.buildSessionFactory(serviceRegistry);
		} catch (Throwable ex) {
			System.err.println("Initial SessionFactory creation failed." + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	/**
	 * Frees all the resources. Do this when you destroy the context.
	 */
	public static void shutdown() {
		sessionFactory.close();
	}
	
	/**
	 * This gets the current session. If none exists it creates one.
	 * @return The current session.
	 */
	public static Session getCurrentSession(){
		return sessionFactory.getCurrentSession();
	}

	/**
	 * Close the current session. Do this whenever you have finished with the session. 
	 * Otherwise you will get errors saying you have multiple sessions associated with the same object.
	 */
	public static void close() {
		getCurrentSession().close();
	}
	
	/**
	 * Delegates to Session.merge
	 * @param object The object to merge.
	 */
	public static Object merge(Object object){
		return getCurrentSession().merge(object);
	}
	
	/**
	 * Begins a transaction. Also opens a session if none previously existed.
	 */
	public static void beginTransaction(){
		getCurrentSession().beginTransaction();
	}
	
	/**
	 * Commits the current transaction. May also close session but safest to also call {@link #close() close}.
	 */
	public static void commitTransaction(){
		getCurrentSession().getTransaction().commit();
	}
	
	/**
	 * Rolls back a transaction. Put this in your catch statement.
	 */
	public static void rollbackTransaction(){
		if(getCurrentSession().getTransaction().isActive())
			getCurrentSession().getTransaction().rollback();
	}
	
	/**
	 * Persist and object or changes to an object in the database.
	 * @param object The object to persist or update in the DB.
	 */
	public static void saveOrUpdate(Object object) {
		getCurrentSession().saveOrUpdate(object);
	}
	
	/**
	 * Persists changes to an object. Also reattaches an object to your session. 
	 * If you have an object from a different session that is now closed, call update on that object and it will be 
	 * reattached to the current session.
	 * @param object
	 */
	public static void update(Object object){
		getCurrentSession().update(object);
	}
	
	/**
	 * Gets an object by it's ID from the DB.
	 * @param theClass The class of the object
	 * @param id The Id of the object.
	 * @return The object with that ID. This assumes it exists, it will not return null if it does not - you will get an error instead when
	 * you try to use the object. Use get if you are unsure it exists.
	 */
	public static Object load(Class theClass, Serializable id){
		return getCurrentSession().load(theClass, id);
	}
	
	/**
	 * Gets an object by it's ID from the DB.
	 * @param theClass The class of the object
	 * @param id The Id of the object.
	 * @return The object with that ID or null if none exists.
	 */
	public static Object get(Class theClass, Serializable id) {
		return getCurrentSession().get(theClass, id);
	}
	
	/**
	 * Gets all the objects of a class
	 * @param theClass The class of the objects you want.
	 * @return A list of all the objects in the DB.
	 */
	public static List getAll(Class theClass){
		return getCurrentSession().createCriteria(theClass).list();
	}

	/**
	 * Flushes the current session. See Session.flush()
	 */
	public static void flush() {
		getCurrentSession().flush();		
	}

	/**
	 * Evicts everything. Good if you want to control the size of a session when doing batch jobs.
	 */
	public static void clear() {
		getCurrentSession().clear();
	}

	/**
	 * Evicts an object from the session.
	 * @param o The object to be evicted.
	 */
	public static void evict(Object o) {
		getCurrentSession().evict(o);
	}

	public static void refresh(Object o) {
		getCurrentSession().refresh(o);
		
	}

	
}
