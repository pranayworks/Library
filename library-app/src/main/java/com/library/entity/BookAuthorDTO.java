package com.library.entity;

/**
 * Projection / DTO returned by the INNER JOIN custom query.
 * (Not an @Entity – just a plain Java record.)
 */
public record BookAuthorDTO(
        Long   bookId,
        String title,
        String isbn,
        Integer publicationYear,
        String genre,
        Integer pages,
        Long   authorId,
        String authorFirstName,
        String authorLastName,
        String nationality
) {
    /** Convenience for JSP display */
    public String getAuthorFullName() {
        return authorFirstName + " " + authorLastName;
    }
}
