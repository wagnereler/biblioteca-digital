package com.wagner.biblioteca.services;

import com.wagner.biblioteca.domain.Member;
import com.wagner.biblioteca.repositories.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MemberService {

    private final MemberRepository repo;

    public MemberService(MemberRepository repo) {
        this.repo = repo;
    }

    public List<Member> findAll() {
        return repo.findAll();
    }

    public Optional<Member> findById(UUID id) {
        return repo.findById(id);
    }

    public Member create(Member member) {
        member.setId(null);
        // registration é AUTO_INCREMENT no DB, não precisamos setar
        return repo.save(member);
    }

    public Optional<Member> update(UUID id, Member dto) {
        return repo.findById(id)
                .map(existing -> {
                    existing.setName(dto.getName());
                    existing.setEmail(dto.getEmail());
                    existing.setPhone(dto.getPhone());
                    return repo.save(existing);
                });
    }

    public boolean delete(UUID id) {
        return repo.findById(id)
                .map(m -> {
                    repo.delete(m);
                    return true;
                })
                .orElse(false);
    }
}
