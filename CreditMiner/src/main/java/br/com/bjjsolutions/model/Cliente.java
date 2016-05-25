package br.com.bjjsolutions.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Cliente {

	@Id
	@Column(name = "id", nullable = false, unique = true)
	private Long id;

	@Column(name = "cpf")
	private String cpf;
	
	@Column(name = "colaborador")
	private String colaborador;
	
	@Column(name = "matricula")
	private String matricula;
	
	@Column(name = "secretaria")
	private String secretaria;
	
	@Column(name = "nascimento")
	private String nascimento;
	
	@Column(name = "margem")
	private String margem;
	
	@Column(name = "banco")
	private String banco;
	
	@Column(name = "valorAutorizado")
	private String valorAutorizado;
	
	@Column(name = "parcelas")
	private String parcelas;
	
	@Column(name = "pagas")
	private String pagas;
	
	@Column(name = "pesquisado")
	private String pesquisado;

	public Cliente() {
		super();
	}

	public Cliente(Long id, String cpf, String colaborador, String matricula,
			String secretaria, String nascimento, String margem, String banco,
			String valorAutorizado, String parcelas, String pagas,
			String pesquisado) {
		super();
		this.id = id;
		this.cpf = cpf;
		this.colaborador = colaborador;
		this.matricula = matricula;
		this.secretaria = secretaria;
		this.nascimento = nascimento;
		this.margem = margem;
		this.banco = banco;
		this.valorAutorizado = valorAutorizado;
		this.parcelas = parcelas;
		this.pagas = pagas;
		this.pesquisado = pesquisado;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the cpf
	 */
	public String getCpf() {
		return cpf;
	}

	/**
	 * @param cpf
	 *            the cpf to set
	 */
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	/**
	 * @return the colaborador
	 */
	public String getColaborador() {
		return colaborador;
	}

	/**
	 * @param colaborador
	 *            the colaborador to set
	 */
	public void setColaborador(String colaborador) {
		this.colaborador = colaborador;
	}

	/**
	 * @return the matricula
	 */
	public String getMatricula() {
		return matricula;
	}

	/**
	 * @param matricula
	 *            the matricula to set
	 */
	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	/**
	 * @return the secretaria
	 */
	public String getSecretaria() {
		return secretaria;
	}

	/**
	 * @param secretaria
	 *            the secretaria to set
	 */
	public void setSecretaria(String secretaria) {
		this.secretaria = secretaria;
	}

	/**
	 * @return the nascimento
	 */
	public String getNascimento() {
		return nascimento;
	}

	/**
	 * @param nascimento
	 *            the nascimento to set
	 */
	public void setNascimento(String nascimento) {
		this.nascimento = nascimento;
	}

	/**
	 * @return the margem
	 */
	public String getMargem() {
		return margem;
	}

	/**
	 * @param margem
	 *            the margem to set
	 */
	public void setMargem(String margem) {
		this.margem = margem;
	}

	/**
	 * @return the banco
	 */
	public String getBanco() {
		return banco;
	}

	/**
	 * @param banco
	 *            the banco to set
	 */
	public void setBanco(String banco) {
		this.banco = banco;
	}

	/**
	 * @return the valorAutorizado
	 */
	public String getValorAutorizado() {
		return valorAutorizado;
	}

	/**
	 * @param valorAutorizado
	 *            the valorAutorizado to set
	 */
	public void setValorAutorizado(String valorAutorizado) {
		this.valorAutorizado = valorAutorizado;
	}

	/**
	 * @return the parcelas
	 */
	public String getParcelas() {
		return parcelas;
	}

	/**
	 * @param parcelas
	 *            the parcelas to set
	 */
	public void setParcelas(String parcelas) {
		this.parcelas = parcelas;
	}

	/**
	 * @return the pagas
	 */
	public String getPagas() {
		return pagas;
	}

	/**
	 * @param pagas
	 *            the pagas to set
	 */
	public void setPagas(String pagas) {
		this.pagas = pagas;
	}

	/**
	 * @return the pesquisado
	 */
	public String getPesquisado() {
		return pesquisado;
	}

	/**
	 * @param pesquisado
	 *            the pesquisado to set
	 */
	public void setPesquisado(String pesquisado) {
		this.pesquisado = pesquisado;
	}

}
