package com.hashcode.hashcode.io;

import com.hashcode.hashcode.model.Book;
import com.hashcode.hashcode.model.Library;
import com.hashcode.hashcode.model.Time;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
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
     * @param libraries the libraries.
     * @param time      the time.
     * @param fileName  the input file name.
     */
    public static void readFile(List<Library> libraries, Time time, String fileName) {
        String filePath = INPUTS_FOLDER + fileName + FILE_EXTENSION;

        try (Scanner myReader = new Scanner(new File(filePath))) {
            // Read header.
            String header = myReader.nextLine();
            String[] headerParts = header.split(" ");
            int librariesNb = Integer.parseInt(headerParts[1]);
            time.days = Integer.parseInt(headerParts[2]);
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
                library.id = i;
                library.signUpProcess = Integer.parseInt(libraryParts[1]);
                library.maxBooks = Integer.parseInt(libraryParts[2]);

                // Read books.
                String[] bookParts = myReader.nextLine().split(" ");
                for (String book : bookParts) {
                    library.books.add(books.get(Integer.parseInt(book)));
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
    public static void writeFile(List<Library> libraries, String fileName) {
        long now = new Timestamp(System.currentTimeMillis()).getTime();
        String filePath = OUTPUTS_PATH + fileName + "_" + now + FILE_EXTENSION;
        File file = new File(filePath);

        try {
            if (file.createNewFile()) {
                log.info("File created: {}", file.getName());

                try (FileWriter myWriter = new FileWriter(filePath)) {
                    // Write number of libraries.
                    myWriter.write("" + libraries.size());
                    myWriter.write(System.getProperty(LINE_SEPARATOR));
                    for (Library library : libraries) {
                        // Write number of books.
                        myWriter.write(library.id + " " + library.books.size());
                        myWriter.write(System.getProperty(LINE_SEPARATOR));
                        // Write books.
                        StringBuilder books = new StringBuilder();
                        for (Book book : library.books) {
                            books.append(book.id).append(" ");
                        }
                        books = new StringBuilder(removeLastCharacter(books.toString()));
                        myWriter.write(books.toString());
                        myWriter.write(System.getProperty(LINE_SEPARATOR));
                    }
                    log.info("Successfully wrote to the file.");

                } catch (IOException e) {
                    log.error(ERROR_OCCURRED, e);
                }
            } else {
                log.error("File already exists.");
            }

        } catch (IOException e) {
            log.error(ERROR_OCCURRED, e);
        }
    }

    /**
     * Method to delete the last character of a string.
     *
     * @param str the string from which the last character must be removed.
     * @return the <tt>String</tt> without the last character.
     */
    public static String removeLastCharacter(String str) {
        return Optional.ofNullable(str)
                .filter(sStr -> sStr.length() != 0)
                .map(sStr -> sStr.substring(0, sStr.length() - 1))
                .orElse(str);
    }
}
