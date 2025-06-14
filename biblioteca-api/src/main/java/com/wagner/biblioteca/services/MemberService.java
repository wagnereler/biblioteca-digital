package com.wagner.biblioteca.services;

import com.wagner.biblioteca.domain.Member;
import com.wagner.biblioteca.dto.MemberDto;
import com.wagner.biblioteca.repositories.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MemberService {

    private final MemberRepository repo;

    public MemberService(MemberRepository repo) {
        this.repo = repo;
    }

    public List<MemberDto> findAll() {
        return repo.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public MemberDto findById(UUID id) {
        return repo.findById(id)
                .map(this::toDto)
                .orElse(null);
    }

    public MemberDto create(MemberDto dto) {
        // monta a entidade a partir do DTO (id e registration ficam nulos)
        Member member = Member.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .build();
        Member saved = repo.save(member);
        return toDto(saved);
    }

    public MemberDto update(UUID id, MemberDto dto) {
        return repo.findById(id)
                .map(existing -> {
                    existing.setName(dto.getName());
                    existing.setEmail(dto.getEmail());
                    existing.setPhone(dto.getPhone());
                    Member updated = repo.save(existing);
                    return toDto(updated);
                })
                .orElse(null);
    }

    public boolean delete(UUID id) {
        return repo.findById(id)
                .map(m -> {
                    repo.delete(m);
                    return true;
                })
                .orElse(false);
    }

    // --- helpers de mapeamento ---

    private MemberDto toDto(Member m) {
        return new MemberDto(
                m.getId(),
                m.getName(),
                m.getEmail(),
                m.getPhone(),
                m.getRegistration()
        );
    }
}
