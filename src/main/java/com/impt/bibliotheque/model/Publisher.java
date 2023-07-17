package com.impt.bibliotheque.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "publishers")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Publisher {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long publisher_id;

	@Column(name = "name", length = 100, nullable = false, unique = true)
	private String name;

	@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "publishers")
	private Set<Book> books = new HashSet<Book>();
}
