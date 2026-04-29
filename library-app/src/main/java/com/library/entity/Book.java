package com.library.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "books",
       uniqueConstraints = @UniqueConstraint(columnNames = "isbn", name = "uk_books_isbn"))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(max = 200)
    @Column(nullable = false, length = 200)
    private String title;

    @NotBlank(message = "ISBN is required")
    @Size(max = 20)
    @Column(nullable = false, length = 20)
    private String isbn;

    @Min(value = 1000, message = "Publication year seems invalid")
    @Max(value = 2100)
    @Column(name = "publication_year", nullable = false)
    private Integer publicationYear;

    @NotBlank(message = "Genre is required")
    @Size(max = 80)
    @Column(nullable = false, length = 80)
    private String genre;

    @Min(value = 1)
    @Column(nullable = false)
    private Integer pages;

    // Many Books → One Author
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    @NotNull(message = "Author is required")
    private Author author;
}
