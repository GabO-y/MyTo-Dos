import './App.css';
import {Routes, Route, Navigate} from 'react-router-dom';
import Login from './pages/login';
import Register from './pages/register';
import { Board } from './pages/board';
import type {JSX} from "react";

function ProtectedRoute({ children }: { children: JSX.Element }) {
    const token = localStorage.getItem("token");

    if (!token) {
        return <Navigate to="/" replace />;
    }

    return children;
}

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
