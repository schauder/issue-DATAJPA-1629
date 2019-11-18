/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.demo;

import com.example.demo.entity.MyEntity;
import org.assertj.core.api.SoftAssertions;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * @author Jens Schauder
 */
class TestUtil {
	static MyEntity createInstanceForDate(LocalDate date) {
		MyEntity entity = new MyEntity();
		entity.setLocalDate(date);

		return entity;
	}

	static void compare(SoftAssertions softly, String date, LocalDate testDate, LocalDate reloaded, long expectedDifference) {
		softly.assertThat(ChronoUnit.DAYS.between(reloaded, testDate))
				.describedAs(date + " should show a difference of " + expectedDifference)
				.isEqualTo(expectedDifference);
	}

	static void compare(LocalDate date, LocalDate reloaded, long expectedDifference) {
		SoftAssertions.assertSoftly(softly -> compare(softly, "", date, reloaded, expectedDifference));
	}
}
