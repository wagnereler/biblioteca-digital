package com.wagner.biblioteca.repositories;

import com.wagner.biblioteca.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MemberRepository extends JpaRepository<Member, UUID> {
    /** Busca por name usando LIKE (ignore case) */
    List<Member> findByNameContainingIgnoreCase(String name);

    /** Busca por registration exato */
    List<Member> findByRegistration(Integer registration);
}
