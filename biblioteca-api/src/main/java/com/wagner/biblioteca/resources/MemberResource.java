package com.wagner.biblioteca.resources;

import com.wagner.biblioteca.dto.MemberDto;
import com.wagner.biblioteca.services.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/members")
public class MemberResource {
    private final MemberService service;

    public MemberResource(MemberService service) {
        this.service = service;
    }

    /**
     * GET /api/v1/members
     *   ?name=xxx        — busca name LIKE xxx
     *   &registration=5  — busca registration == 5
     * Se nenhum parâmetro, retorna todos.
     */
    @GetMapping
    public ResponseEntity<List<MemberDto>> list(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer registration
    ) {
        return ResponseEntity.ok(
                service.findAll(name, registration)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<MemberDto> getById(@PathVariable UUID id) {
        MemberDto dto = service.findById(id);
        return dto != null
                ? ResponseEntity.ok(dto)
                : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<MemberDto> create(@RequestBody MemberDto dto) {
        MemberDto saved = service.create(dto);
        return ResponseEntity
                .created(URI.create("/api/v1/members/" + saved.getId()))
                .body(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MemberDto> update(
            @PathVariable UUID id,
            @RequestBody MemberDto dto) {
        MemberDto updated = service.update(id, dto);
        return updated != null
                ? ResponseEntity.ok(updated)
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        return service.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }
}
