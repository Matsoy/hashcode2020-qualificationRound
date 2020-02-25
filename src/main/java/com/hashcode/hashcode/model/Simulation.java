package com.hashcode.hashcode.model;

import lombok.*;

import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class Simulation {
	private Integer time = 0;

	/**
	 * Method to run the run the algorithm.
	 *
	 * @param libraries the libraries.
	 * @return the score.
	 */
	public int run(List<Library> libraries) {
		// Manipulate the libraries to maximize the final score.
		orderLibrariesAndBooks(libraries);

		// Prepare libraries generate the output file.
		return prepareLibraries(libraries);
	}

	/**
	 * Method that will manipulate the libraries in order to maximize the final score.
	 *
	 * @param libraries the libraries.
	 */
	private void orderLibrariesAndBooks(List<Library> libraries) {
		// Sort libraries according to compareTo method.
		Collections.sort(libraries);

		// For each libraries, sort its books by descending score.
		Comparator<Book> compareScores = Comparator.comparing(Book::getScore);
		libraries.forEach(
				l -> l.sortBooks(compareScores, true)
		);
	}

	/**
	 * Method that will prepare the libraries to generate the output file.
	 *
	 * @param libraries the libraries.
	 * @
	 */
	private int prepareLibraries(List<Library> libraries) {
		// Result map. library id -> books
		LinkedHashMap<Integer, LinkedList<Book>> resultMap = new LinkedHashMap<>();
		libraries.forEach(library -> resultMap.put(
				library.getId(),
				new LinkedList<>()
		));

		// Map to iterate on. library -> books
		Map<Library, List<Book>> allBooksMap = new LinkedHashMap<>();

		for (Library library : libraries) {
			// Clear lists because useless.
			library.setBooksWithoutDuplications(null);
			allBooksMap.put(library, new LinkedList<>(library.getBooks()));
		}

		// Add null books to simulate the sign up process.
		int offset = 0;
		for (Library library : allBooksMap.keySet()) {
			offset += library.getSignUpProcess();
			for (int i = 0; i < offset; i++) {
				((LinkedList<Book>) allBooksMap.get(library)).addFirst(null);
			}
		}

		Set<Book> scannedBooks = new HashSet<>();
		// For each day
		for (int day = 0; day < this.time; day++) {
			// For each library
			Iterator<Library> it = allBooksMap.keySet().iterator();
			while (it.hasNext()) {
				// Key.
				Library library = it.next();
				// Value.
				List<Book> books = allBooksMap.get(library);

				int booksPerDay = library.getBooksPerDay();

				int added = 0;
				// Try to add "booksPerDay" books.
				while (added < booksPerDay && !books.isEmpty()) {
					Book book = ((LinkedList<Book>) books).pollFirst();
					if (book == null) {
						// If the library is not available.
						added = booksPerDay;
					} else if (scannedBooks.contains(book)) {
						// If the same book has already been scanned.
						books.remove(book);
					} else {
						// if it is a new book that has not already been scanned.
						added++;
						scannedBooks.add(book);
						resultMap.get(library.getId()).addLast(book);
					}
				}
				// If all the books in the library have been scanned
				if (books.isEmpty()) {
					it.remove();
				}
			}
		}

		List<Library> libraryTmp = new ArrayList<>();
		for (Map.Entry<Integer, LinkedList<Book>> entry : resultMap.entrySet()) {
			libraryTmp.add(new Library(entry.getKey(), entry.getValue()));
		}

		libraries = libraryTmp.stream()
				.filter(library -> !library.getBooks().isEmpty())
				.collect(Collectors.toList());

		return scannedBooks.stream()
				.mapToInt(Book::getScore)
				.sum();
	}
}
