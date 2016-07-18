package br.com.mjsolutions.dao;

import java.util.List;

import br.com.mjsolutions.model.Entidade;

public interface GenericDAO {
	
	public void save(Entidade entidade);
	public Entidade findById(Long id);
	public List<Entidade> list();
	public void remove(Entidade entidade);
	public void update(Entidade entidade);

}