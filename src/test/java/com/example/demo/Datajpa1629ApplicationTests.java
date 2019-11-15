package com.example.demo;

import com.example.demo.entity.MyEntity;
import com.example.demo.repository.MyEntityRepository;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class Datajpa1629ApplicationTests {

	@Autowired
	MyEntityRepository repo;

//	@Autowired
//	EntityManager em;

	@Test
	void contextLoads() {

		LocalDate newestDateNotWorking = LocalDate.parse("1800-09-29");
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

		SoftAssertions.assertSoftly(softly -> {
			check(softly, this::saveAndReload, "1893-04-01", 1L);
			check(softly, this::saveAndReload, "1893-04-02", 0L);
		});
	}

//	@Test
//	@Disabled
//	void reproduceWithEM() {
//
//		checkEm("1893-04-01", 1L);
//		checkEm("1893-04-02", 0L);
//	}

	private void check(SoftAssertions softly, Function<LocalDate, LocalDate> saveAndReload, String date, long expectedDifference) {

		LocalDate testDate = LocalDate.parse(date);

		softly.assertThat(ChronoUnit.DAYS.between(saveAndReload.apply(testDate), testDate))
				.describedAs(testDate + " should show a difference of " + expectedDifference)
				.isEqualTo(expectedDifference);
	}

//	private void checkEm(String date, long expectedDifference) {
//		LocalDate testDate = LocalDate.parse(date);
//		assertThat(ChronoUnit.DAYS.between(saveAndReloadEm(testDate), testDate)).isEqualTo(expectedDifference);
//	}

//	private LocalDate saveAndReloadEm(LocalDate currentDate) {
//		MyEntity entity = createInstanceForDate(currentDate);
//		em.persist(entity);
//		MyEntity saved = em.find(MyEntity.class, entity.getId());
//		return saved.getLocalDate();
//	}


	private MyEntity createInstanceForDate(LocalDate date) {
		MyEntity entity = new MyEntity();
		entity.setLocalDate(date);

		return entity;
	}

}
