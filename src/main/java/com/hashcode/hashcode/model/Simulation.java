package com.hashcode.hashcode.model;

import javafx.util.Pair;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
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
	public Pair<Integer, List<Library>> run(List<Library> libraries) {
		log.info("run");

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
		log.info("orderLibrariesAndBooks");

		// Sort libraries according to compareTo method.
		Collections.sort(libraries);

		List<List<Book>> allBooksList = libraries.stream()
				.map(library -> new ArrayList<>(library.getBooks()))
				.collect(Collectors.toList());

		// Keep track of books we've already seen.
		Set<Book> duplicateBooks = new HashSet<>();
		for (List<Book> books : allBooksList) {
			for (Iterator<Book> it = books.iterator(); it.hasNext(); ) {
				Book book = it.next();
				if (duplicateBooks.contains(book)) {
					it.remove();
				} else {
					duplicateBooks.add(book);
				}
			}
		}

		for (int libraryIndex = 0; libraryIndex < libraries.size(); libraryIndex++) {
			Library library = libraries.get(libraryIndex);
			List<Book> booksWithoutDuplications = allBooksList.get(libraryIndex);
			library.setBooksWithoutDuplications(booksWithoutDuplications);
			library.setUseBooksWithoutDuplications(true);
			library.updateScore();
		}

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
	 * @return the score and libraries for output.
	 */
	private Pair<Integer, List<Library>> prepareLibraries(List<Library> libraries) {
		log.info("prepareLibraries");
		log.info("Filling libraries with empty LinkedList");

		// Result map. library id -> books
		LinkedHashMap<Integer, List<Book>> resultMap = new LinkedHashMap<>();
		libraries.forEach(library -> resultMap.put(
				library.getId(),
				new LinkedList<>()
		));

		log.info("Setting offsets");
		// Add null books to simulate the sign up process.
		int offset = 0;
		for (Library library : libraries) {
			offset = library.addOffset(offset);
		}

		log.info("Running main loop");
		Set<Book> scannedBooks = new HashSet<>();
		// For each day
		for (int day = 0; day < this.time; day++) {
			if ((day + 1) % 5000 == 0) {
				log.info("Day {} / {}", day + 1, this.time);
			}

			libraries.sort((u1, u2) -> {
				Integer size1 = u1.getBooks().size() - u1.getBooksWithoutDuplications().size();
				Integer size2 = u2.getBooks().size() - u2.getBooksWithoutDuplications().size();
				return size2.compareTo(size1);
			});

			// For each library
			Iterator<Library> it = libraries.iterator();
			while (it.hasNext()) {
				Library library = it.next();
				List<Book> books = library.getBooks();

				int booksPerDay = library.getBooksPerDay();

				int added = 0;
				// Try to add "booksPerDay" books.
				while (added < booksPerDay && !books.isEmpty()) {
					// Try to decrement offset
					boolean canScan = !library.decrementOffset();

					if (canScan) {
						Book book = books.get(0);
						books.remove(0);
						if (!scannedBooks.contains(book)) {
							// if it is a new book that has not already been scanned.
							added++;
							scannedBooks.add(book);
							resultMap.get(library.getId()).add(book);
						}
					} else {
						// If the library is not available.
						added = booksPerDay;
					}
				}
				// If all the books in the library have been scanned
				if (books.isEmpty()) {
					it.remove();
				}
			}
		}

		log.info("Filling librariesTmp");
		List<Library> librariesTmp = new ArrayList<>();
		Iterator<Integer> it = resultMap.keySet().iterator();
		while (it.hasNext()) {
			Integer key = it.next();
			librariesTmp.add(new Library(key, resultMap.get(key)));
			it.remove();
		}

		log.info("Removing empty libraries");
		librariesTmp.removeIf(library -> library.getBooks().isEmpty());

		return new Pair<>(
				scannedBooks.stream()
						.mapToInt(Book::getScore)
						.sum(),
				librariesTmp);
	}
}
