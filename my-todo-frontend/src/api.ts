import axios from "axios";

// Configuração básica do axios com a URL da API
const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL,
});

export default api;
