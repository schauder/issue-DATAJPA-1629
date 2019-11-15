package com.example.demo;

import com.example.demo.entity.MyEntity;
import com.example.demo.repository.MyEntityRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class Datajpa1629ApplicationTests {

	@Autowired
	MyEntityRepository repo;

	@Test
	void contextLoads() {

		LocalDate newestDateNotWorking = LocalDate.parse("1880-09-29");
		LocalDate oldestDateWorking = LocalDate.parse("1893-09-29");

		long difference = ChronoUnit.DAYS.between(newestDateNotWorking, oldestDateWorking);


		System.out.println(difference);

		while (difference > 1) {

			LocalDate currentDate = newestDateNotWorking.plusDays(difference / 2);

			LocalDate reloadedDate = saveAndReload(currentDate);

			System.out.print(currentDate + " - ");

			if (reloadedDate.equals(currentDate)) {
				oldestDateWorking = currentDate;
				System.out.println("ok");
			} else {
				newestDateNotWorking = currentDate;
				System.out.println("nope");
			}

			difference = ChronoUnit.DAYS.between(newestDateNotWorking, oldestDateWorking);
		}
	}

	private LocalDate saveAndReload(LocalDate currentDate) {
		MyEntity entity = createInstanceForDate(currentDate);
		repo.save(entity);
		MyEntity saved = repo.findById(entity.getId()).get();
		return saved.getLocalDate();
	}

	@Test
	void reproduceWithSpringData() {

		check("1893-04-01", 1L);
		check("1893-04-02", 0L);
	}

	private void check(String date, long expectedDifferenc) {
		LocalDate testDate = LocalDate.parse(date);
		assertThat(ChronoUnit.DAYS.between( saveAndReload(testDate), testDate)).isEqualTo(expectedDifferenc);
	}


	private MyEntity createInstanceForDate(LocalDate date) {
		MyEntity entity = new MyEntity();
		entity.setLocalDate(date);

		return entity;
	}

}
