package com.example.demo;

import com.example.demo.entity.MyEntity;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDate;

class HibernateEntityManagerTest {

	@Test
	void reproduceWithEntityManagerSeparateTransaction() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("com.example.myentity");
		EntityManager em = emf.createEntityManager();

		LocalDate date = LocalDate.parse("1890-09-30");
		MyEntity entity = createInstanceForDate(date);

		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.persist(entity);
		tx.commit();

		em = emf.createEntityManager();
		tx = em.getTransaction();
		tx.begin();
		MyEntity saved = em.find(MyEntity.class, entity.getId());
		tx.commit();

		TestUtil.compare(date, saved.getLocalDate(), 1L);
	}

	private MyEntity createInstanceForDate(LocalDate date) {
		MyEntity entity = new MyEntity();
		entity.setLocalDate(date);

		return entity;
	}

}
