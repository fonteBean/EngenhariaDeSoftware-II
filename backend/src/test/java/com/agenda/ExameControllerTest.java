package com.agenda;

import com.agenda.model.Exame;
import com.agenda.repository.ExameRepository;
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
class ExameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ExameRepository exameRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void limparBanco() {
        exameRepository.deleteAll();
    }

    @Test
    void deveCriarExame() throws Exception {
        Exame exame = new Exame();
        exame.setDescricao("Hemograma completo");
        exame.setPosologia("Realizar em jejum de 8 horas");

        mockMvc.perform(post("/api/exames")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(exame)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.descricao").value("Hemograma completo"))
                .andExpect(jsonPath("$.posologia").value("Realizar em jejum de 8 horas"));
    }

    @Test
    void deveListarExames() throws Exception {
        Exame exame = new Exame();
        exame.setDescricao("Raio-X do tórax");
        exame.setPosologia("Realizar conforme solicitação médica");

        exameRepository.save(exame);

        mockMvc.perform(get("/api/exames"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].descricao").value("Raio-X do tórax"));
    }

    @Test
    void deveBuscarExamePorId() throws Exception {
        Exame exame = new Exame();
        exame.setDescricao("Exame de glicemia");
        exame.setPosologia("Jejum de 8 horas");

        Exame salvo = exameRepository.save(exame);

        mockMvc.perform(get("/api/exames/" + salvo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value("Exame de glicemia"));
    }

    @Test
    void deveAtualizarExame() throws Exception {
        Exame exame = new Exame();
        exame.setDescricao("Descrição antiga");
        exame.setPosologia("Posologia antiga");

        Exame salvo = exameRepository.save(exame);

        Exame dadosAtualizados = new Exame();
        dadosAtualizados.setDescricao("Descrição atualizada");
        dadosAtualizados.setPosologia("Posologia atualizada");

        mockMvc.perform(put("/api/exames/" + salvo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dadosAtualizados)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.descricao").value("Descrição atualizada"))
                .andExpect(jsonPath("$.posologia").value("Posologia atualizada"));
    }

    @Test
    void deveRemoverExame() throws Exception {
        Exame exame = new Exame();
        exame.setDescricao("Exame para remover");
        exame.setPosologia("Sem posologia");

        Exame salvo = exameRepository.save(exame);

        mockMvc.perform(delete("/api/exames/" + salvo.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/exames/" + salvo.getId()))
                .andExpect(status().isNotFound());
    }
}