package com.library;

import com.library.entity.Author;
import com.library.entity.Book;
import com.library.repository.AuthorRepository;
import com.library.repository.BookRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class LibraryApplication {

    public static void main(String[] args) {
        SpringApplication.run(LibraryApplication.class, args);
    }

    /**
     * Populates the database with 10 sample authors and 10 sample books on startup
     * (only if tables are empty).
     */
    @Bean
    CommandLineRunner initDatabase(AuthorRepository authorRepository,
                                   BookRepository bookRepository) {
        return args -> {
            if (authorRepository.count() == 0) {
                // ── 10 Authors ──────────────────────────────────────────────
                Author a1  = authorRepository.save(new Author(null, "George",       "Orwell",      "British",  1903, null));
                Author a2  = authorRepository.save(new Author(null, "J.K.",         "Rowling",     "British",  1965, null));
                Author a3  = authorRepository.save(new Author(null, "F. Scott",     "Fitzgerald",  "American", 1896, null));
                Author a4  = authorRepository.save(new Author(null, "Harper",       "Lee",         "American", 1926, null));
                Author a5  = authorRepository.save(new Author(null, "Gabriel",      "Garcia Marquez","Colombian",1927,null));
                Author a6  = authorRepository.save(new Author(null, "J.R.R.",       "Tolkien",     "British",  1892, null));
                Author a7  = authorRepository.save(new Author(null, "Fyodor",       "Dostoevsky",  "Russian",  1821, null));
                Author a8  = authorRepository.save(new Author(null, "Jane",         "Austen",      "British",  1775, null));
                Author a9  = authorRepository.save(new Author(null, "Mark",         "Twain",       "American", 1835, null));
                Author a10 = authorRepository.save(new Author(null, "Leo",          "Tolstoy",     "Russian",  1828, null));

                // ── 10 Books ─────────────────────────────────────────────────
                bookRepository.saveAll(List.of(
                    new Book(null, "1984",                          "978-0451524935", 1949, "Dystopian Fiction",      328,  a1),
                    new Book(null, "Animal Farm",                   "978-0451526342", 1945, "Political Satire",       112,  a1),
                    new Book(null, "Harry Potter and the Sorcerer's Stone","978-0590353427",1997,"Fantasy",           309,  a2),
                    new Book(null, "The Great Gatsby",              "978-0743273565", 1925, "Literary Fiction",       180,  a3),
                    new Book(null, "To Kill a Mockingbird",         "978-0061935466", 1960, "Southern Gothic",        281,  a4),
                    new Book(null, "One Hundred Years of Solitude", "978-0060883287", 1967, "Magical Realism",        417,  a5),
                    new Book(null, "The Lord of the Rings",         "978-0618640157", 1954, "High Fantasy",          1178,  a6),
                    new Book(null, "Crime and Punishment",          "978-0486415871", 1866, "Psychological Fiction",  545,  a7),
                    new Book(null, "Pride and Prejudice",           "978-0141439518", 1813, "Romantic Novel",         432,  a8),
                    new Book(null, "War and Peace",                 "978-1400079988", 1869, "Historical Fiction",    1296,  a10)
                ));

                System.out.println("✅  Database seeded with 10 authors and 10 books.");
            }
        };
    }
}
