package com.wagner.biblioteca.resources;

import com.wagner.biblioteca.domain.Category;
import com.wagner.biblioteca.services.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/categories")
public class CategoryResource {

    private final CategoryService categoryService;

    public CategoryResource(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /** GET /categories → lista todas as categorias */
    @GetMapping
    public ResponseEntity<List<Category>> list() {
        List<Category> categories = categoryService.findAll();
        return ResponseEntity.ok(categories);
    }

    /** GET /categories/{id} → busca categoria por ID, ou 404 se não existir */
    @GetMapping("/{id}")
    public ResponseEntity<Category> findById(@PathVariable UUID id) {
        return categoryService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
