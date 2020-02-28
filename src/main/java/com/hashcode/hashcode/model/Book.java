package com.hashcode.hashcode.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
public class Book {
	@EqualsAndHashCode.Include
	private Integer id = 0;
	private Integer score = 0;
}
