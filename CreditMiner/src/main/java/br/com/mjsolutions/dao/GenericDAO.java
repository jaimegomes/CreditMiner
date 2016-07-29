package br.com.mjsolutions.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;

import br.com.mjsolutions.model.Entidade;

public class GenericDAO<E extends Entidade> {
	
	private final static String UNIT_NAME = "CreditMiner";

	private EntityManager manager;
	
	public GenericDAO() {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory(UNIT_NAME);
		manager = factory.createEntityManager();
	}

	public void save(E entity) throws Exception {
		try {
			manager.getTransaction().begin();  
			if (entity.getId() == null) {
				manager.persist(entity);
			} else {
				manager.merge(entity);
			}
            manager.getTransaction().commit();  
            manager.refresh(entity);  
		} catch (Exception e) {
			manager.getTransaction().rollback();  
			throw new Exception("Falha ao salvar", e);
		}
	}

	protected void delete(E entity) throws Exception {
		try {
			Object remove;
			remove = manager.find(entity.getClass(), entity.getId());
			manager.remove(remove);
			manager.flush();
		} catch (Exception e) {
			throw new Exception("Falha ao Deletar", e);
		}
	}

	@SuppressWarnings("unchecked")
	protected E get(String sql) {
		return (E) manager.createQuery(sql).getSingleResult();
	}

	protected List<E> loadAll(Class<E> clazz) {
		CriteriaQuery<E> criteria = manager.getCriteriaBuilder().createQuery(clazz);

		return manager.createQuery(criteria.select(criteria.from(clazz)))
				.getResultList();
	}

	protected E find(Class<E> clazz, Object id) {
		return manager.find(clazz, id);
	}

	@SuppressWarnings("unchecked")
	protected List<E> load(Class<E> clazz, int firstItem, int pageSize) {
		String sql = "SELECT C FROM " + clazz.getName() + " C ";

		Query query = manager.createQuery(sql);
		query.setFirstResult(firstItem);
		query.setMaxResults(pageSize);

		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	protected List<E> load(String query) throws Exception {
		List<E> list = null;
		try {
			list = manager.createQuery(query).getResultList();
		} catch (Exception e) {
			throw new Exception("Falha ao listar metodo LOAD: ", e);
		}
		return list;
	}

}