import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuthStore } from '../../store/authStore';
import Button from '../../components/ui/Button';

export default function LoginPage() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  const { login, loading, error } = useAuthStore();
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    const result = await login(email, password);

    if (result.success) {
      if (result.role === 'ADMIN') navigate('/admin');
      else if (result.role === 'MONITEUR') navigate('/moniteur');
      else navigate('/candidat');
    }
  };

  return (
    <div className="au card">
      <div className="t" style={{ marginBottom: '20px' }}>
        <i>Bon retour</i>
        <h2>Connexion</h2>
      </div>

      <div className="tabs" style={{ pointerEvents: 'none', cursor: 'default' }}>
        <div style={{ flex: 1, padding: '8px', textAlign: 'center', fontSize: '13px' }}>
          Candidat
        </div>
        <div style={{ flex: 1, padding: '8px', textAlign: 'center', fontSize: '13px' }}>
          Moniteur
        </div>
        <div style={{ flex: 1, padding: '8px', textAlign: 'center', fontSize: '13px' }}>
          Admin
        </div>
      </div>

      {error && (
        <div style={{ background: '#d6334a22', color: 'var(--ko)', padding: '10px', borderRadius: '8px', marginBottom: '16px', fontSize: '13px', textAlign: 'center' }}>
          {error}
        </div>
      )}

      <form className="fm" onSubmit={handleSubmit}>
        <div>
          <label>E-mail</label>
          <input
            type="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            placeholder="votre@email.com"
            required
          />
        </div>
        <div>
          <label>Mot de passe</label>
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            placeholder="••••••••"
            required
          />
        </div>

        <Button type="submit" disabled={loading} style={{ marginTop: '10px', justifyContent: 'center' }}>
          {loading ? 'Connexion...' : 'Se connecter'}
        </Button>
      </form>
    </div>
  );
}