package com.example.demo;

import java.time.LocalDate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.jupiter.api.Test;

import com.example.demo.entity.MyEntity;

public class HibernateEntityManagerTest {

    @Test
    void reproduceWithEntityManagerSeparateTransaction() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("com.example.myentity");
        EntityManager em = emf.createEntityManager();

        MyEntity entity = createInstanceForDate(LocalDate.parse("1890-09-30"));

        em.getTransaction().begin();
        em.persist(entity);
        em.getTransaction().commit();

        em.getTransaction().begin();
        MyEntity saved = em.find(MyEntity.class, entity.getId());
        em.getTransaction().commit();

        System.out.println("reproduceWithEntityManagerSeparateTransaction(): " + entity + " - " + saved);
    }

    private MyEntity createInstanceForDate(LocalDate date) {
        MyEntity entity = new MyEntity();
        entity.setLocalDate(date);

        return entity;
    }

}
