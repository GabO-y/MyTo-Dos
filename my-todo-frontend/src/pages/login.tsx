import type { FormEvent } from "react";
import api from "../api";
import { useState } from "react";
import { useNavigate } from "react-router-dom";

// Função para fazer login, retorna o token e armazena no localStorage
export async function login(username: string, password: string) {
  const response = await api.post("/auth/authenticate", { username, password });
  const token = response.data.token;

  localStorage.setItem("token", token);

  return token;
}

// Componente de Login
export default function Login({ onToggle }: { onToggle?: () => void }) {
  // Estados para armazenar o username e password
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate();

  // Função para lidar com o submit do formulário de login
  async function handleSubmit(event: FormEvent<HTMLFormElement>): Promise<void> {
    event.preventDefault();
    if (!username.trim() || !password.trim()) {
      alert("Preencha usuário e senha.");
      return;
    }
    try {
      await login(username, password);
      navigate("/board");
    } catch (error: any) {
      if (error.response && error.response.status === 404) {
        alert("Usuário ou senha incorretos.");
      } else {
        alert("Erro ao fazer login");
      }
      console.error(error);
    }
  }

  // Renderização do componente
  return (
    <div>
      <h1>Login</h1>
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          placeholder="Username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
        />
        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        <button type="submit">Login</button>
      </form>
      <p>
        Não tem conta?{" "}
        <button
          type="button"
          onClick={() => navigate("/register")}
          style={{
            background: "none",
            border: "none",
            color: "blue",
            textDecoration: "underline",
            cursor: "pointer",
          }}
        >
          Cadastre-se
        </button>
      </p>
    </div>
  );
}
