package com.hashcode.hashcode;

import com.hashcode.hashcode.model.Book;
import com.hashcode.hashcode.model.Library;
import com.hashcode.hashcode.model.Time;

import java.util.*;
import java.util.stream.Collectors;

public class Simulation {
	public List<Library> run(List<Library> libraries, Time time) {
		// The libraries list used to generate the output file.
		List<Library> librariesResult = new ArrayList<>();

		// Sort libraries.
		Collections.sort(libraries);

		// Remove duplicated books.
		removeDuplicateBooks(libraries);

		// Update the sum of the scores of the books.
		libraries.forEach(Library::updateTotalScore);

		// Sort libraries a second time.
		Collections.sort(libraries);

		// The current time.
		int currentDay = 0;

		for (Library library : libraries) {
			currentDay += library.getSignUpProcess();
			Library newLibrary = new Library();
			newLibrary.setId(library.getId());
			newLibrary.setTotalScore(library.getTotalScore());

			int bookIndex = 0;
			// Can scan maxBooks books per day.
			for (int i = 0; i < library.getMaxBooks(); i++) {
				// For each available days after the sign up process.
				for (int j = library.getSignUpProcess(); j < time.getDays(); j++) {
					bookIndex++;
					// If all the books in this library have already been scanned.
					if (library.getBooks().size() <= bookIndex) {
						break;
					} else {
						newLibrary.getBooks().add(library.getBooks().get(bookIndex));
					}
				}
				// If all the books in this library have already been shipped.
				if (library.getBooks().size() <= bookIndex) {
					break;
				}
			}

			librariesResult.add(newLibrary);

			// If we have passed the last day on which the books can be sent to the scanning facility.
			if (currentDay >= time.getDays()) {
				break;
			}
		}

		return librariesResult;
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
}
