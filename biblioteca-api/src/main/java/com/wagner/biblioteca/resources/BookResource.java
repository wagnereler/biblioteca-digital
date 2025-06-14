package com.wagner.biblioteca.resources;

import com.wagner.biblioteca.domain.Book;
import com.wagner.biblioteca.services.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/books")
public class BookResource {
    private final BookService service;

    public BookResource(BookService service) {
        this.service = service;
    }

    /**
     * Se vier qualquer parâmetro de query, faz a busca “por contexto” (like ou exato em isbn);
     * se não vier nenhum, retorna tudo.
     *
     * Exemplo de URL:
     *  GET /api/v1/books?title=clean&author=martin
     */
    @GetMapping
    public ResponseEntity<List<Book>> list(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String isbn,
            @RequestParam(name = "category", required = false) String categoryName
    ) {
        List<Book> result = service.search(title, author, isbn, categoryName);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getById(@PathVariable UUID id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Book> create(@RequestBody Book book) {
        Book saved = service.create(book);
        return ResponseEntity
                .created(URI.create("/api/v1/books/" + saved.getId()))
                .body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Book> update(
            @PathVariable UUID id,
            @RequestBody Book dto) {
        return service.update(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        return service.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
