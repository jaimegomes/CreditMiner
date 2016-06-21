package br.com.bjjsolutions.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "solicitacao")
@NamedQueries({
    @NamedQuery(name = "Solicitacao.findAll", query = "SELECT s FROM Solicitacao s")})
public class Solicitacao extends Entidade implements Serializable {
	
    private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)	
	@Column(name = "id", nullable = false, unique = true)
	private Long id;
	
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
	
	@JoinColumn(name="id_cliente", referencedColumnName = "id")
    @ManyToOne(optional = false)
	private Cliente cliente;
	
	public Solicitacao() {
		super();
	}

	public Solicitacao(Long id, String banco, String valorAutorizado, String parcelas, String pagas, String pesquisado, Cliente cliente) {
		super();
		this.id = id;
		this.banco = banco;
		this.valorAutorizado = valorAutorizado;
		this.parcelas = parcelas;
		this.pagas = pagas;
		this.pesquisado = pesquisado;
		this.cliente = cliente;
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
	
	public Cliente getCliente() {
		return cliente;
	}
	
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
}