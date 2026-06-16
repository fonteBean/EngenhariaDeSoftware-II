package com.agenda.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Entity
@Table(name = "atendimentos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Atendimento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Data é obrigatória")
    private LocalDate data;

    @NotNull(message = "Horário é obrigatório")
    private LocalTime horario;

    @NotBlank(message = "Título é obrigatório")
    @Column(length = 200, nullable = false)
    private String titulo;

    @Column(name = "link_videochamada", length = 300)
    private String linkVideochamada;

    @Column(columnDefinition = "TEXT")
    private String receita;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "profissional_id")
    private ProfissionalSaude profissional;

    @Column(name = "criado_em")
    private LocalDateTime criadoEm = LocalDateTime.now();
}