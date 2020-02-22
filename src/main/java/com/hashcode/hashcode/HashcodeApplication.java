package com.hashcode.hashcode;

import com.hashcode.hashcode.io.IO;
import com.hashcode.hashcode.model.Library;
import com.hashcode.hashcode.model.Time;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class HashcodeApplication {

	public static void main(String[] args) {
		SpringApplication.run(HashcodeApplication.class, args);

		List<String> fileNames = Arrays.asList("a_example", "b_read_on", "c_incunabula",
				"d_tough_choices", "e_so_many_books", "f_libraries_of_the_world");

		// Create the folder that will contain the output files.
		String folderName = Long.toString(new Timestamp(System.currentTimeMillis()).getTime());
		if (IO.createFolder(folderName)) {
			List<Library> libraries = new ArrayList<>();
			Time time = new Time();

			fileNames.forEach(fileName -> {
				// Fill libraries list and time from the input file.
				IO.readFile(libraries, time, fileName);

				// Run the algorithm.
				List<Library> result = new Algo().run(libraries, time);

				// Generate the output file from resulting libraries.
				IO.writeFile(result, folderName, fileName);
			});
		}
	}
}
