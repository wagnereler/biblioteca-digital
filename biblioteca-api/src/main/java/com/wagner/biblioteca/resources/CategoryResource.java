package com.wagner.biblioteca.resources;

import com.wagner.biblioteca.domain.Category;
import com.wagner.biblioteca.services.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryResource {

    private final CategoryService service;

    public CategoryResource(CategoryService service) {
        this.service = service;
    }

    /**
     * GET /api/v1/categories?name=xxx
     * - se name for fornecido faz LIKE
     * - se não, retorna todas
     */
    @GetMapping
    public ResponseEntity<List<Category>> list(
            @RequestParam(name = "name", required = false) String name
    ) {
        return ResponseEntity.ok(service.findAll(name));
    }

    /** GET  /api/v1/categories/{id} */
    @GetMapping("/{id}")
    public ResponseEntity<Category> getById(@PathVariable UUID id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** POST /api/v1/categories */
    @PostMapping
    public ResponseEntity<Category> create(@RequestBody Category category) {
        Category saved = service.create(category);
        URI location = URI.create("/api/v1/categories/" + saved.getId());
        return ResponseEntity.created(location).body(saved);
    }

    /** PUT  /api/v1/categories/{id} */
    @PutMapping("/{id}")
    public ResponseEntity<Category> update(
            @PathVariable UUID id,
            @RequestBody Category dados) {

        return service.update(id, dados)
                .map(updated -> ResponseEntity.ok(updated))
                .orElse(ResponseEntity.notFound().build());
    }

    /** DELETE /api/v1/categories/{id} */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        boolean deleted = service.delete(id);
        return deleted
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
