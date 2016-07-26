package br.com.mjsolutions.model;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "processamento")
@NamedQueries({@NamedQuery(name = "Processamento.findAll", query = "SELECT c FROM Processamento c")})
public class Processamento extends Entidade implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id", nullable = false, unique = true)
	private Long id;
	
	@Column(name = "status")
	private String status;
	
	@Column(name = "nome")
	private String nome;
	
	@Column(name = "total_linhas")
	private Long totalLinhas;
	
	@Column(name = "ultima_linha")
	private Long ultimaLinha;

	public Processamento(String status, String nome, Long totalLinhas,
			Long ultimaLinha) {
		super();
		this.status = status;
		this.nome = nome;
		this.totalLinhas = totalLinhas;
		this.ultimaLinha = ultimaLinha;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public Long getId() {
		return id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Long getTotalLinhas() {
		return totalLinhas;
	}

	public void setTotalLinhas(Long totalLinhas) {
		this.totalLinhas = totalLinhas;
	}

	public Long getUltimaLinha() {
		return ultimaLinha;
	}

	public void setUltimaLinha(Long ultimaLinha) {
		this.ultimaLinha = ultimaLinha;
	}
	
}
