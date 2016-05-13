package br.com.etecmam.cartolafc.negocios;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Time {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;	
	private String nome;
		
	@OneToOne(targetEntity = Jogador.class,  
			  cascade = CascadeType.ALL ,
			  fetch = FetchType.EAGER)
	
	private List<Jogador> jogadores;
	
	
	public Time() {	
	}
	
	

	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}


	public List<Jogador> getJogadores() {
		return jogadores;
	}


	public void setJogadores(List<Jogador> jogadores) {
		this.jogadores = jogadores;
	}
	
	
}
