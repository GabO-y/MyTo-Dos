import './App.css';
import {Routes, Route, Navigate} from 'react-router-dom';
import Login from './pages/login';
import Register from './pages/register';
import { Board } from './pages/board';
import type {JSX} from "react";

// Componente para proteger rotas que precisam de autenticação
function ProtectedRoute({ children }: { children: JSX.Element }) {
    const token = localStorage.getItem("token");

    if (!token) {
        return <Navigate to="/" replace />;
    }

    return children;
}


// Componente principal da aplicação
function App() {
  return (
    <Routes>
  <Route path="/" element={<Login />} />
  <Route path="/login" element={<Login />} />
  <Route path="/register" element={<Register />} />
        <Route
            path="/board"
            element={
                <ProtectedRoute>
                    <Board />
                </ProtectedRoute>
            }
        />
    </Routes>
  );
}

export default App;
