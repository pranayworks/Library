package com.library.repository;

import com.library.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    /** Find authors by nationality (case-insensitive) */
    List<Author> findByNationalityIgnoreCase(String nationality);

    /** Find author by last name */
    Optional<Author> findByLastNameIgnoreCase(String lastName);

    /** All authors ordered by last name */
    List<Author> findAllByOrderByLastNameAsc();

    /**
     * Custom JPQL – fetch authors who have at least one book
     * (demonstrates a custom @Query using INNER JOIN)
     */
    @Query("SELECT DISTINCT a FROM Author a INNER JOIN a.books b")
    List<Author> findAuthorsWithBooks();
}
