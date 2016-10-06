package br.com.mjsolutions.controller;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import br.com.mjsolutions.model.Usuario;

public class UsuarioController {

	private List<SelectItem> combo;
	private String usuarioSelecionado;
	private String bancoSelecionado;

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
			combo.add(new SelectItem(4, "Daycoval"));
			combo.add(new SelectItem(5, "Safra"));
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
		Usuario u1 = new Usuario(0L, "SC01R021432", "joaonunes02", "Paraná Banco");
		listUsuarios.add(u1);
		Usuario u2 = new Usuario(1L, "SC01C0277506704", "joaonunes03", "Pan");
		listUsuarios.add(u2);
		Usuario u3 = new Usuario(2L, "SC01R02385", "joaonunes03", "BMG");
		listUsuarios.add(u3);
		Usuario u4 = new Usuario(3L, "SC01C0290506708", "joaonunes03", "Bom Sucesso");
		listUsuarios.add(u4);
		Usuario u5 = new Usuario(4L, "SC01C0305220", "joaonunes03", "Daycoval");
		listUsuarios.add(u5);
		Usuario u6 = new Usuario(5L, "SC01C0328506613", "joaonunes02", "Safra");
		listUsuarios.add(u6);
		
		return listUsuarios;
	}
	
	public String getBancoSelecionado() {
		return bancoSelecionado;
	}

	public void setBancoSelecionado(String bancoSelecionado) {
		this.bancoSelecionado = bancoSelecionado;
	}
	
	public String getUsuarioSelecionado() {
		return usuarioSelecionado;
	}

	public void setUsuarioSelecionado(String usuarioSelecionado) {
		if (usuarioSelecionado != null && !usuarioSelecionado.equals("")){
			Usuario usuarioBanco = findUsuarioById(usuarioSelecionado);
			setBancoSelecionado(usuarioBanco.getBanco());
		}
		this.usuarioSelecionado = usuarioSelecionado;
	}

}
