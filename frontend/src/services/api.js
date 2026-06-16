import axios from 'axios';

function resolveApiUrl() {
  const raw = process.env.REACT_APP_API_URL;

  if (!raw) {
    return 'http://localhost:8080/api';
  }

  let url = raw.trim();

  if (!/^https?:\/\//i.test(url)) {
    url = `https://${url}`;
  }

  url = url.replace(/\/+$/, '');

  if (!/\/api$/i.test(url)) {
    url = `${url}/api`;
  }

  return url;
}

const API_URL = resolveApiUrl();

const api = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json'
  }
});

export const profissionalService = {
  listar: () => api.get('/profissionais'),
  buscar: (id) => api.get(`/profissionais/${id}`),
  criar: (profissional) => api.post('/profissionais', profissional),
  atualizar: (id, profissional) => api.put(`/profissionais/${id}`, profissional),
  deletar: (id) => api.delete(`/profissionais/${id}`)
};

export const atendimentoService = {
  listar: () => api.get('/atendimentos'),
  buscar: (id) => api.get(`/atendimentos/${id}`),
  criar: (atendimento) => api.post('/atendimentos', atendimento),
  atualizar: (id, atendimento) => api.put(`/atendimentos/${id}`, atendimento),
  deletar: (id) => api.delete(`/atendimentos/${id}`)
};

export const exameService = {
  listar: () => api.get('/exames'),
  buscar: (id) => api.get(`/exames/${id}`),
  criar: (exame) => api.post('/exames', exame),
  atualizar: (id, exame) => api.put(`/exames/${id}`, exame),
  deletar: (id) => api.delete(`/exames/${id}`)
};

export default api;