package br.com.mjsolutions.dao;

import java.util.List;

import br.com.mjsolutions.model.Entidade;

public class UsuarioDAO implements GenericoDAO {

	private static final long serialVersionUID = 1L;
	private BaseDAO baseDAO = new BaseDAO();

	public UsuarioDAO() {

	}

	@SuppressWarnings("unchecked")
	public List<Entidade> list() {
		List<Entidade> list = null;
		try {
			list = (List<Entidade>) baseDAO.getEm().createNamedQuery("Usuario.findAll");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;

	}

	@Override
	public void save(Entidade entidade) {

	}

	@Override
	public Entidade findById(Long id) {
		Entidade entidade = (Entidade) baseDAO.getEm().createQuery("Select u FROM Usuario u WHERE u.id = :id ").setParameter("id", id);
		return entidade;
	}

	@Override
	public void remove(Entidade entidade) {

	}

	@Override
	public void update(Entidade entidade) {

	}

}