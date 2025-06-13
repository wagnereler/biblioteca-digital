package com.wagner.biblioteca.repositories;

import com.wagner.biblioteca.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface MemberRepository extends JpaRepository<Member, UUID> {
    // Exemplo:
    // Optional<Member> findByEmail(String email);
}
