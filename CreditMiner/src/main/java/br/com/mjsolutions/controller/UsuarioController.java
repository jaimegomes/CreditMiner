package br.com.mjsolutions.controller;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import br.com.mjsolutions.model.Usuario;

public class UsuarioController {

	private List<SelectItem> combo;
	private String usuarioSelecionado;

	/**
	 * @return the usuarioSelecionado
	 */
	public String getUsuarioSelecionado() {
		return usuarioSelecionado;
	}

	/**
	 * @param usuarioSelecionado
	 *            the usuarioSelecionado to set
	 */
	public void setUsuarioSelecionado(String usuarioSelecionado) {
		this.usuarioSelecionado = usuarioSelecionado;
	}

	public UsuarioController() {
		super();
	}

	/**
	 * @return the listUsuarios
	 */
	public List<SelectItem> getCombo() {

		if (combo == null) {
			combo = new ArrayList<SelectItem>();
			combo.add(new SelectItem(0, "Paraná Banco"));
			combo.add(new SelectItem(1, "Pan"));
			combo.add(new SelectItem(2, "BMG"));
			combo.add(new SelectItem(3, "Bom Sucesso"));
		}
		return combo;
	}

	public Usuario findUsuarioById(Object value) {
		for (Usuario usu : getListUsuarios()) {
			if (usu.getId() == Long.parseLong(value.toString())) {
				return usu;
			}
		}
		return null;

	}


	public List<Usuario> getListUsuarios() {
		List<Usuario> listUsuarios = new ArrayList<Usuario>();
		Usuario u1 = new Usuario(0L, "SC01R021432", "joaonunes01", "Paraná Banco");
		listUsuarios.add(u1);
		Usuario u2 = new Usuario(1L, "SC01C02775067", "joaonunes55", "Pan");
		listUsuarios.add(u2);
		Usuario u3 = new Usuario(2L, "SC01R02385", "joaonunes02", "BMG");
		listUsuarios.add(u3);
		Usuario u4 = new Usuario(3L, "SC01C0290506708", "joaonunes01", "Bom Sucesso");
		listUsuarios.add(u4);

		return listUsuarios;

	}

}
