package de.grepo.Model;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;



public class DBFactory {


	private static EntityManager em;

	public static void initEM() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("GrepoTool");
		em = emf.createEntityManager();
	}

	@SuppressWarnings("unchecked")
	public static void cleanDB() {
		if(DBFactory.em == null) {
			System.out.println("EntityManager not initialized! Aborted...");
			return;
		}else {
			em.getTransaction().begin();
			
			//Clean Alliances
			final List<Alliance> resultAlliance = em.createQuery("select a from Alliance a").getResultList();
			if(resultAlliance.size()  <= 0) {
				System.out.println("No Alliances found...");
			}else {
				System.out.println("Alliance found:\t"+resultAlliance.size());
				Query q = em.createQuery("delete from Alliance a where a IN ?1");
				q.setParameter(1, resultAlliance);
				q.executeUpdate();			
			}

			
			//CleanPlayers
			final List<Alliance> resultPlayer = em.createQuery("select p from Player p").getResultList();
			if(resultPlayer.size()  <= 0) {
				System.out.println("No Players found...");
			}else {
				System.out.println("Players found:\t"+resultPlayer.size());
				Query q = em.createQuery("delete from Player p where p IN ?1");
				q.setParameter(1, resultPlayer);
				q.executeUpdate();			
			}
			
			
			//CleanTowns
			final List<Alliance> resultTown = em.createQuery("select t from Town t").getResultList();
			if(resultTown.size()  <= 0) {
				System.out.println("No Towns found...");
			}else {
				System.out.println("Towns found:\t"+resultTown.size());
				Query q = em.createQuery("delete from Town t where t IN ?1");
				q.setParameter(1, resultTown);
				q.executeUpdate();			
			}
			em.getTransaction().commit();
		}
		
		

	}

	public static void close() {
		em.close();
		em.getEntityManagerFactory().close();
		
		
	}
	
	public static EntityManager getEm() {
		return em;
	}



}
