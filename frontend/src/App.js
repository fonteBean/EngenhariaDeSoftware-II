import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';

import ProfissionalList from './components/ProfissionalList';
import ProfissionalForm from './components/ProfissionalForm';

import AtendimentoList from './components/AtendimentoList';
import AtendimentoForm from './components/AtendimentoForm';

import ExameList from './components/ExameList';
import ExameForm from './components/ExameForm';

import './App.css';

function App() {
  return (
    <Router>
      <div className="App">
        <header>
          <h1>Sistema de Saúde</h1>

          <nav>
            <Link to="/profissionais">Profissionais</Link>
            {' | '}
            <Link to="/atendimentos">Atendimentos</Link>
            {' | '}
            <Link to="/exames">Exames</Link>
          </nav>
        </header>

        <main>
          <Routes>
            <Route path="/" element={<ProfissionalList />} />

            <Route path="/profissionais" element={<ProfissionalList />} />
            <Route path="/profissionais/novo" element={<ProfissionalForm />} />
            <Route path="/profissionais/editar/:id" element={<ProfissionalForm />} />

            <Route path="/atendimentos" element={<AtendimentoList />} />
            <Route path="/atendimentos/novo" element={<AtendimentoForm />} />
            <Route path="/atendimentos/editar/:id" element={<AtendimentoForm />} />

            <Route path="/exames" element={<ExameList />} />
            <Route path="/exames/novo" element={<ExameForm />} />
            <Route path="/exames/editar/:id" element={<ExameForm />} />
          </Routes>
        </main>
      </div>
    </Router>
  );
}

export default App;