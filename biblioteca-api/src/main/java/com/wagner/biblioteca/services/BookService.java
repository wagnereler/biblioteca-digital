package com.wagner.biblioteca.services;

import com.wagner.biblioteca.domain.Book;
import com.wagner.biblioteca.domain.Category;
import com.wagner.biblioteca.repositories.BookRepository;
import com.wagner.biblioteca.repositories.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class BookService {

    private final BookRepository bookRepo;
    private final CategoryRepository categoryRepo;

    public BookService(BookRepository bookRepo,
                       CategoryRepository categoryRepo) {
        this.bookRepo     = bookRepo;
        this.categoryRepo = categoryRepo;
    }

    /**
     * Retorna todos os livros.
     */
    public List<Book> findAll() {
        return bookRepo.findAll();
    }

    /**
     * Busca por ID.
     */
    public Optional<Book> findById(UUID id) {
        return bookRepo.findById(id);
    }

    /**
     * Busca contextual: title (like), author (like), isbn (exato),
     * ou category.name (like). Se nenhum parâmetro for passado,
     * retorna tudo.
     */
    public List<Book> search(String title,
                             String author,
                             String isbn,
                             String categoryName) {

        Set<Book> results = new LinkedHashSet<>();

        if (StringUtils.hasText(isbn)) {
            results.addAll(bookRepo.findByIsbn(isbn));
        }
        if (StringUtils.hasText(title)) {
            results.addAll(bookRepo.findByTitleContainingIgnoreCase(title));
        }
        if (StringUtils.hasText(author)) {
            results.addAll(bookRepo.findByAuthorContainingIgnoreCase(author));
        }
        if (StringUtils.hasText(categoryName)) {
            results.addAll(bookRepo.findByCategoryNameContainingIgnoreCase(categoryName));
        }

        // se nenhum filtro foi passado, retorna tudo
        if (!StringUtils.hasText(isbn)
                && !StringUtils.hasText(title)
                && !StringUtils.hasText(author)
                && !StringUtils.hasText(categoryName)) {
            return findAll();
        }

        return new ArrayList<>(results);
    }

    /**
     * Cria um novo livro validando que a categoria exista.
     */
    public Book create(Book book) {
        UUID catId = book.getCategory().getId();
        Category cat = categoryRepo.findById(catId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + catId));

        book.setId(null);
        book.setCategory(cat);
        return bookRepo.save(book);
    }

    /**
     * Atualiza apenas os campos mutáveis de um livro existente.
     */
    public Optional<Book> update(UUID id, Book dto) {
        return bookRepo.findById(id)
                .map(existing -> {
                    existing.setTitle(dto.getTitle());
                    existing.setAuthor(dto.getAuthor());
                    existing.setIsbn(dto.getIsbn());
                    existing.setAvailableCopies(dto.getAvailableCopies());
                    if (dto.getCategory() != null) {
                        UUID newCatId = dto.getCategory().getId();
                        Category newCat = categoryRepo.findById(newCatId)
                                .orElseThrow(() -> new IllegalArgumentException("Category not found: " + newCatId));
                        existing.setCategory(newCat);
                    }
                    return bookRepo.save(existing);
                });
    }

    /**
     * Remove um livro por ID.
     */
    public boolean delete(UUID id) {
        return bookRepo.findById(id)
                .map(b -> {
                    bookRepo.delete(b);
                    return true;
                })
                .orElse(false);
    }
}
