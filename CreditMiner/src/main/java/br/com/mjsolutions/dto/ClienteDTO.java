package br.com.mjsolutions.dto;

import java.util.List;

public class ClienteDTO {
	
	private String cpf;
	private String colaborador;
	private String matricula;
	private String secretaria;
	private String nascimento;
	private String margem;
	private String ultimaFolhaMovimentada;
	private String cargoFuncao;
	private List<SolicitacaoDTO> solicitacaes;	

	public ClienteDTO() {
		super();
	}

	public ClienteDTO(String cpf, String colaborador, String matricula,
			String secretaria, String nascimento, String margem, String ultimaFolhaMovimentada,String cargoFuncao) {
		super();
		this.cpf = cpf;
		this.colaborador = colaborador;
		this.matricula = matricula;
		this.secretaria = secretaria;
		this.nascimento = nascimento;
		this.margem = margem;
		this.ultimaFolhaMovimentada = ultimaFolhaMovimentada;
		this.cargoFuncao = cargoFuncao;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getColaborador() {
		return colaborador;
	}

	public void setColaborador(String colaborador) {
		this.colaborador = colaborador;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getSecretaria() {
		return secretaria;
	}

	public void setSecretaria(String secretaria) {
		this.secretaria = secretaria;
	}

	public String getNascimento() {
		return nascimento;
	}

	public void setNascimento(String nascimento) {
		this.nascimento = nascimento;
	}

	public String getMargem() {
		return margem;
	}

	public void setMargem(String margem) {
		this.margem = margem;
	}
	
	public String getUltimaFolhaMovimentada() {
		return ultimaFolhaMovimentada;
	}
	
	public void setUltimaFolhaMovimentada(String ultimaFolhaMovimentada) {
		this.ultimaFolhaMovimentada = ultimaFolhaMovimentada;
	}
	
	public List<SolicitacaoDTO> getSolicitacaes() {
		return solicitacaes;
	}
	
	public String getCargoFuncao() {
		return cargoFuncao;
	}

	public void setCargoFuncao(String cargoFuncao) {
		this.cargoFuncao = cargoFuncao;
	}

	public void setSolicitacaes(List<SolicitacaoDTO> solicitacaes) {
		this.solicitacaes = solicitacaes;
	}

}

