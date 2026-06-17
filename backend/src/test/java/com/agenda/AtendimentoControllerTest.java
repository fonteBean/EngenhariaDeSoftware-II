package com.agenda;

import com.agenda.model.Atendimento;
import com.agenda.model.CategoriaProfissional;
import com.agenda.model.ProfissionalSaude;
import com.agenda.repository.AtendimentoRepository;
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

import java.time.LocalDate;
import java.time.LocalTime;

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
class AtendimentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AtendimentoRepository atendimentoRepository;

    @Autowired
    private ProfissionalSaudeRepository profissionalRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void limparBanco() {
        atendimentoRepository.deleteAll();
        profissionalRepository.deleteAll();
    }

    @Test
    void deveCriarAtendimentoComProfissional() throws Exception {
        ProfissionalSaude profissional = criarProfissionalMedico();

        Atendimento atendimento = new Atendimento();
        atendimento.setData(LocalDate.of(2026, 6, 20));
        atendimento.setHorario(LocalTime.of(14, 30));
        atendimento.setTitulo("Consulta médica");
        atendimento.setLinkVideochamada("https://meet.google.com/teste");
        atendimento.setReceita("Tomar medicamento X a cada 8 horas");
        atendimento.setProfissional(profissional);

        mockMvc.perform(post("/api/atendimentos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(atendimento)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.titulo").value("Consulta médica"))
                .andExpect(jsonPath("$.profissional.nome").value("Dr. Carlos Silva"))
                .andExpect(jsonPath("$.profissional.categoria").value("MEDICO"));
    }

    @Test
    void deveListarAtendimentos() throws Exception {
        ProfissionalSaude profissional = criarProfissionalMedico();

        Atendimento atendimento = new Atendimento();
        atendimento.setData(LocalDate.of(2026, 6, 20));
        atendimento.setHorario(LocalTime.of(14, 30));
        atendimento.setTitulo("Consulta de retorno");
        atendimento.setLinkVideochamada("https://meet.google.com/retorno");
        atendimento.setReceita("Continuar medicação");
        atendimento.setProfissional(profissional);

        atendimentoRepository.save(atendimento);

        mockMvc.perform(get("/api/atendimentos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].titulo").value("Consulta de retorno"))
                .andExpect(jsonPath("$[0].profissional.nome").value("Dr. Carlos Silva"));
    }

    @Test
    void deveBuscarAtendimentoPorId() throws Exception {
        ProfissionalSaude profissional = criarProfissionalMedico();

        Atendimento atendimento = new Atendimento();
        atendimento.setData(LocalDate.of(2026, 6, 21));
        atendimento.setHorario(LocalTime.of(10, 0));
        atendimento.setTitulo("Consulta por ID");
        atendimento.setReceita("Receita teste");
        atendimento.setProfissional(profissional);

        Atendimento salvo = atendimentoRepository.save(atendimento);

        mockMvc.perform(get("/api/atendimentos/" + salvo.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Consulta por ID"))
                .andExpect(jsonPath("$.profissional.categoria").value("MEDICO"));
    }

    @Test
    void deveAtualizarAtendimento() throws Exception {
        ProfissionalSaude profissional = criarProfissionalMedico();

        Atendimento atendimento = new Atendimento();
        atendimento.setData(LocalDate.of(2026, 6, 22));
        atendimento.setHorario(LocalTime.of(9, 0));
        atendimento.setTitulo("Título antigo");
        atendimento.setReceita("Receita antiga");
        atendimento.setProfissional(profissional);

        Atendimento salvo = atendimentoRepository.save(atendimento);

        Atendimento dadosAtualizados = new Atendimento();
        dadosAtualizados.setData(LocalDate.of(2026, 6, 23));
        dadosAtualizados.setHorario(LocalTime.of(11, 30));
        dadosAtualizados.setTitulo("Título atualizado");
        dadosAtualizados.setLinkVideochamada("https://meet.google.com/atualizado");
        dadosAtualizados.setReceita("Receita atualizada");
        dadosAtualizados.setProfissional(profissional);

        mockMvc.perform(put("/api/atendimentos/" + salvo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dadosAtualizados)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Título atualizado"))
                .andExpect(jsonPath("$.receita").value("Receita atualizada"));
    }

    @Test
    void deveRemoverAtendimento() throws Exception {
        ProfissionalSaude profissional = criarProfissionalMedico();

        Atendimento atendimento = new Atendimento();
        atendimento.setData(LocalDate.of(2026, 6, 24));
        atendimento.setHorario(LocalTime.of(15, 0));
        atendimento.setTitulo("Atendimento para remover");
        atendimento.setReceita("Receita teste");
        atendimento.setProfissional(profissional);

        Atendimento salvo = atendimentoRepository.save(atendimento);

        mockMvc.perform(delete("/api/atendimentos/" + salvo.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/atendimentos/" + salvo.getId()))
                .andExpect(status().isNotFound());
    }

    private ProfissionalSaude criarProfissionalMedico() {
        ProfissionalSaude profissional = new ProfissionalSaude();
        profissional.setNome("Dr. Carlos Silva");
        profissional.setTelefone("31999999999");
        profissional.setEndereco("Rua A, 123");
        profissional.setCategoria(CategoriaProfissional.MEDICO);

        return profissionalRepository.save(profissional);
    }
}