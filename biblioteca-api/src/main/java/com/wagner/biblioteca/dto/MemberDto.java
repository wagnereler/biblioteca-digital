package com.wagner.biblioteca.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
    private UUID id;
    private String name;
    private String email;
    private String phone;
    private Integer registration;
}
