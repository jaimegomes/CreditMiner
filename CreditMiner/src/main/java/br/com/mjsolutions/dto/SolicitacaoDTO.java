package br.com.mjsolutions.dto;

public class SolicitacaoDTO {
	
	private String banco;
	private String valorAutorizado;
	private String parcelas;
	private String pagas;
	private String pesquisado;
	private ClienteDTO cliente;
	
	public SolicitacaoDTO() {
		super();
	}

	public SolicitacaoDTO(String banco, String valorAutorizado, String parcelas, String pagas, String pesquisado, ClienteDTO cliente) {
		super();
		this.banco = banco;
		this.valorAutorizado = valorAutorizado;
		this.parcelas = parcelas;
		this.pagas = pagas;
		this.pesquisado = pesquisado;
		this.cliente = cliente;
	}

	public String getBanco() {
		return banco;
	}

	public void setBanco(String banco) {
		this.banco = banco;
	}

	public String getValorAutorizado() {
		return valorAutorizado;
	}

	public void setValorAutorizado(String valorAutorizado) {
		this.valorAutorizado = valorAutorizado;
	}

	public String getParcelas() {
		return parcelas;
	}

	public void setParcelas(String parcelas) {
		this.parcelas = parcelas;
	}

	public String getPagas() {
		return pagas;
	}

	public void setPagas(String pagas) {
		this.pagas = pagas;
	}

	public String getPesquisado() {
		return pesquisado;
	}

	public void setPesquisado(String pesquisado) {
		this.pesquisado = pesquisado;
	}
	
	public ClienteDTO getCliente() {
		return cliente;
	}

	public void setCliente(ClienteDTO clienteDTO) {
		this.cliente = clienteDTO;
	}
}