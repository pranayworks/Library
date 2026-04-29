package com.library.service;

import com.library.entity.Author;
import com.library.repository.AuthorRepository;
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
class AuthorServiceTest {

    @Mock    AuthorRepository  authorRepository;
    @InjectMocks AuthorService authorService;

    private Author author;

    @BeforeEach
    void setUp() {
        author = new Author(1L, "George", "Orwell", "British", 1903, null);
    }

    @Test
    @DisplayName("getAllAuthors delegates to repository")
    void getAllAuthors() {
        when(authorRepository.findAllByOrderByLastNameAsc()).thenReturn(List.of(author));
        List<Author> result = authorService.getAllAuthors();
        assertThat(result).hasSize(1);
        verify(authorRepository).findAllByOrderByLastNameAsc();
    }

    @Test
    @DisplayName("getAuthorById returns author when found")
    void getAuthorById_found() {
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        Author result = authorService.getAuthorById(1L);
        assertThat(result.getFirstName()).isEqualTo("George");
    }

    @Test
    @DisplayName("getAuthorById throws when not found")
    void getAuthorById_notFound() {
        when(authorRepository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> authorService.getAuthorById(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("not found");
    }

    @Test
    @DisplayName("saveAuthor persists new author")
    void saveAuthor_success() {
        Author newAuthor = new Author(null, "Jane", "Austen", "British", 1775, null);
        when(authorRepository.findByLastNameIgnoreCase("Austen")).thenReturn(Optional.empty());
        when(authorRepository.save(newAuthor)).thenReturn(new Author(2L, "Jane", "Austen", "British", 1775, null));

        Author saved = authorService.saveAuthor(newAuthor);
        assertThat(saved.getId()).isEqualTo(2L);
        verify(authorRepository).save(newAuthor);
    }

    @Test
    @DisplayName("saveAuthor throws on duplicate name+birthYear")
    void saveAuthor_duplicate() {
        Author duplicate = new Author(null, "George", "Orwell", "British", 1903, null);
        when(authorRepository.findByLastNameIgnoreCase("Orwell")).thenReturn(Optional.of(author));

        assertThatThrownBy(() -> authorService.saveAuthor(duplicate))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    @DisplayName("updateAuthor updates fields correctly")
    void updateAuthor_success() {
        Author updated = new Author(null, "Eric", "Orwell", "British", 1903, null);
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(authorRepository.save(any(Author.class))).thenAnswer(inv -> inv.getArgument(0));

        Author result = authorService.updateAuthor(1L, updated);
        assertThat(result.getFirstName()).isEqualTo("Eric");
    }

    @Test
    @DisplayName("deleteAuthor calls repository delete")
    void deleteAuthor_success() {
        when(authorRepository.existsById(1L)).thenReturn(true);
        authorService.deleteAuthor(1L);
        verify(authorRepository).deleteById(1L);
    }

    @Test
    @DisplayName("deleteAuthor throws when id not found")
    void deleteAuthor_notFound() {
        when(authorRepository.existsById(99L)).thenReturn(false);
        assertThatThrownBy(() -> authorService.deleteAuthor(99L))
                .isInstanceOf(RuntimeException.class);
    }
}
