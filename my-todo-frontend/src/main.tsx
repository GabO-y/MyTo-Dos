import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { BrowserRouter } from 'react-router-dom';
import './index.css'
import App from './App';

const root = createRoot(document.getElementById('root') as HTMLElement);

// Renderização da aplicação dentro do BrowserRouter para habilitar roteamento
root.render(
  <StrictMode>
    <BrowserRouter>
      <App />
    </BrowserRouter>
  </StrictMode>
);
