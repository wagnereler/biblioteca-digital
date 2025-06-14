package com.wagner.biblioteca.services;

import com.wagner.biblioteca.domain.Category;
import com.wagner.biblioteca.repositories.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CategoryService {

    private final CategoryRepository repo;

    public CategoryService(CategoryRepository repo) {
        this.repo = repo;
    }

    /**
     * Se name for fornecido, faz LIKE (ignore-case);
     * se não, retorna todas as categorias.
     */
    public List<Category> findAll(String name) {
        if (StringUtils.hasText(name)) {
            return repo.findByNameContainingIgnoreCase(name);
        }
        return repo.findAll();
    }

    public Optional<Category> findById(UUID id) {
        return repo.findById(id);
    }

    public Category create(Category category) {
        category.setId(null);
        return repo.save(category);
    }

    public Optional<Category> update(UUID id, Category dados) {
        return repo.findById(id).map(existing -> {
            existing.setName(dados.getName());
            return repo.save(existing);
        });
    }

    public boolean delete(UUID id) {
        return repo.findById(id)
                .map(cat -> {
                    repo.deleteById(id);
                    return true;
                })
                .orElse(false);
    }
}
