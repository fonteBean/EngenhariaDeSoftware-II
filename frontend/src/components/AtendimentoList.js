import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { atendimentoService } from '../services/api';

function AtendimentoList() {
  const [atendimentos, setAtendimentos] = useState([]);

  useEffect(() => {
    carregarAtendimentos();
  }, []);

  async function carregarAtendimentos() {
    try {
      const response = await atendimentoService.listar();
      setAtendimentos(response.data);
    } catch (error) {
      console.error('Erro ao carregar atendimentos:', error);
      alert('Erro ao carregar atendimentos.');
    }
  }

  async function deletarAtendimento(id) {
    const confirmar = window.confirm('Deseja realmente remover este atendimento?');

    if (!confirmar) {
      return;
    }

    try {
      await atendimentoService.deletar(id);
      carregarAtendimentos();
    } catch (error) {
      console.error('Erro ao remover atendimento:', error);
      alert('Erro ao remover atendimento.');
    }
  }

  return (
    <div>
      <h2>Atendimentos</h2>

      <Link to="/atendimentos/novo">
        <button>Novo Atendimento</button>
      </Link>

      <table border="1" cellPadding="8" style={{ marginTop: '16px', width: '100%' }}>
        <thead>
          <tr>
            <th>Data</th>
            <th>Horário</th>
            <th>Título</th>
            <th>Profissional</th>
            <th>Link</th>
            <th>Receita / Prescrição</th>
            <th>Ações</th>
          </tr>
        </thead>

        <tbody>
          {atendimentos.map((atendimento) => (
            <tr key={atendimento.id}>
              <td>{atendimento.data}</td>
              <td>{atendimento.horario}</td>
              <td>{atendimento.titulo}</td>
              <td>{atendimento.profissional?.nome}</td>
              <td>
                {atendimento.linkVideochamada ? (
                  <a
                    href={atendimento.linkVideochamada}
                    target="_blank"
                    rel="noreferrer"
                  >
                    Acessar
                  </a>
                ) : (
                  'Não informado'
                )}
              </td>
              <td>{atendimento.receita}</td>
              <td>
                <Link to={`/atendimentos/editar/${atendimento.id}`}>
                  <button>Editar</button>
                </Link>

                <button onClick={() => deletarAtendimento(atendimento.id)}>
                  Remover
                </button>
              </td>
            </tr>
          ))}

          {atendimentos.length === 0 && (
            <tr>
              <td colSpan="7">Nenhum atendimento cadastrado.</td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
}

export default AtendimentoList;