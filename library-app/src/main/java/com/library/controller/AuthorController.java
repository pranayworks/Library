package com.library.controller;

import com.library.entity.Author;
import com.library.service.AuthorService;
import jakarta.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/authors")
public class AuthorController {

    private final AuthorService authorService;

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    // ── LIST (READ) ───────────────────────────────────────────────────────────
    @GetMapping
    public String listAuthors(Model model) {
        model.addAttribute("authors", authorService.getAllAuthors());
        model.addAttribute("pageTitle", "All Authors");
        return "authors/list";
    }

    // ── ADD FORM ──────────────────────────────────────────────────────────────
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("author", new Author());
        model.addAttribute("pageTitle", "Add Author");
        return "authors/form";
    }

    // ── SAVE (CREATE) ─────────────────────────────────────────────────────────
    @PostMapping("/save")
    public String saveAuthor(@Valid @ModelAttribute("author") Author author,
                             BindingResult bindingResult,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Add Author");
            return "authors/form";
        }
        try {
            authorService.saveAuthor(author);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Author '" + author.getFullName() + "' saved successfully!");
            return "redirect:/authors";
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("pageTitle", "Add Author");
            return "authors/form";
        }
    }

    // ── EDIT FORM (UPDATE) ────────────────────────────────────────────────────
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model,
                               RedirectAttributes redirectAttributes) {
        try {
            Author author = authorService.getAuthorById(id);
            model.addAttribute("author", author);
            model.addAttribute("pageTitle", "Edit Author");
            return "authors/form";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/authors";
        }
    }

    // ── UPDATE ────────────────────────────────────────────────────────────────
    @PostMapping("/update/{id}")
    public String updateAuthor(@PathVariable Long id,
                               @Valid @ModelAttribute("author") Author author,
                               BindingResult bindingResult,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Edit Author");
            return "authors/form";
        }
        try {
            authorService.updateAuthor(id, author);
            redirectAttributes.addFlashAttribute("successMessage",
                    "Author updated successfully!");
            return "redirect:/authors";
        } catch (DataIntegrityViolationException | RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("pageTitle", "Edit Author");
            return "authors/form";
        }
    }

    // ── DELETE ────────────────────────────────────────────────────────────────
    @GetMapping("/delete/{id}")
    public String deleteAuthor(@PathVariable Long id,
                               RedirectAttributes redirectAttributes) {
        try {
            authorService.deleteAuthor(id);
            redirectAttributes.addFlashAttribute("successMessage", "Author deleted.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Cannot delete author: " + e.getMessage());
        }
        return "redirect:/authors";
    }
}
