// src/components/layout/Header.jsx
import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import logo_complet from '../../assets/logo_complet.png'; 
import { Icons } from '../../utils/icons';

export default function Header() {
  const [theme, setTheme] = useState(() => {
    return localStorage.getItem('ed-theme') || 'light';
  });

  useEffect(() => {
    document.documentElement.setAttribute('data-theme', theme);
    localStorage.setItem('ed-theme', theme);
  }, [theme]);

  const toggleTheme = () => {
    setTheme(theme === 'dark' ? 'light' : 'dark');
  };

  return (
    <header className="header">
      <div className="w">
        <Link to="/" style={{ display: 'flex', alignItems: 'center' }}>
          <img src={logo_complet} alt="EduDrive Logo" className="logo" />
        </Link>

        <nav className="nav">
          <a href="#forfaits">Forfaits</a>
          <a href="#fonctionnalites">Fonctionnalités</a>
          <a href="#faq">FAQ</a>
          <a href="#contact">Contact</a>
          <Link to="/login" className="btn o">Se connecter</Link>
        </nav>

        {/* Le bouton rond pour le thème */}
        <button id="th" className="btn o" onClick={toggleTheme} aria-label="Changer de thème">
          {theme === 'dark' ? Icons.sun : Icons.moon}
        </button>
      </div>
    </header>
  );
}