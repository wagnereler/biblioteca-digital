package com.wagner.biblioteca.services;

import com.wagner.biblioteca.domain.Book;
import com.wagner.biblioteca.domain.Category;
import com.wagner.biblioteca.repositories.BookRepository;
import com.wagner.biblioteca.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BookService {

    private final BookRepository bookRepo;
    private final CategoryRepository categoryRepo;

    public BookService(BookRepository bookRepo,
                       CategoryRepository categoryRepo) {
        this.bookRepo     = bookRepo;
        this.categoryRepo = categoryRepo;
    }

    public List<Book> findAll() {
        return bookRepo.findAll();
    }

    public Optional<Book> findById(UUID id) {
        return bookRepo.findById(id);
    }

    public Book create(Book book) {
        // valida categoria existente
        UUID catId = book.getCategory().getId();
        Category cat = categoryRepo.findById(catId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + catId));
        book.setId(null);
        book.setCategory(cat);
        return bookRepo.save(book);
    }

    public Optional<Book> update(UUID id, Book dto) {
        return bookRepo.findById(id)
                .map(existing -> {
                    existing.setTitle(dto.getTitle());
                    existing.setAuthor(dto.getAuthor());
                    existing.setIsbn(dto.getIsbn());
                    existing.setAvailableCopies(dto.getAvailableCopies());
                    // opcional: permitir mudar categoria
                    if (dto.getCategory() != null) {
                        UUID newCatId = dto.getCategory().getId();
                        Category newCat = categoryRepo.findById(newCatId)
                                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + newCatId));
                        existing.setCategory(newCat);
                    }
                    return bookRepo.save(existing);
                });
    }

    public boolean delete(UUID id) {
        return bookRepo.findById(id)
                .map(b -> {
                    bookRepo.delete(b);
                    return true;
                })
                .orElse(false);
    }
}
