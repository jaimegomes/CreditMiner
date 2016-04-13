package br.com.bjjsolutions.model;


public class Pessoa {
	
	private String nome;
	private String email;
	private String dataNascimento;
	private double margem;
	
	
	public Pessoa() {
		super();
	}


	public Pessoa(String nome, String email, String dataNascimento, double margem) {
		super();
		this.nome = nome;
		this.email = email;
		this.dataNascimento = dataNascimento;
		this.margem = margem;
	}


	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getDataNascimento() {
		return dataNascimento;
	}


	public void setDataNascimento(String dataNascimento) {
		this.dataNascimento = dataNascimento;
	}


	public double getMargem() {
		return margem;
	}


	public void setMargem(double margem) {
		this.margem = margem;
	}
	
	
	
	

}
