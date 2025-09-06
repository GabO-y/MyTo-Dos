import React from "react";

// Estilo base dos botoẽs
  const baseButtonStyle: React.CSSProperties = {
    border: '1px solid #fff',
    borderRadius: '50%',
    background: 'none',
    cursor: 'pointer',
    padding: 0,
    width: 24,
    height: 24,
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
    fontSize: 14,
    };

// Props customizadas para o IconButton
type IconButtonProps = React.ButtonHTMLAttributes<HTMLButtonElement> & {
  color?: string;     // cor customizada 
  size?: number;      // tamanho customizado
};

// Componente de botão com ícone
const IconButton: React.FC<IconButtonProps> = ({ 
  children, 
  color = "#fff", 
  size = 24, 
  style, 
  ...props 
}) => {
  return (
    <button
      {...props}
      style={{
        ...baseButtonStyle,
        color,
        width: size,
        height: size,
        ...style, // sobrescreve se precisar
      }}
    >
      {children}
    </button>
  );
};

export default IconButton;
