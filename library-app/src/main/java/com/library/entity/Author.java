package com.library.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "authors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "First name is required")
    @Size(max = 50)
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(max = 50)
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @NotBlank(message = "Nationality is required")
    @Size(max = 60)
    @Column(nullable = false, length = 60)
    private String nationality;

    @Min(value = 1, message = "Birth year must be positive")
    @Max(value = 2100)
    @Column(name = "birth_year", nullable = false)
    private Integer birthYear;

    // One Author → Many Books  (cascade so orphan books are removed with author)
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude          // avoid circular toString
    @EqualsAndHashCode.Exclude
    private List<Book> books = new ArrayList<>();

    /** Convenience – full name for JSP display */
    public String getFullName() {
        return firstName + " " + lastName;
    }
}
