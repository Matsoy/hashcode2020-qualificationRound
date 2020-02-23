package com.hashcode.hashcode.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Library implements Comparable<Library> {
	private Integer id = 0;
	private List<Book> books = new ArrayList<>();
	private Integer maxBooks = 0;
	private Integer signUpProcess = 0;
	private Integer totalScore = 0;

	/**
	 * Method to update <code>totalScore</code> attribute.
	 */
	public void updateTotalScore() {
		this.totalScore = this.books.stream()
				.mapToInt(Book::getScore)
				.sum();
	}

	/**
	 * Method to add a book to the list of books.
	 *
	 * @param book the book to add to the list of books.
	 */
	public void addBook(Book book) {
		this.books.add(book);
	}


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
