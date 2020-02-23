package com.hashcode.hashcode.model;

import lombok.*;

import java.util.ArrayList;
import java.util.Comparator;
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
	private Integer booksPerDay = 0;
	private Integer signUpProcess = 0;
	private Integer score = 0;

	/**
	 * Method to update <code>score</code> attribute.
	 */
	public void updateScore() {
		this.score = this.books.stream()
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

	/**
	 * Method to sort le list of books.
	 *
	 * @param comparator   the comparator.
	 * @param reverseOrder <code>true</code> if to reverse the sort order;
	 *                     * <code>false</code> otherwise.
	 */
	public void sortBooks(Comparator<Book> comparator, boolean reverseOrder) {
		this.books.sort(
				reverseOrder ?
						comparator.reversed() :
						comparator
		);
	}


	@Override
	public int compareTo(Library o) {
		// Sort libraries by ascending signUpProcess.
		int signUpProcessComparison = this.signUpProcess.compareTo(o.signUpProcess);
		if (signUpProcessComparison != 0) {
			return signUpProcessComparison;
		}

		// Sort libraries by descending score.
		int scoreComparison = o.score.compareTo(this.score);
		if (scoreComparison != 0) {
			return scoreComparison;
		}

		// Sort libraries by descending number of books.
		Integer booksSize1 = this.books.size();
		Integer booksSize2 = o.books.size();
		return booksSize2.compareTo(booksSize1);
	}
}
