package com.library.repository;

import com.library.entity.Book;
import com.library.entity.BookAuthorDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    /** Derived query – books by genre */
    List<Book> findByGenreIgnoreCase(String genre);

    /** Derived query – books by author id */
    List<Book> findByAuthorId(Long authorId);

    /** Check for duplicate ISBN (excluding a given book id, used for updates) */
    Optional<Book> findByIsbnAndIdNot(String isbn, Long id);

    /** Check for duplicate ISBN (for new inserts) */
    Optional<Book> findByIsbn(String isbn);

    /**
     * INNER JOIN between books and authors – returns a flat DTO
     * that the JSP list view uses.
     *
     * Requirements: "Implement a custom query method in the repository layer
     * that performs an inner join between both entities and returns the result."
     */
    @Query("""
           SELECT new com.library.entity.BookAuthorDTO(
               b.id, b.title, b.isbn, b.publicationYear, b.genre, b.pages,
               a.id, a.firstName, a.lastName, a.nationality
           )
           FROM Book b
           INNER JOIN b.author a
           ORDER BY b.title ASC
           """)
    List<BookAuthorDTO> findAllBooksWithAuthorDetails();

    /**
     * Same join filtered by author id – useful for author-detail pages.
     */
    @Query("""
           SELECT new com.library.entity.BookAuthorDTO(
               b.id, b.title, b.isbn, b.publicationYear, b.genre, b.pages,
               a.id, a.firstName, a.lastName, a.nationality
           )
           FROM Book b
           INNER JOIN b.author a
           WHERE a.id = :authorId
           ORDER BY b.publicationYear ASC
           """)
    List<BookAuthorDTO> findBooksByAuthorIdWithDetails(@Param("authorId") Long authorId);
}
