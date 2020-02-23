package com.hashcode.hashcode.io;

import com.hashcode.hashcode.model.Book;
import com.hashcode.hashcode.model.Library;
import com.hashcode.hashcode.model.Simulation;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

@FieldDefaults(level = AccessLevel.PUBLIC)
@Slf4j
public class IO {
	static final String INPUTS_FOLDER = "inputs\\";
	static final String OUTPUTS_PATH = "outputs\\";
	static final String FILE_EXTENSION = ".txt";
	static final String ERROR_OCCURRED = "An error occurred.";
	static final String LINE_SEPARATOR = "line.separator";

	/**
	 * Define a non-public constructor.
	 */
	private IO() {
	}

	/**
	 * Method to read the output file.
	 *
	 * @param libraries  the libraries.
	 * @param simulation the simulation instance.
	 * @param fileName   the input file name.
	 */
	public static void readInputFile(Simulation simulation, List<Library> libraries, String fileName) {
		String filePath = INPUTS_FOLDER + fileName + FILE_EXTENSION;

		try (Scanner myReader = new Scanner(new File(filePath))) {
			// Read header.
			String header = myReader.nextLine();
			String[] headerParts = header.split(" ");
			int librariesNb = Integer.parseInt(headerParts[1]);
			simulation.setTime(Integer.parseInt(headerParts[2]));
			List<Book> books = new ArrayList<>();

			// Read book scores.
			String[] bookScores = myReader.nextLine().split(" ");
			for (int i = 0; i < bookScores.length; i++) {
				books.add(new Book(i, Integer.parseInt(bookScores[i])));
			}

			// Read libraries.
			for (int i = 0; i < librariesNb; i++) {
				String[] libraryParts = myReader.nextLine().split(" ");
				Library library = new Library();
				library.setId(i);
				library.setSignUpProcess(Integer.parseInt(libraryParts[1]));
				library.setBooksPerDay(Integer.parseInt(libraryParts[2]));

				// Read books.
				String[] bookParts = myReader.nextLine().split(" ");
				for (String book : bookParts) {
					Book b = books.get(Integer.parseInt(book));
					library.addBook(b);
					library.setScore(library.getScore() + b.getScore());
				}
				libraries.add(library);
			}

		} catch (FileNotFoundException e) {
			log.error(ERROR_OCCURRED, e);
		}
	}

	/**
	 * Method to generate the output file.
	 *
	 * @param libraries the libraries ordered in writing order.
	 * @param fileName  the output file name.
	 */
	public static void writeOutputFile(List<Library> libraries, String folderName, String fileName) {
		// Create the output file.
		File file = new File(OUTPUTS_PATH + folderName, fileName + FILE_EXTENSION);
		int line = 0;
		if (createFile(file)) {
			try (FileWriter myWriter = new FileWriter(file)) {
				// Write number of libraries.
				myWriter.write("" + libraries.size());
				line++;
				if(line == 28089){
					boolean ici = true;
				}
				myWriter.write(System.getProperty(LINE_SEPARATOR));
				line++;
				if(line == 28089){
					boolean ici = true;
				}
				for (Library library : libraries) {
					// Write number of books.
					myWriter.write(library.getId() + " " + library.getBooks().size());
					line++;
					if(line == 28089){
						boolean ici = true;
					}
					myWriter.write(System.getProperty(LINE_SEPARATOR));
					line++;
					if(line == 28089){
						boolean ici = true;
					}
					// Write books.
					StringBuilder books = new StringBuilder();
					for (Book book : library.getBooks()) {
						books.append(book.getId()).append(" ");
					}
					books = new StringBuilder(removeLastCharacter(books.toString()));
					myWriter.write(books.toString());
					line++;
					if(line == 28089){
						boolean ici = true;
					}
					myWriter.write(System.getProperty(LINE_SEPARATOR));
					line++;
					if(line == 28089){
						boolean ici = true;
					}
				}
				log.info("Successfully wrote to the file.");

			} catch (IOException e) {
				log.error(ERROR_OCCURRED, e);
			}
		}
	}

	/**
	 * Method to create the folder that will contain the output files.
	 *
	 * @param folderName the folder name.
	 * @return <code>true</code> if the folder is created;
	 * <code>false</code> otherwise.
	 */
	public static boolean createFolder(String folderName) {
		//Creating a File object
		File file = new File(OUTPUTS_PATH + folderName);
		//Creating the directory
		boolean isCreated = file.mkdir();
		if (isCreated) {
			log.info("Directory {} created successfully", folderName);
		} else {
			log.error("Sorry couldn't create directory {}", folderName);
		}
		return isCreated;
	}

	/**
	 * Method to create the output file.
	 *
	 * @param file the file to create.
	 * @return <code>true</code> if the file is created;
	 * * <code>false</code> otherwise.
	 */
	private static boolean createFile(File file) {
		boolean isCreated = false;
		try {
			isCreated = file.createNewFile();
			if (isCreated) {
				log.info("File {} created successfully", file.getPath());
			} else {
				log.error("File {} already exists.", file.getPath());
			}
		} catch (IOException e) {
			log.error(ERROR_OCCURRED, e);
		}

		return isCreated;
	}

	/**
	 * Method to delete the last character of a string.
	 *
	 * @param str the string from which the last character must be removed.
	 * @return the <code>String</code> without the last character.
	 */
	private static String removeLastCharacter(String str) {
		return Optional.ofNullable(str)
				.filter(sStr -> sStr.length() != 0)
				.map(sStr -> sStr.substring(0, sStr.length() - 1))
				.orElse(str);
	}
}
