package com.hashcode.hashcode.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@FieldDefaults(level = AccessLevel.PUBLIC)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Library implements Comparable<Library> {
	Integer id = 0;
	List<Book> books = new ArrayList<>();
	Integer maxBooks = 0;
	Integer signUpProcess = 0;
	Integer totalScore = 0;

	@Override
	public int compareTo(Library o) {
		// Sort libraries by ascending signUpProcess.
		int signUpProcessComparison = this.signUpProcess.compareTo(o.signUpProcess);
		if (signUpProcessComparison != 0) {
			return signUpProcessComparison;
		}

		// Sort libraries by descending totalScore.
		int totalScoreComparison = o.totalScore.compareTo(this.totalScore);
		if (totalScoreComparison != 0) {
			return totalScoreComparison;
		}

		// Sort libraries by descending number of books.
		Integer booksSize1 = this.books.size();
		Integer booksSize2 = o.books.size();
		return booksSize2.compareTo(booksSize1);
	}
}
