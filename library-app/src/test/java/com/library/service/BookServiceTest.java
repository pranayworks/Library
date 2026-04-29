package com.library.service;

import com.library.entity.Author;
import com.library.entity.Book;
import com.library.entity.BookAuthorDTO;
import com.library.repository.AuthorRepository;
import com.library.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock    BookRepository   bookRepository;
    @Mock    AuthorRepository authorRepository;
    @InjectMocks BookService  bookService;

    private Author author;
    private Book   book;

    @BeforeEach
    void setUp() {
        author = new Author(1L, "George", "Orwell", "British", 1903, null);
        book   = new Book(1L, "1984", "978-0451524935", 1949, "Dystopian Fiction", 328, author);
    }

    @Test
    @DisplayName("getAllBooks delegates to repository")
    void getAllBooks() {
        when(bookRepository.findAll()).thenReturn(List.of(book));
        assertThat(bookService.getAllBooks()).hasSize(1);
    }

    @Test
    @DisplayName("getBookById returns book when found")
    void getBookById_found() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        Book result = bookService.getBookById(1L);
        assertThat(result.getTitle()).isEqualTo("1984");
    }

    @Test
    @DisplayName("getBookById throws when not found")
    void getBookById_notFound() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> bookService.getBookById(99L))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("getAllBooksWithAuthorDetails uses INNER JOIN query")
    void getAllBooksWithAuthorDetails() {
        BookAuthorDTO dto = new BookAuthorDTO(
                1L, "1984", "978-0451524935", 1949, "Dystopian Fiction", 328,
                1L, "George", "Orwell", "British");
        when(bookRepository.findAllBooksWithAuthorDetails()).thenReturn(List.of(dto));

        List<BookAuthorDTO> result = bookService.getAllBooksWithAuthorDetails();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getAuthorFullName()).isEqualTo("George Orwell");
        verify(bookRepository).findAllBooksWithAuthorDetails();
    }

    @Test
    @DisplayName("saveBook persists when ISBN is unique")
    void saveBook_success() {
        Book newBook = new Book(null, "Animal Farm", "978-0451526342", 1945, "Satire", 112, author);
        when(bookRepository.findByIsbn("978-0451526342")).thenReturn(Optional.empty());
        when(bookRepository.save(newBook)).thenReturn(new Book(2L, "Animal Farm", "978-0451526342", 1945, "Satire", 112, author));

        Book saved = bookService.saveBook(newBook);
        assertThat(saved.getId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("saveBook throws DataIntegrityViolationException on duplicate ISBN")
    void saveBook_duplicateISBN() {
        Book duplicate = new Book(null, "Another 1984", "978-0451524935", 1949, "Fiction", 300, author);
        when(bookRepository.findByIsbn("978-0451524935")).thenReturn(Optional.of(book));

        assertThatThrownBy(() -> bookService.saveBook(duplicate))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("already exists");
    }

    @Test
    @DisplayName("updateBook updates all fields and resolves author")
    void updateBook_success() {
        Book updated = new Book(null, "1984 – Revised", "978-0451524935", 1949, "Fiction", 350, author);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.findByIsbnAndIdNot("978-0451524935", 1L)).thenReturn(Optional.empty());
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(bookRepository.save(any(Book.class))).thenAnswer(inv -> inv.getArgument(0));

        Book result = bookService.updateBook(1L, updated);
        assertThat(result.getTitle()).isEqualTo("1984 – Revised");
        assertThat(result.getPages()).isEqualTo(350);
    }

    @Test
    @DisplayName("updateBook throws on ISBN conflict with another book")
    void updateBook_isbnConflict() {
        Book conflicting = new Book(2L, "Other Book", "978-0451524935", 1945, "Satire", 112, author);
        Book updated = new Book(null, "New Title", "978-0451524935", 1949, "Fiction", 200, author);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookRepository.findByIsbnAndIdNot("978-0451524935", 1L))
                .thenReturn(Optional.of(conflicting));

        assertThatThrownBy(() -> bookService.updateBook(1L, updated))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("deleteBook calls repository delete")
    void deleteBook_success() {
        when(bookRepository.existsById(1L)).thenReturn(true);
        bookService.deleteBook(1L);
        verify(bookRepository).deleteById(1L);
    }
}
