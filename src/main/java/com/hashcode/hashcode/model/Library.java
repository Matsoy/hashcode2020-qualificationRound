package com.hashcode.hashcode.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@FieldDefaults(level = AccessLevel.PUBLIC)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Library {
    Integer id = 0;
    List<Book> books = new ArrayList<>();
    Integer maxBooks = 0;
    Integer signUpProcess = 0;
}
