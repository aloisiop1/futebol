package br.com.etecmam.cartolafc.negocios;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Transient;


@Entity
public class Jogador {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
		
	private String nome;
	private String posicao;
	
	private LocalDate dataNascimento;
	
	@Transient
	private int idade;
	
    @Lob
    private byte[] image;
 
		
	public Jogador() {
	}

	//CRIAR GETTERS AND SETTERS

	
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

	public LocalDate getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(LocalDate dataNascimento) {
		this.dataNascimento = dataNascimento;
	}


	public int getIdade() {
		
		idade = (int) ChronoUnit.YEARS.between(dataNascimento, LocalDate.now());		
		return idade;
	}



	public byte[] getImage() {
		return image;
	}



	public void setImage(byte[] image) {
		this.image = image;
	}



	public String getPosicao() {
		return posicao;
	}



	public void setPosicao(String posicao) {
		this.posicao = posicao;
	}
	
	
	
	
}
