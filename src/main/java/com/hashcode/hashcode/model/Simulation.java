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
	 * @return the sublist of libraries used to generate the output file.
	 */
	public List<Library> run(List<Library> libraries) {
		// Manipulate the libraries to maximize the final score.
		manipulateLibraries(libraries);

		// Get libraries ready to generate the output file.
		return this.prepareLibraries(libraries);
	}

	/**
	 * Method that will manipulate the libraries in order to maximize the final score.
	 *
	 * @param libraries the libraries.
	 */
	private void manipulateLibraries(List<Library> libraries) {
		// Sort libraries according to compareTo method.
		Collections.sort(libraries);

		// For each libraries, sort its books by descending score.
		Comparator<Book> compareScores = Comparator.comparing(Book::getScore);
		libraries.forEach(
				l -> l.sortBooks(compareScores, true)
		);

		// Remove duplicated books.
		removeDuplicateBooks(libraries);

		// Remove libraries that have run out of books.
		libraries = libraries.stream()
				.filter(l -> !l.getBooks().isEmpty())
				.collect(Collectors.toList());

		// Update totalScore.
		libraries.forEach(Library::updateTotalScore);

		// Sort libraries a second time.
		Collections.sort(libraries);
	}

	/**
	 * Method that will prepare the libraries to generate the output file.
	 *
	 * @param libraries the libraries.
	 * @
	 */
	private List<Library> prepareLibraries(List<Library> libraries) {
		// The libraries list used to generate the output file.
		List<Library> result = new ArrayList<>();
		// The current day of the simulation.
		int currentDay = 0;

		for (Library library : libraries) {
			// Increment current day.
			currentDay += library.getSignUpProcess();
			// Prepare a library instance to the output file from a given library.
			Library newLibrary = getOutputLibrary(library);
			result.add(newLibrary);

			// If we have passed the last day on which the books can be sent to the scanning facility.
			if (currentDay >= this.time) {
				break;
			}
		}

		return result;
	}

	/**
	 * Method to remove duplicate books.
	 *
	 * @param libraries the libraries.
	 */
	private void removeDuplicateBooks(List<Library> libraries) {
		List<List<Book>> booksWithoutDuplication = libraries.stream()
				.map(Library::getBooks)
				.collect(Collectors.toList());

		// Keep track of books we've already seen.
		Set<Book> bookCache = new HashSet<>();

		// Iterate and remove if seen before
		for (List<Book> list : booksWithoutDuplication) {
			for (Iterator<Book> it = list.iterator(); it.hasNext(); ) {
				Book book = it.next();
				if (bookCache.contains(book)) {
					it.remove();
				} else {
					bookCache.add(book);
				}
			}
		}

		// Update libraries from the list of books without applications.
		for (int i = 0; i < libraries.size(); i++) {
			Library library = libraries.get(i);
			library.setBooks(booksWithoutDuplication.get(i));
		}
	}

	/**
	 * Method to prepare a library instance to the output file from a given library.
	 *
	 * @param library the library.
	 * @return the library instance created from the given library.
	 */
	private Library getOutputLibrary(Library library) {
		Library outputLibrary = new Library();
		outputLibrary.setId(library.getId());
		outputLibrary.setTotalScore(library.getTotalScore());

		int bookIndex = 0;
		// Can scan maxBooks books per day.
		for (int i = 0; i < library.getMaxBooks(); i++) {
			// For each available days after the sign up process.
			for (int j = library.getSignUpProcess(); j < this.time; j++) {
				// If all the books in this library have already been scanned.
				if (library.getBooks().size() <= bookIndex) {
					break;
				} else {
					outputLibrary.addBook(library.getBooks().get(bookIndex));
				}
				bookIndex++;
			}
			// If all the books in this library have already been shipped.
			if (library.getBooks().size() <= bookIndex) {
				break;
			}
		}

		return outputLibrary;
	}
}
