package com.library.service;

import com.library.entity.Author;
import com.library.repository.AuthorRepository;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AuthorService {

    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    // ── READ ─────────────────────────────────────────────────────────────────

    public List<Author> getAllAuthors() {
        return authorRepository.findAllByOrderByLastNameAsc();
    }

    public Author getAuthorById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Author not found with id: " + id));
    }

    public List<Author> getAuthorsWithBooks() {
        return authorRepository.findAuthorsWithBooks();
    }

    // ── CREATE ────────────────────────────────────────────────────────────────

    public Author saveAuthor(Author author) {
        // Business rule: prevent duplicate (firstName + lastName + birthYear)
        Optional<Author> existing = authorRepository.findByLastNameIgnoreCase(author.getLastName());
        if (existing.isPresent()
                && existing.get().getFirstName().equalsIgnoreCase(author.getFirstName())
                && existing.get().getBirthYear().equals(author.getBirthYear())
                && !existing.get().getId().equals(author.getId())) {
            throw new DataIntegrityViolationException(
                    "An author with that name and birth year already exists.");
        }
        return authorRepository.save(author);
    }

    // ── UPDATE ────────────────────────────────────────────────────────────────

    public Author updateAuthor(Long id, Author updated) {
        Author existing = getAuthorById(id);
        existing.setFirstName(updated.getFirstName());
        existing.setLastName(updated.getLastName());
        existing.setNationality(updated.getNationality());
        existing.setBirthYear(updated.getBirthYear());
        return authorRepository.save(existing);
    }

    // ── DELETE ────────────────────────────────────────────────────────────────

    public void deleteAuthor(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new RuntimeException("Author not found with id: " + id);
        }
        authorRepository.deleteById(id);
    }
}
