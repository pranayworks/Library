package com.library.controller;

import com.library.entity.Author;
import com.library.entity.Book;
import com.library.service.AuthorService;
import com.library.service.BookService;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService   bookService;
    private final AuthorService authorService;

    public BookController(BookService bookService, AuthorService authorService) {
        this.bookService   = bookService;
        this.authorService = authorService;
    }

    // ── LIST with INNER JOIN (READ) ───────────────────────────────────────────
    @GetMapping
    public String listBooks(Model model) {
        // Uses the custom INNER JOIN query from BookRepository
        model.addAttribute("bookDetails", bookService.getAllBooksWithAuthorDetails());
        model.addAttribute("pageTitle", "All Books");
        return "books/list";
    }

    // ── ADD FORM ──────────────────────────────────────────────────────────────
    @GetMapping("/add")
    public String showAddForm(Model model) {
        List<Author> authors = authorService.getAllAuthors();
        model.addAttribute("book", new Book());
        model.addAttribute("authors", authors);
        model.addAttribute("pageTitle", "Add Book");
        return "books/form";
    }

    // ── SAVE (CREATE) ─────────────────────────────────────────────────────────
    @PostMapping("/save")
    public String saveBook(@Valid @ModelAttribute("book") Book book,
                           BindingResult bindingResult,
                           @RequestParam("authorId") Long authorId,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("authors", authorService.getAllAuthors());
            model.addAttribute("pageTitle", "Add Book");
            return "books/form";
        }
        try {
            Author author = authorService.getAuthorById(authorId);
            book.setAuthor(author);
            bookService.saveBook(book);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Book '" + book.getTitle() + "' added successfully!");
            return "redirect:/books";
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("authors", authorService.getAllAuthors());
            model.addAttribute("pageTitle", "Add Book");
            return "books/form";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", "Error: " + e.getMessage());
            model.addAttribute("authors", authorService.getAllAuthors());
            model.addAttribute("pageTitle", "Add Book");
            return "books/form";
        }
    }

    // ── EDIT FORM (UPDATE) ────────────────────────────────────────────────────
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model,
                               RedirectAttributes redirectAttributes) {
        try {
            Book book = bookService.getBookById(id);
            model.addAttribute("book", book);
            model.addAttribute("authors", authorService.getAllAuthors());
            model.addAttribute("selectedAuthorId", book.getAuthor().getId());
            model.addAttribute("pageTitle", "Edit Book");
            return "books/form";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/books";
        }
    }

    // ── UPDATE ────────────────────────────────────────────────────────────────
    @PostMapping("/update/{id}")
    public String updateBook(@PathVariable Long id,
                             @Valid @ModelAttribute("book") Book book,
                             BindingResult bindingResult,
                             @RequestParam("authorId") Long authorId,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("authors", authorService.getAllAuthors());
            model.addAttribute("selectedAuthorId", authorId);
            model.addAttribute("pageTitle", "Edit Book");
            return "books/form";
        }
        try {
            Author author = authorService.getAuthorById(authorId);
            book.setAuthor(author);
            bookService.updateBook(id, book);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Book updated successfully!");
            return "redirect:/books";
        } catch (DataIntegrityViolationException | RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("authors", authorService.getAllAuthors());
            model.addAttribute("selectedAuthorId", authorId);
            model.addAttribute("pageTitle", "Edit Book");
            return "books/form";
        }
    }

    // ── DELETE ────────────────────────────────────────────────────────────────
    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            bookService.deleteBook(id);
            redirectAttributes.addFlashAttribute("successMessage", "Book deleted.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Cannot delete: " + e.getMessage());
        }
        return "redirect:/books";
    }
}
