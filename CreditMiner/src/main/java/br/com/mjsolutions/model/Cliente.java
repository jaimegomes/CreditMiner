package br.com.mjsolutions.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "cliente")
@NamedQueries({
    @NamedQuery(name = "Cliente.findAll", query = "SELECT c FROM Cliente c")})
public class Cliente extends Entidade implements Serializable {
	
    private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
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
	
	@OneToMany(mappedBy = "cliente", targetEntity = Solicitacao.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<Solicitacao> solicitacaes;	

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

}
