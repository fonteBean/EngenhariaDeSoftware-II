import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { exameService } from '../services/api';

function ExameList() {
  const [exames, setExames] = useState([]);

  useEffect(() => {
    carregarExames();
  }, []);

  async function carregarExames() {
    try {
      const response = await exameService.listar();
      setExames(response.data);
    } catch (error) {
      console.error('Erro ao carregar exames:', error);
      alert('Erro ao carregar exames.');
    }
  }

  async function deletarExame(id) {
    const confirmar = window.confirm('Deseja realmente remover este exame?');

    if (!confirmar) {
      return;
    }

    try {
      await exameService.deletar(id);
      carregarExames();
    } catch (error) {
      console.error('Erro ao remover exame:', error);
      alert('Erro ao remover exame.');
    }
  }

  return (
    <div>
      <h2>Exames</h2>

      <Link to="/exames/novo">
        <button>Novo Exame</button>
      </Link>

      <table border="1" cellPadding="8" style={{ marginTop: '16px', width: '100%' }}>
        <thead>
          <tr>
            <th>Descrição</th>
            <th>Posologia</th>
            <th>Ações</th>
          </tr>
        </thead>

        <tbody>
          {exames.map((exame) => (
            <tr key={exame.id}>
              <td>{exame.descricao}</td>
              <td>{exame.posologia}</td>
              <td>
                <Link to={`/exames/editar/${exame.id}`}>
                  <button>Editar</button>
                </Link>

                <button onClick={() => deletarExame(exame.id)}>
                  Remover
                </button>
              </td>
            </tr>
          ))}

          {exames.length === 0 && (
            <tr>
              <td colSpan="3">Nenhum exame cadastrado.</td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
}

export default ExameList;