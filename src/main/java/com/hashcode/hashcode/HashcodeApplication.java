package com.hashcode.hashcode;

import com.hashcode.hashcode.io.IO;
import com.hashcode.hashcode.model.Book;
import com.hashcode.hashcode.model.Library;
import com.hashcode.hashcode.model.Time;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class HashcodeApplication {

    public static void main(String[] args) {
        SpringApplication.run(HashcodeApplication.class, args);

        List<Library> libraries = new ArrayList<>();
        Time time = new Time();
        // a_example | b_read_on | c_incunabula | d_tough_choices | e_so_many_books | f_libraries_of_the_world
        String fileName = "a_example";

        // Fill libraries list and time from the input file.
        IO.readFile(libraries, time, fileName);

        // The libraries list used to generate the output file.
        List<Library> librariesResult = new ArrayList<>();

        // Sort libraries by ascending signupProcess.
        Comparator<Library> compareSignupProcess = Comparator.comparing((Library o) -> o.signUpProcess);
        libraries.sort(compareSignupProcess);
        // For each libraries, sort its books by descending score.
        Comparator<Book> compareScores = Comparator.comparing((Book o) -> o.score);
        libraries.forEach(
                l -> l.books.sort(compareScores.reversed())
        );

        // TODO Supprimer les duplication dans les library qui on le + de signUpProcess
        // faire une map ordonnées par signUpProcess et supprimer dans les dernières

        // The current time.
        int currentDay = 0;

        for (Library library : libraries) {
            currentDay += library.signUpProcess;
            Library newLibrary = new Library();
            newLibrary.id = library.id;

            int bookIndex = 0;
            // Can scan maxBooks books per day.
            for (int i = 0; i < library.maxBooks; i++) {
                // For each available days after the sign up process.
                for (int j = library.signUpProcess; j < time.days; j++) {
                    bookIndex++;
                    // If all the books in this library have already been scanned.
                    if (library.books.size() <= bookIndex) {
                        break;
                    } else {
                        newLibrary.books.add(library.books.get(bookIndex));
                    }
                }
                // If all the books in this library have already been shipped.
                if (library.books.size() <= bookIndex) {
                    break;
                }
            }

            librariesResult.add(newLibrary);

            // If we have passed the last day on which the books can be sent to the scanning facility.
            if (currentDay >= time.days) {
                break;
            }
        }

        // Generate the output file from libraries list.
        IO.writeFile(librariesResult, fileName);
    }

}
