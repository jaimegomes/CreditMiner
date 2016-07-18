package br.com.mjsolutions.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class BaseDAO {
	
	private EntityManagerFactory factory;
	private EntityManager em;

	@SuppressWarnings("unused")
	public BaseDAO() {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("CreditMiner");
		EntityManager em = factory.createEntityManager();

	}

	/**
	 * @return the factory
	 */
	public EntityManagerFactory getFactory() {
		return factory;
	}

	/**
	 * @param factory
	 *            the factory to set
	 */
	public void setFactory(EntityManagerFactory factory) {
		this.factory = factory;
	}

	/**
	 * @return the em
	 */
	public EntityManager getEm() {
		return em;
	}

	/**
	 * @param em
	 *            the em to set
	 */
	public void setEm(EntityManager em) {
		this.em = em;
	}

}
