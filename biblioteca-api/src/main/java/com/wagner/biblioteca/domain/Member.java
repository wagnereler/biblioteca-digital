package com.wagner.biblioteca.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import lombok.EqualsAndHashCode.Include;
import org.hibernate.annotations.GenericGenerator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "member")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Member implements Serializable {

    @Include
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    private String phone;

    /**
     * Agora indicamos ao JPA para NÃO inserir nem atualizar essa coluna,
     * e na DDL definimos AUTO_INCREMENT para que o próprio H2 preencha.
     */
    @Column(
            name = "registration",
            nullable = false,
            insertable = false,
            updatable = false,
            columnDefinition = "INT AUTO_INCREMENT"
    )
    private Integer registration;

    @OneToMany(
            mappedBy = "member",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonManagedReference(value = "member-loans")
    private List<Loan> loans = new ArrayList<>();
}
