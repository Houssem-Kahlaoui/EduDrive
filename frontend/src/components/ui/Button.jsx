// src/components/ui/Button.jsx
export default function Button({ 
  children, 
  variant = 'primary',
  size = '',
  onClick, 
  type = 'button',
  disabled = false,
  className = '',
  style = {} 
}) {
  const classNames = ['btn'];
  if (variant === 'outline') classNames.push('o');
  if (variant === 'gold') classNames.push('g');
  if (variant === 'danger') classNames.push('r');
  if (size === 'small') classNames.push('s');
  if (className) classNames.push(className);

  return (
    <button
      type={type}
      onClick={onClick}
      disabled={disabled}
      className={classNames.join(' ')}
      style={{ 
        ...(disabled ? { opacity: 0.5, cursor: 'not-allowed' } : {}), 
        ...style 
      }}
    >
      {children}
    </button>
  );
}