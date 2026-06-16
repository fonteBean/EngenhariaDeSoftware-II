package com.agenda.controller;

import com.agenda.model.ProfissionalSaude;
import com.agenda.repository.ProfissionalSaudeRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/profissionais")
@CrossOrigin(origins = "*")
public class ProfissionalSaudeController {

    private final ProfissionalSaudeRepository repository;

    public ProfissionalSaudeController(ProfissionalSaudeRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public ResponseEntity<ProfissionalSaude> criar(@Valid @RequestBody ProfissionalSaude profissional) {
        ProfissionalSaude salvo = repository.save(profissional);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @GetMapping
    public ResponseEntity<List<ProfissionalSaude>> listar() {
        List<ProfissionalSaude> profissionais = repository.findAllByOrderByNomeAsc();
        return ResponseEntity.ok(profissionais);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfissionalSaude> buscar(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfissionalSaude> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProfissionalSaude dados
    ) {
        return repository.findById(id)
                .map(profissional -> {
                    profissional.setNome(dados.getNome());
                    profissional.setTelefone(dados.getTelefone());
                    profissional.setEndereco(dados.getEndereco());
                    profissional.setCategoria(dados.getCategoria());

                    return ResponseEntity.ok(repository.save(profissional));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deletar(@PathVariable Long id) {
        return repository.findById(id)
                .map(profissional -> {
                    repository.delete(profissional);
                    return ResponseEntity.ok(Map.of("mensagem", "Profissional removido com sucesso"));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}