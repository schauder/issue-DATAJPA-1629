package com.example.demo;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.demo.entity.MyEntity;
import com.example.demo.repository.MyEntityRepository;

@SpringBootApplication
public class Datajpa1629Application implements CommandLineRunner {

    @Autowired
    private MyEntityRepository myEntityRepository;

    public static void main(String[] args) {
        SpringApplication.run(Datajpa1629Application.class, args);
    }

    private void testWithJpaRepository() {
        MyEntity created = new MyEntity();

//		1893-04-02 - ok
//		1893-04-01 - nope
        created.setLocalDate(LocalDate.parse("1893-04-01"));
        created = myEntityRepository.save(created);

        Optional<MyEntity> retrieved = myEntityRepository.findById(created.getId());
        System.err.println(created + " - " + retrieved.get());
    }

	@Override
	public void run(String... args) throws Exception {

        testWithJpaRepository();


		LocalDate oldestDateWorking = LocalDate.parse("1893-09-29");
		LocalDate newestDateNotWorking = LocalDate.parse("1880-09-29");

	}
}
