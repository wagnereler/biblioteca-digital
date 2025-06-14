package com.wagner.biblioteca.services;

import com.wagner.biblioteca.domain.Book;
import com.wagner.biblioteca.domain.Loan;
import com.wagner.biblioteca.domain.Member;
import com.wagner.biblioteca.repositories.BookRepository;
import com.wagner.biblioteca.repositories.LoanRepository;
import com.wagner.biblioteca.repositories.MemberRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class LoanService {

    private final LoanRepository loanRepo;
    private final MemberRepository memberRepo;
    private final BookRepository bookRepo;

    public LoanService(LoanRepository loanRepo,
                       MemberRepository memberRepo,
                       BookRepository bookRepo) {
        this.loanRepo   = loanRepo;
        this.memberRepo = memberRepo;
        this.bookRepo   = bookRepo;
    }

    public List<Loan> findAll() {
        return loanRepo.findAll();
    }

    public Optional<Loan> findById(UUID id) {
        return loanRepo.findById(id);
    }

    /**
     * Cria um novo empréstimo:
     * - decrementa availableCopies do livro
     * - persiste o Loan com datas e status
     */
    public Loan createLoan(UUID memberId, UUID bookId) {
        Member member = memberRepo.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found: " + memberId));
        Book book = bookRepo.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("Book not found: " + bookId));

        if (book.getAvailableCopies() <= 0) {
            throw new IllegalStateException("No copies available for book: " + bookId);
        }

        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepo.save(book);
        LocalDate today = LocalDate.now();

        Loan loan = Loan.builder()
                .loanDate(today)
                .dueDate(today.plusDays(7))
                .status("ACTIVE")
                .member(member)
                .book(book)
                .build();

        return loanRepo.save(loan);
    }

    /**
     * Processa a devolução:
     * - seta returnDate e status
     * - incrementa availableCopies do livro
     */
    public Loan returnLoan(UUID loanId) {
        Loan loan = loanRepo.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found: " + loanId));

        if (loan.getReturnDate() != null) {
            throw new IllegalStateException("Loan already returned: " + loanId);
        }

        loan.setReturnDate(LocalDate.now());
        loan.setStatus("RETURNED");
        loanRepo.save(loan);

        Book book = loan.getBook();
        book.setAvailableCopies(book.getAvailableCopies() + 1);
        bookRepo.save(book);

        return loan;
    }

}
