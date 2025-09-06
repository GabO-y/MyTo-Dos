import { useState } from "react";
import type { FormEvent } from "react";
import api from "../api";
import { useNavigate } from "react-router-dom";

// Função para registrar um novo usuário
export function register(username: string, password: string) {
    return api.post("/auth/register", { username, password });
}

// Componente de Registro
export default function Register() {

    // Estados para armazenar o username e password
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const navigate = useNavigate();


    // Função para lidar com o submit do formulário de registro
    async function handleSubmit(event: FormEvent<HTMLFormElement>): Promise<void> {
        event.preventDefault();
        try {
            await register(username, password);
            // Após registrar, pode redirecionar para login
            navigate("/");
        } catch (error: any) {
            if (error.response && error.response.status === 400) {
                alert("Usuário já existe ou dados inválidos.");
            } else {
                alert("Erro ao registrar usuário");
            }
            console.error(error);
        }
    }

    // Renderização do componente
    return (
        <div>
            <h1>Register</h1>
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
                <button type="submit">Register</button>
            </form>
            <p>
                Já tem conta?{' '}
                <button type="button" onClick={() => navigate("/")} style={{ background: 'none', border: 'none', color: 'blue', textDecoration: 'underline', cursor: 'pointer' }}>
                    Entrar
                </button>
            </p>
        </div>
    );
}