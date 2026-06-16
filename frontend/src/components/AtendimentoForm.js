import React, { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { atendimentoService, profissionalService } from '../services/api';

function AtendimentoForm() {
  const navigate = useNavigate();
  const { id } = useParams();

  const [profissionais, setProfissionais] = useState([]);

  const [atendimento, setAtendimento] = useState({
    data: '',
    horario: '',
    titulo: '',
    linkVideochamada: '',
    receita: '',
    profissional: {
      id: ''
    }
  });

  useEffect(() => {
    carregarProfissionais();

    if (id) {
      carregarAtendimento();
    }
  }, [id]);

  async function carregarProfissionais() {
    try {
      const response = await profissionalService.listar();
      setProfissionais(response.data);
    } catch (error) {
      console.error('Erro ao carregar profissionais:', error);
      alert('Erro ao carregar profissionais.');
    }
  }

  async function carregarAtendimento() {
    try {
      const response = await atendimentoService.buscar(id);

      setAtendimento({
        ...response.data,
        profissional: {
          id: response.data.profissional?.id || ''
        }
      });
    } catch (error) {
      console.error('Erro ao carregar atendimento:', error);
      alert('Erro ao carregar atendimento.');
    }
  }

  function alterarCampo(event) {
    const { name, value } = event.target;

    setAtendimento({
      ...atendimento,
      [name]: value
    });
  }

  function alterarProfissional(event) {
    const profissionalId = event.target.value;

    setAtendimento({
      ...atendimento,
      profissional: {
        id: profissionalId
      }
    });
  }

  function obterProfissionalSelecionado() {
    return profissionais.find(
      (profissional) => String(profissional.id) === String(atendimento.profissional.id)
    );
  }

  function obterRotuloReceita() {
    const profissional = obterProfissionalSelecionado();

    if (!profissional) {
      return 'Receita / Prescrição:';
    }

    if (profissional.categoria === 'MEDICO') {
      return 'Receita de remédio:';
    }

    if (profissional.categoria === 'FISIOTERAPEUTA') {
      return 'Prescrição de atividade física:';
    }

    if (profissional.categoria === 'PSICOLOGO') {
      return 'Prescrição de atividade mental:';
    }

    return 'Receita / Prescrição:';
  }

  async function salvar(event) {
    event.preventDefault();

    if (!atendimento.profissional.id) {
      alert('Selecione um profissional.');
      return;
    }

    const dadosParaSalvar = {
      ...atendimento,
      profissional: {
        id: Number(atendimento.profissional.id)
      }
    };

    try {
      if (id) {
        await atendimentoService.atualizar(id, dadosParaSalvar);
      } else {
        await atendimentoService.criar(dadosParaSalvar);
      }

      navigate('/atendimentos');
    } catch (error) {
      console.error('Erro ao salvar atendimento:', error);
      alert('Erro ao salvar atendimento.');
    }
  }

  return (
    <div>
      <h2>{id ? 'Editar Atendimento' : 'Novo Atendimento'}</h2>

      <form onSubmit={salvar}>
        <div>
          <label>Data:</label>
          <input
            type="date"
            name="data"
            value={atendimento.data || ''}
            onChange={alterarCampo}
            required
          />
        </div>

        <div>
          <label>Horário:</label>
          <input
            type="time"
            name="horario"
            value={atendimento.horario || ''}
            onChange={alterarCampo}
            required
          />
        </div>

        <div>
          <label>Título:</label>
          <input
            type="text"
            name="titulo"
            value={atendimento.titulo || ''}
            onChange={alterarCampo}
            required
          />
        </div>

        <div>
          <label>Link da videochamada:</label>
          <input
            type="url"
            name="linkVideochamada"
            value={atendimento.linkVideochamada || ''}
            onChange={alterarCampo}
            placeholder="https://..."
          />
        </div>

        <div>
          <label>Profissional:</label>
          <select
            value={atendimento.profissional.id}
            onChange={alterarProfissional}
            required
          >
            <option value="">Selecione</option>

            {profissionais.map((profissional) => (
              <option key={profissional.id} value={profissional.id}>
                {profissional.nome} - {profissional.categoria}
              </option>
            ))}
          </select>
        </div>

        <div>
          <label>{obterRotuloReceita()}</label>
          <textarea
            name="receita"
            value={atendimento.receita || ''}
            onChange={alterarCampo}
            rows="4"
          />
        </div>

        <button type="submit">Salvar</button>
        <button type="button" onClick={() => navigate('/atendimentos')}>
          Cancelar
        </button>
      </form>
    </div>
  );
}

export default AtendimentoForm;