package com.library.repository;

import com.library.entity.Author;
import com.library.entity.Book;
import com.library.entity.BookAuthorDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class BookRepositoryTest {

    @Autowired BookRepository   bookRepository;
    @Autowired AuthorRepository authorRepository;

    private Author author;
    private Book   book;

    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();
        authorRepository.deleteAll();

        author = authorRepository.save(
                new Author(null, "George", "Orwell", "British", 1903, null));
        book = bookRepository.save(
                new Book(null, "1984", "978-0451524935", 1949, "Dystopian Fiction", 328, author));
    }

    @Test
    @DisplayName("Save and find book by id")
    void saveAndFindById() {
        Optional<Book> found = bookRepository.findById(book.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("1984");
    }

    @Test
    @DisplayName("findByIsbn returns correct book")
    void findByIsbn() {
        Optional<Book> found = bookRepository.findByIsbn("978-0451524935");
        assertThat(found).isPresent();
        assertThat(found.get().getGenre()).isEqualTo("Dystopian Fiction");
    }

    @Test
    @DisplayName("findByGenreIgnoreCase is case-insensitive")
    void findByGenre() {
        List<Book> result = bookRepository.findByGenreIgnoreCase("DYSTOPIAN FICTION");
        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("findByAuthorId returns books for given author")
    void findByAuthorId() {
        List<Book> books = bookRepository.findByAuthorId(author.getId());
        assertThat(books).hasSize(1);
        assertThat(books.get(0).getTitle()).isEqualTo("1984");
    }

    @Test
    @DisplayName("findAllBooksWithAuthorDetails (INNER JOIN) returns correct DTO")
    void innerJoinQuery() {
        List<BookAuthorDTO> dtos = bookRepository.findAllBooksWithAuthorDetails();
        assertThat(dtos).hasSize(1);

        BookAuthorDTO dto = dtos.get(0);
        assertThat(dto.title()).isEqualTo("1984");
        assertThat(dto.authorFirstName()).isEqualTo("George");
        assertThat(dto.authorLastName()).isEqualTo("Orwell");
        assertThat(dto.nationality()).isEqualTo("British");
        assertThat(dto.getAuthorFullName()).isEqualTo("George Orwell");
    }

    @Test
    @DisplayName("findByIsbnAndIdNot excludes the book itself")
    void findByIsbnAndIdNot() {
        // Another book with different ISBN – should not conflict
        Book other = bookRepository.save(
                new Book(null, "Animal Farm", "978-0451526342", 1945, "Satire", 112, author));

        // Checking for conflict when updating 'book' with its own ISBN → should be empty
        Optional<Book> conflict = bookRepository.findByIsbnAndIdNot("978-0451524935", book.getId());
        assertThat(conflict).isEmpty();

        // Checking for conflict when updating 'other' with book's ISBN → should find book
        Optional<Book> clash = bookRepository.findByIsbnAndIdNot("978-0451524935", other.getId());
        assertThat(clash).isPresent();
    }

    @Test
    @DisplayName("findBooksByAuthorIdWithDetails filters by author")
    void filterByAuthor() {
        Author other = authorRepository.save(
                new Author(null, "Jane", "Austen", "British", 1775, null));
        bookRepository.save(
                new Book(null, "Pride and Prejudice", "978-0141439518", 1813, "Novel", 432, other));

        List<BookAuthorDTO> orwellBooks = bookRepository.findBooksByAuthorIdWithDetails(author.getId());
        assertThat(orwellBooks).hasSize(1);
        assertThat(orwellBooks.get(0).authorLastName()).isEqualTo("Orwell");
    }
}
