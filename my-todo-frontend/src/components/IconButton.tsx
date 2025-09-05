import React from "react";

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

type IconButtonProps = React.ButtonHTMLAttributes<HTMLButtonElement> & {
  color?: string;     // cor customizada (ex: vermelho)
  size?: number;      // tamanho customizado
};

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
