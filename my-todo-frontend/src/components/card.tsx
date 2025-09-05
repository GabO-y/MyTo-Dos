export type Task = {
    id: number;
    name: string;
    description: string;
    status?: 'DOING' | 'COMPLETED' | 'LATE';
};

export type CardProps = {
    name: string;
    description: string;
    tasks?: Task[];
};

import React, { useState } from 'react';
import axios from 'axios';
import IconButton from './IconButton';

type CardExtraProps = { id?: number; onDelete?: () => void };
const Card: React.FC<CardProps & CardExtraProps> = (props) => {
    const { name, description, tasks, id, onDelete } = props;

    const [showDescription, setShowDescription] = useState(false);
    const [editingCard, setEditingCard] = useState(false);
    const [editCardName, setEditCardName] = useState(name);
    const [editCardDesc, setEditCardDesc] = useState(description);
    const [showAddTask, setShowAddTask] = useState(false);
    const [newTaskName, setNewTaskName] = useState("");
    const [newTaskDesc, setNewTaskDesc] = useState("");
    const [cardTasks, setCardTasks] = useState<Task[]>(tasks || []);
    const API_URL = import.meta.env.VITE_API_URL;


    const handleDeleteTask = async (taskId: number) => {
        const token = localStorage.getItem("token");
        if (!token) return;
        try {
            await axios.delete(`${API_URL}/tasks/${taskId}`, {
                headers: { Authorization: `Bearer ${token}` },
            });
            setCardTasks(cardTasks.filter(t => t.id !== taskId));
        } catch (err) {
            alert("Erro ao deletar task");
            console.error(err);
        }
    };

    const handleCompleteTask = async (task: Task) => {
        const token = localStorage.getItem("token");
        if (!token) return;
        try {
            await axios.put(
                `${API_URL}/tasks`,
                {
                    id: task.id,
                    name: task.name,
                    description: task.description,
                    status: "COMPLETED",
                    cardName: name
                },
                { headers: { Authorization: `Bearer ${token}` } }
            );
            setCardTasks(cardTasks.map(t => t.id === task.id ? { ...t, status: "COMPLETED" } : t));
        } catch (err) {
            alert("Erro ao concluir task");
            console.error(err);
        }
    };
    const [expectedEnd, setExpectedEnd] = useState<string>("");

    const handleAddTask = async (e: React.FormEvent) => {
        e.preventDefault();
        // Validação: nome obrigatório
        if (!newTaskName.trim()) {
            alert("O nome da task é obrigatório.");
            return;
        }
        // Validação: expectedEnd (se fornecido) deve ser no futuro
        if (expectedEnd) {
            const now = new Date();
            const endDate = new Date(expectedEnd);
            if (endDate <= now) {
                alert("A data esperada deve ser maior que agora.");
                return;
            }
        }
        try {
            const token = localStorage.getItem("token");
            if (!token) return;
            const body: any = {
                name: newTaskName,
                description: newTaskDesc,
                cardName: name
            };
            if (expectedEnd) {
                body.expectedEnd = expectedEnd;
            }
            const response = await axios.post(
                `${API_URL}/tasks`,
                body,
                {
                    headers: {
                        Authorization: `Bearer ${token}`,
                    },
                }
            );
            setCardTasks([...cardTasks, response.data]);
            setNewTaskName("");
            setNewTaskDesc("");
            setExpectedEnd("");
            setShowAddTask(false);
        } catch (err) {
            alert("Erro ao adicionar task");
            console.error(err);
        }
    };

    // Estado para controlar qual task está com descrição aberta
    const [openTaskId, setOpenTaskId] = useState<number | null>(null);
    // Estado para edição de task
    const [editingTaskId, setEditingTaskId] = useState<number | null>(null);
    const [editTaskName, setEditTaskName] = useState("");
    const [editTaskDesc, setEditTaskDesc] = useState("");

    const handleEditTask = (task: Task) => {
        setEditingTaskId(task.id);
        setEditTaskName(task.name);
        setEditTaskDesc(task.description || "");
    };

    const handleSaveEditTask = async (task: Task) => {
        const token = localStorage.getItem("token");
        if (!token) return;
        try {
            await axios.put(
                `${API_URL}/tasks`,
                {
                    id: task.id,
                    name: editTaskName,
                    description: editTaskDesc,
                    status: task.status,
                    cardName: name
                },
                { headers: { Authorization: `Bearer ${token}` } }
            );
            setCardTasks(cardTasks.map(t => t.id === task.id ? { ...t, name: editTaskName, description: editTaskDesc } : t));
            setEditingTaskId(null);
        } catch (err) {
            alert("Erro ao editar task");
            console.error(err);
        }
    };

    const [cardName, setCardName] = useState(name);
    const [cardDesc, setCardDesc] = useState(description);

    const handleSaveEditCard = async (e: React.FormEvent) => {
        e.preventDefault();
        const token = localStorage.getItem("token");
        if (!token || !id) return;
        try {
            await axios.put(
                `${API_URL}/cards`,
                { id, name: editCardName, description: editCardDesc },
                { headers: { Authorization: `Bearer ${token}` } }
            );
            setCardName(editCardName);
            setCardDesc(editCardDesc);
            setEditingCard(false);
        } catch (err) {
            alert("Erro ao editar card");
            console.error(err);
        }
    };

    return (
        <div
            style={{ border: '1px solid #ccc', borderRadius: 8, padding: 16, margin: 0, width: '100%', boxSizing: 'border-box', cursor: 'pointer' }}
            onClick={(e) => {
                if (e.detail === 2) { 
                    setEditingCard(true);
                } else { 
                    setShowDescription((prev) => !prev);
                }
            }}
        >
            <div style={{ position: 'relative', minHeight: 32, marginBottom: 8, display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
                <div style={{ width: '100%', display: 'flex', justifyContent: 'space-between', position: 'relative', minHeight: 32 }}>
                    {!editingCard && (
                        <IconButton onClick={e => { e.stopPropagation(); setShowAddTask(true); }}>
                        +
                        </IconButton>
                    )}
                    {!editingCard && onDelete && (
                    
                        <IconButton onClick={e => { e.stopPropagation(); onDelete(); }}>
                        x
                        </IconButton>
                    )}
                </div>
                <div style={{ width: '100%' }}>
                {editingCard ? (
                    <form onSubmit={handleSaveEditCard} style={{ display: 'flex', flexDirection: 'column', gap: 8, width: '100%' }} onClick={e => e.stopPropagation()}>
                        <input
                            type="text"
                            value={editCardName}
                            onChange={e => setEditCardName(e.target.value)}
                            maxLength={255}
                            required
                            style={{ 
                                fontWeight: 'bold', 
                                fontSize: 18,
                                width: '100%',
                                padding: '8px',
                                borderRadius: 4,
                                border: '1px solid #ccc'
                            }}
                        />
                        <textarea
                            value={editCardDesc}
                            onChange={e => setEditCardDesc(e.target.value)}
                            maxLength={255}
                            style={{
                                fontSize: 14,
                                minHeight: 80,
                                width: '100%',
                                padding: '8px',
                                borderRadius: 4,
                                border: '1px solid #ccc',
                                resize: 'vertical'
                            }}
                            style={{ resize: 'vertical', fontSize: 14 }}
                        />
                        <div style={{ display: 'flex', gap: 8, marginTop: 4 }}>
                            <button type="submit">Salvar</button>
                            <button type="button" onClick={() => { setEditingCard(false); setEditCardName(cardName); setEditCardDesc(cardDesc); }}>Cancelar</button>
                        </div>
                    </form>
                ) : (
                    <h3
                        style={{
                            margin: 0,
                            cursor: 'pointer',
                            textAlign: 'center',
                            width: '100%',
                            marginTop: 8,
                            wordBreak: 'break-word',
                            overflowWrap: 'break-word',
                            whiteSpace: 'pre-line',
                            maxWidth: '100%',
                        }}
                        onClick={e => { e.stopPropagation(); setEditingCard(true); }}
                        title="Clique para editar o card"
                    >
                        {cardName}
                    </h3>
                )}
                </div>
            </div>
            {!editingCard && showDescription && (
                <div style={{
                    padding: '8px',
                    background: 'rgba(255,255,255,0.05)',
                    borderRadius: 4,
                    marginTop: 8,
                    fontSize: 14,
                    color: '#888',
                    whiteSpace: 'pre-line',
                    wordBreak: 'break-word',
                    overflowWrap: 'break-word'
                }}>
                    {cardDesc}
                </div>
            )}
            {cardTasks && cardTasks.length > 0 && (
                <ul style={{ marginTop: 12, padding: 0 }}>
                    {cardTasks.map((task) => (
                        <li key={task.id} style={{ display: 'flex', flexDirection: 'column', gap: 4, listStyle: 'none', padding: '8px 0', borderBottom: '1px solid rgba(255,255,255,0.1)' }}>
                            {editingTaskId === task.id ? (
                                <form onSubmit={e => { e.preventDefault(); handleSaveEditTask(task); }} style={{ display: 'flex', flexDirection: 'column', gap: 8, width: '100%' }}>
                                    <input
                                        type="text"
                                        value={editTaskName}
                                        onChange={e => setEditTaskName(e.target.value)}
                                        maxLength={255}
                                        required
                                        style={{ fontWeight: 'bold', fontSize: 15, width: '100%', padding: '4px 8px' }}
                                    />
                                    <textarea
                                        value={editTaskDesc}
                                        onChange={e => setEditTaskDesc(e.target.value)}
                                        maxLength={255}
                                        style={{ fontSize: 14, minHeight: 60, width: '100%', padding: '4px 8px', resize: 'vertical' }}
                                        placeholder="Descrição da task"
                                    />
                                    <div style={{ display: 'flex', gap: 8, justifyContent: 'flex-end' }}>
                                        <button type="submit">Salvar</button>
                                        <button type="button" onClick={() => setEditingTaskId(null)}>Cancelar</button>
                                    </div>
                                </form>
                            ) : (
                                <>
                                    <div style={{ display: 'flex', gap: 4 }}>
                                        {task.status !== "COMPLETED" && (
                                            <IconButton onClick={e => { e.stopPropagation(); handleCompleteTask(task); }} color="#4CAF50" >
                                            ✔️
                                            </IconButton>
                                        )}
                                        <IconButton onClick={e => {e.stopPropagation(); handleDeleteTask(task.id); }}>
                                        🗑️
                                        </IconButton>
                                    </div>
                                    <div
                                        onClick={e => {
                                            e.stopPropagation();
                                            if (e.detail === 2) { 
                                                handleEditTask(task);
                                            } else {
                                                setOpenTaskId(openTaskId === task.id ? null : task.id);
                                            }
                                        }}
                                        style={{
                                            display: 'flex',
                                            flexDirection: 'column',
                                            gap: 4,
                                            cursor: 'pointer',
                                            textDecoration: task.status === "COMPLETED" ? 'line-through' : 'none'
                                        }}
                                    >
                                        <div style={{ display: 'flex', alignItems: 'center', gap: 8 }}>
                                            <strong style={{ fontSize: 15 }}>{task.name}</strong>
                                            {task.status === 'LATE' && <span style={{ color: 'red', fontSize: 13 }}>(Atrasada)</span>}
                                        </div>
                                        {openTaskId === task.id && task.description && (
                                            <div style={{
                                                padding: '8px',
                                                background: 'rgba(255,255,255,0.05)',
                                                borderRadius: 4,
                                                fontSize: 14,
                                                color: '#888',
                                                whiteSpace: 'pre-line',
                                                wordBreak: 'break-word'
                                            }}>
                                                {task.description}
                                            </div>
                                        )}
                                    </div>
                                </>
                            )}
                        </li>
                    ))}
                </ul>
            )}
            {showAddTask && (
                <div
                    style={{
                        position: 'fixed',
                        top: '10%',
                        left: '50%',
                        transform: 'translate(-50%, 0)',
                        background: '#fff',
                        border: '1px solid #ccc',
                        borderRadius: 8,
                        padding: 16,
                        zIndex: 1000,
                        boxShadow: '0 2px 8px rgba(0,0,0,0.15)'
                    }}
                    onClick={e => e.stopPropagation()}
                >
                    <form onSubmit={handleAddTask} style={{ display: 'flex', flexDirection: 'column', gap: 8 }}>
                        <input
                            type="text"
                            placeholder="Nome da Task"
                            value={newTaskName}
                            onChange={e => setNewTaskName(e.target.value)}
                            required
                        />
                        <input
                            type="text"
                            placeholder="Descrição"
                            value={newTaskDesc}
                            onChange={e => setNewTaskDesc(e.target.value)}
                            maxLength={255}
                        />
                        <input
                            type="datetime-local"
                            placeholder="Data esperada"
                            value={expectedEnd}
                            onChange={e => setExpectedEnd(e.target.value)}
                        />
                        <div style={{ display: 'flex', gap: 8, justifyContent: 'flex-end' }}>
                            <button type="submit">Adicionar</button>
                            <button type="button" onClick={() => setShowAddTask(false)}>Cancelar</button>
                        </div>
                    </form>
                </div>
            )}
        </div>
    );
};

export default Card;