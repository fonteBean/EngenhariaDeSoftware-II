import React, { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { profissionalService } from '../services/api';

function ProfissionalForm() {
  const navigate = useNavigate();
  const { id } = useParams();

  const [profissional, setProfissional] = useState({
    nome: '',
    telefone: '',
    endereco: '',
    categoria: 'MEDICO'
  });

  useEffect(() => {
    if (id) {
      carregarProfissional();
    }
  }, [id]);

  async function carregarProfissional() {
    try {
      const response = await profissionalService.buscar(id);
      setProfissional(response.data);
    } catch (error) {
      console.error('Erro ao carregar profissional:', error);
      alert('Erro ao carregar profissional.');
    }
  }

  function alterarCampo(event) {
    const { name, value } = event.target;

    setProfissional({
      ...profissional,
      [name]: value
    });
  }

  async function salvar(event) {
    event.preventDefault();

    try {
      if (id) {
        await profissionalService.atualizar(id, profissional);
      } else {
        await profissionalService.criar(profissional);
      }

      navigate('/profissionais');
    } catch (error) {
      console.error('Erro ao salvar profissional:', error);
      alert('Erro ao salvar profissional.');
    }
  }

  return (
    <div>
      <h2>{id ? 'Editar Profissional' : 'Novo Profissional'}</h2>

      <form onSubmit={salvar}>
        <div>
          <label>Nome:</label>
          <input
            type="text"
            name="nome"
            value={profissional.nome}
            onChange={alterarCampo}
            required
          />
        </div>

        <div>
          <label>Telefone:</label>
          <input
            type="text"
            name="telefone"
            value={profissional.telefone || ''}
            onChange={alterarCampo}
          />
        </div>

        <div>
          <label>Endereço:</label>
          <input
            type="text"
            name="endereco"
            value={profissional.endereco || ''}
            onChange={alterarCampo}
          />
        </div>

        <div>
          <label>Categoria:</label>
          <select
            name="categoria"
            value={profissional.categoria}
            onChange={alterarCampo}
            required
          >
            <option value="MEDICO">Médico</option>
            <option value="PSICOLOGO">Psicólogo</option>
            <option value="FISIOTERAPEUTA">Fisioterapeuta</option>
          </select>
        </div>

        <button type="submit">Salvar</button>
        <button type="button" onClick={() => navigate('/profissionais')}>
          Cancelar
        </button>
      </form>
    </div>
  );
}

export default ProfissionalForm;