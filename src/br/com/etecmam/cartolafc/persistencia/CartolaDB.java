package br.com.etecmam.cartolafc.persistencia;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import br.com.etecmam.cartolafc.negocios.Jogador;

public class CartolaDB{
	
	private static EntityManagerFactory emf  = Persistence.createEntityManagerFactory("CartolaUNIT");
	
	private static CartolaDB banco = null;

	
	private CartolaDB() {
		
	}
	
	public static CartolaDB getInstancia(){
		
		if(banco == null){
			banco = new CartolaDB();
		}
		
		return banco;		
	}
		

		
	
	public Jogador addJogador(Jogador jogador){
		
		try {
			
			EntityManager em = getEmf().createEntityManager();
	
			em.getTransaction().begin();
			em.persist(jogador);
			em.getTransaction().commit();
	
			
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		return jogador;
		
		
	}
	
	
		
	public boolean deleteJogador(int id){
		
		Jogador jogador = null;
		boolean resp = false;
		
		try {
			
			EntityManager em = getEmf().createEntityManager();	
			jogador = em.find(Jogador.class, id);
			
			if(jogador != null){
				em.getTransaction().begin();
				em.remove(jogador);
				em.getTransaction().commit();
				resp = true;
			}					
			
						
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		return resp;		
		
	}
	
	
	public boolean updateJogador(int id, Jogador dados){
		
		Jogador jogador = null;
		boolean resp = false;
		
		try {
			
			EntityManager em = getEmf().createEntityManager();	
			jogador = em.find(Jogador.class, id);
			
			if(jogador != null){
				
				em.getTransaction().begin();
				
				//atualizar nome
				jogador.setNome( dados.getNome() );				
				jogador.setPosicao( dados.getPosicao() );
				jogador.setDataNascimento( dados.getDataNascimento() );
				jogador.setImage( dados.getImage() );
												
				//salvar dados
				em.merge(jogador);
				em.getTransaction().commit();
				
				resp = true;
			}					
			
						
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		return resp;		
		
	}
	
	
	public Jogador getJogador(int id){
		
		Jogador jogador = null;
		
		try {
			
			EntityManager em = getEmf().createEntityManager();	
			jogador = em.find(Jogador.class, id);	
			
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		return jogador;		
		
	}
	
	
	@SuppressWarnings("unchecked")
	
	public List<Jogador> getJogadores(){
		
		List<Jogador> jogadores = null;
				
		try {
			
			EntityManager em = getEmf().createEntityManager();
			
			Query query = em.createQuery("SELECT J FROM Jogador J");
						
			jogadores =  query.getResultList();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
				
		return jogadores;		
		
	}


	public static EntityManagerFactory getEmf() {
		return emf;
	}



}
