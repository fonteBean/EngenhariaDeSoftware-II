package com.agenda;

import com.agenda.model.CategoriaProfissional;
import com.agenda.model.ProfissionalSaude;
import com.agenda.repository.ProfissionalSaudeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProfissionalSaudeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProfissionalSaudeRepository profissionalRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void limparBanco() {
        profissionalRepository.deleteAll();
    }

    @Test
    void deveCriarProfissionalSaude() throws Exception {
        ProfissionalSaude profissional = new ProfissionalSaude();
        profissional.setNome("Dr. Carlos Silva");
        profissional.setTelefone("31999999999");
        profissional.setEndereco("Rua A, 123");
        profissional.setCategoria(CategoriaProfissional.MEDICO);

        mockMvc.perform(post("/api/profissionais")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profissional)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nome").value("Dr. Carlos Silva"))
                .andExpect(jsonPath("$.categoria").value("MEDICO"));
    }

    @Test
    void deveListarProfissionaisSaude() throws Exception {
        ProfissionalSaude profissional = new ProfissionalSaude();
        profissional.setNome("Dra. Mariana Souza");
        profissional.setTelefone("31888888888");
        profissional.setEndereco("Rua B, 456");
        profissional.setCategoria(CategoriaProfissional.PSICOLOGO);

        profissionalRepository.save(profissional);

        mockMvc.perform(get("/api/profissionais"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nome").value("Dra. Mariana Souza"))
                .andExpect(jsonPath("$[0].categoria").value("PSICOLOGO"));
    }

    @Test
    void deveBuscarProfissionalPorId() throws Exception {
        ProfissionalSaude profissional = new ProfissionalSaude();
        profissional.setNome("Fernanda Lima");
        profissional.setTelefone("31777777777");
        profissional.setEndereco("Rua C, 789");
        profissional.setCategoria(CategoriaProfissional.FISIOTERAPEUTA);

        ProfissionalSaude salvo = profissionalRepository.save(profissional);

        mockMvc.perform(get("/api/profissionais/" + salvo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Fernanda Lima"))
                .andExpect(jsonPath("$.categoria").value("FISIOTERAPEUTA"));
    }

    @Test
    void deveAtualizarProfissionalSaude() throws Exception {
        ProfissionalSaude profissional = new ProfissionalSaude();
        profissional.setNome("Nome Antigo");
        profissional.setTelefone("31000000000");
        profissional.setEndereco("Endereço Antigo");
        profissional.setCategoria(CategoriaProfissional.MEDICO);

        ProfissionalSaude salvo = profissionalRepository.save(profissional);

        ProfissionalSaude dadosAtualizados = new ProfissionalSaude();
        dadosAtualizados.setNome("Nome Atualizado");
        dadosAtualizados.setTelefone("31999999999");
        dadosAtualizados.setEndereco("Endereço Atualizado");
        dadosAtualizados.setCategoria(CategoriaProfissional.PSICOLOGO);

        mockMvc.perform(put("/api/profissionais/" + salvo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dadosAtualizados)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Nome Atualizado"))
                .andExpect(jsonPath("$.categoria").value("PSICOLOGO"));
    }

    @Test
    void deveRemoverProfissionalSaude() throws Exception {
        ProfissionalSaude profissional = new ProfissionalSaude();
        profissional.setNome("Profissional para Remover");
        profissional.setTelefone("31999999999");
        profissional.setEndereco("Rua Remoção");
        profissional.setCategoria(CategoriaProfissional.MEDICO);

        ProfissionalSaude salvo = profissionalRepository.save(profissional);

        mockMvc.perform(delete("/api/profissionais/" + salvo.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/profissionais/" + salvo.getId()))
                .andExpect(status().isNotFound());
    }
}