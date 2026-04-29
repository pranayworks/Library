package com.library.service;

import com.library.entity.Author;
import com.library.entity.Book;
import com.library.entity.BookAuthorDTO;
import com.library.repository.AuthorRepository;
import com.library.repository.BookRepository;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class BookService {

    private final BookRepository    bookRepository;
    private final AuthorRepository  authorRepository;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository   = bookRepository;
        this.authorRepository = authorRepository;
    }

    // ── READ ─────────────────────────────────────────────────────────────────

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found with id: " + id));
    }

    /**
     * Uses the custom INNER JOIN query from the repository.
     */
    public List<BookAuthorDTO> getAllBooksWithAuthorDetails() {
        return bookRepository.findAllBooksWithAuthorDetails();
    }

    public List<BookAuthorDTO> getBooksByAuthor(Long authorId) {
        return bookRepository.findBooksByAuthorIdWithDetails(authorId);
    }

    // ── CREATE ────────────────────────────────────────────────────────────────

    public Book saveBook(Book book) {
        // Integrity: ISBN must be unique
        bookRepository.findByIsbn(book.getIsbn()).ifPresent(existing -> {
            throw new DataIntegrityViolationException(
                    "A book with ISBN '" + book.getIsbn() + "' already exists.");
        });
        return bookRepository.save(book);
    }

    // ── UPDATE ────────────────────────────────────────────────────────────────

    public Book updateBook(Long id, Book updated) {
        Book existing = getBookById(id);

        // Integrity: ISBN must still be unique (excluding itself)
        bookRepository.findByIsbnAndIdNot(updated.getIsbn(), id).ifPresent(dup -> {
            throw new DataIntegrityViolationException(
                    "Another book already uses ISBN '" + updated.getIsbn() + "'.");
        });

        // Resolve author
        Author author = authorRepository.findById(updated.getAuthor().getId())
                .orElseThrow(() -> new RuntimeException("Author not found."));

        existing.setTitle(updated.getTitle());
        existing.setIsbn(updated.getIsbn());
        existing.setPublicationYear(updated.getPublicationYear());
        existing.setGenre(updated.getGenre());
        existing.setPages(updated.getPages());
        existing.setAuthor(author);

        return bookRepository.save(existing);
    }

    // ── DELETE ────────────────────────────────────────────────────────────────

    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new RuntimeException("Book not found with id: " + id);
        }
        bookRepository.deleteById(id);
    }
}
