package com.wagner.biblioteca.resources;

import com.wagner.biblioteca.domain.Loan;
import com.wagner.biblioteca.services.LoanService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/loans")
public class LoanResource {

    private final LoanService service;

    public LoanResource(LoanService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<Loan>> list() {
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Loan> getById(@PathVariable UUID id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Loan> create(@RequestBody CreateLoanDto dto) {
        Loan loan = service.createLoan(dto.getMemberId(), dto.getBookId());
        return ResponseEntity
                .created(URI.create("/api/v1/loans/" + loan.getId()))
                .body(loan);
    }

    /**
     * Marca um empréstimo como devolvido:
     * - só precisa do ID na URL
     * - retorna 204 No Content (sem body)
     */
    @PatchMapping("/{id}/return")
    public ResponseEntity<Void> returnLoan(@PathVariable UUID id) {
        service.returnLoan(id);
        return ResponseEntity.noContent().build();
    }

    // DTO interno para capturar memberId e bookId
    @Data
    public static class CreateLoanDto {
        private UUID memberId;
        private UUID bookId;
    }
}
