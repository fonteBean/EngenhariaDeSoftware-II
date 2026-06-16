package com.agenda.controller;

import com.agenda.model.Atendimento;
import com.agenda.repository.AtendimentoRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/atendimentos")
@CrossOrigin(origins = "*")
public class AtendimentoController {

    private final AtendimentoRepository repository;

    public AtendimentoController(AtendimentoRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<Atendimento> criar(@Valid @RequestBody Atendimento atendimento) {
        Atendimento salvo = repository.save(atendimento);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @GetMapping
    public ResponseEntity<List<Atendimento>> listar() {
        List<Atendimento> atendimentos = repository.findAllByOrderByDataAscHorarioAsc();
        return ResponseEntity.ok(atendimentos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Atendimento> buscar(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Atendimento> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody Atendimento dados
    ) {
        return repository.findById(id)
                .map(atendimento -> {
                    atendimento.setData(dados.getData());
                    atendimento.setHorario(dados.getHorario());
                    atendimento.setTitulo(dados.getTitulo());
                    atendimento.setLinkVideochamada(dados.getLinkVideochamada());
                    atendimento.setReceita(dados.getReceita());
                    atendimento.setProfissional(dados.getProfissional());

                    return ResponseEntity.ok(repository.save(atendimento));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deletar(@PathVariable Long id) {
        return repository.findById(id)
                .map(atendimento -> {
                    repository.delete(atendimento);
                    return ResponseEntity.ok(Map.of("mensagem", "Atendimento removido com sucesso"));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}