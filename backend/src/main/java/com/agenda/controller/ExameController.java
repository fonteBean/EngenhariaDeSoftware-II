package com.agenda.controller;

import com.agenda.model.Exame;
import com.agenda.repository.ExameRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/exames")
@CrossOrigin(origins = "*")
public class ExameController {

    private final ExameRepository repository;

    public ExameController(ExameRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<Exame> criar(@Valid @RequestBody Exame exame) {
        Exame salvo = repository.save(exame);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @GetMapping
    public ResponseEntity<List<Exame>> listar() {
        List<Exame> exames = repository.findAllByOrderByIdDesc();
        return ResponseEntity.ok(exames);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Exame> buscar(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Exame> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody Exame dados
    ) {
        return repository.findById(id)
                .map(exame -> {
                    exame.setDescricao(dados.getDescricao());
                    exame.setPosologia(dados.getPosologia());

                    return ResponseEntity.ok(repository.save(exame));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deletar(@PathVariable Long id) {
        return repository.findById(id)
                .map(exame -> {
                    repository.delete(exame);
                    return ResponseEntity.ok(Map.of("mensagem", "Exame removido com sucesso"));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}