package br.com.mjsolutions.controller;

import java.util.ArrayList;
import java.util.List;

import br.com.mjsolutions.model.Usuario;

public class UsuarioController {

	private List<Usuario> listUsuarios;

	public UsuarioController() {
		super();
	}

	/**
	 * @return the listUsuarios
	 */
	public List<Usuario> getListUsuarios() {
		this.listUsuarios = new ArrayList<Usuario>();

		Usuario usuario = new Usuario(0L, "SC01R021432", "joaonunes01", "Paraná Banco");
		listUsuarios.add(usuario);
		Usuario usuario2 = new Usuario(1L, "SC01C02775067", "joaonunes55", "Pan");
		listUsuarios.add(usuario2);
		Usuario usuario3 = new Usuario(2L, "SC01R02385", "joaonunes02", "BMG");
		listUsuarios.add(usuario3);
		Usuario usuario4 = new Usuario(3L, "SC01C0290506708", "joaonunes01", "Bom Sucesso");
		listUsuarios.add(usuario4);

		return listUsuarios;
	}

	public Usuario findUsuarioByBanco(String banco) {
		for (Usuario usu : listUsuarios) {
			if (usu.getBanco().equals(banco)) {
				return usu;
			}
		}
		return null;

	}

	/**
	 * @param listUsuarios
	 *            the listUsuarios to set
	 */
	public void setListUsuarios(List<Usuario> listUsuarios) {
		this.listUsuarios = listUsuarios;
	}

}
