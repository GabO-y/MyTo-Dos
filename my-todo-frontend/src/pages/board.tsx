import {useEffect, useState} from 'react';
import { useNavigate } from 'react-router-dom';
import Card from '../components/card';
import axios from 'axios';

//O modelo do Card conforme a API
type CardType = {
    id: number;
    name: string;
    description: string;
    tasks?: any[];
};

/*
Página do Board onde ficam os cards, um quadro que 
vai conter os cards criados pelo usuário.
*/
export function Board() {
    // Estados para armazenar os cards, o nome e descrição do novo card,
    //  e se está no modo de adicionar
    const [cards, setCards] = useState<CardType[]>([]);
    const [newCardName, setNewCardName] = useState("");
    const [newCardDesc, setNewCardDesc] = useState("");
    const [adding, setAdding] = useState(false);
    const API_URL = import.meta.env.VITE_API_URL;
    const navigate = useNavigate();

    // Efeito para buscar os cards ao carregar o componente
    useEffect(() => {
        const fetchCards = async () => {
            const token = localStorage.getItem('token');
            if (token == null || token === '') {
                alert('Você precisa estar logado para acessar o board.');
                navigate('/login');
                return;
            }
            if (!token) return;
            try {
                const response = await axios.get(`${API_URL}/cards`, {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                });
                setCards(response.data);
            } catch (err) {
                console.error('Erro ao buscar cards:', err);
            }
        };
        fetchCards();
    }, [API_URL]);

    // Função para adicionar um novo card
    const handleAddCard = async (e: React.FormEvent) => {
        e.preventDefault();
        const token = localStorage.getItem('token');
        if (!token) return;
        try {
            const response = await axios.post(
                `${API_URL}/cards`,
                { name: newCardName, description: newCardDesc },
                { headers: { Authorization: `Bearer ${token}` } }
            );
            setCards([...cards, response.data]);
            setNewCardName("");
            setNewCardDesc("");
            setAdding(false);
        } catch (err: any) {
            if (err.response && err.response.status === 409) {
                alert('Já existe um card com esse nome.');
            } else {
                alert('Erro ao adicionar card');
            }
            console.error(err);
        }
    };

    // Função para remover um card
    const handleDeleteCard = async (cardId: number) => {
        const token = localStorage.getItem('token');
        if (!token) return;
        try {
            await axios.delete(`${API_URL}/cards/${cardId}`, {
                headers: { Authorization: `Bearer ${token}` },
            });
            setCards(cards.filter(c => c.id !== cardId));
        } catch (err) {
            alert('Erro ao remover card');
            console.error(err);
        }
    };

    // Renderização do componente
    return (
        <div style={{ padding: 24, position: 'relative', minHeight: '100vh' }}>
            <button
                onClick={() => {
                    localStorage.removeItem('token');
                    navigate('/login');
                }}
                style={{
                    position: 'fixed',
                    top: 16,
                    right: 24,
                    background: '#f44336',
                    color: '#fff',
                    border: 'none',
                    borderRadius: 4,
                    padding: '8px 16px',
                    cursor: 'pointer',
                    zIndex: 100
                }}
            >
                Logout
            </button>
            <button
                onClick={() => setAdding(true)}
                style={{
                    position: 'fixed',
                    top: 16,
                    left: 24,
                    background: '#1976d2',
                    color: '#fff',
                    border: 'none',
                    borderRadius: 4,
                    padding: '8px 16px',
                    cursor: 'pointer',
                    zIndex: 100
                }}
            >
                + Adicionar Card
            </button>
            {adding && (
                <form onSubmit={handleAddCard} style={{ marginBottom: 16 }}>
                    <input
                        type="text"
                        placeholder="Nome do Card"
                        value={newCardName}
                        onChange={e => setNewCardName(e.target.value)}
                        required
                    />
                    <input
                        type="text"
                        placeholder="Descrição"
                        value={newCardDesc}
                        onChange={e => setNewCardDesc(e.target.value)}
                        maxLength={255}
                    />
                    <button type="submit">Criar</button>
                    <button type="button" onClick={() => setAdding(false)} style={{ marginLeft: 8 }}>Cancelar</button>
                </form>
            )}
            <div style={{
                display: 'flex',
                flexWrap: 'wrap',
                gap: 24,
                justifyContent: 'flex-start',
                padding: 12,
                boxSizing: 'border-box',
            }}>
                {cards.map((card) => (
                    <div key={card.id} style={{ minWidth: 0, width: 300, maxWidth: 300, flex: '0 0 300px', margin: 0, position: 'relative' }}>
                        <Card id={card.id} name={card.name} description={card.description} tasks={card.tasks} onDelete={() => handleDeleteCard(card.id)} />
                    </div>
                ))}
            </div>
        </div>
    );
}
