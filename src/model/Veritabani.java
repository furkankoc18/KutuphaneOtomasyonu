package model;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Veritabani 		// Veritabanýný Baðlantýsýný Saðlayan Class
{		
	public static EntityManagerFactory entityManagerFactory=Persistence.createEntityManagerFactory("KutuphaneOtomasyonu2");
	public static EntityManager entityManager=entityManagerFactory.createEntityManager();
	public static EntityTransaction transaction=entityManager.getTransaction();
	
	
	

}
