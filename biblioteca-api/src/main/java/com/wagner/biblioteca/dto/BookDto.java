package com.wagner.biblioteca.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
    private UUID id;
    private String title;
    private String author;
    private String isbn;
    private int availableCopies;
    private CategoryDto category;
}
