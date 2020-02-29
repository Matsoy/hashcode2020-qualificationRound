package com.hashcode.hashcode.model;

import lombok.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Library implements Comparable<Library> {
	@EqualsAndHashCode.Include
	private Integer id = 0;
	private List<Book> books = new ArrayList<>();
	private List<Book> booksWithoutDuplications = new ArrayList<>();
	private boolean useBooksWithoutDuplications = false;
	private Integer booksPerDay = 0;
	private Integer signUpProcess = 0;
	private Integer score = 0;
	private Integer offset = 0;

	/**
	 * Constructor.
	 *
	 * @param id    the id of the library.
	 * @param books the books.
	 */
	public Library(Integer id, List<Book> books) {
		this.id = id;
		this.books = books;
	}

	/**
	 * Increment <code>offset</code>.
	 *
	 * @param add the value to add to offset.
	 * @return the new offset value.
	 */
	public Integer addOffset(Integer add) {
		this.offset += add;
		return this.offset;
	}

	/**
	 * Decrement <code>offset</code>.
	 *
	 * @return <code>true</code> if the offset has been decremented;
	 * <code>false</code> otherwise, because offset isn't > 0.
	 */
	public boolean decrementOffset() {
		if (this.offset > 0) {
			this.offset--;
			return true;
		}
		return false;
	}

	/**
	 * Method to update <code>score</code> attribute.
	 */
	public void updateScore() {
		Stream<Book> bookStream = this.useBooksWithoutDuplications ?
				this.booksWithoutDuplications.stream() :
				this.books.stream();

		this.score = bookStream
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
	 *                     <code>false</code> otherwise.
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

		// Sort libraries by descending score per day x the total number of books.
		Double scorePerDay1 = Double.valueOf(this.score) / (
				(double) this.books.size() / Double.valueOf(this.getBooksPerDay())
		) * (double) this.books.size();
		Double scorePerDay2 = Double.valueOf(o.getScore()) / (
				(double) o.getBooks().size() / Double.valueOf(o.getBooksPerDay())
		) * (double) o.getBooks().size();
		int scorePerDayComparison = scorePerDay2.compareTo(scorePerDay1);
		if (scorePerDayComparison != 0) {
			return scorePerDayComparison;
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
