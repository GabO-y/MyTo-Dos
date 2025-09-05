import './App.css';
import { Routes, Route } from 'react-router-dom';
import Login from './pages/login';
import Register from './pages/register';
import { Board } from './pages/board';

function App() {
  return (
    <Routes>
  <Route path="/" element={<Login />} />
  <Route path="/login" element={<Login />} />
  <Route path="/register" element={<Register />} />
  <Route path="/board" element={<Board />} />
    </Routes>
  );
}

export default App;
