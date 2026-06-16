import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { profissionalService } from '../services/api';

function ProfissionalList() {
  const [profissionais, setProfissionais] = useState([]);

  useEffect(() => {
    carregarProfissionais();
  }, []);

  async function carregarProfissionais() {
    try {
      const response = await profissionalService.listar();
      setProfissionais(response.data);
    } catch (error) {
      console.error('Erro ao carregar profissionais:', error);
      alert('Erro ao carregar profissionais.');
    }
  }

  async function deletarProfissional(id) {
    const confirmar = window.confirm('Deseja realmente remover este profissional?');

    if (!confirmar) {
      return;
    }

    try {
      await profissionalService.deletar(id);
      carregarProfissionais();
    } catch (error) {
      console.error('Erro ao remover profissional:', error);
      alert('Erro ao remover profissional.');
    }
  }

  function formatarCategoria(categoria) {
    const categorias = {
      MEDICO: 'Médico',
      PSICOLOGO: 'Psicólogo',
      FISIOTERAPEUTA: 'Fisioterapeuta'
    };

    return categorias[categoria] || categoria;
  }

  return (
    <div>
      <h2>Profissionais da Saúde</h2>

      <Link to="/profissionais/novo">
        <button>Novo Profissional</button>
      </Link>

      <table border="1" cellPadding="8" style={{ marginTop: '16px', width: '100%' }}>
        <thead>
          <tr>
            <th>Nome</th>
            <th>Telefone</th>
            <th>Endereço</th>
            <th>Categoria</th>
            <th>Ações</th>
          </tr>
        </thead>

        <tbody>
          {profissionais.map((profissional) => (
            <tr key={profissional.id}>
              <td>{profissional.nome}</td>
              <td>{profissional.telefone}</td>
              <td>{profissional.endereco}</td>
              <td>{formatarCategoria(profissional.categoria)}</td>
              <td>
                <Link to={`/profissionais/editar/${profissional.id}`}>
                  <button>Editar</button>
                </Link>

                <button onClick={() => deletarProfissional(profissional.id)}>
                  Remover
                </button>
              </td>
            </tr>
          ))}

          {profissionais.length === 0 && (
            <tr>
              <td colSpan="5">Nenhum profissional cadastrado.</td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
}

export default ProfissionalList;