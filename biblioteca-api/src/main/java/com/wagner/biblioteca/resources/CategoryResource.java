package com.wagner.biblioteca.resources;

import com.wagner.biblioteca.domain.Category;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryResource {

    @GetMapping
    public List<Category> list() {
        Category cat1 = new Category(1, "Informática");
        Category cat2 = new Category(2, "Direito");

        return Arrays.asList(cat1, cat2);
    }
}
