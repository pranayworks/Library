package com.library.repository;

import com.library.entity.Author;
import com.library.entity.Book;
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
class AuthorRepositoryTest {

    @Autowired AuthorRepository authorRepository;
    @Autowired BookRepository   bookRepository;

    private Author savedAuthor;

    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();
        authorRepository.deleteAll();
        savedAuthor = authorRepository.save(
                new Author(null, "George", "Orwell", "British", 1903, null));
    }

    @Test
    @DisplayName("Save and find author by id")
    void saveAndFindById() {
        Optional<Author> found = authorRepository.findById(savedAuthor.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getFirstName()).isEqualTo("George");
    }

    @Test
    @DisplayName("findByLastNameIgnoreCase – case insensitive")
    void findByLastNameIgnoreCase() {
        Optional<Author> found = authorRepository.findByLastNameIgnoreCase("ORWELL");
        assertThat(found).isPresent();
        assertThat(found.get().getLastName()).isEqualTo("Orwell");
    }

    @Test
    @DisplayName("findByNationalityIgnoreCase returns correct results")
    void findByNationality() {
        authorRepository.save(new Author(null, "Jane", "Austen", "British", 1775, null));
        List<Author> british = authorRepository.findByNationalityIgnoreCase("british");
        assertThat(british).hasSize(2);
    }

    @Test
    @DisplayName("findAllByOrderByLastNameAsc returns sorted list")
    void findAllOrdered() {
        authorRepository.save(new Author(null, "Leo", "Tolstoy", "Russian", 1828, null));
        List<Author> sorted = authorRepository.findAllByOrderByLastNameAsc();
        assertThat(sorted.get(0).getLastName()).isLessThanOrEqualTo(sorted.get(1).getLastName());
    }

    @Test
    @DisplayName("findAuthorsWithBooks returns only authors that have books")
    void findAuthorsWithBooks() {
        Author noBooks = authorRepository.save(
                new Author(null, "J.K.", "Rowling", "British", 1965, null));

        // Add a book to savedAuthor
        bookRepository.save(new Book(null, "1984", "978-0451524935", 1949,
                "Dystopian Fiction", 328, savedAuthor));

        List<Author> withBooks = authorRepository.findAuthorsWithBooks();
        assertThat(withBooks).extracting(Author::getId).contains(savedAuthor.getId());
        assertThat(withBooks).extracting(Author::getId).doesNotContain(noBooks.getId());
    }

    @Test
    @DisplayName("Delete author cascades to books")
    void deleteAuthorCascadesBooks() {
        bookRepository.save(new Book(null, "1984", "978-0451524935", 1949,
                "Dystopian Fiction", 328, savedAuthor));

        authorRepository.deleteById(savedAuthor.getId());

        assertThat(authorRepository.findById(savedAuthor.getId())).isEmpty();
        assertThat(bookRepository.findByAuthorId(savedAuthor.getId())).isEmpty();
    }
}
