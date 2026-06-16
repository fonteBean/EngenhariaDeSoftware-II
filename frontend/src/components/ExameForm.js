import React, { useEffect, useState } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { exameService } from '../services/api';

function ExameForm() {
  const navigate = useNavigate();
  const { id } = useParams();

  const [exame, setExame] = useState({
    descricao: '',
    posologia: ''
  });

  useEffect(() => {
    if (id) {
      carregarExame();
    }
  }, [id]);

  async function carregarExame() {
    try {
      const response = await exameService.buscar(id);
      setExame(response.data);
    } catch (error) {
      console.error('Erro ao carregar exame:', error);
      alert('Erro ao carregar exame.');
    }
  }

  function alterarCampo(event) {
    const { name, value } = event.target;

    setExame({
      ...exame,
      [name]: value
    });
  }

  async function salvar(event) {
    event.preventDefault();

    try {
      if (id) {
        await exameService.atualizar(id, exame);
      } else {
        await exameService.criar(exame);
      }

      navigate('/exames');
    } catch (error) {
      console.error('Erro ao salvar exame:', error);
      alert('Erro ao salvar exame.');
    }
  }

  return (
    <div>
      <h2>{id ? 'Editar Exame' : 'Novo Exame'}</h2>

      <form onSubmit={salvar}>
        <div>
          <label>Descrição:</label>
          <textarea
            name="descricao"
            value={exame.descricao || ''}
            onChange={alterarCampo}
            rows="4"
            required
          />
        </div>

        <div>
          <label>Posologia:</label>
          <textarea
            name="posologia"
            value={exame.posologia || ''}
            onChange={alterarCampo}
            rows="4"
          />
        </div>

        <button type="submit">Salvar</button>
        <button type="button" onClick={() => navigate('/exames')}>
          Cancelar
        </button>
      </form>
    </div>
  );
}

export default ExameForm;